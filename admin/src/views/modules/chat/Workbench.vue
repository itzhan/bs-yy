<template>
  <div class="main-content" style="padding:20px 30px">
    <div class="flex" style="gap:16px">
      <!-- 左侧：会话列表/筛选 -->
      <div style="width:360px" class="bg-white p-3 rounded shadow">
        <div class="mb-2 flex items-center" style="gap:8px">
          <el-input v-model="keyword" placeholder="搜索用户名" clearable @keydown.enter.native="loadTickets" />
        </div>
        <div class="mb-2 flex items-center justify-between" style="gap:8px">
          <el-checkbox v-model="filterUnread">未读</el-checkbox>
          <el-checkbox v-model="filterUnreply">未回复</el-checkbox>
          <el-button type="primary" size="small" @click="loadTickets">刷新</el-button>
        </div>
        <el-scrollbar height="560px">
          <el-empty v-if="tickets.length===0" description="暂无会话" />
          <div v-for="it in tickets" :key="it.userid" class="ticket-item" @click="openSession(it)"
               :class="{ active: currentUser && it.userid===currentUser.userid }">
            <div class="title">{{ it.uname || ('用户'+it.userid) }}</div>
            <div class="meta">
              <span>{{ it.addtime }}</span>
              <span v-if="Number(it.isread)===0" class="badge">未读</span>
              <span v-if="Number(it.isreply)===0" class="badge warn">未回复</span>
            </div>
            <div class="last">{{ it.preview }}</div>
          </div>
        </el-scrollbar>
      </div>

      <!-- 右侧：对话窗 -->
      <div class="flex-1 bg-white p-3 rounded shadow flex flex-col">
        <div class="flex items-center justify-between mb-2">
          <div class="text-lg font-bold">{{ currentUser? (currentUser.uname||('用户'+currentUser.userid)) : '请选择左侧会话' }}</div>
          <div class="text-gray">{{ aiTip }}</div>
        </div>
        <el-scrollbar ref="scrollRef" height="520px" class="flex-1">
          <div v-for="(m,i) in messages" :key="i" :class="['msg', m.side]">
            <template v-if="m.type===2">
              <el-image :src="fullUrl(m.content)" style="max-width: 280px" :preview-src-list="[fullUrl(m.content)]" />
            </template>
            <template v-else>
              <div class="bubble">{{ m.content }}</div>
            </template>
          </div>
        </el-scrollbar>
        <div class="mt-2 flex items-start" style="gap:8px">
          <div class="flex-1">
            <el-input type="textarea" :rows="2" v-model="replyText" placeholder="输入回复内容，回车发送" @keydown.enter.native.stop.prevent="sendReply" />
            <div class="mt-2 flex items-center" style="gap:8px">
              <el-upload :action="uploadUrl" :show-file-list="false" :on-success="uploadSuccessImg">
                <el-button size="small">图片</el-button>
              </el-upload>
            </div>
          </div>
          <el-button type="primary" @click="sendReply">发送</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { http } from '@/utils/request'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const auth = useAuthStore()
const role = auth.userInfo?.role || localStorage.getItem('sessionTable') || ''
const isCoach = role === 'coach'
const rawId = Number(auth.userInfo?.id || localStorage.getItem('userid') || 0)
const adminId = isCoach ? rawId + 100000 : rawId

const tickets = ref<any[]>([])
const keyword = ref('')
const filterUnread = ref(false)
const filterUnreply = ref(false)
const currentUser = ref<any | null>(null)
const messages = ref<any[]>([])
const replyText = ref('')
const aiTip = ref('')
const scrollRef = ref()

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const uploadUrl = (baseUrl.endsWith('/')?baseUrl:(baseUrl+'/')) + 'file/upload'
let timer:any = null
let websock: WebSocket | null = null

function scrollBottom(){ nextTick(()=>{ try{ const el = (scrollRef.value as any)?.wrapRef as HTMLElement; if(el){ el.scrollTop = el.scrollHeight } }catch{} }) }

// ---- 教练模式：读 chatmessage 表 ----
async function loadTicketsCoach(){
  // 取所有发给本教练的消息 (fid=adminId)
  const res:any = await http.get('chatmessage/page', { params: { fid: adminId, page:1, limit:500, sort:'id', order:'desc' } })
  const arr = res?.data?.list || []
  // 按 uid 去重，取最新一条
  const map:Record<string, any> = {}
  for(const it of arr){
    const key = String(it.uid)
    if(!map[key]) map[key] = it
  }
  let list = Object.values(map).map((it:any) => ({
    userid: it.uid,        // 发消息用户的 uid
    uname: it.uid,         // 先用 uid 占位，下面异步获取用户名
    preview: it.content,
    isread: it.isread,
    isreply: 1,            // chatmessage 无 isreply 概念
    addtime: it.addtime,
  }))
  // 关键字过滤（此时还没加载到用户名，暂用 uid 过滤）
  if(keyword.value?.trim()){
    const kw = keyword.value.trim()
    list = list.filter(row => String(row.uname).includes(kw) || String(row.userid).includes(kw))
  }
  if(filterUnread.value) list = list.filter(row => Number(row.isread) === 0)
  tickets.value = list
  // 异步补充用户名
  for(const item of list){
    try{
      const ur:any = await http.get(`user/info/${item.userid}`)
      if(ur?.code === 0 && ur?.data){
        item.uname = ur.data.name || ur.data.useraccount || item.userid
        tickets.value = [...tickets.value]
      }
    }catch{}
  }
}

async function loadConversationCoach(refreshTickets = false){
  if(!currentUser.value) return
  const res:any = await http.get('chatmessage/mlist', { params: { uid: adminId, fid: currentUser.value.userid, page:1, limit:500, sort:'id', order:'asc' } })
  const arr = res?.data?.list || []
  messages.value = arr.map((row:any) => ({
    side: Number(row.uid) === adminId ? 'right' : 'left',
    type: isImageMsg(row.content) ? 2 : 1,
    content: row.content,
  }))
  scrollBottom()
  if(refreshTickets) await loadTicketsCoach()
}

async function sendReplyCoach(){
  if(!replyText.value.trim()) return
  if(!currentUser.value){ ElMessage.warning('请先选择会话'); return }
  await http.post('chatmessage/add', { uid: adminId, fid: currentUser.value.userid, content: replyText.value, format: 1, tablename: 'coach' })
  try{ websock?.send(replyText.value) }catch{}
  replyText.value = ''
  await loadConversationCoach(true)
  scrollBottom()
}

async function sendReplyUploadCoach(path:string){
  if(!currentUser.value){ ElMessage.warning('请先选择会话'); return }
  await http.post('chatmessage/add', { uid: adminId, fid: currentUser.value.userid, content: path, format: 2, tablename: 'coach' })
  try{ websock?.send('[图片]') }catch{}
  await loadConversationCoach(true); scrollBottom()
}

// ---- 管理员模式：读 chat 表（原逻辑）----
async function loadTicketsAdmin(){
  const params:any = { page:1, limit:200, sort:'addtime', order:'desc' }
  const res:any = await http.get('chat/page', { params })
  const arr = res?.data?.list || []
  const map:Record<string, any> = {}
  const latest:any[] = []
  for(const it of arr){
    const key = String(it.userid || '0')
    if(!map[key]){ map[key] = it; latest.push(it) }
  }
  let list = latest
  if(keyword.value?.trim()){
    const kw = keyword.value.trim()
    list = list.filter(row => String(row.uname || '').includes(kw))
  }
  if(filterUnread.value) list = list.filter(row => Number(row.isread) === 0)
  if(filterUnreply.value) list = list.filter(row => Number(row.isreply) === 0)
  tickets.value = list
}

async function loadConversationAdmin(refreshTickets = false){
  if(!currentUser.value) return
  const res:any = await http.get('chat/page', { params: { userid: currentUser.value.userid, page:1, limit:200, sort:'id', order:'asc' } })
  const arr = res?.data?.list || []
  const msgs:any[] = []
  const unreadIds:number[] = []
  for(const row of arr){
    if(row.ask) msgs.push({ side: 'left', type: isImageMsg(row.ask)?2:1, content: row.ask })
    if(row.reply) msgs.push({ side: 'right', type: isImageMsg(row.reply)?2:1, content: row.reply })
    if(Number(row.isread)===0 && row.ask && row.id) unreadIds.push(row.id)
  }
  messages.value = msgs
  scrollBottom()
  if(unreadIds.length){
    try{ await Promise.all(unreadIds.map(id => http.post('chat/update', { id, isread:1 }))) }catch{}
    if(currentUser.value) currentUser.value.isread = 1
  }
  if(refreshTickets) await loadTicketsAdmin()
}

async function sendReplyAdmin(){
  if(!replyText.value.trim()) return
  if(!currentUser.value){ ElMessage.warning('请先选择会话'); return }
  const res:any = await http.get('chat/page', { params: { userid: currentUser.value.userid, isreply:0, page:1, limit:1, sort:'id', order:'desc' } })
  const last = res?.data?.list?.[0]
  if(!last){ ElMessage.warning('没有可回复的提问'); return }
  await http.post('chat/update', { id: last.id, reply: replyText.value, replytype:1, uname: currentUser.value.uname, adminid: adminId, isreply:1, isread:1 })
  try{ websock?.send(replyText.value) }catch{}
  replyText.value = ''
  await loadConversationAdmin(true)
}

async function sendReplyUploadAdmin(path:string){
  if(!currentUser.value){ ElMessage.warning('请先选择会话'); return }
  await http.post('chat/add', { userid: currentUser.value.userid, uname: currentUser.value.uname, reply: path, replytype:2, isreply:1, isread:1, adminid: adminId })
  try{ websock?.send('[图片]') }catch{}
  await loadConversationAdmin(true); scrollBottom()
}

// ---- 统一入口 ----
async function loadTickets(){ isCoach ? loadTicketsCoach() : loadTicketsAdmin() }
async function loadConversation(refreshTickets = false){ isCoach ? loadConversationCoach(refreshTickets) : loadConversationAdmin(refreshTickets) }
async function sendReply(){ isCoach ? sendReplyCoach() : sendReplyAdmin() }
function uploadSuccessImg(res:any){ if(res?.code===0){ isCoach ? sendReplyUploadCoach(res.file) : sendReplyUploadAdmin(res.file) } }

async function openSession(it:any){
  currentUser.value = it
  await loadConversation(true)
  initWS()
}

function initWS(){
  try{ websock?.close() }catch{}
  websock = null
  const wsBase = (baseUrl||'').replace(/^http/, 'ws')
  let url = wsBase.endsWith('/')?wsBase:(wsBase+'/')
  url += `ws?user_id=${adminId}&to_id=${currentUser.value?.userid}`
  websock = new WebSocket(url)
  websock.onmessage = ()=>{ loadConversation(false) }
}

function fullUrl(p:string){ if(!p) return ''; return p.startsWith('http')? p : (baseUrl + p) }
function isImageMsg(content:any){ if(!content || typeof content !== 'string') return false; if(content.startsWith('upload/')) return true; return /\.(png|jpe?g|gif|bmp|webp)(\?.*)?$/i.test(content) }

onMounted(()=>{
  loadTickets()
  timer = setInterval(loadTickets, 5000)
})
onBeforeUnmount(()=>{ if(timer) clearInterval(timer); try{ websock?.close() }catch{} })
</script>

<style scoped>
.ticket-item{ padding:8px; border-bottom:1px solid #eee; cursor:pointer }
.ticket-item:hover{ background:#f7f7f7 }
.ticket-item.active{ background:#e8f4ff }
.ticket-item .title{ font-weight:600; }
.ticket-item .meta{ font-size:12px; color:#999; display:flex; gap:8px }
.ticket-item .badge{ background:#999; color:#fff; border-radius:8px; padding:0 6px }
.ticket-item .badge.warn{ background:#e6a23c }
.ticket-item .last{ color:#666; font-size:13px; margin-top:4px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis }
.msg{ display:flex; margin:8px 0 }
.msg.left{ justify-content: flex-start }
.msg.right{ justify-content: flex-end }
.bubble{ max-width: 70%; padding:8px 12px; border-radius:6px; background:#f5f7fa }
.right .bubble{ background:#ecf5ff }
</style>
