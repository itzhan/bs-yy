<template>
  <div class="detail-container">
    <el-card shadow="hover">
      <template #header>
        <div class="detail-header">
          <span>训练记录详情</span>
          <el-button size="small" @click="handleBack">返回</el-button>
        </div>
      </template>
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="计划名称">{{ detail.planname }}</el-descriptions-item>
        <el-descriptions-item label="记录时间">{{ detail.recorddate }}</el-descriptions-item>
        <el-descriptions-item label="训练时长">{{ detail.duration }} 分钟</el-descriptions-item>
        <el-descriptions-item label="完成度">{{ detail.completionrate }}%</el-descriptions-item>
        <el-descriptions-item label="训练内容" :span="2">{{ detail.content }}</el-descriptions-item>
        <el-descriptions-item label="教练点评" :span="2">{{ detail.coachcomment }}</el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="暂无数据" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { http } from '@/utils/request'

const route = useRoute()
const router = useRouter()
const detail = ref<any>(null)

const fetchDetail = async () => {
  const id = route.query.id
  if (!id) return
  try {
    const res: any = await http.get(`trainingrecord/detail/${id}`)
    detail.value = res?.data || null
  } catch {
    detail.value = null
  }
}

const handleBack = () => router.back()

onMounted(fetchDetail)
</script>

<style scoped>
.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
