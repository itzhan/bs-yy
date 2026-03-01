<template>
  <div class="detail-container">
    <el-card shadow="hover">
      <template #header>
        <div class="detail-header">
          <span>训练计划详情</span>
          <el-button size="small" @click="handleBack">返回</el-button>
        </div>
      </template>
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="计划名称">{{ detail.planname }}</el-descriptions-item>
        <el-descriptions-item label="教练">{{ detail.coachname }}</el-descriptions-item>
        <el-descriptions-item label="训练目标" :span="2">{{ detail.plangoal }}</el-descriptions-item>
        <el-descriptions-item label="计划内容" :span="2">{{ detail.plancontent }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detail.starttime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detail.endtime }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">{{ detail.auditstatus }}</el-descriptions-item>
        <el-descriptions-item label="计划状态">{{ detail.planstatus }}</el-descriptions-item>
        <el-descriptions-item label="审核回复" :span="2">{{ detail.auditreply }}</el-descriptions-item>
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
    const res: any = await http.get(`trainingplan/detail/${id}`)
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
