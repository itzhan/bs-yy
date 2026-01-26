<template>
  <div class="update-password-container">
    <el-card class="update-password-card" header="修改密码">
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input 
            v-model="passwordForm.oldPassword" 
            type="password" 
            placeholder="请输入原密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            v-model="passwordForm.newPassword" 
            type="password" 
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="passwordForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleUpdatePassword" :loading="loading">
            确认修改
          </el-button>
          <el-button @click="resetForm">
            重置
          </el-button>
          <el-button @click="goBack">
            返回
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const passwordFormRef = ref<FormInstance>()
const loading = ref(false)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateOldPassword = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入原密码'))
  } else {
    callback()
  }
}

const validateNewPassword = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入新密码'))
  } else if (value === passwordForm.oldPassword) {
    callback(new Error('新密码不能与原密码相同'))
  } else {
    if (passwordForm.confirmPassword !== '') {
      passwordFormRef.value!.validateField('confirmPassword')
    }
    callback()
  }
}

const validateConfirmPassword = (_rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = reactive<FormRules>({
  oldPassword: [
    { required: true, validator: validateOldPassword, trigger: 'blur' }
  ],
  newPassword: [
    { required: true, validator: validateNewPassword, trigger: 'blur' },
    { min: 6, message: '密码长度不少于 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
})

const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        const result = await authStore.updatePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        
        if (result.success) {
          ElMessage.success('密码修改成功！请重新登录')
          
          // 清除登录状态，跳转到登录页
          authStore.logout()
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

const resetForm = () => {
  passwordFormRef.value?.resetFields()
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.update-password-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.update-password-card {
  width: 500px;
  max-width: 100%;
}

.el-form-item {
  margin-bottom: 25px;
}

.el-button {
  margin-right: 10px;
}

.el-button:last-child {
  margin-right: 0;
}

:deep(.el-card__header) {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}
</style>