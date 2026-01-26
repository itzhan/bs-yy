


<template>
  <div v-if="canView" class="front-list-page">
    <div class="breadcrumb-container">
      <el-breadcrumb class="breadcrumb-box" :separator="'>'">
        <el-breadcrumb-item class="breadcrumb-item item-home" to="/">
          <a>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item class="breadcrumb-item item-module">
          <a href="javascript:void(0);">会员交流评论表</a>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div v-if="centerType" class="back-btn-box">
      <el-button class="back-btn" size="small" @click="handleBack">
        <el-icon><ArrowLeft /></el-icon>
        <span>返回</span>
      </el-button>
    </div>

    <div v-if="categoryOptions.length" class="category-container">
      <div
        class="category-item"
        :class="currentCategory === '全部' ? 'active' : ''"
        @click="selectCategory('全部')"
      >全部</div>
      <div
        v-for="item in categoryOptions"
        :key="item.value"
        class="category-item"
        :class="currentCategory === item.value ? 'active' : ''"
        @click="selectCategory(item.value)"
      >
        <span>{{ item.label }}</span>
      </div>
    </div>

    <el-form :inline="true" :model="formSearch" class="search-container">


      <div class="search-actions">
        <el-button type="primary" @click="triggerSearch">查询</el-button>
        <el-button v-if="canCreate" type="primary" @click="handleAdd">添加</el-button>
      </div>
    </el-form>


    <div class="filter-container">
    </div>

    <div v-if="sortButtons.length" class="sort-container">
      <el-button
        v-for="btn in sortButtons"
        :key="btn.field"
        class="sort-btn"
        :type="sortField === btn.field ? 'primary' : 'default'"
        @click="toggleSort(btn.field)"
      >
        <el-icon v-if="sortField !== btn.field"><component :is="btn.icon" /></el-icon>
        <el-icon v-else-if="sortOrder === 'desc'"><SortDown /></el-icon>
        <el-icon v-else><SortUp /></el-icon>
        <span class="text">{{ btn.label }}</span>
      </el-button>
    </div>

    <div v-if="!mapConfig.enabled" class="list-container">
      <el-empty class="no-access" v-if="!loading && renderList.length === 0" description="暂无数据" />
      <div
        v-for="item in renderList"
        :key="item.id"
        class="list-item-row"
        @click="toDetail(item)"
      >
        <div class="thumb-box">
          <img v-if="coverOf(item)" :src="coverOf(item)" alt="thumb" />
          <div v-else class="no-cover">无图</div>
        </div>
        <div class="info-box">
          <div class="title">
              {{ titleOf(item) }}
          </div>
          <div class="info-item">
            <span class="label">头像：</span>
            <span class="value">{{ formatField(item.avatarurl) }}</span>
          </div>
          <div class="info-item">
            <span class="label">评论内容：</span>
            <span class="value">{{ formatField(item.content) }}</span>
          </div>
          <div class="info-item">
            <span class="label">回复内容：</span>
            <span class="value">{{ formatField(item.reply) }}</span>
          </div>
          <div class="info-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ formatField(item.addtime) }}</span>
          </div>
          <div class="stats" v-if="statsAvailable">
            <div v-if="showLike" class="stat"><span class="icon">👍</span>{{ statValue('like', item) }}</div>
            <div v-if="showFavorite" class="stat"><span class="icon">⭐</span>{{ statValue('favorite', item) }}</div>
            <div v-if="showClick" class="stat"><span class="icon">👁</span>{{ statValue('click', item) }}</div>
            <div v-if="showDiscuss" class="stat"><span class="icon">💬</span>{{ statValue('discuss', item) }}</div>
          </div>
        </div>
        <div class="btns">
          <el-button type="primary" size="small">查看详情</el-button>
        </div>
      </div>
    </div>
    <div class="pagination-container">
      <el-pagination
        :current-page="pageIndex"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, sizes, jumper"
        :page-sizes="pageSizes.length ? pageSizes : [10, 20, 50]"
        background
        @current-change="handlePage"
        @size-change="handleSize"
      />
    </div>
    <section v-if="showHotRecommend && hotRecommend.length" class="area-container hot-recommend">
      <div class="area-title-box">
        <h2 class="area-title">会员交流评论表热门推荐</h2>
        <div class="area-subhead">DISCUSSSHARE RECOMMEND</div>
      </div>
      <div class="area-content media">
        <div
          class="area-content-item"
          v-for="item in hotRecommend"
          :key="item.id"
          @click="toDetail(item)"
        >
          <div class="thumb-box">
            <img v-if="coverOf(item)" :src="coverOf(item)" alt="cover" />
            <div v-else class="no-cover">无图</div>
          </div>
          <div class="info-box">
            <div class="info-items">
              <div class="info-item" v-for="field in hotSummaryFields" :key="field.name">
                <span class="label">{{ field.label }}：</span>
                <template v-if="field.formType === 'file'">
                  <span class="text file-links" v-if="item[field.name]">
                    <el-link
                      v-for="(fileItem, fileIndex) in String(item[field.name] || '').split(',').filter(Boolean)"
                      :key="fileIndex"
                      type="primary"
                      :underline="false"
                      @click.stop="openFileLink(fileItem)"
                    >
                      {{ formatFileName(fileItem, fileIndex) }}
                    </el-link>
                  </span>
                  <span class="text" v-else>暂无文件</span>
                </template>
                <span
                  v-else-if="field.formType === 'editor'"
                  class="text rich-text"
                  v-html="item[field.name] || ''"
                ></span>
                <span v-else class="text">{{ formatField(item[field.name]) }}</span>
              </div>
            </div>
            <div class="info-time">
              <span class="icon">🕒</span>
              <span class="label">发布时间：</span>
              <span class="text">{{ (item.addtime || '').toString().split(' ')[0] }}</span>
            </div>
            <div class="info-stats">
              <span v-if="showLike" class="stat"><span class="icon">👍</span><span class="num">{{ statValue('like', item) }}</span></span>
              <span v-if="showFavorite" class="stat"><span class="icon">⭐</span><span class="num">{{ statValue('favorite', item) }}</span></span>
              <span v-if="showClick" class="stat"><span class="icon">👁</span><span class="num">{{ statValue('click', item) }}</span></span>
              <span v-if="showDiscuss" class="stat"><span class="icon">💬</span><span class="num">{{ statValue('discuss', item) }}</span></span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <div v-else class="no-access">
    <el-empty description="暂无权限查看该模块" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, SortDown, SortUp, View, Star, Pointer } from '@element-plus/icons-vue'
import { http } from '@/utils/request'
import { useAuth, useAuthVersion } from '@/utils/auth'

const router = useRouter()
const route = useRoute()
const token = ref(localStorage.getItem('frontToken') || '')
const authVersion = useAuthVersion()
const catePreferEnabled = false
const catePreferStorageBase = 'gym_vclqwy4_catePrefer'
const buildCatePreferKey = () => {
  const uid = localStorage.getItem('frontUserid') || localStorage.getItem('userid') || ''
  return uid ? `${catePreferStorageBase}_${uid}` : catePreferStorageBase
}
function resolveCatePreferParam(table: string) {
  if (!catePreferEnabled || !table) return ''
  try {
    const raw = localStorage.getItem(buildCatePreferKey())
    if (!raw) return ''
    const parsed = JSON.parse(raw)
    const values = parsed?.[table]
    if (Array.isArray(values) && values.length) {
      return values.join(',')
    }
  } catch (error) {
    console.warn('读取分类偏好失败', error)
  }
  return ''
}

const canViewPermission = useAuth('discussshare', '查看')
const canCreate = !false && useAuth('discussshare', '新增')
const canView = computed(() => canViewPermission.value || !token.value)

const imageField = 'nickname'
const titleField = 'nickname'
const summaryFields = [
  { name: 'avatarurl', label: '头像', formType: '' },
  { name: 'content', label: '评论内容', formType: '' },
  { name: 'reply', label: '回复内容', formType: '' },
  { name: 'addtime', label: '创建时间', formType: '' }
]
const hotSummaryFields = computed(() => summaryFields.slice(0, 2))

const selectFilters = [
]
const categoryConfig = null
const showLike = false
const showFavorite = false
const showClick = false
const showDiscuss = false
const statsAvailable = computed(() => showLike || showFavorite || showClick || showDiscuss)

const formSearch = reactive<Record<string, any>>({
})

const selectState = reactive<Record<string, number>>({
})

const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)
const pageSizes = ref<number[]>([])
const dataList = ref<any[]>([])
const loading = ref(false)
const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const HOT_RECOMMEND_LIMIT = 4
const enableHotRecommend = false
const hotRecommend = ref<any[]>([])
const hotLoading = ref(false)
const renderList = computed(() => dataList.value)
const currentCategory = ref('全部')
const categoryOptions = ref<Array<{ label: string; value: string; image?: string }>>([])

const centerType = computed(() => route.query.centerType === '1' || route.query.centerType === 'true')
const showHotRecommend = computed(() => enableHotRecommend && !centerType.value)
const detailRoute = '/index/discussshareDetail'
const detailQuery = computed(() => {
  const query: Record<string, any> = {}
  if (centerType.value) query.centerType = 1
  return query
})

const sortField = ref<string>('id')
const sortOrder = ref<'asc' | 'desc'>('desc')

const sortButtons = computed(() => {
  const items: Array<{ field: string; label: string; icon: any }> = []
  if (showClick) items.push({ field: 'clicknum', label: '点击量', icon: View })
  if (showFavorite) items.push({ field: 'favoritenum', label: '收藏数', icon: Star })
  if (showLike) items.push({ field: 'likenum', label: '点赞数', icon: Pointer })
  return items
})

function formatField(val: unknown) {
  if (val === null || val === undefined || val === '') return '—'
  if (Array.isArray(val)) return val.join('、')
  return String(val)
}

const resolveFileUrl = (path: string) => {
  if (!path) return ''
  if (/^https?:\/\//.test(path)) return path
  if (!baseUrl) {
    return path.startsWith('/') ? path : `/`
  }
  return baseUrl + (path.startsWith('/') ? path : `/`)
}

const formatFileName = (path: string, index: number) => {
  if (!path) return `附件1`
  const segments = path.split('/')
  const raw = decodeURIComponent(segments[segments.length - 1] || '')
  if (!raw) return `附件1`
  const dot = raw.lastIndexOf('.')
  const extension = dot > 0 ? raw.slice(dot) : ''
  const basename = dot > 0 ? raw.slice(0, dot) : raw
  const limit = 12
  const truncated = basename.length > limit ? basename.slice(0, limit) + '…' : basename
  return truncated + extension
}

const openFileLink = (path: string) => {
  const url = resolveFileUrl(path)
  if (!url) return
  const win = window.open(url, '_blank')
  win?.focus()
}

const mapConfig = {
  enabled: false,
  title: '',
  nameField: '',
  descField: '',
  imageField: '',
  latField: '',
  lngField: ''
}
const toCoordinateNumber = (value: unknown): number | null => {
  if (value === null || value === undefined) return null
  const num = Number.parseFloat(String(value).trim())
  if (Number.isFinite(num)) {
    return Number(num.toFixed(6))
  }
  return null
}
const mapMarkers = computed(() => {
  if (!mapConfig.enabled) {
    return []
  }
  return dataList.value
    .map((row: Record<string, any>, index: number) => {
      const lng = toCoordinateNumber(row?.[mapConfig.lngField])
      const lat = toCoordinateNumber(row?.[mapConfig.latField])
      if (lng === null || lat === null) {
        return null
      }
      const nameSource = mapConfig.nameField ? row?.[mapConfig.nameField] : null
      const descSource = mapConfig.descField ? row?.[mapConfig.descField] : null
      const markerImage = (() => {
        if (mapConfig.imageField) {
          const raw = row?.[mapConfig.imageField]
          if (raw) {
            const first = String(raw).split(',')[0]
            return resolveFileUrl(first)
          }
        }
        const cover = coverOf(row)
        return cover || ''
      })()
      return {
        id: row?.id ?? `--`,
        name: nameSource ? String(nameSource) : titleOf(row),
        description: descSource ? String(descSource) : '',
        image: markerImage,
        lng,
        lat
      }
    })
    .filter((item): item is {
      id: string | number
      name: string
      description: string
      image: string
      lng: number
      lat: number
    } => item !== null)
})

function coverOf(item: Record<string, any>) {
  if (!imageField) return ''
  const raw = item[imageField]
  if (!raw) return ''
  const first = String(raw).split(',')[0]
  return /^https?:\/\//.test(first)
    ? first
    : (import.meta.env.VITE_API_BASE_URL || '') + (first.startsWith('/') ? first : '/' + first)
}

function titleOf(item: Record<string, any>) {
  if (titleField && item[titleField]) return item[titleField]
  if (summaryFields.length && item[summaryFields[0].name]) return item[summaryFields[0].name]
  return '会员交流评论表'
}

function statValue(key: 'like' | 'favorite' | 'click' | 'discuss', row: Record<string, any>) {
  let field = ''
  if (key === 'like') field = 'likenum'
  if (key === 'favorite') field = 'favoritenum'
  if (key === 'click') field = 'clicknum'
  if (key === 'discuss') field = 'discussnum'
  return field ? (row[field] ?? 0) : 0
}


function selectDictOption(field: string, index: number) {
  selectState[field] = index
  formSearch[field] = index === -1
    ? ''
    : (selectFilters.find(item => item.field === field)?.options[index] || '')
  pageIndex.value = 1
  fetchList()
}

function selectCategory(val: string) {
  currentCategory.value = val
  pageIndex.value = 1
  fetchList()
}

function toggleSort(field: string) {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
  } else {
    sortField.value = field
    sortOrder.value = 'desc'
  }
  fetchList()
}

function triggerSearch() {
  pageIndex.value = 1
  fetchList()
}

function handleAdd() {
  const query: Record<string, any> = {}
  if (centerType.value) query.centerType = 1
  router.push({ path: '/index/discussshareAdd', query })
}

function handleBack() {
  router.push({ path: '/index/center' })
}

function handlePage(page: number) {
  fetchList(page)
}

function handleSize(size: number) {
  pageSize.value = size
  fetchList(1)
}

function toDetail(row: Record<string, any>) {
  const query: Record<string, any> = { id: row.id }
  if (centerType.value) query.centerType = 1
  router.push({ path: '/index/discussshareDetail', query })
}

async function loadCategoryOptions() {
  if (!categoryConfig) return
  try {
    const res: any = await http.get(`${categoryConfig.table}/list`, { params: { page: 1, limit: 1000, sort: 'id', order: 'asc' } })
    const list = Array.isArray(res?.data?.list)
      ? res.data.list
      : Array.isArray(res?.data?.data?.list)
        ? res.data.data.list
        : []
    categoryOptions.value = list
      .map((row: any) => {
        const value = row?.[categoryConfig.column]
        if (!value) return null
        const image = categoryConfig.imageField && row?.[categoryConfig.imageField]
          ? (() => {
              const raw = String(row[categoryConfig.imageField]).split(',')[0]
              return /^https?:\/\//.test(raw)
                ? raw
                : (import.meta.env.VITE_API_BASE_URL || '') + (raw.startsWith('/') ? raw : '/' + raw)
            })()
          : ''
        return { label: value, value, image }
      })
      .filter(Boolean) as Array<{ label: string; value: string; image?: string }>
  } catch (error) {
    console.warn('加载分类失败', error)
    categoryOptions.value = []
  }
}

async function fetchList(page = pageIndex.value) {
  if (!canView.value) {
    dataList.value = []
    total.value = 0
    return
  }
  loading.value = true
  try {
    const baseParams: Record<string, any> = {
      page,
      limit: pageSize.value
    }
    if (token.value) {
      const preferParam = resolveCatePreferParam('discussshare')
      if (preferParam) {
        baseParams.catePrefer = preferParam
      }
    }
    const query: Record<string, any> = {}




    if (categoryConfig && currentCategory.value && currentCategory.value !== '全部') {
      query[categoryConfig.field] = currentCategory.value
    }


    if (sortField.value && sortField.value !== 'id') {
      query.sort = sortField.value
      query.order = sortOrder.value
    } else {
      query.sort = 'id'
      query.order = 'desc'
    }

    const endpoint = centerType.value ? 'discussshare/page' : 'discussshare/list'
    const res: any = await http.get(endpoint, { params: { ...baseParams, ...query } })
    const payload = res?.data
    const list = Array.isArray(payload?.data?.list)
      ? payload.data.list
      : Array.isArray(payload?.list)
        ? payload.list
        : []
    dataList.value = Array.isArray(list) ? list : []
    total.value = Number(payload?.data?.total ?? payload?.total ?? dataList.value.length)
    pageSize.value = Number(payload?.data?.pageSize ?? payload?.pageSize ?? pageSize.value)
    pageIndex.value = Number(payload?.data?.currPage ?? payload?.currPage ?? page)
    if (!pageSizes.value.length) {
      pageSizes.value = [pageSize.value, pageSize.value * 2, pageSize.value * 3, pageSize.value * 5]
    }
  } catch (error) {
    console.error('加载列表失败', error)
    dataList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

async function fetchHotRecommend(force = false) {
  if (!enableHotRecommend) return
  if (centerType.value || !canView.value) {
    if (hotRecommend.value.length) hotRecommend.value = []
    return
  }
  if (hotLoading.value && !force) return
  hotLoading.value = true
  try {
    const endpoint = 'discussshare/' + (token.value ? 'autoSort2' : 'autoSort')
    const hotParams: Record<string, any> = { page: 1, limit: HOT_RECOMMEND_LIMIT }
    if (token.value) {
      const preferParam = resolveCatePreferParam('discussshare')
      if (preferParam) {
        hotParams.catePrefer = preferParam
      }
    }
    const res: any = await http.get(endpoint, { params: hotParams })
    const payload = res?.data
    const list = Array.isArray(payload?.list)
      ? payload.list
      : Array.isArray(payload?.data?.list)
        ? payload.data.list
        : Array.isArray(res?.list)
          ? res.list
          : []
    hotRecommend.value = Array.isArray(list) ? list.slice(0, HOT_RECOMMEND_LIMIT) : []
  } catch (error) {
    console.warn('加载热门推荐失败', error)
    if (force) hotRecommend.value = []
  } finally {
    hotLoading.value = false
  }
}

onMounted(async () => {
  if (categoryConfig) {
    await loadCategoryOptions()
  }
  const initialCate = typeof route.query.homeFenlei === 'string' ? route.query.homeFenlei : ''
  if (initialCate) {
    currentCategory.value = initialCate
  }
  await fetchList()
  if (enableHotRecommend) {
    await fetchHotRecommend(true)
  }
})

watch(() => route.query.homeFenlei, (val) => {
  const next = typeof val === 'string' && val ? val : '全部'
  if (currentCategory.value !== next) {
    currentCategory.value = next
    pageIndex.value = 1
    fetchList()
  }
})

watch(() => route.query.centerType, () => {
  pageIndex.value = 1
  fetchList()
  if (enableHotRecommend) {
    if (!centerType.value) {
      fetchHotRecommend(true)
    } else {
      hotRecommend.value = []
    }
  }
})

watch(canView, (val) => {
  if (val) {
    fetchList()
    if (enableHotRecommend) {
      fetchHotRecommend(true)
    }
  } else {
    dataList.value = []
    total.value = 0
    hotRecommend.value = []
  }
})

watch(authVersion, () => {
  token.value = localStorage.getItem('frontToken') || ''
  fetchList()
  if (enableHotRecommend) {
    fetchHotRecommend(true)
  }
})
</script>

<style scoped>
.map-view-container {
  margin: 20px 0;
}
</style>
