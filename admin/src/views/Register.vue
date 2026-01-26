<template>
  <div class="register-container">
    <div class="register-form">
      <h2 class="title">注册</h2>
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item v-if="availableRegisterRoles.length > 1" label="注册身份" prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择注册身份" class="w-full">
            <el-option
              v-for="role in availableRegisterRoles"
              :key="role.tableName"
              :label="role.roleName"
              :value="role.roleName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码"
            show-password 
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入密码"
            show-password 
          />
        </el-form-item>
        
        
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading">
            注册
          </el-button>
          <el-button @click="goToLogin">
            已有账号，去登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { menuConfig } from '@/utils/menu'
import type { RoleConfig } from '@/types/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive({
  role: '',
  username: '',
  password: '',
  confirmPassword: ''
})

// 可后台注册的角色（根据 menu 配置）
const availableRegisterRoles = computed(() => {
  return (menuConfig as unknown as RoleConfig[]).filter((r: any) => r.hasBackRegister === '是')
})

const validatePassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (registerForm.confirmPassword !== '') {
      registerFormRef.value!.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = reactive<FormRules>({
  role: [
    { required: true, message: '请选择注册身份', trigger: 'change', validator: (_r: any, _v: any, cb: any) => {
      if (availableRegisterRoles.value.length <= 1) return cb()
      if (!registerForm.role) return cb(new Error('请选择注册身份'))
      cb()
    }}
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePassword, trigger: 'blur' },
    { min: 6, message: '密码长度不少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
})

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        const selectedRoleName = availableRegisterRoles.value.length === 1
          ? availableRegisterRoles.value[0].roleName
          : registerForm.role

        if (!selectedRoleName) {
          ElMessage.warning('请选择注册身份')
          loading.value = false
          return
        }

        const result = await authStore.register({
          username: registerForm.username,
          password: registerForm.password
        }, selectedRoleName)
        
        if (result.success) {
          ElMessage.success(result.message)
          router.push('/login')
        } else {
          ElMessage.error(result.message)
        }
      } catch (error) {
        ElMessage.error('网络错误，请重试')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}

// 初始化默认注册角色（若仅一个或通过 query 指定）
onMounted(() => {
  const queryRole = (route.query.role as string) || ''
  const roles = availableRegisterRoles.value
  if (roles.length === 1) {
    registerForm.role = roles[0].roleName
  } else if (queryRole) {
    const exist = roles.find(r => r.roleName === queryRole)
    if (exist) registerForm.role = queryRole
  }
})
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-form {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  width: 500px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button {
  width: 100px;
  margin-right: 10px;
}

.el-button:last-child {
  margin-right: 0;
}
</style>
