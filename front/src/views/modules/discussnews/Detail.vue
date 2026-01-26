<template>
  <div v-if="canView" class="p-4 front-detail-page">
    <div class="mb-3 back-btn-box">
      <el-button @click="back" class="back-btn">返回</el-button>
    </div>
    <el-card class="detail-container">
      <div class="detail-main">
        <!-- 左侧图片区：取第一个图片字段，多图展示 -->
        <div class="images-box">
          <template v-if="images.length">
            <el-image v-for="(pic,i) in images" :key="i" :src="imgSrc(pic)" fit="cover" class="image" :preview-src-list="images.map(imgSrc)"/>
          </template>
        </div>

        <!-- 右侧信息 -->
        <div class="detail-content">
          <div class="title">
              {{ pageTitle }}
          </div>
          <div class="infos-unique">
            <span class="icon">🕒</span>
            <span class="label">发布时间：</span>
            <span class="text">{{ (info.addtime || '').toString().split(' ')[0] }}</span>
          </div>

          <!-- 字段列表：改用后端富化的 formFields，并在前端仅排除图片类字段（左侧已集中展示） -->
          <div class="infos">
            <div class="info-item">
              <span class="label">关联表id：</span>
              <span class="text">{{ info.refid }}</span>
            </div>
            <div class="info-item">
              <span class="label">头像：</span>
              <span class="text">{{ info.avatarurl }}</span>
            </div>
            <div class="info-item">
              <span class="label">评论内容：</span>
              <span class="text">{{ info.content }}</span>
            </div>
            <div class="info-item">
              <span class="label">回复内容：</span>
              <span class="text">{{ info.reply }}</span>
            </div>
          </div>

          <!-- 操作区：按表配置显示 -->
          <div class="actions">
          </div>
        </div>
      </div>
    </el-card>
  </div>
  <div v-else class="p-6 front-detail-page">
    <el-result icon="warning" title="暂无权限查看该模块" sub-title="请联系管理员或更换角色" />
    <div style="margin-top: 16px;" class="back-btn-box">
      <el-button type="primary" @click="back" class="back-btn">返回</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, onBeforeUnmount, watch, toRaw } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { http } from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuth, useTableButtons, useAuthVersion, notifyAuthChanged } from '@/utils/auth'
import RichEditor from '@/components/RichEditor.vue'

const route = useRoute()
const router = useRouter()
const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const resolveFileUrl = (path: string) => {
  if (!path) return ''
  if (/^https?:\/\//.test(path)) return path
  return baseUrl + (path.startsWith('/') ? path : `/${path}`)
}

const musicConfigMeta = {
  enabled: false,
  nameField: '',
  artistField: '',
  coverField: '',
  urlField: '',
  lyricsField: ''
}

const musicPlayerRef = ref<any>(null)
const currentMusic = ref<MusicTrack | null>(null)

const formatFileName = (path: string, index: number) => {
  if (!path) return `附件${index + 1}`
  const segments = path.split('/')
  const name = segments[segments.length - 1] || ''
  return name ? decodeURIComponent(name) : `附件${index + 1}`
}

const extractFirstValue = (input: unknown): string => {
  if (input === null || input === undefined) return ''
  if (Array.isArray(input)) {
    for (const item of input) {
      const candidate = extractFirstValue(item)
      if (candidate) return candidate
    }
    return ''
  }
  const text = String(input).trim()
  if (!text) return ''
  if (text.includes(',')) {
    const parts = text.split(',').map(part => part.trim()).filter(Boolean)
    return parts.length ? parts[0] : ''
  }
  return text
}

const openFileLink = (path: string) => {
  const url = resolveFileUrl(path)
  if (!url) return
  const win = window.open(url, '_blank')
  win?.focus()
}

const username = localStorage.getItem('username') || ''
const sessionRawCommon = localStorage.getItem('frontSession')
let sessionObj: any = {}
try {
  sessionObj = sessionRawCommon ? JSON.parse(sessionRawCommon) : {}
} catch (error) {
  sessionObj = {}
}
const token = ref(localStorage.getItem('frontToken') || '')
const authVersion = useAuthVersion()
const canViewPermission = useAuth('discussnews', '查看')
const tableButtonsPermission = useTableButtons('discussnews')
const canView = computed(() => canViewPermission.value || !token.value)
const tableButtons = computed(() => tableButtonsPermission.value)
const isMallProductTable = false
const isMallOrdersTable = false
const hideMallCancelButton = isMallProductTable || isMallOrdersTable
const mallCrossButtonNames: string[] = [
]
const frontDisabledButtons = new Set<string>([
])
const actionPermissionSet = computed(() => {
  const granted = new Set<string>(tableButtons.value)
  mallCrossButtonNames.forEach((name) => {
    if (name && !frontDisabledButtons.has(name)) granted.add(name)
  })
  if (isMallProductTable) {
    if (!frontDisabledButtons.has('立即购买')) granted.add('立即购买')
    if (!frontDisabledButtons.has('加入购物车')) granted.add('加入购物车')
  }
  if (isMallOrdersTable) {
    if (!frontDisabledButtons.has('立即购买')) granted.add('立即购买')
  }
  return granted
})
function hasActionPermission(name: string): boolean {
  if (!name || frontDisabledButtons.has(name)) return false
  return actionPermissionSet.value.has(name)
}
const canComment = computed(() => tableButtons.value.includes('评论'))
const canFavorite = computed(() => tableButtons.value.includes('收藏'))
const canChat = computed(() => tableButtons.value.includes('私聊'))
const tableHasAudit = false
const tableHasPay = false

const auditApproved = computed(() => {
  if (!tableHasAudit) return true
  const candidates = [
    (info as any).auditstatus,
    (info as any).auditStatus,
    (info as any).audit,
    (info as any).auditstate,
    (info as any).checkstatus,
    (info as any).approvalstatus,
    (info as any).shenhe,
    (info as any).shenhezhuangtai
  ]
  return candidates.some((val) => {
    if (val === undefined || val === null) return false
    const text = String(val).trim()
    return text === '通过'
  })
})

const payCompleted = computed(() => String(info.ispay || '').trim() === '已支付')
const orderStatusText = computed(() => String((info as any).orderstatus || '').trim())
const orderStatusForbidPay = ['已取消', '已退款', '已完成']
const orderStatusMeta = computed(() => {
  const status = orderStatusText.value
  if (status) {
    switch (status) {
      case '未支付':
        return { label: '未支付', className: 'status-unpaid unpaid' }
      case '已支付':
        return { label: '已支付', className: 'status-paid paid' }
      case '已取消':
        return { label: '已取消', className: 'status-cancel review' }
      case '已退款':
        return { label: '已退款', className: 'status-refund review' }
      case '已发货':
        return { label: '已发货', className: 'status-shipping paid' }
      case '已完成':
        return { label: '已完成', className: 'status-finished paid' }
      default:
        return { label: status, className: 'status-unpaid unpaid' }
    }
  }
  if (tableHasAudit && !auditApproved.value) {
    return { label: '待审核', className: 'status-review review' }
  }
  if (payCompleted.value) {
    return { label: '已支付', className: 'status-paid paid' }
  }
  return { label: '待支付', className: 'status-unpaid unpaid' }
})
const orderStatusDisplay = computed(() => orderStatusMeta.value.label)
const orderStatusClass = computed(() => orderStatusMeta.value.className)

interface GuardResult {
  ok: boolean
  message: string
}
const info = reactive<any>({})
const images = ref<string[]>([])
const detailMusicTrack = computed<MusicTrack | null>(() => {
  if (!musicConfigMeta.enabled || !musicConfigMeta.urlField) return null
  const urlRaw = extractFirstValue((info as any)[musicConfigMeta.urlField])
  if (!urlRaw) return null
  const url = resolveFileUrl(urlRaw)
  if (!url) return null
  const nameRaw = musicConfigMeta.nameField ? extractFirstValue((info as any)[musicConfigMeta.nameField]) : ''
  const artistRaw = musicConfigMeta.artistField ? extractFirstValue((info as any)[musicConfigMeta.artistField]) : ''
  const coverRaw = musicConfigMeta.coverField ? extractFirstValue((info as any)[musicConfigMeta.coverField]) : ''
  const lyricsRaw = musicConfigMeta.lyricsField ? (info as any)[musicConfigMeta.lyricsField] : ''
  const cover = coverRaw ? resolveFileUrl(coverRaw) : (images.value.length ? imgSrc(images.value[0]) : '')
  return {
    id: (info as any).id ?? url,
    name: nameRaw || pageTitle.value,
    artist: artistRaw,
    cover,
    url,
    lyrics: typeof lyricsRaw === 'string' ? lyricsRaw : (lyricsRaw ?? '').toString()
  }
})
const musicPlayable = computed(() => detailMusicTrack.value !== null)
watch(
  detailMusicTrack,
  track => {
    if (!track) {
      currentMusic.value = null
      return
    }
    if (currentMusic.value && currentMusic.value.id === track.id) {
      currentMusic.value = track
    }
  },
  { immediate: true }
)
// 页面标题：优先使用后端富化的稳定标题字段
const titleField = 'nickname'
const pageTitle = computed(() => {
  const tf = titleField
  const v = tf ? (info as any)[tf] : null
  const text = (v === undefined || v === null) ? '' : String(v).trim()
  return text || '公告资讯评论表'
})
const fav = ref(false)
const favId = ref<number | null>(null)
const liked = ref(false)
const stepped = ref(false)
const likeId = ref<number | null>(null)
const stepId = ref<number | null>(null)
const currentId = ref<number | null>(null)
let currentUserId = Number(localStorage.getItem('frontUserid') || localStorage.getItem('userid') || sessionObj.id || sessionObj.userid || sessionObj.userId || 0)
function hasToken(){ return !!token.value }

const playCurrentMusic = () => {
  if (!musicPlayable.value || !detailMusicTrack.value) return
  currentMusic.value = detailMusicTrack.value
}

if (typeof window !== 'undefined') {
  window.addEventListener('front-auth-updated', () => {
    try {
      token.value = localStorage.getItem('frontToken') || ''
      const raw = localStorage.getItem('frontSession')
      if (raw) {
        const parsed = JSON.parse(raw)
        const idCandidate = parsed?.id ?? parsed?.userid ?? parsed?.userId
        if (idCandidate !== undefined && idCandidate !== null) {
          const asNumber = Number(idCandidate)
          if (!Number.isNaN(asNumber)) currentUserId = asNumber
        }
      }
    } catch (error) {
      console.warn('更新评论用户ID失败', error)
    }
  })
}

function back(){ router.back() }
function imgSrc(p:string){ return /^https?:\/\//.test(p) ? p : (baseUrl + p) }



async function loadDetail(id: number) {
  if (!id || !canView.value) return
  const res: any = await http.get('discussnews/detail/' + id)
  Object.assign(info, res?.data || {})
  computeImages()
  await refreshSocialStatus(id)
}

onMounted(async () => {
  const id = Number((route.query as any).id)
  if (!id) return
  currentId.value = id
  await loadDetail(id)
})

watch(canView, (val) => {
  if (val && currentId.value) {
    loadDetail(currentId.value)
  }
})


watch(authVersion, () => {
  token.value = localStorage.getItem('frontToken') || ''
})

function computeImages(){
  images.value = []
  const field = 'nickname'
  if (field) {
    const v = (info as any)[field]
    if (v) images.value = String(v).split(',').filter(Boolean)
  }
}








async function refreshSocialStatus(refid: number){
  if (!hasToken() || !canView.value) return
}
// 联系TA
</script>



