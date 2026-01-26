<template>
  <div v-if="hasAI" class="ai-widget-root">
    <div class="ai-widget"  ref="widgetRef">
      <div class="widget-launch" @click="toggle">
        <div class="tooltip-container">
          <span class="tooltip">AI/人工客服</span>
          <span class="text">
            <div class="borde-back">
              <div class="icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 28 28">
                  <path
                      fill="#fff"
                      d="M14.75 2.25a.75.75 0 0 0-1.5 0V3h-4.5A2.75 2.75 0 0 0 6 5.75v5.5A2.75 2.75 0 0 0 8.75 14h9.084q.1-.181.165-.38l.303-1.12H8.75c-.69 0-1.25-.56-1.25-1.25v-5.5c0-.69.56-1.25 1.25-1.25h10.5c.69 0 1.25.56 1.25 1.25v5.332q.213.076.4.207a1.7 1.7 0 0 1 .59.8l.164.497c.22-.396.346-.851.346-1.336v-5.5A2.75 2.75 0 0 0 19.25 3h-4.5zM6.75 16h7.617a1.58 1.58 0 0 0-.29 1.5H6.75c-.69 0-1.25.56-1.25 1.25v.75c0 1.423.664 2.633 2.033 3.52c1.398.907 3.55 1.48 6.467 1.48s5.069-.573 6.467-1.48a6 6 0 0 0 .235-.16q.117-.055.227-.13l.13-.11A1.3 1.3 0 0 0 21 23c.003.282.09.557.25.79a1.3 1.3 0 0 0 .302.307a7 7 0 0 1-.268.182C19.556 25.4 17.083 26 14 26s-5.556-.602-7.283-1.72C4.961 23.141 4 21.476 4 19.5v-.75A2.75 2.75 0 0 1 6.75 16m5.75-7.5a1.5 1.5 0 1 1-3 0a1.5 1.5 0 0 1 3 0M17 10a1.5 1.5 0 1 0 0-3a1.5 1.5 0 0 0 0 3m1.171 8.828a3.16 3.16 0 0 1 .761 1.24l.498 1.529a.605.605 0 0 0 1.14 0l.498-1.53a3.15 3.15 0 0 1 1.998-1.996l1.53-.497a.604.604 0 0 0 0-1.14l-.03-.008l-1.531-.497a3.15 3.15 0 0 1-1.998-1.996l-.497-1.53a.604.604 0 0 0-1.14 0l-.498 1.53l-.013.038a3.15 3.15 0 0 1-1.955 1.958l-1.53.497a.604.604 0 0 0 0 1.14l1.53.497c.467.155.89.418 1.237.765m8.65 3.53l.918.298l.019.004a.362.362 0 0 1 0 .684l-.919.298a1.9 1.9 0 0 0-1.198 1.198l-.299.918a.363.363 0 0 1-.684 0l-.299-.918a1.89 1.89 0 0 0-1.198-1.202l-.919-.298a.362.362 0 0 1 0-.684l.919-.298a1.9 1.9 0 0 0 1.18-1.198l.299-.918a.363.363 0 0 1 .684 0l.298.918a1.89 1.89 0 0 0 1.199 1.198"
                  />
                </svg>
              </div>
            </div>
          </span>
        </div>
      </div>
      <transition name="chat-fade">
        <div v-if="open" class="chat-wrapper">
          <div class="chat-window">
            <div class="chat-header">
              <div>
                <div class="chat-title">联系客服</div>
                <div v-if="aiLoading" class="chat-tip">AI正在思考，请稍等...</div>
              </div>
              <div class="chat-controls">
                <div class="mode-toggle">
                  <button class="toggle-btn" :class="{ active: askType===1 }" @click.stop="setAskType(1)">AI</button>
                  <button class="toggle-btn" :class="{ active: askType===2 }" @click.stop="setAskType(2)">人工</button>
                </div>
                <button class="close-btn" @click="toggle">×</button>
              </div>
            </div>
            <div class="chat-body" ref="scrollRef">
              <template v-if="chatList.length">
                <div
                    v-for="(m,i) in chatList"
                    :key="i"
                    :class="['chat-message', isMine(m)? 'self' : 'other']"
                >
                  <template v-if="m.loading">
                    <div class="bubble typing-bubble">
                      <span class="typing-dot"></span>
                      <span class="typing-dot"></span>
                      <span class="typing-dot"></span>
                    </div>
                  </template>
                  <template v-else-if="isImageMessage(m)">
                    <div class="bubble image-bubble">
                      <img :src="resolveMedia(m.content)" alt="图片消息" @click="openImage(resolveMedia(m.content))" />
                    </div>
                  </template>
                  <template v-else>
                    <div class="bubble text-bubble">{{ m.content }}</div>
                  </template>
                </div>
              </template>
              <div v-else class="chat-empty">快来向我们提问吧～</div>
            </div>
            <div class="chat-footer">
              <div class="input-row">
                <input
                    class="chat-input"
                    v-model="form.ask"
                    :placeholder="askType===1 ? '请输入问题，仅支持文字' : '请输入问题或发送图片'"
                    @keydown.enter.prevent="send"
                />
                <label v-if="askType===2" class="upload-btn">
                  <input type="file" accept="image/*" @change="handleImageUpload" />
                  <el-icon><Picture /></el-icon>
                </label>
                <button class="send-btn" @click="send">发送</button>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick, onBeforeUnmount, reactive } from 'vue'
import { http } from '@/utils/request'

type ChatMessage = { role: 'user' | 'assistant', content: string, type: number, loading?: boolean }

const hasAI = true
const open = ref(false)
const aiLoading = ref(false)
const askType = ref(1)
const form = ref({ ask: '' })
const chatList = ref<ChatMessage[]>([])
const scrollRef = ref<HTMLDivElement | null>(null)
const widgetRef = ref<HTMLElement | null>(null)
const aiPendingIndex = ref<number | null>(null)

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const uploadUrl = (baseUrl.endsWith('/') ? baseUrl : baseUrl + '/') + 'file/upload'
const myid = computed(() => Number(localStorage.getItem('frontUserid') || 0))
const session = computed(()=>JSON.parse(localStorage.getItem('frontSession') || '{}'))
let websock: WebSocket | null = null

function toggle() {
  open.value = !open.value
  if (open.value) {
    loadHistory()
    nextTick(() => {
      scrollBottom()
    })
  }
}

function setAskType(val: number) {
  if (askType.value === val) return
  askType.value = val
  if (val === 2) {
    initWebSocket(1)
  } else {
    try { websock?.close() } catch (e) {}
    websock = null
  }
}

function isMine(m: ChatMessage) {
  return m.role === 'user'
}

function isImageContent(content: string) {
  if (!content) return false
  return content.startsWith('upload/') || /\.(png|jpe?g|gif|bmp|webp)(\?.*)?$/i.test(content)
}

function isImageMessage(m: ChatMessage) {
  return m.type === 2 || isImageContent(m.content)
}

function resolveMedia(path: string) {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return baseUrl + path
}

function openImage(url: string) {
  if (!url) return
  window.open(url, '_blank')
}

function scrollBottom() {
  nextTick(() => {
    const el = scrollRef.value
    if (el) {
      el.scrollTop = el.scrollHeight
    }
  })
}

async function loadHistory() {
  try {
    const params: Record<string, any> = { page: 1, limit: 200, sort: 'id', order: 'asc' }
    if (myid.value) {
      params.userid = myid.value
    }
    const res: any = await http.get('chat/list', { params })
    const arr: any[] = (res?.data?.list || []).filter((row: any) => !myid.value || Number(row.userid) === Number(myid.value))
    const result: ChatMessage[] = []
    arr.forEach((it) => {
      const askTypeValue = Number(it.asktype ?? 0)
      const askMsgType = askTypeValue > 0 ? askTypeValue : (it.ask && isImageContent(it.ask) ? 2 : 1)
      if (it.ask) {
        result.push({ role: 'user', content: it.ask, type: askMsgType })
      }
      if (it.reply) {
        const replyTypeValue = Number(it.replytype ?? 0)
        const replyMsgType = replyTypeValue > 0
          ? replyTypeValue
          : (isImageContent(it.reply) ? 2 : 1)
        result.push({ role: 'assistant', content: it.reply, type: replyMsgType })
      }
    })
    chatList.value = result
    aiPendingIndex.value = null
    scrollBottom()
  } catch (err) {
    console.warn('loadHistory failed', err)
  }
}

async function send() {
  const ask = form.value.ask?.trim()
  if (!ask) {
    return
  }
  form.value.ask = ''
  chatList.value.push({ role: 'user', content: ask, type: 1 })
  scrollBottom()

  const add: any = await http.post('chat/add', { userid: myid.value, ask, asktype: 1, uname: session.value.name })
  const id = add?.data
  if (askType.value === 1) {
    const loadingMsg: ChatMessage = { role: 'assistant', content: 'AI正在思考中…', type: 1, loading: true }
    chatList.value.push(loadingMsg)
    aiPendingIndex.value = chatList.value.length - 1
    scrollBottom()
    try {
      aiLoading.value = true
      const rs: any = await http.post('ai/chat', { ask },{timeout: 600000})
      const reply = rs?.data || '抱歉，暂无法回答。'
      replacePending(reply, 1)
      if (id) {
        await http.post('chat/update', { id, reply, replytype: 1, isreply: 1, isread: 1 })
      }
    } catch (e) {
      console.error(e)
      replacePending('主人，AI 还不够聪明，请稍后再试~', 1)
    } finally {
      aiLoading.value = false
      scrollBottom()
    }
  } else {
    initWebSocket(1)
  }
}

async function handleImageUpload(event: Event) {
  if (askType.value !== 2) return
  const input = event.target as HTMLInputElement
  const file = input?.files?.[0]
  if (!file) return
  try {
    const formData = new FormData()
    formData.append('file', file)
    const response = await fetch(uploadUrl, {
      method: 'POST',
      body: formData
    })
    const res: any = await response.json()
    if (res?.code === 0 && res.file) {
      await http.post('chat/add', { userid: myid.value, ask: `${res.file}`, uname: session.value.name, asktype: 2 })
      await loadHistory()
      initWebSocket(1)
    }
  } catch (err) {
    console.warn('upload failed', err)
  } finally {
    if (input) {
      input.value = ''
    }
  }
}

function initWebSocket(toId: number) {
  try { websock?.close() } catch (e) {}
  websock = null
  const wsBase = (baseUrl || '').replace(/^http/, 'ws')
  let url = wsBase.endsWith('/') ? wsBase : wsBase + '/'
  url += `ws?user_id=&to_id=`
  try {
    websock = new WebSocket(url)
    websock.onmessage = () => loadHistory()
  } catch (err) {
    console.warn('ws init failed', err)
  }
}
function replacePending(content: string, type: number) {
  if (aiPendingIndex.value !== null && chatList.value[aiPendingIndex.value]) {
    chatList.value[aiPendingIndex.value] = { role: 'assistant', content, type }
  } else {
    chatList.value.push({ role: 'assistant', content, type })
  }
  aiPendingIndex.value = null
}

onBeforeUnmount(() => {
  try { websock?.close() } catch (e) {}
})
</script>

<style scoped>
.ai-widget-root {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 9999;
}
.ai-widget {
  position: fixed;
  top: 15%;
  right: 7%;
  pointer-events: auto;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
  transition: top 0.2s ease-out, left 0.2s ease-out;
}
.widget-launch {
  cursor: grab;
}
.widget-launch:active {
  cursor: grabbing;
}
.chat-wrapper {
  width: 320px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 45px rgba(15, 23, 42, 0.2);
  overflow: hidden;
}
.chat-window {
  display: flex;
  flex-direction: column;
}
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: #60a5fa;
  color: #fff;
  cursor: grab;
}
.chat-header:active {
  cursor: grabbing;
}
.chat-title {
  font-size: 16px;
  font-weight: 600;
}
.chat-tip {
  font-size: 12px;
  margin-top: 4px;
  opacity: 0.9;
}
.chat-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}
.mode-toggle {
  display: flex;
  background: rgba(255, 255, 255, 0.25);
  border-radius: 99px;
  padding: 2px;
}
.toggle-btn {
  border: none;
  background: transparent;
  color: #fff;
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s ease;
}
.toggle-btn.active {
  background: rgba(255, 255, 255, 0.4);
  color: #fff;
}
.toggle-btn:not(.active):hover {
  background: rgba(255, 255, 255, 0.2);
}
.close-btn {
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  font-size: 18px;
  line-height: 1;
  cursor: pointer;
  transition: background 0.2s ease;
}
.close-btn:hover {
  background: rgba(255, 255, 255, 0.35);
}
.chat-body {
  padding: 16px;
  height: 360px;
  overflow-y: auto;
  background: #fafafa;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.chat-body::-webkit-scrollbar {
  width: 6px;
}
.chat-body::-webkit-scrollbar-thumb {
  background: rgba(96, 165, 250, 0.4);
  border-radius: 3px;
}
.chat-message {
  display: flex;
  gap: 8px;
}
.chat-message.self {
  justify-content: flex-end;
}
.chat-message.other {
  justify-content: flex-start;
}
.bubble {
  max-width: 220px;
  padding: 10px 12px;
  border-radius: 14px;
  font-size: 13px;
  line-height: 1.5;
  word-break: break-word;
  box-shadow: 0 8px 16px rgba(15, 23, 42, 0.08);
}
.text-bubble {
  background: #60a5fa;
  color: #fff;
}
.chat-message.other .text-bubble {
  background: #e5e7eb;
  color: #1f2937;
}
.image-bubble {
  padding: 4px;
  background: #fff;
}
.image-bubble img {
  max-width: 220px;
  border-radius: 12px;
  cursor: zoom-in;
}
.typing-bubble {
  background: #e5e7eb;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}
.chat-message.self .typing-bubble {
  background: rgba(96, 165, 250, 0.2);
}
.typing-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #9ca3af;
  animation: typing 1s infinite ease-in-out;
}
.chat-message.self .typing-dot {
  background: #3b82f6;
}
.typing-dot:nth-child(2) {
  animation-delay: 0.15s;
}
.typing-dot:nth-child(3) {
  animation-delay: 0.3s;
}
@keyframes typing {
  0%, 80%, 100% {
    opacity: 0.3;
    transform: translateY(0);
  }
  40% {
    opacity: 1;
    transform: translateY(-2px);
  }
}
.chat-empty {
  text-align: center;
  color: #9ca3af;
  font-size: 13px;
  margin-top: 32px;
}
.chat-footer {
  padding: 12px 16px 16px;
  border-top: 1px solid #e5e7eb;
  background: #fff;
}
.input-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
.chat-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 12px;
  font-size: 13px;
  outline: none;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.chat-input:focus {
  border-color: #60a5fa;
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.2);
}
.upload-btn {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 9px 12px;
  border-radius: 12px;
  background: rgba(96, 165, 250, 0.12);
  color: #2563eb;
  font-size: 13px;
  cursor: pointer;
  transition: background 0.2s ease;
}
.upload-btn:hover {
  background: rgba(96, 165, 250, 0.24);
}
.upload-btn input {
  position: absolute;
  inset: 0;
  opacity: 0;
  cursor: pointer;
}
.send-btn {
  flex-shrink: 0;
  border: none;
  background: #60a5fa;
  color: #fff;
  padding: 6px 12px;
  border-radius: 5px;
  font-size: 13px;
  cursor: pointer;
  transition: background 0.2s ease;
}
.send-btn:hover {
  background: #3b82f6;
}

.chat-fade-enter-active,
.chat-fade-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}
.chat-fade-enter-from,
.chat-fade-leave-to {
  opacity: 0;
  transform: translateY(8px);
}

.tooltip-container {
  position: relative;
  background-color: #f47fff;
  cursor: grab;
  transition: all 0.2s;
  font-size: 17px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  fill: #fff;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 25px;
  border: 1px solid rgba(255, 255, 255, 0.18);
}
.tooltip-container .borde-back {
  width: 60px;
  height: 60px;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  box-shadow: none;
}
.tooltip-container .icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  background-color: #5865f2;
  cursor: pointer;
}
.tooltip {
  position: absolute;
  top: 0;
  left: -45px;
  transform: translateX(-32%);
  width: 190px;
  height: 52px;
  opacity: 0;
  pointer-events: none;
  transition: all 0.6s;
  border-radius: 50px;
  background-image: linear-gradient(-90deg, #f47fff 0%, #5865f2 75%, #5865f2 100%);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 16px;
  color: #fff;
  font-size: 18px;
  font-family: 'Montserrat', sans-serif;
  font-weight: 600;
}
.tooltip::before {
  position: absolute;
  content: '';
  height: 0.6em;
  width: 0.6em;
  right: -0.2em;
  top: 50%;
  transform: translateY(-50%) rotate(45deg);
  background: linear-gradient(-90deg, #f47fff 0%, #5865f2 75%, #5865f2 100%);
}
.tooltip-container:hover .tooltip {
  left: 100%;
  opacity: 1;
  visibility: visible;
  pointer-events: auto;
  z-index: -10;
}
.tooltip-container:hover {
  transform: translateX(-12px);
}
</style>
