<template>
  <div class="detail-container">
    <el-card shadow="hover">
      <template #header>
        <div class="detail-header">
          <span>计划明细详情</span>
          <el-button size="small" @click="handleBack">返回</el-button>
        </div>
      </template>
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="计划名称">{{ detail.planname }}</el-descriptions-item>
        <el-descriptions-item label="序号">{{ detail.dayno }}</el-descriptions-item>
        <el-descriptions-item label="训练内容" :span="2">{{ detail.itemcontent }}</el-descriptions-item>
        <el-descriptions-item label="目标">{{ detail.target }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ detail.sortno }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
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
    const res: any = await http.get(`trainingplanitem/detail/${id}`)
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
