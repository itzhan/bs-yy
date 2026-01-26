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
            <span class="spacer"></span>
            <span class="icon">👁</span>
            <span class="label">浏览：</span>
            <span class="text">{{ info.clicknum || 0 }}</span>
            <span class="spacer"></span>
            <span class="icon">💬</span>
            <span class="label">评论：</span>
            <span class="text">{{ discussCount }}</span>
          </div>

          <!-- 字段列表：改用后端富化的 formFields，并在前端仅排除图片类字段（左侧已集中展示） -->
          <div class="infos">
            <div class="info-item">
              <span class="label">标题：</span>
              <span class="text">{{ info.title }}</span>
            </div>
            <div class="info-item">
              <span class="label">简介：</span>
              <span class="text">{{ info.introduction }}</span>
            </div>
            <div class="info-item">
              <span class="label">分类名称：</span>
              <span class="text">{{ info.typename }}</span>
            </div>
            <div class="info-item">
              <span class="label">发布人：</span>
              <span class="text">{{ info.name }}</span>
            </div>
            <div class="info-item">
              <span class="label">内容：</span>
              <div class="text rich-text" v-html="info.content || ''"></div>
            </div>
          </div>

          <!-- 操作区：按表配置显示 -->
          <div class="actions">
            <template v-if="!liked && !stepped">
              <el-button size="small" @click="doLike">👍 {{ info.likenum || 0 }}</el-button>
              <el-button size="small" @click="doStep">👎 {{ info.stepnum || 0 }}</el-button>
            </template>
            <template v-else>
              <el-button v-if="liked" size="small" type="success" @click="cancelLike">已赞（{{ info.likenum || 0 }}）</el-button>
              <el-button v-if="stepped" size="small" type="danger" @click="cancelStep">已踩（{{ info.stepnum || 0 }}）</el-button>
            </template>
            <el-button size="small" type="warning" @click="toggleFav" v-if="canFavorite">{{ fav ? '取消收藏' : '收藏' }}（{{ info.favoritenum || 0 }}）</el-button>
            <el-button
              v-if="canAudit && !auditApproved.value"
              size="small"
              type="warning"
              :loading="auditSubmitting"
              @click="openAuditDialog"
            >审核</el-button>
          </div>
        </div>
      </div>
    </el-card>
    <el-card class="mt-4 comment-container" ref="commentSectionRef">
      <template #header>
        <div class="comment-header">
          <span>评价</span>
          <span v-if="discussCount" class="comment-count">{{ discussCount }}</span>
        </div>
      </template>
      <div class="comment-content">
        <div v-if="discussLoading" class="comment-loading">正在加载...</div>
        <template v-else>
          <div v-if="discussList.length" class="comment-list">
            <div v-for="item in discussList" :key="item.id" class="comment-item">
              <div class="comment-author">
                <span class="author">{{ item.nickname || '匿名用户' }}</span>
                <span class="time">{{ item.addtime || '' }}</span>
              </div>
              <div class="comment-info" v-html="item.content"></div>
              <div class="comment-ops">
                <el-button
                  link
                  size="small"
                  :type="item.liked ? 'primary' : 'info'"
                  :disabled="item._actionLoading === true"
                  @click.stop="toggleDiscussLike(item)"
                >👍 {{ item.likeCount || 0 }}</el-button>
                <el-button
                  link
                  size="small"
                  :type="item.stepped ? 'danger' : 'info'"
                  :disabled="item._actionLoading === true"
                  @click.stop="toggleDiscussStep(item)"
                >👎 {{ item.stepCount || 0 }}</el-button>
                <el-button
                  v-if="isOwnDiscuss(item)"
                  link
                  size="small"
                  type="danger"
                  :disabled="item._actionLoading === true"
                  @click.stop="deleteDiscuss(item)"
                >删除</el-button>
              </div>
              <div v-if="item.reply" class="comment-reply">
                <span class="reply-label">回复</span>
                <div class="reply-content" v-html="item.reply"></div>
              </div>
            </div>
          </div>
          <el-empty class="no-access" v-else description="暂无评价" />
        </template>
      </div>
      <div v-if="discussTotal > discussPageSize" class="comment-pagination">
        <el-pagination
          layout="prev, pager, next"
          :total="discussTotal"
          :page-size="discussPageSize"
          :current-page="discussPage"
          @current-change="handleDiscussPage"
        />
      </div>
      <div v-if="canComment" class="comment-form">
        <RichEditor
          v-model="discussForm.content"
          placeholder="请输入评论内容..."
          height="220px"
        />
        <div class="comment-actions">
          <el-button type="primary" :loading="discussSubmitting" @click="submitDiscuss">发布评论</el-button>
        </div>
      </div>
    </el-card>
    <el-dialog
      v-model="auditDialogVisible"
      title="审核处理"
      width="420px"
      append-to-body
    >
      <el-form label-width="96px">
        <el-form-item label="审核状态">
          <el-select v-model="auditForm.status" placeholder="请选择">
            <el-option
              v-for="opt in auditOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input
            v-model="auditForm.reply"
            type="textarea"
            :rows="4"
            placeholder="请输入审核说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="auditSubmitting" @click="submitAudit">确 认</el-button>
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
const canViewPermission = useAuth('news', '查看')
const tableButtonsPermission = useTableButtons('news')
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
const canAudit = computed(() => tableButtons.value.includes('审核'))
const auditDialogVisible = ref(false)
const auditSubmitting = ref(false)
const auditForm = reactive({ status: '通过', reply: '' })
const auditOptions = [
  { label: '通过', value: '通过' },
  { label: '不通过', value: '不通过' },
  { label: '待审核', value: '待审核' }
]
const tableHasAudit = true
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
const commentSectionRef = ref<HTMLElement | null>(null)
const discussList = ref<any[]>([])
const discussLoading = ref(false)
const discussPage = ref(1)
const discussPageSize = ref(5)
const discussTotal = ref(0)
const discussForm = reactive({ content: '' })
const discussSubmitting = ref(false)
const discussCount = computed(() => Number(info.discussnum || 0))
const discussTableName = 'discussnews'
// 页面标题：优先使用后端富化的稳定标题字段
const titleField = 'title'
const pageTitle = computed(() => {
  const tf = titleField
  const v = tf ? (info as any)[tf] : null
  const text = (v === undefined || v === null) ? '' : String(v).trim()
  return text || '公告资讯'
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


function openAuditDialog() {
  if (!canAudit.value) {
    ElMessage.warning('暂无审核权限')
    return
  }
  auditForm.status = '通过'
  auditForm.reply = ''
  auditDialogVisible.value = true
}

async function submitAudit() {
  if (auditSubmitting.value || !info.id) return
  if (!auditForm.status) {
    ElMessage.warning('请选择审核状态')
    return
  }
  auditSubmitting.value = true
  try {
    const status = encodeURIComponent(auditForm.status)
    const reply = encodeURIComponent(auditForm.reply || '')
    const url = 'news/audit/batch?auditstatus=' + status + '&auditreply=' + reply
    const res: any = await http.post(url, [info.id])
    if (res?.code === 0) {
      ElMessage.success('审核完成')
      auditDialogVisible.value = false
      await loadDetail(info.id)
    } else {
      ElMessage.error(res?.msg || '审核失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '审核失败')
  } finally {
    auditSubmitting.value = false
  }
}

async function loadDetail(id: number) {
  if (!id || !canView.value) return
  const res: any = await http.get('news/detail/' + id)
  Object.assign(info, res?.data || {})
  computeImages()
  discussPage.value = 1
  await loadDiscuss(1)
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
  const field = 'image'
  if (field) {
    const v = (info as any)[field]
    if (v) images.value = String(v).split(',').filter(Boolean)
  }
}


async function like(type: '1'|'2'){
  await http.get(`news/like/` + info.id, { params: { type } })
  if (type==='1') info.likenum = (info.likenum||0) + 1
  else info.stepnum = (info.stepnum||0) + 1
}

async function toggleFav(){
  if (!canFavorite.value) { ElMessage.warning('暂无权限'); return }
  if (!hasToken()) { window.dispatchEvent(new CustomEvent('front-auth-open')); return }
  if (fav.value) {
    // 取消收藏：查到 social 记录 id 后删除
    try {
      if (!favId.value) {
        const st: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid: info.id, type: '1' } })
        const arr = st?.data?.list || []
        if (arr.length) favId.value = arr[0].id
      }
      if (favId.value) await http.post('social/delete', [favId.value])
    } finally {
      fav.value = false
      info.favoritenum = Math.max(0,(info.favoritenum||0)-1)
      favId.value = null
      try { await http.post('news/update', info) } catch(e) {}
      await refreshSocialStatus(info.id)
    }
  } else {
    // 添加收藏：写 social
    const payload: any = { tablename: 'news', refid: info.id, type: '1' }
    // 供后端展示，可带上名称与图片
    try { payload.name = String(info.id) } catch (e) {}
    const coverField = 'image'
    if (coverField && (info as any)[coverField]) payload.picture = String((info as any)[coverField]).split(',')[0]
    const ret: any = await http.post('social/add', payload)
    fav.value = true
    info.favoritenum = (info.favoritenum||0)+1
    // 记录 social id，便于取消
    try { favId.value = ret?.data || null } catch(e) {}
    try { await http.post('news/update', info) } catch(e) {}
    await refreshSocialStatus(info.id)
  }
}

async function doLike(){
  if (liked.value || stepped.value) return
  const payload: any = { tablename: 'news', refid: info.id, type: '21' }
  try { payload.name = String(info.id) } catch (e) {}
  const coverField2 = 'image'
  if (coverField2 && (info as any)[coverField2]) payload.picture = String((info as any)[coverField2]).split(',')[0]
  const ret: any = await http.post('social/add', payload)
  liked.value = true
  info.likenum = (info.likenum||0) + 1
  try { likeId.value = ret?.data || null } catch(e) {}
  try { await http.post('news/update', info) } catch(e) {}
  await refreshSocialStatus(info.id)
}

async function cancelLike(){
  if (!liked.value) return
  try {
    if (!likeId.value) {
      const st: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid: info.id, type: '21' } })
      const arr = st?.data?.list || []
      if (arr.length) likeId.value = arr[0].id
    }
    if (likeId.value) await http.post('social/delete', [likeId.value])
  } finally {
    liked.value = false
    info.likenum = Math.max(0,(info.likenum||0)-1)
    likeId.value = null
    try { await http.post('news/update', info) } catch(e) {}
    await refreshSocialStatus(info.id)
  }
}

async function doStep(){
  if (liked.value || stepped.value) return
  const payload: any = { tablename: 'news', refid: info.id, type: '22' }
  try { payload.name = String(info.id) } catch (e) {}
  const coverField3 = 'image'
  if (coverField3 && (info as any)[coverField3]) payload.picture = String((info as any)[coverField3]).split(',')[0]
  const ret: any = await http.post('social/add', payload)
  stepped.value = true
  info.stepnum = (info.stepnum||0) + 1
  try { stepId.value = ret?.data || null } catch(e) {}
  try { await http.post('news/update', info) } catch(e) {}
  await refreshSocialStatus(info.id)
}

async function cancelStep(){
  if (!stepped.value) return
  try {
    if (!stepId.value) {
      const st: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid: info.id, type: '22' } })
      const arr = st?.data?.list || []
      if (arr.length) stepId.value = arr[0].id
    }
    if (stepId.value) await http.post('social/delete', [stepId.value])
  } finally {
    stepped.value = false
    info.stepnum = Math.max(0,(info.stepnum||0)-1)
    stepId.value = null
    try { await http.post('news/update', info) } catch(e) {}
    await refreshSocialStatus(info.id)
  }
}


async function loadDiscuss(page = discussPage.value) {
  if (!info.id) {
    discussList.value = []
    discussTotal.value = 0
    return
  }
  discussLoading.value = true
  try {
    discussPage.value = page
    const params: Record<string, any> = {
      page,
      limit: discussPageSize.value,
      refid: info.id,
      sort: 'id',
      order: 'desc'
    }
    const res: any = await http.get('discussnews/list', { params })
    const data = res?.data || {}
    const rawList = Array.isArray(data.list) ? data.list : []
    discussList.value = rawList.map((item: any) => ({
      ...item,
      likeCount: Number(item.likenum || 0),
      stepCount: Number(item.stepnum || 0),
      liked: false,
      stepped: false,
      likeRecordId: null,
      stepRecordId: null,
      _actionLoading: false
    }))
    discussTotal.value = Number(data.total || 0)
    if (data.pageSize) {
      const size = Number(data.pageSize)
      if (isFinite(size) && size > 0) discussPageSize.value = size
    }
    if (discussList.value.length && hasToken()) {
      await Promise.all(discussList.value.map(refreshDiscussInteraction))
    }
  } catch (error) {
    console.warn('加载评论失败', error)
    discussList.value = []
  } finally {
    discussLoading.value = false
  }
}

function handleDiscussPage(page: number) {
  loadDiscuss(page)
}

async function submitDiscuss() {
  if (!canComment.value) {
    ElMessage.warning('暂无权限')
    return
  }
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  if (!info.id) {
    ElMessage.warning('未找到目标记录')
    return
  }
  const rawContent = discussForm.content || ''
  const plainText = rawContent.replace(/<[^>]*>/g, '').replace(/&nbsp;/g, ' ').trim()
  if (!plainText) {
    ElMessage.warning('请输入评论内容')
    return
  }
  discussSubmitting.value = true
  try {
    const payload: Record<string, any> = {
      refid: info.id,
      content: rawContent
    }
    const nickname = sessionObj?.nickname || sessionObj?.name || sessionObj?.username || username
    if (nickname) payload.nickname = nickname
    const res: any = await http.post('discussnews/add', payload)
    if (res?.code === 0) {
      discussForm.content = '<p></p>'
      discussPage.value = 1
      info.discussnum = Number(info.discussnum || 0) + 1
      try { await http.post('news/update', info) } catch(e) {}
      await loadDiscuss(1)
      ElMessage.success('评论成功')
    } else {
      ElMessage.error(res?.msg || '评论失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '评论失败')
  } finally {
    discussSubmitting.value = false
  }
}

function isOwnDiscuss(item: any) {
  if (!item) return false
  const uid = item.userid ?? item.userId ?? item.uid
  if (uid === undefined || uid === null) return false
  return String(uid) === String(currentUserId)
}

async function fetchDiscussSocial(item: any, type: '21' | '22') {
  if (!currentUserId) return { recordId: null }
  try {
    const res: any = await http.get('social/list', {
      params: {
        page: 1,
        limit: 1,
        tablename: discussTableName,
        refid: item.id,
        type,
        userid: currentUserId
      }
    })
    const list = Array.isArray(res?.data?.list) ? res.data.list : []
    const record = list.length ? list[0] : null
    return { recordId: record?.id ?? null }
  } catch (error) {
    console.warn('加载评论互动失败', error)
    return { recordId: null }
  }
}

async function refreshDiscussInteraction(item: any) {
  if (!item || !item.id) return
  item.likeCount = Number(item.likenum ?? item.likeCount ?? 0)
  item.stepCount = Number(item.stepnum ?? item.stepCount ?? 0)
  const [likeRes, stepRes] = await Promise.all([
    fetchDiscussSocial(item, '21'),
    fetchDiscussSocial(item, '22')
  ])
  item.likeRecordId = likeRes.recordId
  item.stepRecordId = stepRes.recordId
  item.liked = !!likeRes.recordId
  item.stepped = !!stepRes.recordId
}

function setDiscussActionLoading(item: any, value: boolean) {
  if (item) item._actionLoading = value
}

async function updateDiscussCounters(item: any) {
  try {
    await http.post('discussnews/update', {
      id: item.id,
      likenum: item.likeCount,
      stepnum: item.stepCount
    })
    item.likenum = item.likeCount
    item.stepnum = item.stepCount
  } catch (error) {
    console.warn('更新评论计数失败', error)
  }
}

async function toggleDiscussLike(item: any) {
  if (!item || !item.id) return
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  setDiscussActionLoading(item, true)
  try {
    if (item.liked && item.likeRecordId) {
      await http.post('social/delete', [item.likeRecordId])
      item.liked = false
      item.likeRecordId = null
      item.likeCount = Math.max(0, Number(item.likeCount || 0) - 1)
    } else {
      if (item.stepped && item.stepRecordId) {
        await http.post('social/delete', [item.stepRecordId])
        item.stepped = false
        item.stepRecordId = null
        item.stepCount = Math.max(0, Number(item.stepCount || 0) - 1)
      }
      const payload: Record<string, any> = {
        tablename: discussTableName,
        refid: item.id,
        type: '21',
        name: String(item.nickname || info.id || '')
      }
      const res: any = await http.post('social/add', payload)
      item.liked = true
      item.likeRecordId = res?.data || null
      item.likeCount = Number(item.likeCount || 0) + 1
    }
    await updateDiscussCounters(item)
  } catch (error: any) {
    ElMessage.error(error?.message || '操作失败')
    await refreshDiscussInteraction(item)
  } finally {
    setDiscussActionLoading(item, false)
  }
}

async function toggleDiscussStep(item: any) {
  if (!item || !item.id) return
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  setDiscussActionLoading(item, true)
  try {
    if (item.stepped && item.stepRecordId) {
      await http.post('social/delete', [item.stepRecordId])
      item.stepped = false
      item.stepRecordId = null
      item.stepCount = Math.max(0, Number(item.stepCount || 0) - 1)
    } else {
      if (item.liked && item.likeRecordId) {
        await http.post('social/delete', [item.likeRecordId])
        item.liked = false
        item.likeRecordId = null
        item.likeCount = Math.max(0, Number(item.likeCount || 0) - 1)
      }
      const payload: Record<string, any> = {
        tablename: discussTableName,
        refid: item.id,
        type: '22',
        name: String(item.nickname || info.id || '')
      }
      const res: any = await http.post('social/add', payload)
      item.stepped = true
      item.stepRecordId = res?.data || null
      item.stepCount = Number(item.stepCount || 0) + 1
    }
    await updateDiscussCounters(item)
  } catch (error: any) {
    ElMessage.error(error?.message || '操作失败')
    await refreshDiscussInteraction(item)
  } finally {
    setDiscussActionLoading(item, false)
  }
}

async function deleteDiscuss(item: any) {
  if (!item || !item.id) return
  if (!hasToken()) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return
  }
  if (!isOwnDiscuss(item)) {
    ElMessage.warning('只能删除自己的评论')
    return
  }
  try {
    await ElMessageBox.confirm('确定删除该评论吗？', '提示', { type: 'warning' })
  } catch {
    return
  }
  setDiscussActionLoading(item, true)
  try {
    const res: any = await http.post('discussnews/delete', [item.id])
    if (res?.code === 0) {
      discussList.value = discussList.value.filter((comment: any) => comment.id !== item.id)
      info.discussnum = Math.max(0, Number(info.discussnum || 0) - 1)
      discussTotal.value = Math.max(0, discussTotal.value - 1)
      ElMessage.success('已删除')
    } else {
      ElMessage.error(res?.msg || '删除失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '删除失败')
  } finally {
    setDiscussActionLoading(item, false)
  }
}



async function refreshSocialStatus(refid: number){
  if (!hasToken() || !canView.value) return
  try {
    const st: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid, type: '1' } })
    const arr = st?.data?.list || []
    fav.value = arr.length > 0
    favId.value = arr.length ? arr[0].id : null
  } catch(e) {}
  try {
    const stLike: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid, type: '21' } })
    const likeArr = stLike?.data?.list || []
    liked.value = likeArr.length > 0
    likeId.value = likeArr.length ? likeArr[0].id : null
  } catch(e) {}
  try {
    const stStep: any = await http.get('social/list', { params: { page: 1, limit: 1, tablename: 'news', refid, type: '22' } })
    const stepArr = stStep?.data?.list || []
    stepped.value = stepArr.length > 0
    stepId.value = stepArr.length ? stepArr[0].id : null
  } catch(e) {}
}
// 联系TA
</script>



