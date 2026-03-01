<template>
  <div v-if="canView" class="front-list-page">
    <div v-if="centerType" class="back-btn-box">
      <el-button class="back-btn" size="small" @click="handleBack">
        <el-icon><ArrowLeft /></el-icon>
        <span>返回</span>
      </el-button>
    </div>

    <el-card class="list-card" shadow="hover">
      <template #header>
        <div class="list-card-header">训练计划</div>
      </template>

      <el-form :inline="true" :model="formSearch" class="search-form">
        <el-form-item label="计划名称">
          <el-input v-model="formSearch.planname" placeholder="请输入计划名称" clearable />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="formSearch.auditstatus" placeholder="请选择审核状态" clearable>
            <el-option label="待审核" value="待审核" />
            <el-option label="通过" value="通过" />
            <el-option label="不通过" value="不通过" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划状态">
          <el-select v-model="formSearch.planstatus" placeholder="请选择计划状态" clearable>
            <el-option label="待审核" value="待审核" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已驳回" value="已驳回" />
            <el-option label="已暂停" value="已暂停" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="dataList" v-loading="loading" border class="w-full">
        <el-table-column prop="planname" label="计划名称" min-width="160" />
        <el-table-column prop="coachname" label="教练" min-width="120" />
        <el-table-column prop="auditstatus" label="审核状态" min-width="100" />
        <el-table-column prop="planstatus" label="计划状态" min-width="100" />
        <el-table-column prop="starttime" label="开始时间" min-width="160" />
        <el-table-column prop="endtime" label="结束时间" min-width="160" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="toDetail(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-box">
        <el-pagination
          v-model:current-page="pageIndex"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchList"
          @current-change="fetchList"
        />
      </div>
    </el-card>
  </div>
  <div v-else class="no-access">暂无权限</div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { http } from '@/utils/request'
import { isAuth } from '@/utils/auth'

const router = useRouter()
const route = useRoute()

const centerType = computed(() => route.query.centerType === '1' || route.query.centerType === 'true')
const canView = computed(() => isAuth('trainingplan', '查看'))

const formSearch = reactive({
  planname: '',
  auditstatus: '',
  planstatus: ''
})

const loading = ref(false)
const dataList = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchList = async () => {
  if (!canView.value) return
  loading.value = true
  try {
    const params: Record<string, any> = {
      page: pageIndex.value,
      limit: pageSize.value
    }
    if (formSearch.planname) params.planname = '%' + formSearch.planname + '%'
    if (formSearch.auditstatus) params.auditstatus = formSearch.auditstatus
    if (formSearch.planstatus) params.planstatus = formSearch.planstatus
    const endpoint = centerType.value ? 'trainingplan/page' : 'trainingplan/list'
    const res: any = await http.get(endpoint, { params })
    const payload = res?.data
    const list = Array.isArray(payload?.data?.list)
      ? payload.data.list
      : Array.isArray(payload?.list)
        ? payload.list
        : []
    dataList.value = list
    total.value = Number(payload?.data?.total ?? payload?.total ?? list.length)
  } catch {
    dataList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const search = () => {
  pageIndex.value = 1
  fetchList()
}

const reset = () => {
  formSearch.planname = ''
  formSearch.auditstatus = ''
  formSearch.planstatus = ''
  pageIndex.value = 1
  fetchList()
}

const toDetail = (row: any) => {
  router.push({ path: '/index/trainingplanDetail', query: { id: row.id, centerType: centerType.value ? '1' : undefined } })
}

const handleBack = () => router.back()

onMounted(fetchList)
</script>

<style scoped>
.list-card-header {
  font-weight: 600;
}
.pagination-box {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
.no-access {
  padding: 40px;
  text-align: center;
  color: #999;
}
</style>
