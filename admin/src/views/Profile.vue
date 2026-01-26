<template>
  <div class="profile-container">
    <el-card class="profile-card" header="个人信息">
      <div class="profile-content">
        <el-button 
          type="primary" 
          @click="openEditDialog"
          :loading="loading"
        >
          编辑个人信息
        </el-button>
        
        <el-button @click="goBack">
          返回
        </el-button>
      </div>
      
      <!-- 动态加载对应角色的AddOrUpdate组件作为弹窗 -->
      <component
        :is="currentComponent"
        v-if="currentComponent && showDialog"
        ref="addOrUpdateRef"
        @refreshDataList="handleRefresh"
      />
      
      <!-- 加载失败的提示 -->
      <el-result
        v-if="!currentComponent"
        icon="warning"
        title="暂无个人信息编辑功能"
        sub-title="当前角色暂不支持个人信息编辑"
      >
        <template #extra>
          <el-button @click="goBack">返回</el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, shallowRef, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const addOrUpdateRef = ref()
const loading = ref(false)
const showDialog = ref(false)

const currentRole = computed(() => authStore.currentRole)

// 动态组件
const currentComponent = shallowRef(null)

// 加载对应角色的AddOrUpdate组件
const loadComponent = async () => {
  const roleName = currentRole.value
  if (!roleName) return
  
  try {
    const module = await import(`@/views/modules/${roleName}/AddOrUpdate.vue`)
    currentComponent.value = module.default
  } catch (error) {
    console.warn(`无法加载组件: ${roleName}/AddOrUpdate.vue`, error)
  }
}

// 打开编辑弹窗
const openEditDialog = async () => {
  if (!currentComponent.value) {
    ElMessage.warning('当前角色暂不支持个人信息编辑')
    return
  }
  
  loading.value = true
  try {
    const result = await authStore.getUserProfile()
    if (result.success && result.data) {
      showDialog.value = true
      // 等待组件挂载完成后初始化
      await nextTick()
      if (addOrUpdateRef.value && addOrUpdateRef.value.init) {
        addOrUpdateRef.value.init(result.data.id)
      }
    } else {
      ElMessage.error(result.message || '获取个人信息失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请重试')
  } finally {
    loading.value = false
  }
}

// 处理数据刷新
const handleRefresh = () => {
  ElMessage.success('个人信息更新成功')
  showDialog.value = false
}

// 返回
const goBack = () => {
  router.go(-1)
}

onMounted(async () => {
  await loadComponent()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.profile-card {
  width: 700px;
  max-width: 100%;
}

.profile-content {
  text-align: center;
  padding: 40px 20px;
}

.profile-content .el-button {
  margin: 0 10px;
}

:deep(.el-card__header) {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}
</style>