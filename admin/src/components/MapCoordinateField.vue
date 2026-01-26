<template>
  <div class="map-coordinate-field">
    <div class="coordinate-inputs">
      <el-input
        v-model="localLng"
        :placeholder="lngPlaceholder"
        :readonly="readonly"
        :disabled="disabled"
        @blur="normalizeLng"
      >
        <template #prepend>经度</template>
      </el-input>
      <el-input
        v-model="localLat"
        :placeholder="latPlaceholder"
        :readonly="readonly"
        :disabled="disabled"
        @blur="normalizeLat"
      >
        <template #prepend>纬度</template>
      </el-input>
      <el-button type="primary" @click="openPicker" :disabled="readonly || disabled">
        <el-icon><Location /></el-icon>
        地图选点
      </el-button>
    </div>
    <el-dialog
      v-model="pickerVisible"
      title="地图选点"
      width="70%"
      :close-on-click-modal="false"
      append-to-body
      @closed="destroyMap"
    >
      <div class="picker-wrapper">
        <div class="picker-search">
          <el-input
            v-model="searchKeyword"
            placeholder="输入地址搜索"
            @keyup.enter.native="searchLocation"
            clearable
          />
          <el-button type="primary" :loading="searchLoading" @click="searchLocation">
            搜索
          </el-button>
        </div>
        <div ref="mapContainer" class="picker-map"></div>
        <div class="picker-info">
          <span>经度: {{ displayTempLng }}</span>
          <span>纬度: {{ displayTempLat }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" :disabled="tempLng === null || tempLat === null" @click="confirmSelection">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick, computed, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import { loadAmap, parseCoordinate, getDefaultCenter } from '@/utils/amap'

interface Props {
  lng?: number | string | null
  lat?: number | string | null
  readonly?: boolean
  disabled?: boolean
  precision?: number
}

const props = withDefaults(defineProps<Props>(), {
  lng: null,
  lat: null,
  readonly: false,
  disabled: false,
  precision: 6
})

const emit = defineEmits<{
  (e: 'update:lng', value: string | null): void
  (e: 'update:lat', value: string | null): void
}>()

const localLng = ref<string>(formatDisplay(props.lng, props.precision) ?? '')
const localLat = ref<string>(formatDisplay(props.lat, props.precision) ?? '')
const pickerVisible = ref(false)
const mapContainer = ref<HTMLDivElement | null>(null)
const tempLng = ref<number | null>(parseCoordinate(props.lng))
const tempLat = ref<number | null>(parseCoordinate(props.lat))
const searchKeyword = ref('')
const searchLoading = ref(false)

let map: any = null
let marker: any = null
let geocoder: any = null

const lngPlaceholder = computed(() => `请输入经度 (${props.precision}位小数)`)
const latPlaceholder = computed(() => `请输入纬度 (${props.precision}位小数)`)
const displayTempLng = computed(() => (tempLng.value === null ? '未选择' : tempLng.value.toFixed(props.precision)))
const displayTempLat = computed(() => (tempLat.value === null ? '未选择' : tempLat.value.toFixed(props.precision)))

watch(
  () => props.lng,
  (val) => {
    const formatted = formatDisplay(val, props.precision) ?? ''
    if (formatted !== localLng.value) {
      localLng.value = formatted
    }
    tempLng.value = parseCoordinate(val)
  }
)

watch(
  () => props.lat,
  (val) => {
    const formatted = formatDisplay(val, props.precision) ?? ''
    if (formatted !== localLat.value) {
      localLat.value = formatted
    }
    tempLat.value = parseCoordinate(val)
  }
)

watch(localLng, (val) => {
  emit('update:lng', sanitizeEmitValue(val))
})

watch(localLat, (val) => {
  emit('update:lat', sanitizeEmitValue(val))
})

watch(pickerVisible, (visible) => {
  if (!visible) {
    destroyMap()
  }
})

function sanitizeEmitValue(value: string | null): string | null {
  if (value === null) return null
  const trimmed = value.trim()
  return trimmed === '' ? null : trimmed
}

function normalizeLng() {
  localLng.value = formatDisplay(localLng.value, props.precision) ?? ''
}

function normalizeLat() {
  localLat.value = formatDisplay(localLat.value, props.precision) ?? ''
}

async function openPicker() {
  if (props.readonly || props.disabled) {
    return
  }
  pickerVisible.value = true
  tempLng.value = parseCoordinate(localLng.value)
  tempLat.value = parseCoordinate(localLat.value)
  await nextTick()
  await initMap()
}

async function initMap() {
  if (!mapContainer.value) return
  try {
    const AMap = await loadAmap()
    const center = tempLng.value !== null && tempLat.value !== null
      ? [tempLng.value, tempLat.value]
      : getDefaultCenter()
    map = new AMap.Map(mapContainer.value, {
      zoom: tempLng.value !== null ? 12 : 5,
      center,
      viewMode: '2D',
      mapStyle: 'amap://styles/normal',
      resizeEnable: true
    })
    map.on('click', (event: { lnglat: { lng: number; lat: number } }) => {
      updateTempCoordinate(event.lnglat.lng, event.lnglat.lat)
    })
    AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.Geocoder'], () => {
      map.addControl(new AMap.ToolBar({ position: 'RB' }))
      map.addControl(new AMap.Scale({ position: 'LB' }))
      geocoder = new AMap.Geocoder()
    })
    if (tempLng.value !== null && tempLat.value !== null) {
      updateMarker(tempLng.value, tempLat.value)
      openInfoWindow(tempLng.value, tempLat.value)
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('加载地图失败，请检查网络或密钥配置')
  }
}

function updateTempCoordinate(lng: number, lat: number) {
  tempLng.value = Number(lng.toFixed(props.precision))
  tempLat.value = Number(lat.toFixed(props.precision))
  updateMarker(tempLng.value, tempLat.value)
}

function updateMarker(lng: number | null, lat: number | null) {
  if (!map || lng === null || lat === null) return
  const AMap = (window as any).AMap
  if (!AMap) return
  if (!marker) {
    marker = new AMap.Marker({
      position: new AMap.LngLat(lng, lat),
      draggable: true
    })
    marker.on('dragend', (event: { lnglat: { lng: number; lat: number } }) => {
      updateTempCoordinate(event.lnglat.lng, event.lnglat.lat)
    })
    marker.setMap(map)
  } else {
    marker.setPosition(new AMap.LngLat(lng, lat))
  }
  map.setCenter(new AMap.LngLat(lng, lat))
}

function openInfoWindow(lng: number, lat: number) {
  const AMap = (window as any).AMap
  if (!AMap || !map) return
  const infoWindow = new AMap.InfoWindow({
    content: `<div style="padding: 8px;">经度: ${lng}<br/>纬度: ${lat}</div>`
  })
  infoWindow.open(map, new AMap.LngLat(lng, lat))
}

function destroyMap() {
  if (marker) {
    marker.setMap(null)
    marker = null
  }
  if (map) {
    map.destroy()
    map = null
  }
  geocoder = null
  searchKeyword.value = ''
  searchLoading.value = false
}

function handleCancel() {
  pickerVisible.value = false
}

function confirmSelection() {
  if (tempLng.value === null || tempLat.value === null) {
    ElMessage.warning('请先选择坐标')
    return
  }
  localLng.value = tempLng.value.toFixed(props.precision)
  localLat.value = tempLat.value.toFixed(props.precision)
  pickerVisible.value = false
}

function formatDisplay(value: unknown, precision = 6): string | null {
  const parsed = parseCoordinate(value)
  if (parsed === null) {
    if (value === null || value === undefined) {
      return ''
    }
    const str = String(value).trim()
    if (!str || str === 'undefined' || str === 'null') {
      return ''
    }
    return str
  }
  return parsed.toFixed(precision)
}

async function searchLocation() {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  if (!geocoder || !map) {
    ElMessage.warning('地图未初始化完成')
    return
  }
  searchLoading.value = true
  geocoder.getLocation(searchKeyword.value.trim(), (status: string, result: any) => {
    searchLoading.value = false
    if (status !== 'complete' || !result || !result.geocodes || !result.geocodes.length) {
      ElMessage.warning('未找到匹配的位置')
      return
    }
    const location = result.geocodes[0].location
    if (!location) {
      ElMessage.warning('未获取到位置信息')
      return
    }
    updateTempCoordinate(location.lng, location.lat)
    if (map) {
      map.setZoomAndCenter(15, new (window as any).AMap.LngLat(location.lng, location.lat))
    }
  })
}

onBeforeUnmount(() => {
  destroyMap()
})
</script>

<style scoped>
.map-coordinate-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.coordinate-inputs {
  display: flex;
  gap: 10px;
  align-items: center;
}

.coordinate-inputs .el-input {
  flex: 1;
}

.picker-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.picker-search {
  display: flex;
  gap: 12px;
}

.picker-map {
  height: 420px;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.picker-info {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: #555;
}
</style>
