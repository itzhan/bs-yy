<template>
  <div class="main-content bg-gray-50 p-6">
    <!-- 搜索表单区域 -->
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <div class="inline-block mr-4">
          <label class="inline-block mr-2 leading-10">用户名</label>
          <el-input
            v-model="searchForm.nickname"
            placeholder="用户名"
            clearable
            @keydown.enter="search()"
          />
        </div>
        <div class="inline-block mr-4">
          <label class="inline-block mr-2 leading-10">评论内容</label>
          <el-input
            v-model="searchForm.content"
            placeholder="评论内容"
            clearable
            @keydown.enter="search()"
          />
        </div>
        <el-button type="primary" @click="search()">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <!-- 返回按钮 -->
        <el-button type="warning" @click="$router.go(-1)" v-if="route.query.refid">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
      </el-form>
    </div>

    <!-- 数据表格区域 -->
    <div class="bg-white rounded-lg shadow">
      <el-table
        class="w-full"
        :data="dataList"
        v-loading="dataListLoading"
        stripe
        border
      >
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column prop="nickname" label="评论用户" />
        <el-table-column prop="content" label="评论内容" :show-overflow-tooltip="true">
          <template #default="scope">
            <span v-html="scope.row.content"></span>
          </template>
        </el-table-column>
        <el-table-column prop="reply" label="回复内容" :show-overflow-tooltip="true">
          <template #default="scope">
            <span v-html="scope.row.reply"></span>
          </template>
        </el-table-column>
        <el-table-column prop="addtime" label="评论时间" />

        <!-- 操作列 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="replyHandler(scope.row)"
            >
              回复
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteHandler([scope.row.id])"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="flex justify-center p-4">
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </div>

    <!-- 回复对话框 -->
    <el-dialog v-model="replyVisible" title="回复评论" width="60%">
      <div class="reply-dialog">
        <div class="original-comment mb-6 p-4 border-l-4 border-blue-400 bg-blue-50 rounded-lg shadow-sm">
          <div class="flex items-start space-x-3">
            <div class="flex-1 min-w-0">
              <div class="flex items-center space-x-2 mb-2">
                <h5 class="text-sm font-medium text-gray-900">
                  {{ currentReplyComment.nickname || '匿名用户' }}
                </h5>
                <span class="text-xs text-gray-500">发表了评论</span>
              </div>
              <div class="text-sm text-gray-700 leading-relaxed">
                <span v-html="currentReplyComment.content"></span>
              </div>
              <div v-if="currentReplyComment.addtime" class="text-xs text-gray-400 mt-2">
                {{ currentReplyComment.addtime }}
              </div>
            </div>
          </div>
        </div>
        
        <el-form :model="replyForm" ref="replyFormRef">
          <el-form-item label="回复内容" prop="reply">
            <RichEditor
                v-model="replyForm.reply"
                placeholder="请输入回复内容..."
                height="300px"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="replyVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReply" :loading="submittingReply">
            发布回复
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 图片预览 -->
    <el-dialog v-model="previewVisible" title="预览图" width="50%">
      <img :src="previewImg" alt="" style="width: 100%;">
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {  ref, reactive, onMounted } from 'vue'
import { http } from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, ArrowLeft } from '@element-plus/icons-vue'
import type { DiscusscourseItem } from '@/types/common'
import { useRoute } from 'vue-router'

const route = useRoute()

// 响应式数据
const dataList = ref<DiscusscourseItem[]>([])
const dataListLoading = ref(false)

// 分页数据
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)

// 图片预览
const previewImg = ref('')
const previewVisible = ref(false)

// 回复功能
const replyVisible = ref(false)
const submittingReply = ref(false)
const currentReplyComment = ref<DiscusscourseItem>({} as DiscusscourseItem)
// 回复表单
const replyForm = reactive({
  reply: ''
})
const replyFormRef = ref()

// 搜索表单
const searchForm = reactive({
  nickname: '',
  content: ''
})
// 获取数据列表
const getDataList = async () => {
  dataListLoading.value = true
  const params: any = {
    page: pageIndex.value,
    limit: pageSize.value,
    sort: 'id',
    order: 'desc'
  }
  if (searchForm.nickname) {
    params.nickname = '%' + searchForm.nickname + '%'
  }
  if (searchForm.content) {
    params.content = '%' + searchForm.content + '%'
  }
  // 添加refid参数，用于查看关联的评论
  if (route.query.refid) {
    params.refid = route.query.refid
  }
  try{
    const { data } = await http.get('discusscourse/page', { params })
    dataList.value = data.list || []
    totalPage.value = data.total || 0
  } finally {
    dataListLoading.value = false
  }
}

// 搜索
const search = () => {
  pageIndex.value = 1
  getDataList()
}

// 分页相关
const sizeChangeHandle = (val: number) => {
  pageSize.value = val
  pageIndex.value = 1
  getDataList()
}

const currentChangeHandle = (val: number) => {
  pageIndex.value = val
  getDataList()
}


// 删除
const deleteHandler = async (ids?: number[]) => {
  const deleteIds = ids || []
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
    await http.post('discusscourse/delete', deleteIds)
    await getDataList()
  } catch {
    // 用户取消操作
  }
}

// 回复处理
const replyHandler = (comment: DiscusscourseItem) => {
  currentReplyComment.value = comment
  replyForm.reply = comment.reply
  replyVisible.value = true
}

// 提交回复
const submitReply = async () => {
  if (!replyForm.reply.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  submittingReply.value = true
  try {
    const updateData = {
      ...currentReplyComment.value,
      reply: replyForm.reply
    }
    await http.post('discusscourse/update', updateData)
    replyVisible.value = false
    await getDataList() // 刷新数据
  } finally {
    submittingReply.value = false
  }
}

// 组件挂载时获取数据
onMounted(async () => {
  await getDataList()
})
</script>