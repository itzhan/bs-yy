<template>
  <div class="main-content bg-gray-50 p-6">
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="教练姓名">
          <el-input v-model="searchForm.coachname" placeholder="教练姓名" clearable @keydown.enter="search" />
        </el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button type="primary" @click="reset">重置</el-button>
      </el-form>
    </div>

    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-button v-if="isAuth('coachreview', '新增')" type="success" @click="addOrUpdateHandler()">新增</el-button>
      <el-button v-if="isAuth('coachreview', '删除')" type="danger" :disabled="!dataListSelections.length" @click="deleteHandler()">删除</el-button>
    </div>

    <div class="bg-white p-4 rounded-lg shadow">
      <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="coachname" label="教练" min-width="120" />
        <el-table-column prop="username" label="会员" min-width="120" />
        <el-table-column prop="rating" label="评分" width="120">
          <template #default="scope">
            <el-rate v-model="scope.row.rating" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="220" />
        <el-table-column prop="reply" label="回复" min-width="200" />
        <el-table-column prop="addtime" label="时间" min-width="160" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="addOrUpdateHandler(scope.row.id, 'info')">查看</el-button>
            <el-button v-if="isAuth('coachreview', '修改')" size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="mt-4 text-right"
        v-model:current-page="pageIndex"
        v-model:page-size="pageSize"
        :total="totalPage"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
      />
    </div>

    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/request'
import { isAuth } from '@/utils/auth'
import AddOrUpdate from './AddOrUpdate.vue'

const searchForm = reactive({
  coachname: ''
})

const dataList = ref<any[]>([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const dataListSelections = ref<any[]>([])

const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref<any>()

const getDataList = async () => {
  dataListLoading.value = true
  try {
    const params: Record<string, any> = {
      page: pageIndex.value,
      limit: pageSize.value
    }
    if (searchForm.coachname) params.coachname = '%' + searchForm.coachname + '%'
    const res: any = await http.get('coachreview/page', { params })
    const payload = res?.data || {}
    dataList.value = payload.list || []
    totalPage.value = Number(payload.total ?? 0)
  } finally {
    dataListLoading.value = false
  }
}

const search = () => {
  pageIndex.value = 1
  getDataList()
}

const reset = () => {
  searchForm.coachname = ''
  pageIndex.value = 1
  getDataList()
}

const sizeChangeHandle = (val: number) => {
  pageSize.value = val
  pageIndex.value = 1
  getDataList()
}

const currentChangeHandle = (val: number) => {
  pageIndex.value = val
  getDataList()
}

const selectionChangeHandler = (val: any[]) => {
  dataListSelections.value = val
}

const addOrUpdateHandler = (id?: number, type?: string) => {
  addOrUpdateVisible.value = true
  nextTick(() => {
    addOrUpdateRef.value?.init(id, type)
  })
}

const deleteHandler = async (ids?: number[]) => {
  const deleteIds = ids || dataListSelections.value.map(item => item.id)
  if (!deleteIds.length) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  try {
    await ElMessageBox.confirm('确定要删除选中记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await http.post('coachreview/delete', deleteIds)
    ElMessage.success('操作成功')
    getDataList()
  } catch {
    // ignore
  }
}

onMounted(getDataList)
</script>
