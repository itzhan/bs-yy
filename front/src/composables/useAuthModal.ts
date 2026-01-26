import { ref, computed } from 'vue'

// 单例状态：弹窗可见性与模式
const _visible = ref(false)
const _mode = ref<'login'|'register'>('login')

// 全局登录态（便于导航栏等联动刷新）
const _token = ref<string>(localStorage.getItem('frontToken') || '')
const _username = ref<string>(localStorage.getItem('username') || '')
const _sessionTable = ref<string>(localStorage.getItem('frontSessionTable') || '')
const _isLoggedIn = computed(() => !!_token.value)

export function useAuthModal() {
  const openLogin = () => { _mode.value = 'login'; _visible.value = true }
  const openRegister = () => { _mode.value = 'register'; _visible.value = true }
  const open = (m: 'login'|'register' = 'login') => { _mode.value = m; _visible.value = true }
  const close = () => { _visible.value = false }

  const setAuth = (token: string, username: string, table: string) => {
    _token.value = token
    _username.value = username
    _sessionTable.value = table
  }
  const clearAuth = () => {
    _token.value = ''
    _username.value = ''
    _sessionTable.value = ''
  }

  return {
    // 弹窗控制
    visible: _visible,
    mode: _mode,
    open, openLogin, openRegister, close,
    // 登录态
    token: _token,
    username: _username,
    sessionTable: _sessionTable,
    isLoggedIn: _isLoggedIn,
    setAuth, clearAuth,
  }
}

