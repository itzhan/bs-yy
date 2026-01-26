import * as Vue from 'vue'

const DEFAULT_RECONNECT_DELAY = 3000
const hasRef = typeof Vue.ref === 'function'
const hasOnUnmounted = typeof Vue.onUnmounted === 'function'

function createRef(initial) {
  return hasRef ? Vue.ref(initial) : { value: initial }
}

function runOnUnmounted(cb) {
  if (hasOnUnmounted) {
    Vue.onUnmounted(cb)
  } else {
    if (typeof window !== 'undefined') {
      const handler = () => cb()
      window.addEventListener('beforeunload', handler, { once: true })
    }
  }
}

function resolveUserId() {
  return localStorage.getItem('frontUserid') || localStorage.getItem('userid') || ''
}

function resolveBaseUrl() {
  let base = ''
  try {
    // 在支持 ESM 的环境下优先取配置的 API 基础地址
    base = (import.meta && import.meta.env && import.meta.env.VITE_API_BASE_URL) || ''
  } catch (e) {
    base = ''
  }
  if (!base && typeof window !== 'undefined') {
    base = window.location?.origin || ''
  }
  return base
}

function buildWsUrl(userId, toId) {
  if (!userId) return ''
  let wsBase = resolveBaseUrl().replace(/^http/i, 'ws')
  if (!wsBase.endsWith('/')) wsBase += '/'
  const target = toId ?? ''
  return `${wsBase}ws?user_id=${encodeURIComponent(String(userId))}&to_id=${encodeURIComponent(String(target))}`
}

export function useWebsocket(options = {}) {
  const websock = createRef(null)
  const reconnectTimer = createRef(null)
  const lastTarget = createRef(null)

  const onMessage = options.onMessage || (() => {})
  const onOpen = options.onOpen
  const onClose = options.onClose
  const onError = options.onError
  const disableReconnect = options.disableReconnect === true
  const reconnectDelay = options.reconnectDelay ?? DEFAULT_RECONNECT_DELAY
  const shouldReconnect = options.shouldReconnect || (() => true)

  function clearReconnect() {
    if (reconnectTimer.value) {
      clearTimeout(reconnectTimer.value)
      reconnectTimer.value = null
    }
  }

  function closeWebSocket() {
    if (websock.value) {
      try { websock.value.close() } catch (e) {}
      websock.value = null
    }
  }

  function scheduleReconnect() {
    if (disableReconnect) return
    if (!shouldReconnect()) return
    if (lastTarget.value === null || lastTarget.value === undefined) return
    clearReconnect()
    reconnectTimer.value = window.setTimeout(() => {
      initWebSocket(lastTarget.value)
    }, reconnectDelay)
  }

  function initWebSocket(toId) {
    const userId = resolveUserId()
    const url = buildWsUrl(userId, toId)
    if (!url) return
    lastTarget.value = toId ?? ''
    clearReconnect()
    closeWebSocket()

    try {
      const socket = new WebSocket(url)
      websock.value = socket
      socket.onopen = (evt) => {
        clearReconnect()
        onOpen && onOpen(evt, socket)
      }
      socket.onerror = (evt) => {
        onError && onError(evt, socket)
        scheduleReconnect()
      }
      socket.onclose = (evt) => {
        onClose && onClose(evt, socket)
        scheduleReconnect()
      }
      socket.onmessage = (evt) => {
        onMessage(evt, socket)
      }
    } catch (e) {
      scheduleReconnect()
    }
  }

  function websocketSend(payload) {
    try {
      if (websock.value) {
        websock.value.send(payload)
      }
    } catch (err) {
      console.warn('send failed', err)
    }
  }

  runOnUnmounted(() => {
    clearReconnect()
    closeWebSocket()
  })

  return {
    websock,
    initWebSocket,
    websocketSend,
    closeWebSocket,
    clearReconnect,
    scheduleReconnect,
    reconnectTimer,
  }
}

export const WebsocketMixin = {
  data() {
    return {
      websock: null,
      reconnectTimer: null,
      wsTargetId: null,
      reconnectDelay: DEFAULT_RECONNECT_DELAY,
    }
  },
  methods: {
    initWebSocket(toId) {
      const userId = resolveUserId()
      const url = buildWsUrl(userId, toId)
      if (!url) return
      this.wsTargetId = toId ?? ''
      this.clearReconnect()
      this.closeWebSocket()

      try {
        const socket = new WebSocket(url)
        this.websock = socket
        socket.onopen = (evt) => {
          this.clearReconnect()
          if (typeof this.websocketOnopen === 'function') {
            this.websocketOnopen(evt)
          }
        }
        socket.onerror = (evt) => {
          if (typeof this.websocketOnerror === 'function') {
            this.websocketOnerror(evt)
          }
          this.scheduleReconnect()
        }
        socket.onclose = (evt) => {
          if (typeof this.websocketOnclose === 'function') {
            this.websocketOnclose(evt)
          }
          this.scheduleReconnect()
        }
        socket.onmessage = (evt) => {
          if (typeof this.websocketOnmessage === 'function') {
            this.websocketOnmessage(evt)
          }
        }
      } catch (e) {
        this.scheduleReconnect()
      }
    },
    websocketOnopen() {},
    websocketOnerror() {},
    websocketOnclose() {},
    websocketOnmessage() {},
    websocketSend(text) {
      try {
        this.websock && this.websock.send(text)
      } catch (err) {
        console.warn('send failed', err)
      }
    },
    shouldReconnect() {
      return true
    },
    scheduleReconnect() {
      if (this.reconnectDelay === 0) return
      if (!this.shouldReconnect()) return
      if (this.wsTargetId === null || this.wsTargetId === undefined) return
      this.clearReconnect()
      this.reconnectTimer = window.setTimeout(() => {
        this.initWebSocket(this.wsTargetId)
      }, this.reconnectDelay || DEFAULT_RECONNECT_DELAY)
    },
    clearReconnect() {
      if (this.reconnectTimer) {
        clearTimeout(this.reconnectTimer)
        this.reconnectTimer = null
      }
    },
    closeWebSocket() {
      if (this.websock) {
        try { this.websock.close() } catch (e) {}
        this.websock = null
      }
    },
  },
  beforeUnmount() {
    this.clearReconnect()
    this.closeWebSocket()
  },
}
