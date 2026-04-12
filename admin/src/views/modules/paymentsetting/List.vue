<template>
  <div class="main-content bg-gray-50 p-6">
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <div class="inline-block mr-4">
          <label class="inline-block mr-2 leading-10">支付方式名称</label>
          <el-input v-model="searchForm.payname" placeholder="支付方式名称" clearable @keydown.enter="search()" />
        </div>
        <div class="inline-block mr-4">
          <label class="inline-block mr-2 leading-10">是否启用</label>
          <el-select v-model="searchForm.enabled" placeholder="全部" clearable>
            <el-option label="是" value="是" />
            <el-option label="否" value="否" />
          </el-select>
        </div>
        <el-button type="primary" @click="search()"><el-icon><Search /></el-icon>搜索</el-button>
        <el-button type="primary" @click="reset()"><el-icon><RefreshLeft /></el-icon>重置</el-button>
      </el-form>
    </div>

    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <div class="flex justify-between items-center">
        <div class="space-x-2">
          <el-button type="success" @click="addOrUpdateHandler()"><el-icon><Plus /></el-icon>新增</el-button>
          <el-button type="danger" :disabled="!dataListSelections.length" @click="deleteHandler()"><el-icon><Delete /></el-icon>删除</el-button>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow">
      <el-table class="w-full" :data="dataList" v-loading="dataListLoading" @selection-change="selectionChangeHandler" stripe border>
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column prop="payname" label="支付方式名称" />
        <el-table-column prop="paycode" label="编码" />
        <el-table-column prop="payicon" label="图标">
          <template #default="scope">
            <el-image v-if="scope.row.payicon" :src="imgSrc(scope.row.payicon)" style="width:40px;height:40px" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="是否启用">
          <template #default="scope">
            <el-tag :type="scope.row.enabled === '是' ? 'success' : 'danger'">{{ scope.row.enabled }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortorder" label="排序" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="addOrUpdateHandler(scope.row.id, 'info')">查看</el-button>
            <el-button type="warning" size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
            <el-button type="danger" size="small" @click="deleteHandler([scope.row.id])">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-center p-4">
        <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex" :page-sizes="[10, 20, 50]" :page-size="pageSize" :total="totalPage" layout="total, sizes, prev, pager, next, jumper" />
      </div>
    </div>

    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { http } from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import AddOrUpdate from './AddOrUpdate.vue'

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const imgSrc = (p: string) => { if (!p) return ''; return p.startsWith('http') ? p : baseUrl + (p.startsWith('/') ? p : '/' + p) }

const dataList = ref<any[]>([])
const dataListLoading = ref(false)
const dataListSelections = ref<any[]>([])
const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref()
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const searchForm = reactive({ payname: undefined as any, enabled: undefined as any })

const getDataList = () => {
  dataListLoading.value = true
  const params: any = { page: pageIndex.value, limit: pageSize.value, sort: 'sortorder', order: 'asc' }
  if (searchForm.payname) params.payname = '%' + searchForm.payname + '%'
  if (searchForm.enabled) params.enabled = searchForm.enabled
  http.get('paymentsetting/page', { params }).then((res: any) => {
    if (res?.code === 0) {
      dataList.value = res.data?.list || []
      totalPage.value = Number(res.data?.total ?? 0)
    } else {
      dataList.value = []
      totalPage.value = 0
    }
    dataListLoading.value = false
  }).catch(() => { dataList.value = []; totalPage.value = 0; dataListLoading.value = false })
}

const search = () => { pageIndex.value = 1; getDataList() }
const reset = () => { searchForm.payname = undefined; searchForm.enabled = undefined; pageIndex.value = 1; getDataList() }
const sizeChangeHandle = (val: number) => { pageSize.value = val; pageIndex.value = 1; getDataList() }
const currentChangeHandle = (val: number) => { pageIndex.value = val; getDataList() }
const selectionChangeHandler = (val: any[]) => { dataListSelections.value = val }

const addOrUpdateHandler = (id?: number, type?: string) => {
  addOrUpdateVisible.value = true
  nextTick(() => { addOrUpdateRef.value.init(id, type) })
}

const deleteHandler = async (ids?: number[]) => {
  const deleteIds = ids || dataListSelections.value.map(item => item.id)
  if (!deleteIds.length) { ElMessage.warning('请选择要删除的记录'); return }
  try {
    await ElMessageBox.confirm('确定要删除选中记录吗？', '提示', { type: 'warning' })
    await http.post('paymentsetting/delete', deleteIds)
    ElMessage.success('操作成功')
    getDataList()
  } catch {}
}

onMounted(() => { getDataList() })
</script>
