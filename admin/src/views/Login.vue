<template>
  <div class="login-container min-h-screen bg-gradient-to-br from-blue-400 to-purple-600 flex items-center justify-center">
    <div class="login-card bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <h1 class="text-3xl font-bold text-gray-800">{{ appTitle }}</h1>
        <p class="text-gray-600 mt-2">登录您的账户</p>
      </div>

      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="rules"
          class="login-form"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              size="large"
              :prefix-icon="User"
              clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              size="large"
              :prefix-icon="Lock"
              show-password
              clearable
          />
        </el-form-item>

        <el-form-item v-if="availableRoles.length > 1" prop="role">
          <el-select
              v-model="loginForm.role"
              placeholder="请选择角色"
              size="large"
              class="w-full"
              clearable
          >
            <el-option
                v-for="role in availableRoles"
                :key="role.tableName"
                :label="role.roleName"
                :value="role.roleName"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              size="large"
              class="w-full"
              :loading="loading"
              @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>

        <div v-if="availableRegisterRoles.length > 0 && !adminOnlyBackLogin" class="text-center mt-4">
          <el-link type="primary" @click="goToRegister">
            还没有账号？立即注册
          </el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { menuConfig } from '@/utils/menu'

// 应用标题
const appTitle = import.meta.env.VITE_APP_TITLE || '管理系统'

interface RoleConfig {
  tableName: string
  roleName: string
  hasBackLogin: string
  hasBackRegister?: string
}

const router = useRouter()
const authStore = useAuthStore()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

// 表单数据
const loginForm = reactive({
  username: '',
  password: '',
  role: ''
})

// 可登录后台的角色
const availableRoles = computed(() => {
  return menuConfig.filter((role: RoleConfig) => role.hasBackLogin === '是')
})

// 可后台注册的角色
const availableRegisterRoles = computed(() => {
  return menuConfig.filter((role: RoleConfig) => role.hasBackRegister === '是')
})

// 仅管理员可后台登录时，关闭注册入口
const adminOnlyBackLogin = computed(() => {
  return availableRoles.value.length === 1 && availableRoles.value[0]?.tableName === 'admin'
})

// 验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return

    // 如果有多个角色且未选择角色
    if (availableRoles.value.length > 1 && !loginForm.role) {
      ElMessage.warning('请选择角色')
      return
    }

    // 如果只有一个角色，自动选择
    const selectedRole = availableRoles.value.length === 1
        ? availableRoles.value[0].roleName
        : loginForm.role

    loading.value = true
    const success = await authStore.login(loginForm.username, loginForm.password, selectedRole)

    if (success) {
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 跳转到注册页面
const goToRegister = () => {
  // 如果只有一个可注册身份，带上默认 role；
  // 若登录选择了角色且该角色可注册，也带上该 role；否则让注册页自行选择
  let roleQuery = ''
  if (availableRegisterRoles.value.length === 1) {
    roleQuery = availableRegisterRoles.value[0].roleName
  } else if (loginForm.role) {
    const can = availableRegisterRoles.value.find(r => r.roleName === loginForm.role)
    if (can) roleQuery = loginForm.role
  }
  router.push({ path: '/register', query: roleQuery ? { role: roleQuery } : undefined })
}

// 初始化默认角色
onMounted(() => {
  // 如果只有一个可登录的角色，自动选择
  if (availableRoles.value.length === 1) {
    loginForm.role = availableRoles.value[0].roleName
  }
})
</script>

<style scoped>
.login-container {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  backdrop-filter: blur(10px);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.login-form .el-form-item {
  margin-bottom: 24px;
}

.login-form .el-input__inner {
  border-radius: 8px;
}
</style>
