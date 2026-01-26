<template>
  <div class="map-distribution">
    <div class="map-panel">
      <div ref="mapContainer" class="map-container"></div>
    </div>
    <div class="list-panel">
      <header class="list-header">
        <h3 class="list-title">{{ computedTitle }}</h3>
        <el-input
          v-model="keyword"
          placeholder="输入关键字过滤"
          clearable
          size="small"
          @keyup.enter.native="refreshMarkers()"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </header>
      <div class="list-body" v-if="activeMarkers.length">
        <div
          v-for="item in activeMarkers"
          :key="item.id"
          class="marker-item"
          :class="{ active: selectedId === item.id }"
          @click="focusMarker(item.id)"
        >
          <img v-if="item.image" :src="item.image" alt="" class="marker-thumb" />
          <div class="marker-info">
            <div class="marker-name">{{ item.name }}</div>
            <div v-if="item.description" class="marker-desc">{{ item.description }}</div>
            <div class="marker-coordinate">
              <span>经度: {{ item.lng.toFixed(6) }}</span>
              <span>纬度: {{ item.lat.toFixed(6) }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="list-empty">
        <el-empty description="暂无可展示的数据" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { loadAmap, parseCoordinate, getDefaultCenter } from '@/utils/amap'
import markerImg from '@/assets/img/map/marker.png'

interface MarkerInput {
  id: string | number
  name: string
  lng: number | string
  lat: number | string
  description?: string
  image?: string
}

interface MapOptions {
  zoom?: number
  center?: [number, number]
  mapStyle?: string
}

interface MarkerInternal extends MarkerInput {
  lng: number
  lat: number
}

const props = withDefaults(defineProps<{
  markers: MarkerInput[]
  title?: string
  mapOptions?: MapOptions
  defaultIcon?: string
}>(), {
  markers: () => [],
  title: '',
  mapOptions: () => ({}),
  defaultIcon: markerImg
})

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''

const resolveFileUrl = (path?: string) => {
  if (!path) return ''
  if (path.startsWith('http')) return path
  if (!baseUrl) {
    return path.startsWith('/') ? path : `/${path}`
  }
  return baseUrl + (path.startsWith('/') ? path : `/${path}`)
}

const computedTitle = computed(() => props.title || '地图分布')
const keyword = ref('')
const mapContainer = ref<HTMLDivElement | null>(null)
const selectedId = ref<string | number | null>(null)
let map: any = null
let mapMarkers: Array<{ id: string | number; marker: any; infoWindow: any; data: MarkerInternal }> = []
let userLocation: { lng: number; lat: number } | null = null

const normalizedMarkers = computed<MarkerInternal[]>(() => {
  return props.markers
    .map((item) => {
      const lng = parseCoordinate(item.lng)
      const lat = parseCoordinate(item.lat)
      if (lng === null || lat === null) {
        return null
      }
      const name = String(item.name ?? '').trim() || '未命名地点'
      return {
        ...item,
        id: item.id ?? `${name}-${lng}-${lat}`,
        name,
        lng,
        lat,
        image: resolveFileUrl(item.image),
        description: item.description ?? ''
      }
    })
    .filter((item): item is MarkerInternal => item !== null)
})

const activeMarkers = computed<MarkerInternal[]>(() => {
  const text = keyword.value.trim().toLowerCase()
  if (!text) {
    return normalizedMarkers.value
  }
  return normalizedMarkers.value.filter((item) => {
    return (
      item.name.toLowerCase().includes(text) ||
      (item.description ?? '').toLowerCase().includes(text)
    )
  })
})

watch(activeMarkers, async (markers) => {
  await refreshMarkers(markers)
}, { immediate: true, deep: true })

const waitForAmapReady = async () => {
  let attempts = 0
  while (!(window as any).AMap?.Map && attempts < 20) {
    await new Promise((resolve) => setTimeout(resolve, 100))
    attempts += 1
  }
  return (window as any).AMap
}

async function ensureMap() {
  if (map || !mapContainer.value) {
    return map
  }
  const AMap = await loadAmap()
  const options = {
    zoom: props.mapOptions.zoom ?? 5,
    center: props.mapOptions.center ?? getDefaultCenter(),
    viewMode: '2D',
    mapStyle: props.mapOptions.mapStyle ?? 'amap://styles/normal',
    resizeEnable: true
  }
  map = new AMap.Map(mapContainer.value, options)
  AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.MapType'], () => {
    map.addControl(new AMap.ToolBar({ position: 'RB' }))
    map.addControl(new AMap.Scale({ position: 'LB' }))
    map.addControl(new AMap.MapType({ position: 'RT' }))
  })
  return map
}

async function getUserLocation() {
  try {
    const AMap = (await loadAmap()) || await waitForAmapReady()
    if (!AMap?.Geolocation) {
      return null
    }
    return new Promise<{ lng: number; lat: number } | null>((resolve) => {
      AMap.plugin('AMap.Geolocation', () => {
        const geolocation = new AMap.Geolocation({
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0,
          convert: true,
          showButton: false,
          showMarker: false,
          showCircle: false
        })
        geolocation.getCurrentPosition((status: string, result: any) => {
          if (status === 'complete' && result.position) {
            const location = {
              lng: result.position.lng,
              lat: result.position.lat
            }
            userLocation = location
            resolve(location)
          } else {
            resolve(null)
          }
        })
      })
    })
  } catch (error) {
    console.error('获取位置失败:', error)
    return null
  }
}

async function refreshMarkers(candidate?: MarkerInternal[]) {
  const markers = candidate ?? activeMarkers.value
  if (!markers.length) {
    clearMarkers()
    return
  }
  const AMap = await loadAmap()
  const mapInstance = await ensureMap()
  if (!mapInstance) return
  clearMarkers()
  const iconUrl = props.defaultIcon || markerImg
  markers.forEach((item) => {
    const position = new AMap.LngLat(item.lng, item.lat)
    const markerEntity = new AMap.Marker({
      position,
      title: item.name,
      icon: new AMap.Icon({
        image: iconUrl,
        size: new AMap.Size(32, 32),
        imageSize: new AMap.Size(32, 32)
      })
    })
    const infoWindow = new AMap.InfoWindow({
      content: buildInfoWindowContent(item),
      offset: new AMap.Pixel(0, -30)
    })
    markerEntity.on('click', () => {
      selectedId.value = item.id
      infoWindow.open(mapInstance, position)
    })
    markerEntity.setMap(mapInstance)
    mapMarkers.push({ id: item.id, marker: markerEntity, infoWindow, data: item })
  })
  mapInstance.setFitView(mapMarkers.map((m) => m.marker))
}

function navigateToMarker(item: MarkerInternal) {
  const endLng = item.lng
  const endLat = item.lat
  const endName = encodeURIComponent(item.name)
  const webUrl = `https://uri.amap.com/navigation?to=${endLng},${endLat},${endName}&mode=car&policy=1&src=myapp&coordinate=gaode&callnative=1`
  window.open(webUrl, '_blank')
}

function buildInfoWindowContent(item: MarkerInternal): HTMLElement {
  const container = document.createElement('div')
  container.style.padding = '10px'
  container.style.maxWidth = '280px'
  container.style.color = 'black'

  const title = document.createElement('h3')
  title.style.margin = '0 0 8px 0'
  title.style.fontSize = '16px'
  title.textContent = item.name
  container.appendChild(title)

  if (item.image) {
    const image = document.createElement('img')
    image.src = item.image
    image.style.width = '240px'
    image.style.height = '140px'
    image.style.objectFit = 'cover'
    image.style.borderRadius = '6px'
    image.style.marginBottom = '8px'
    container.appendChild(image)
  }

  if (item.description) {
    const desc = document.createElement('p')
    desc.style.margin = '0 0 8px 0'
    desc.style.lineHeight = '1.5'
    desc.textContent = item.description
    container.appendChild(desc)
  }

  const lng = document.createElement('p')
  lng.style.margin = '0'
  lng.style.color = '#666'
  lng.style.fontSize = '12px'
  lng.textContent = `经度: ${item.lng.toFixed(6)}`
  container.appendChild(lng)

  const lat = document.createElement('p')
  lat.style.margin = '0'
  lat.style.color = '#666'
  lat.style.fontSize = '12px'
  lat.textContent = `纬度: ${item.lat.toFixed(6)}`
  container.appendChild(lat)

  const navButton = document.createElement('button')
  navButton.type = 'button'
  navButton.textContent = '📍导航到这里'
  navButton.style.marginTop = '10px'
  navButton.style.width = '100%'
  navButton.style.border = 'none'
  navButton.style.borderRadius = '6px'
  navButton.style.padding = '8px 12px'
  navButton.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  navButton.style.color = '#fff'
  navButton.style.cursor = 'pointer'
  navButton.style.fontSize = '14px'
  navButton.style.fontWeight = '500'
  navButton.style.transition = 'all 0.3s ease'
  navButton.addEventListener('mouseenter', () => {
    navButton.style.transform = 'translateY(-2px)'
    navButton.style.boxShadow = '0 4px 12px rgba(102, 126, 234, 0.4)'
  })
  navButton.addEventListener('mouseleave', () => {
    navButton.style.transform = 'translateY(0)'
    navButton.style.boxShadow = 'none'
  })
  navButton.addEventListener('click', (event) => {
    event.preventDefault()
    event.stopPropagation()
    navigateToMarker(item)
  })
  container.appendChild(navButton)

  return container
}

function clearMarkers() {
  if (!mapMarkers.length) return
  mapMarkers.forEach(({ marker, infoWindow }) => {
    if (infoWindow) {
      infoWindow.close()
    }
    if (marker) {
      marker.setMap(null)
    }
  })
  mapMarkers = []
}

async function focusMarker(id: string | number) {
  const mapInstance = await ensureMap()
  if (!mapInstance) return
  const target = mapMarkers.find((item) => item.id === id)
  if (!target) return
  const { marker, infoWindow, data } = target
  selectedId.value = id
  const AMap = (window as any).AMap
  if (!AMap) return
  const position = new AMap.LngLat(data.lng, data.lat)
  mapInstance.setZoomAndCenter(12, position, false, 300)
  mapInstance.panTo(position)
  infoWindow.open(mapInstance, position)
  marker.setAnimation('AMAP_ANIMATION_BOUNCE')
  setTimeout(() => marker.setAnimation('AMAP_ANIMATION_NONE'), 800)
}

onMounted(async () => {
  if (mapContainer.value) {
    await ensureMap()
    await refreshMarkers()
    await getUserLocation()
  }
})

onBeforeUnmount(() => {
  clearMarkers()
  if (map) {
    map.destroy()
    map = null
  }
})
</script>

<style scoped>
.map-distribution {
  display: flex;
  gap: 16px;
  height: 100%;
}

.map-panel {
  flex: 1 1 70%;
  min-height: 520px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.map-container {
  width: 100%;
  height: 100%;
}

.list-panel {
  flex: 0 0 320px;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.list-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 12px;
}

.list-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2225;
}

.list-body {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.marker-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  background: #f7f9fc;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.marker-item:hover {
  background: #edf4ff;
  transform: translateX(-4px);
}

.marker-item.active {
  border: 1px solid #409eff;
  background: #e8f3ff;
}

.marker-thumb {
  width: 72px;
  height: 72px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.marker-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.marker-name {
  font-weight: 600;
  font-size: 15px;
  color: #1f2225;
}

.marker-desc {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  max-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.marker-coordinate {
  font-size: 12px;
  color: #8d939d;
  display: flex;
  gap: 8px;
}

.list-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
