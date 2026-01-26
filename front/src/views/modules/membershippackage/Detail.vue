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
              <span class="label">套餐名称：</span>
              <span class="text">{{ info.packagename }}</span>
            </div>
            <div class="info-item">
              <span class="label">套餐类型：</span>
              <span class="text">{{ info.packagetype }}</span>
            </div>
            <div class="info-item">
              <span class="label">价格：</span>
              <span class="text">{{ info.packageprice }}</span>
            </div>
            <div class="info-item">
              <span class="label">有效期：</span>
              <span class="text">{{ info.validdays }}</span>
            </div>
            <div class="info-item">
              <span class="label">包含课时：</span>
              <span class="text">{{ info.includedcourses }}</span>
            </div>
            <div class="info-item">
              <span class="label">套餐说明：</span>
              <span class="text">{{ info.packagedesc }}</span>
            </div>
          </div>

          <!-- 操作区：按表配置显示 -->
          <div class="actions">
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('办卡')"
              :disabled="isCrossDisabled('办卡')"
              :title="crossDisabledReason('办卡') || undefined"
              @click="onCross('办卡')"
            >办卡</el-button>
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('续卡')"
              :disabled="isCrossDisabled('续卡')"
              :title="crossDisabledReason('续卡') || undefined"
              @click="onCross('续卡')"
            >续卡</el-button>
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
const canViewPermission = useAuth('membershippackage', '查看')
const tableButtonsPermission = useTableButtons('membershippackage')
const canView = computed(() => canViewPermission.value || !token.value)
const tableButtons = computed(() => tableButtonsPermission.value)
const isMallProductTable = false
const isMallOrdersTable = false
const hideMallCancelButton = isMallProductTable || isMallOrdersTable
const mallCrossButtonNames: string[] = [
  '办卡','续卡'
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
const titleField = 'packagename'
const pageTitle = computed(() => {
  const tf = titleField
  const v = tf ? (info as any)[tf] : null
  const text = (v === undefined || v === null) ? '' : String(v).trim()
  return text || '会员卡套餐'
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
  const res: any = await http.get('membershippackage/detail/' + id)
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
  const field = 'packageimage'
  if (field) {
    const v = (info as any)[field]
    if (v) images.value = String(v).split(',').filter(Boolean)
  }
}







const crossProcessing = ref(false)

interface CrossButtonConfig {
  name: string
  targetTable: string
  tips: string
  statusField: string
  statusValue: string
  // 库存更新（可选）
  stockTargetTable?: string
  stockMode?: 'plus' | 'minus' | string
  stockField?: string
  stockAmountField?: string
  prevValue: string | null
  prevButtonName: string
  flowIndex: number
}

const crossButtonsConfig: CrossButtonConfig[] = [
  {
    name: '办卡',
    targetTable: 'cardapplication',
    tips: '办卡成功',
    statusField: 'orderstatus',
    statusValue: '未支付',
    stockTargetTable: '',
    stockMode: '',
    stockField: '',
    stockAmountField: '',
    prevValue: null,
    prevButtonName: '',
    flowIndex: -1
  },
  {
    name: '续卡',
    targetTable: 'cardrenewal',
    tips: '续卡成功',
    statusField: 'orderstatus',
    statusValue: '未支付',
    stockTargetTable: '',
    stockMode: '',
    stockField: '',
    stockAmountField: '',
    prevValue: null,
    prevButtonName: '',
    flowIndex: -1
  }]

const crossStatusFlows = new Map<string, string[]>()
const lastButtonByField = new Map<string, CrossButtonConfig>()

crossButtonsConfig.forEach((btn) => {
  if (btn.statusField && btn.statusValue) {
    const flow = crossStatusFlows.get(btn.statusField) || []
    if (!flow.includes(btn.statusValue)) {
      flow.push(btn.statusValue)
    }
    crossStatusFlows.set(btn.statusField, flow)
  }
})

crossButtonsConfig.forEach((btn) => {
  if (btn.statusField && btn.statusValue) {
    const previous = lastButtonByField.get(btn.statusField)
    btn.prevValue = previous ? previous.statusValue : null
    btn.prevButtonName = previous ? previous.name : ''
    lastButtonByField.set(btn.statusField, btn)
    const flow = crossStatusFlows.get(btn.statusField) || []
    btn.flowIndex = flow.indexOf(btn.statusValue)
  } else {
    btn.prevValue = null
    btn.prevButtonName = ''
    btn.flowIndex = -1
  }
})

const crossButtonMap = new Map<string, CrossButtonConfig>()
crossButtonsConfig.forEach((btn) => {
  crossButtonMap.set(btn.name, btn)
})
function evaluatePaymentCrossGuard(button: CrossButtonConfig, showMessage = false): GuardResult {
  const warn = (message: string): GuardResult => {
    if (showMessage && message) ElMessage.warning(message)
    return { ok: false, message }
  }
  const target = String(button.statusValue || '').trim()
  const current = orderStatusText.value
  if (target && current === target) {
    return warn(`当前状态已为${target}`)
  }
  switch (target) {
    case '已取消':
      if (payCompleted.value) return warn('已支付订单不可取消')
      if (current === '已退款') return warn('已退款订单不可取消')
      if (current === '已完成') return warn('订单已完成，无法取消')
      return { ok: true, message: '' }
    case '已退款':
      if (!payCompleted.value) return warn('未支付订单无需退款')
      if (!['已支付', '已发货'].includes(current)) {
        const message = current ? `当前状态为${current}，无法退款` : '当前状态不支持退款'
        return warn(message)
      }
      return { ok: true, message: '' }
    case '已发货':
      if (!payCompleted.value) return warn('请先完成支付')
      if (current && !['已支付', '已发货'].includes(current)) {
        const message = current === '已退款'
          ? '已退款订单无法发货'
          : `当前状态为${current}，无法发货`
        return warn(message)
      }
      return { ok: true, message: '' }
    case '已完成':
      if (current !== '已发货') {
        const message = current ? `当前状态为${current}，无法确认收货` : '请先发货后再确认收货'
        return warn(message)
      }
      return { ok: true, message: '' }
    default:
      return { ok: true, message: '' }
  }
}

function evaluateCrossGuard(button: CrossButtonConfig | undefined, showMessage = false): GuardResult {
  if (!button) {
    const message = '操作不存在'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (info.id === undefined || info.id === null) {
    const message = '请先加载详情数据'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (tableHasAudit && !auditApproved.value) {
    const message = '请先完成审核'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (tableHasPay && button.statusField && button.statusValue) {
    const fieldLower = String(button.statusField).trim().toLowerCase()
    if (fieldLower === 'orderstatus') {
      return evaluatePaymentCrossGuard(button, showMessage)
    }
  }
  // 不再强制“先支付再操作”；仅保留审核校验
  if (button.statusField && button.statusValue) {
    const currentRaw = (info as any)[button.statusField]
    const currentText = currentRaw === undefined || currentRaw === null ? '' : String(currentRaw).trim()
    const targetText = String(button.statusValue).trim()
    if (targetText && currentText === targetText) {
      const message = `当前状态已为${targetText}`
      if (showMessage) ElMessage.warning(message)
      return { ok: false, message }
    }
    if (button.prevValue) {
      const prevText = String(button.prevValue).trim()
      if (prevText && currentText !== prevText) {
        const message = button.prevButtonName
          ? `请先完成“${button.prevButtonName}”操作`
          : '请先完成上一阶段操作'
        if (showMessage) ElMessage.warning(message)
        return { ok: false, message }
      }
    }
    const flow = crossStatusFlows.get(button.statusField) || []
    if (flow.length && button.flowIndex > -1) {
      const currentIndex = flow.indexOf(currentText)
      if (currentIndex !== -1 && currentIndex > button.flowIndex) {
        const message = '当前流程已完成，无需重复操作'
        if (showMessage) ElMessage.warning(message)
        return { ok: false, message }
      }
    }
  }
  return { ok: true, message: '' }
}

function isCrossDisabled(name: string): boolean {
  const result = evaluateCrossGuard(crossButtonMap.get(name), false)
  return !result.ok
}

function crossDisabledReason(name: string): string {
  const result = evaluateCrossGuard(crossButtonMap.get(name), false)
  return result.ok ? '' : result.message
}

async function onCross(name: string){
  if (!hasActionPermission(name)) {
    ElMessage.warning('暂无权限')
    return
  }
  if (crossProcessing.value) return
  const button = crossButtonMap.get(name)
  if (!button) return
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  const guard = evaluateCrossGuard(button, true)
  if (!guard.ok) {
    return
  }
  crossProcessing.value = true
  try {
    const statusColumnName = (button.statusField || '').trim()
    const statusColumnValue = button.statusValue
    const tips = button.tips || '操作成功'
    const targetTableName = (button.targetTable || '').trim()
    const inlineOnly = !targetTableName || targetTableName === 'membershippackage'

    // 商场特例处理
    if (targetTableName === 'cart' && name === '加入购物车') {
      try {
        const uidStr = localStorage.getItem('userid') || String((myid as any).value || '')
        const uidNum = parseInt(uidStr as string, 10)
        const merchantAccountField = ''
        const merchantIdField = ''
        let merchantAccountVal = (() => {
          const raw = merchantAccountField ? (info as any)[merchantAccountField] : (info as any).merchantaccount
          return raw === undefined || raw === null ? '' : String(raw).trim()
        })()
        let merchantIdVal = (() => {
          const raw = merchantIdField ? (info as any)[merchantIdField] : (info as any).merchantid
          const num = Number(raw)
          return Number.isFinite(num) ? num : 0
        })()
        const merchantNameVal = String((info as any).merchantname || (info as any).shopname || (info as any).shangjianame || '')
        const payload: Record<string, any> = {
          tablename: (localStorage.getItem('crossTable') || 'membershippackage') || 'membershippackage',
          userid: Number.isFinite(uidNum) ? uidNum : undefined,
          goodid: (info as any).id,
          goodname: (info as any)['name'] || (pageTitle as any).value || '',
          picture: (info as any)['picture'] || ((images as any).value?.[0] || ''),
          buynumber: 1,
          price: Number((info as any)['price'] || 0),
          merchantid: merchantIdVal || null,
          merchantaccount: merchantAccountVal,
          merchantname: merchantNameVal
        }
        let payloadPlain: any
        try {
          payloadPlain = JSON.parse(JSON.stringify(payload))
        } catch (_) {
          payloadPlain = {
            tablename: String(payload.tablename||''),
            userid: Number(payload.userid||0),
            goodid: Number(payload.goodid||0),
            goodname: String(payload.goodname||''),
            picture: String(payload.picture||''),
            buynumber: Number(payload.buynumber||1),
            price: Number(payload.price||0),
            merchantid: payload.merchantid,
            merchantaccount: payload.merchantaccount,
            merchantname: payload.merchantname
          }
        }
        const res:any = await http.post('cart/add', payloadPlain)
        if (res?.code === 0) {
          ElMessage.success('已加入购物车')
        } else {
          ElMessage.error(res?.msg || '操作失败')
        }
      } finally { crossProcessing.value = false }
      return
    }

    if (targetTableName === 'orders' && name === '立即购买') {
      const imageVal = (info as any)['picture'] || ((images as any).value?.[0] || '')
      const unitVal = Number((info as any)['price'] || 0)
      const merchantAccountField = ''
      const merchantIdField = ''
      let merchantAccountVal = (() => {
        const raw = merchantAccountField ? (info as any)[merchantAccountField] : (info as any).merchantaccount
        return raw === undefined || raw === null ? '' : String(raw).trim()
      })()
      let merchantIdVal = (() => {
        const raw = merchantIdField ? (info as any)[merchantIdField] : (info as any).merchantid
        const num = Number(raw)
        return Number.isFinite(num) ? num : 0
      })()
      const merchantNameVal = String((info as any).merchantname || (info as any).shopname || (info as any).shangjianame || '')
      const item = {
        tablename: 'membershippackage',
        goodid: (info as any).id,
        goodname: (info as any)['name'] || (pageTitle as any).value || '',
        picture: imageVal,
        goodimage: imageVal,
        buynumber: 1,
        unitprice: unitVal,
        totalprice: unitVal,
        merchantid: merchantIdVal || null,
        merchantaccount: merchantAccountVal,
        merchantname: merchantNameVal
      }
      localStorage.setItem('checkoutList', JSON.stringify([item]))
      crossProcessing.value = false
      router.push('/index/checkout')
      return
    }

    // 默认跨表
    if (statusColumnName && !statusColumnName.startsWith('[')) {
      const currentVal = (info as any)[statusColumnName]
      if (currentVal === statusColumnValue) {
        ElMessage.warning(tips)
        return
      }
    }
    if (!inlineOnly) {
      // 避免 Proxy 循环：转为原始对象后再序列化
      let infoPlain: any
      try {
        infoPlain = toRaw(info)
      } catch (_) {
        infoPlain = info
      }
      try {
        infoPlain = JSON.parse(JSON.stringify(infoPlain))
      } catch (_) {
        // 兜底：仅挑选常见字段
        const pick = ['id','userid','goodid','goodname','goodimage','picture','unitprice','price','totalprice','total','tablename','orderstatus','status','ispay','addtime']
        const tmp: Record<string, any> = {}
        pick.forEach(k=> { if ((info as any)[k] !== undefined) tmp[k] = (info as any)[k] })
        infoPlain = tmp
      }
      localStorage.setItem('crossObj', JSON.stringify(infoPlain))
      localStorage.setItem('crossTable', 'membershippackage')
      if (statusColumnName) { localStorage.setItem('statusColumnName', statusColumnName) } else { localStorage.removeItem('statusColumnName') }
      if (statusColumnValue !== undefined) { localStorage.setItem('statusColumnValue', JSON.stringify(statusColumnValue)) } else { localStorage.removeItem('statusColumnValue') }
      if (button.tips) { localStorage.setItem('tips', button.tips) } else { localStorage.removeItem('tips') }
      localStorage.setItem('crossrefid', info.crossrefid && info.crossrefid > 0 ? info.crossrefid : info.id)
      // 写入库存预演配置
      try {
        if (button.stockTargetTable && button.stockField) {
          localStorage.setItem('stockUpdate.targetTable', button.stockTargetTable)
          localStorage.setItem('stockUpdate.mode', (button.stockMode || 'minus'))
          localStorage.setItem('stockUpdate.field', (button.stockField || ''))
        } else {
          ;['stockUpdate.targetTable','stockUpdate.mode','stockUpdate.field','stockUpdatePlan'].forEach(k=>localStorage.removeItem(k))
        }
      } catch (e) {}
      crossProcessing.value = false
      router.push({ path: `/index/${targetTableName}Add`, query: { type: 'cross' } })
      return
    }

    await inlineCrossAction(
      tips,
      statusColumnName,
      statusColumnValue,
      button.stockTargetTable || '',
      button.stockMode || 'minus',
      button.stockField || '',
      button.stockAmountField || '',
      button.name
    )
  } catch (error) {
    console.error('跨表操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    crossProcessing.value = false
  }
}

async function inlineCrossAction(
  tips: string,
  statusColumnName: string,
  statusColumnValue: any,
  stockTargetTable: string,
  stockMode: string,
  stockField: string,
  stockAmountField?: string,
  btnName?: string
) {
  const successMessage = tips?.trim() || '操作成功'
  const confirmTitle = (btnName || '').trim() || '提示'
  const confirmMessage = successMessage.endsWith('？')
    ? successMessage
    : `${successMessage}，是否继续？`
  const statusKey = (statusColumnName || '').trim()
  const statusTarget = statusColumnValue === undefined || statusColumnValue === null
    ? ''
    : String(statusColumnValue).trim()
  if (statusKey && statusTarget) {
    const currentStatus = String((info as any)?.[statusKey] ?? '').trim()
    if (currentStatus === statusTarget) {
      ElMessage.warning('当前状态已为目标状态，无需重复操作')
      return
    }
  }
  try {
    await ElMessageBox.confirm(confirmMessage, confirmTitle)
  } catch {
    return
  }
  try {
    if (statusKey && statusTarget) {
      const payload = JSON.parse(JSON.stringify(info))
      payload[statusKey] = statusColumnValue
      await http.post('membershippackage/update', payload)
      Object.assign(info, payload)
    }
    const target = (stockTargetTable || '').trim()
    const fieldKey = (stockField || '').trim()
    const amountKey = (stockAmountField || stockField || '').trim()
    if (!target || !fieldKey) {
      ElMessage.success(successMessage)
      if (info.id) { await loadDetail(info.id) }
      return
    }
    const refId = (info as any)?.crossrefid || (target === 'membershippackage' ? (info as any)?.id : undefined)
    if (!refId) {
      ElMessage.error('缺少 crossrefid，无法定位源记录')
      return
    }
    let amount = 1
    if (amountKey) {
      amount = Number((info as any)?.[amountKey] ?? 0)
      if (!isFinite(amount) || amount <= 0) {
        ElMessage.error('数量必须为大于0的数字')
        return
      }
    }
    const { data: src }: any = await http.get(`${target}/info/${refId}`)
    if (!src) {
      ElMessage.error('未找到源记录')
      return
    }
    const current = Number(src?.[fieldKey] ?? 0)
    if (!isFinite(current)) {
      ElMessage.error('源表数量无效')
      return
    }
    const mode = (stockMode || 'minus').trim() === 'plus' ? 'plus' : 'minus'
    const next = mode === 'minus' ? current - amount : current + amount
    if (next < 0) {
      ElMessage.error('操作后数量小于0，已阻止')
      return
    }
    const payload2 = { ...src, [fieldKey]: next }
    await http.post(`${target}/update`, payload2)

    ElMessage.success(successMessage)
    if (info.id) { await loadDetail(info.id) }
  } catch (error) {
    console.error('内联跨表操作失败:', error)
    ElMessage.error('操作失败')
  }
}

async function refreshSocialStatus(refid: number){
  if (!hasToken() || !canView.value) return
}
// 联系TA
</script>



