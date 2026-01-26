<template>
  <div v-if="visible" class="auth-overlay" @click="onOverlay">
    <div class="auth-modal" role="dialog" aria-modal="true" @click.stop>
      <div class="auth-header">
        <div class="auth-tabs">
          <button :class="['auth-tab', mode==='login' && 'active']" @click="switchMode('login')">登录</button>
          <button :class="['auth-tab', mode==='register' && 'active']" @click="switchMode('register')">注册</button>
        </div>
        <button class="auth-close" aria-label="关闭" @click="close">✕</button>
      </div>

      <div class="auth-body">
        <!-- 登录表单 -->
        <div v-show="mode==='login'" class="auth-pane auth-pane-login">
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" @keyup.enter="handleLogin" class="auth-form">
            <el-form-item prop="username">
              <label class="auth-label">用户名</label>
              <el-input v-model="loginForm.username" :prefix-icon="User" size="large" :placeholder="'用户名'" clearable />
            </el-form-item>
            <el-form-item prop="password">
              <label class="auth-label">密码</label>
              <el-input v-model="loginForm.password" :prefix-icon="Lock" size="large" type="password" show-password :placeholder="'密码'" clearable />
            </el-form-item>
            <el-form-item v-if="availableLoginRoles.length>1" prop="tableName">
              <label class="auth-label">角色</label>
              <el-select v-model="loginForm.tableName" class="w-full" size="large" placeholder="请选择角色">
                <el-option v-for="r in availableLoginRoles" :key="r.tableName" :label="r.roleName" :value="r.tableName" />
              </el-select>
            </el-form-item>
            <el-button type="primary" class="auth-submit" size="large" :loading="loading" @click="handleLogin">
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
            <div v-if="availableRegisterRoles.length>0" class="auth-tip">
              还没有账号？
              <el-link type="primary" @click="switchMode('register')">立即注册</el-link>
            </div>
          </el-form>
        </div>

        <!-- 注册表单 -->
        <div v-show="mode==='register'" class="auth-pane">
          <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-width="0" class="auth-form">
            <el-form-item v-if="availableRegisterRoles.length>1" prop="role">
              <label class="auth-label">注册身份</label>
              <el-select v-model="registerForm.role" class="w-full" placeholder="请选择注册身份">
                <el-option v-for="r in availableRegisterRoles" :key="r.tableName" :label="r.roleName" :value="r.roleName" />
              </el-select>
            </el-form-item>
            <el-form-item label-width="0" prop="username">
              <label class="auth-label">用户名</label>
              <el-input v-model="registerForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label-width="0" prop="password">
              <label class="auth-label">密码</label>
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
            <el-form-item label-width="0" prop="confirmPassword">
              <label class="auth-label">确认密码</label>
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
            </el-form-item>
            <el-button type="primary" class="auth-submit" size="large" :loading="loading" @click="handleRegister">注册</el-button>
            <div class="auth-tip">
              已有账号？
              <el-link type="primary" @click="switchMode('login')">去登录</el-link>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { http } from '@/utils/request'
import { menuConfig } from '@/config/menu'
import { useAuthModal } from '@/composables/useAuthModal'

interface RoleConfig {
  tableName: string
  roleName: string
  hasFrontLogin?: string
  hasFrontRegister?: string
  usernameField?: string
  passwordField?: string
}

const {
  visible,
  mode,
  close,
  setAuth,
} = useAuthModal()

const onOverlay = () => close()
const switchMode = (m: 'login'|'register') => {
  mode.value = m
}

// 登录
const loginFormRef = ref<FormInstance>()
const loading = ref(false)
const loginForm = reactive({ username: '', password: '', tableName: '' })
const availableLoginRoles = computed<RoleConfig[]>(() => (menuConfig as RoleConfig[]).filter(r => r.hasFrontLogin === '是'))
const availableRegisterRoles = computed<RoleConfig[]>(() => (menuConfig as RoleConfig[]).filter(r => r.hasFrontRegister === '是'))

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  tableName: [{ required: true, trigger: 'change', validator: (_: any, __: any, cb: any) => {
    if (availableLoginRoles.value.length <= 1) return cb()
    if (!loginForm.tableName) return cb(new Error('请选择角色'))
    cb()
  }}]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    const selectedTable = availableLoginRoles.value.length === 1 ? availableLoginRoles.value[0].tableName : loginForm.tableName
    if (!selectedTable) { ElMessage.warning('请选择角色'); return }
    loading.value = true
    const res: any = await http.get(`${selectedTable}/login`, { params: { username: loginForm.username, password: loginForm.password } })
    if (res?.code === 0) {
      localStorage.setItem('frontToken', res.token)
      localStorage.setItem('UserTableName', selectedTable)
      localStorage.setItem('username', loginForm.username)
      localStorage.setItem('frontSessionTable', selectedTable)
      const session: any = await http.get(`${selectedTable}/session`)
      const sessionData = session?.data || {}
      localStorage.setItem('frontSession', JSON.stringify(sessionData))
      const sessionId = sessionData.id ?? sessionData.userid ?? sessionData.userId ?? sessionData.uid
      if (sessionId !== undefined && sessionId !== null) {
        localStorage.setItem('frontUserid', String(sessionId))
        localStorage.setItem('userid', String(sessionId))
      }
      setAuth(res.token, loginForm.username, selectedTable)
      // 通知外层（如导航栏）刷新登录态
      window.dispatchEvent(new CustomEvent('front-auth-updated'))
      ElMessage.success('登录成功')
      close()
    } else {
      ElMessage.error(res?.msg || '用户名或密码错误')
    }
  } catch (e) {
    ElMessage.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 注册
const registerFormRef = ref<FormInstance>()
const registerForm = reactive({ role: '', username: '', password: '', confirmPassword: '' })
const registerRules = reactive<FormRules>({
  role: [{ required: true, trigger: 'change', validator: (_r: any, _v: any, cb: any) => {
    if (availableRegisterRoles.value.length <= 1) return cb()
    if (!registerForm.role) return cb(new Error('请选择注册身份'))
    cb()
  }}],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, trigger: 'blur', validator: (_: any, v: any, cb: any) => {
      if (!v) return cb(new Error('请再次输入密码'))
      if (v !== registerForm.password) return cb(new Error('两次输入密码不一致'))
      cb()
    }}
  ]
})

const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      const roles = availableRegisterRoles.value
      const selected = roles.length === 1 ? roles[0] : roles.find(r => r.roleName === registerForm.role)
      if (!selected) { ElMessage.warning('请选择注册身份'); return }
      const payload: Record<string, any> = {}
      const userField = selected.usernameField || 'username'
      const passField = selected.passwordField || 'password'
      payload[userField] = registerForm.username
      payload[passField] = registerForm.password
      const res: any = await http.post(`${selected.tableName}/register`, payload)
      if (res?.code === 0) {
        ElMessage.success('注册成功，请登录')
        switchMode('login')
      } else {
        ElMessage.error(res?.msg || '注册失败')
      }
    } catch (e) {
      ElMessage.error('网络错误，请重试')
    }
  })
}

onMounted(() => {
  if (availableLoginRoles.value.length === 1) loginForm.tableName = availableLoginRoles.value[0].tableName
  const roles = availableRegisterRoles.value
  if (roles.length === 1) registerForm.role = roles[0].roleName
})
</script>

<style>

</style>
