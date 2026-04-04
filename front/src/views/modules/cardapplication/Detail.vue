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
              <span class="label">数量：</span>
              <span class="text">{{ info.quantity }}</span>
            </div>
            <div class="info-item">
              <span class="label">状态：</span>
              <span class="text">{{ info.orderstatus }}</span>
            </div>
            <div class="info-item">
              <span class="label">物流信息：</span>
              <span class="text">{{ info.logistics }}</span>
            </div>
          </div>

          <!-- 操作区：按表配置显示 -->
          <div class="actions">
            <el-button
              size="small"
              type="primary"
              v-if="canPay"
              :disabled="payActionDisabled"
              :loading="payProcessing"
              :title="payDisabledText || undefined"
              @click="openPayDialog"
            >{{ payButtonLabel }}</el-button>
            <span v-else-if="info.ispay === '已支付'" class="pay-state-tag">已支付</span>
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('取消办卡记录')"
              :disabled="isCrossDisabled('取消办卡记录')"
              :title="crossDisabledReason('取消办卡记录') || undefined"
              @click="onCross('取消办卡记录')"
            >取消办卡记录</el-button>
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('退款')"
              :disabled="isCrossDisabled('退款')"
              :title="crossDisabledReason('退款') || undefined"
              @click="onCross('退款')"
            >退款</el-button>
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('发货')"
              :disabled="isCrossDisabled('发货')"
              :title="crossDisabledReason('发货') || undefined"
              @click="onCross('发货')"
            >发货</el-button>
            <el-button
              size="small"
              type="primary"
              v-if="hasActionPermission('确认收货')"
              :disabled="isCrossDisabled('确认收货')"
              :title="crossDisabledReason('确认收货') || undefined"
              @click="onCross('确认收货')"
            >确认收货</el-button>
          </div>
        </div>
      </div>
    </el-card>
    <el-card class="mt-4 pay-container" v-if="paySummaryVisible">
      <div class="pay-header">
        <span class="title">支付信息</span>
        <span class="status" :class="orderStatusClass">{{ orderStatusDisplay }}</span>
      </div>
      <div class="pay-content">
        <div class="pay-line" v-if="displayPayAmount">
          <span class="label">应付金额：</span>
          <span class="value amount">{{ displayPayAmount }}</span>
        </div>
        <div class="pay-line" v-if="payQuantityField && displayPayQuantity">
          <span class="label">数量：</span>
          <span class="value">{{ displayPayQuantity }}</span>
        </div>
        <div class="pay-line" v-if="balanceField && balanceText">
          <span class="label">当前余额：</span>
          <span class="value" :class="{ warning: balanceInsufficient }">{{ balanceText }}</span>
        </div>
        <el-button
          class="pay-btn"
          type="primary"
          :disabled="payActionDisabled"
          :loading="payProcessing"
          :title="payDisabledText || undefined"
          @click="openPayDialog"
        >{{ orderStatusDisplay }}</el-button>
      </div>
    </el-card>
    <el-dialog
      v-model="payDialogVisible"
      title="选择支付方式"
      width="520px"
      append-to-body
      @close="resetPayDialog"
    >
      <div class="pay-dialog">
        <div class="pay-amount" v-if="displayPayAmount">
          <span class="label">应付金额：</span>
          <span class="value">{{ displayPayAmount }}</span>
        </div>
        <div class="pay-amount" v-if="payType === 'balance' && balanceField">
          <span class="label">支付后余额：</span>
          <span class="value" :class="{ warning: balanceInsufficient }">{{ balanceAfterPayText }}</span>
        </div>
        <el-radio-group v-model="payType" class="pay-options">
          <div
            v-for="opt in payOptions"
            :key="opt.value"
            class="pay-option"
          >
            <el-radio :label="opt.value">
              <div class="option-body">
                <div class="thumb" v-if="opt.img">
                  <img :src="opt.img" :alt="opt.label" />
                </div>
                <div class="icon" v-else>
                  <el-icon><Wallet /></el-icon>
                </div>
                <div class="texts">
                  <div class="main">{{ opt.label }}</div>
                  <div class="sub" v-if="opt.value === 'balance'">
                    <span v-if="balanceField && balanceText">当前余额：{{ balanceText }}</span>
                    <span v-else>将自动扣减账户余额</span>
                  </div>
                </div>
              </div>
            </el-radio>
          </div>
        </el-radio-group>
      </div>
      <template #footer>
        <el-button @click="resetPayDialog">取消</el-button>
        <el-button type="primary" :loading="payProcessing" @click="submitPay">确认支付</el-button>
      </template>
    </el-dialog>
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
import weixinImg from '@/assets/img/pay/weixin.png'
import zhifubaoImg from '@/assets/img/pay/zhifubao.png'
import jiansheImg from '@/assets/img/pay/jianshe.png'
import jiaotongImg from '@/assets/img/pay/jiaotong.png'
import nongyeImg from '@/assets/img/pay/nongye.png'
import zhongguoImg from '@/assets/img/pay/zhongguo.png'
import { Wallet } from '@element-plus/icons-vue'
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
const canViewPermission = useAuth('cardapplication', '查看')
const tableButtonsPermission = useTableButtons('cardapplication')
const canView = computed(() => canViewPermission.value || !token.value)
const tableButtons = computed(() => tableButtonsPermission.value)
const isMallProductTable = false
const isMallOrdersTable = false
const hideMallCancelButton = isMallProductTable || isMallOrdersTable
const mallCrossButtonNames: string[] = [
  '取消办卡记录','退款','发货','确认收货'
]
const frontDisabledButtons = new Set<string>([
  '发货'
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
const canPay = computed(() => hasActionPermission('立即购买'))
const tableHasAudit = false
const tableHasPay = true

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
  return text || '办卡记录'
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

const payDialogVisible = ref(false)
const payProcessing = ref(false)
const payType = ref('')
const payAmountField = 'packageprice'
const payQuantityField = 'quantity'
const payTotalFieldName = 'packageprice'
const payOptions = [
  { value: 'balance', label: '余额支付', img: '' },
  { value: 'weixin', label: '微信支付', img: weixinImg },
  { value: 'zhifubao', label: '支付宝支付', img: zhifubaoImg },
  { value: 'jianshe', label: '建设银行', img: jiansheImg },
  { value: 'jiaotong', label: '交通银行', img: jiaotongImg },
  { value: 'nongye', label: '农业银行', img: nongyeImg },
  { value: 'zhongguo', label: '中国银行', img: zhongguoImg }
]
const userRoleTable = (localStorage.getItem('frontSessionTable') || localStorage.getItem('UserTableName') || '').replace(/"/g, '').trim()
const userInfo = reactive<Record<string, any>>({})
const balanceField = ref('money')
const balanceLoading = ref(false)

function toNumber(val: unknown): number {
  const num = Number(val)
  return Number.isFinite(num) ? Number(num) : 0
}

// 不再自动探测余额字段，固定 money

function ensureLocalUserInfo() {
  if (!Object.keys(userInfo).length && sessionObj && typeof sessionObj === 'object') {
    Object.assign(userInfo, sessionObj)
  }
  // 固定使用 money 字段
}

const payQuantity = computed(() => {
  if (!payQuantityField) return 1
  return toNumber((info as any)[payQuantityField]) || 1
})

const payAmountValue = computed(() => {
  // 优先使用总价字段，若不存在再回退为 单价×数量
  if (payTotalFieldName) {
    const total = toNumber((info as any)[payTotalFieldName])
    if (total) return Number(total.toFixed(2))
  }
  if (!payAmountField) return 0
  const unit = toNumber((info as any)[payAmountField])
  if (!unit) return 0
  const qty = payQuantity.value || 1
  return Number((unit * qty).toFixed(2))
})

const displayPayAmount = computed(() => {
  const value = payAmountValue.value
  if (!value) return ''
  return `￥${value.toFixed(2)}`
})

const displayPayQuantity = computed(() => {
  if (!payQuantityField) return ''
  const qty = payQuantity.value
  if (!qty) return ''
  return String(qty)
})

const payActionDisabled = computed(() => {
  if (payProcessing.value) return true
  if (String(info.ispay || '').trim() === '已支付') return true
  const status = orderStatusText.value
  if (status && orderStatusForbidPay.includes(status)) return true
  if (!canPay.value) return true
  if (tableHasAudit && !auditApproved.value) return true
  return false
})

const payDisabledText = computed(() => {
  if (payProcessing.value) return '处理中...'
  if (String(info.ispay || '').trim() === '已支付') return '已支付'
  const status = orderStatusText.value
  if (status && orderStatusForbidPay.includes(status)) {
    return status === '已完成' ? '订单已完成' : `订单${status}`
  }
  if (!canPay.value) return '暂无支付权限'
  if (tableHasAudit && !auditApproved.value) return '请先完成审核'
  return ''
})

const payButtonLabel = computed(() => {
  const status = orderStatusText.value
  if (status && orderStatusForbidPay.includes(status)) return status
  if (String(info.ispay || '').trim() === '已支付') return '已支付'
  if (payProcessing.value) return '处理中...'
  if (tableHasAudit && !auditApproved.value) return '待审核'
  if (!canPay.value) return '暂无权限'
  return '支付'
})

const paySummaryVisible = computed(() => canPay.value || info.ispay === '已支付')


const balanceValue = computed(() => {
  if (!balanceField.value) return 0
  return toNumber(userInfo[balanceField.value])
})

const balanceText = computed(() => {
  if (!balanceField.value) return ''
  return `￥${balanceValue.value.toFixed(2)}`
})

const balanceInsufficient = computed(() => {
  if (!payAmountValue.value || !balanceField.value) return false
  return balanceValue.value < payAmountValue.value
})

const balanceAfterPayText = computed(() => {
  if (!balanceField.value) return ''
  const remain = balanceValue.value - payAmountValue.value
  return `￥${remain.toFixed(2)}`
})


function syncSessionStorage(updated: Record<string, any>) {
  try {
    const merged = { ...sessionObj, ...updated }
    sessionObj = merged
    const idCandidate = merged.id ?? merged.userid ?? merged.userId
    if (idCandidate !== undefined && idCandidate !== null) {
      const parsed = Number(idCandidate)
      if (!Number.isNaN(parsed)) currentUserId = parsed
    }
    localStorage.setItem('frontSession', JSON.stringify(merged))
  } catch (error) {
    console.warn('更新本地会话失败', error)
  }
  notifyAuthChanged()
}

async function fetchUserSession(force = false) {
  if (!userRoleTable || !hasToken()) return
  if (!force && Object.keys(userInfo).length) return
  balanceLoading.value = true
  try {
    const res: any = await http.get(`${userRoleTable}/session`)
    const data = res?.data || {}
    Object.assign(userInfo, data)
    syncSessionStorage(userInfo)
  } catch (error) {
    console.warn('获取用户余额失败', error)
  } finally {
    balanceLoading.value = false
  }
}

function evaluatePayGuard(showMessage = false): GuardResult {
  if (!tableHasPay) {
    return { ok: true, message: '' }
  }
  if (info.id === undefined || info.id === null) {
    const message = '请先加载详情数据'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (String(info.ispay || '').trim() === '已支付') {
    const message = '订单已支付'
    if (showMessage) ElMessage.success(message)
    return { ok: false, message }
  }
  const status = orderStatusText.value
  if (status && orderStatusForbidPay.includes(status)) {
    const message = status === '已完成' ? '订单已完成' : `订单${status}`
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (!canPay.value) {
    const message = '暂无支付权限'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  if (tableHasAudit && !auditApproved.value) {
    const message = '请先完成审核'
    if (showMessage) ElMessage.warning(message)
    return { ok: false, message }
  }
  return { ok: true, message: '' }
}

const userIdForPay = computed(() => {
  if (sessionObj && (sessionObj.id || sessionObj.userid || sessionObj.userId)) {
    return Number(sessionObj.id || sessionObj.userid || sessionObj.userId)
  }
  return myid
})


function resetPayDialog() {
  payDialogVisible.value = false
  payType.value = ''
}

async function openPayDialog() {
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  const guard = evaluatePayGuard(true)
  if (!guard.ok) {
    return
  }
  ensureLocalUserInfo()
  await fetchUserSession()
  if (balanceField.value) {
    // 若余额充足，默认选中余额支付，确保联动扣减
    payType.value = balanceInsufficient.value ? '' : 'balance'
  }
  payDialogVisible.value = true
}

async function deductBalanceIfNeeded(): Promise<boolean> {
  if (payType.value !== 'balance') return true
  if (!userRoleTable || !balanceField.value) {
    ElMessage.warning('未找到余额字段，无法余额支付')
    return false
  }
  const amount = payAmountValue.value
  if (!amount) {
    ElMessage.warning('未识别到支付金额')
    return false
  }
  const current = balanceValue.value
  if (current < amount) {
    ElMessage.error('余额不足，请先充值')
    return false
  }
  const userIdVal = userIdForPay.value
  if (!userIdVal) {
    ElMessage.error('未找到用户信息，无法扣款')
    return false
  }
  const payload: Record<string, any> = { id: userIdVal }
  const remain = Number((current - amount).toFixed(2))
  payload[balanceField.value] = remain
  try {
    const res: any = await http.post(`${userRoleTable}/update`, payload)
    if (res?.code === 0) {
      userInfo[balanceField.value] = remain
      syncSessionStorage(userInfo)
      return true
    }
    ElMessage.error(res?.msg || '余额扣减失败')
    return false
  } catch (error: any) {
    ElMessage.error(error?.message || '余额扣减失败')
    return false
  }
}

async function submitPay() {
  if (payProcessing.value) return
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  const guardCheck = evaluatePayGuard(true)
  if (!guardCheck.ok) {
    return
  }
  if (!payAmountValue.value) {
    ElMessage.warning('未识别到需要支付的金额')
    return
  }
  if (!payType.value) {
    ElMessage.warning('请选择支付方式')
    return
  }
  try {
    await ElMessageBox.confirm('确认支付当前订单吗？', '提示', { type: 'warning' })
  } catch {
    return
  }
  payProcessing.value = true
  try {
    const guardAfterConfirm = evaluatePayGuard(true)
    if (!guardAfterConfirm.ok) {
      return
    }
    const deductOk = await deductBalanceIfNeeded()
    if (!deductOk) {
      return
    }
    const payload = { ...info, ispay: '已支付' }
    if ('orderstatus' in payload) {
      payload.orderstatus = '已支付'
    } else {
      payload['orderstatus'] = '已支付'
    }
    const res: any = await http.post('cardapplication/update', payload)
    if (res?.code === 0) {
      info.ispay = '已支付'
      info.orderstatus = '已支付'
      if (info.id) {
        await loadDetail(info.id)
      }
      ElMessage.success('支付成功')
      resetPayDialog()
    } else {
      ElMessage.error(res?.msg || '支付失败')
      if (payType.value === 'balance') {
        await fetchUserSession(true)
      }
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '支付失败')
  } finally {
    payProcessing.value = false
  }
}


async function loadDetail(id: number) {
  if (!id || !canView.value) return
  const res: any = await http.get('cardapplication/detail/' + id)
  Object.assign(info, res?.data || {})
  computeImages()
  await refreshSocialStatus(id)
}

onMounted(async () => {
  const id = Number((route.query as any).id)
  if (!id) return
  currentId.value = id
  await loadDetail(id)
  ensureLocalUserInfo()
  fetchUserSession().catch(() => {})
})

watch(canView, (val) => {
  if (val && currentId.value) {
    loadDetail(currentId.value)
  }
  if (val) {
    ensureLocalUserInfo()
    fetchUserSession().catch(() => {})
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
    name: '取消办卡记录',
    targetTable: 'cancelcardapplication',
    tips: '办卡记录已取消',
    statusField: 'orderstatus',
    statusValue: '已取消',
    stockTargetTable: '',
    stockMode: '',
    stockField: '',
    stockAmountField: '',
    prevValue: null,
    prevButtonName: '',
    flowIndex: -1
  },
  {
    name: '退款',
    targetTable: 'refundcardapplication',
    tips: '退款申请已提交，请等待管理员审核',
    statusField: 'orderstatus',
    statusValue: '退款审核中',
    stockTargetTable: '',
    stockMode: '',
    stockField: '',
    stockAmountField: '',
    prevValue: null,
    prevButtonName: '',
    flowIndex: -1
  },
  {
    name: '发货',
    targetTable: '',
    tips: '已发货',
    statusField: 'orderstatus',
    statusValue: '已发货',
    stockTargetTable: '',
    stockMode: '',
    stockField: '',
    stockAmountField: '',
    prevValue: null,
    prevButtonName: '',
    flowIndex: -1
  },
  {
    name: '确认收货',
    targetTable: '',
    tips: '已确认收货',
    statusField: 'orderstatus',
    statusValue: '已完成',
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
    const inlineOnly = !targetTableName || targetTableName === 'cardapplication'

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
          tablename: (localStorage.getItem('crossTable') || 'cardapplication') || 'cardapplication',
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
        tablename: 'cardapplication',
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
      localStorage.setItem('crossTable', 'cardapplication')
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
      await http.post('cardapplication/update', payload)
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
    const refId = (info as any)?.crossrefid || (target === 'cardapplication' ? (info as any)?.id : undefined)
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



