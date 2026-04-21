<template>
  <div class="main-content bg-gray-50 p-6">
    <!-- 搜索表单区域 -->
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">课程名称</label>
            <div>
                <el-select
                    v-model="searchForm.coursename"
                    placeholder="请选择课程名称"
                    clearable
                    class="w-40"
                >
                    <el-option
                        v-for="(item, index) in coursenameOptions"
                        :key="index"
                        :label="item"
                        :value="item"
                    />
                </el-select>
            </div>
        </div>
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">课程分类</label>
            <div>
                <el-select
                    v-model="searchForm.coursetype"
                    placeholder="请选择课程分类"
                    clearable
                    class="w-40"
                >
                    <el-option
                        v-for="(item, index) in coursetypeOptions"
                        :key="index"
                        :label="item"
                        :value="item"
                    />
                </el-select>
            </div>
        </div>
        <el-button type="primary" @click="search()">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button type="primary" @click="reset()">
          <el-icon><RefreshLeft /></el-icon>
          重置
        </el-button>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <div class="flex justify-between items-center">
        <div class="space-x-2">
          <el-button
            v-if="!false && isAuth('cancelcourseenrollment', '新增')"
            type="success"
            @click="addOrUpdateHandler()"
          >
            <el-icon><Plus /></el-icon>
            新增
          </el-button>
          <el-button
            v-if="!false && isAuth('cancelcourseenrollment', '删除')"
            type="danger"
            :disabled="!dataListSelections.length"
            @click="deleteHandler()"
          >
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
          <download-excel
            v-if="isAuth('cancelcourseenrollment', '导出')"
            class="inline-block align-middle"
            :data="exportRows"
            :fields="exportFields"
            :name="exportFileName"
            type="xls"
          >
            <el-button type="primary">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </download-excel>
        </div>
      </div>
    </div>

    <!-- 数据展示区域 -->
    <div v-if="!mapConfig.enabled || viewMode === 'table'" class="bg-white rounded-lg shadow">
      <el-table
        v-if="isAuth('cancelcourseenrollment', '查看')"
        class="w-full"
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandler"
        stripe
        border
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column prop="coursename" label="课程名称" />
        <el-table-column prop="courseimage" label="课程封面" width="200">
        <template #default="scope">
            <div v-if="scope.row.courseimage">
            <img
                v-if="scope.row.courseimage.substring(0,4)=='http'"
                :src="scope.row.courseimage.split(',')[0]"
                width="100"
                height="100"
                style="object-fit: cover"
                @click="imgPreView(scope.row.courseimage.split(',')[0])"
                class="cursor-pointer"
            >
            <img
                v-else
                :src="baseUrl + scope.row.courseimage.split(',')[0]"
                width="100"
                height="100"
                style="object-fit: cover"
                @click="imgPreView(baseUrl + scope.row.courseimage.split(',')[0])"
                class="cursor-pointer"
            >
            </div>
            <div v-else class="text-gray-400">无图片</div>
        </template>
        </el-table-column>
        <el-table-column prop="coursetype" label="课程分类" />
        <el-table-column prop="classtime" label="上课时间" />
        <el-table-column prop="coachname" label="教练" />
        <el-table-column prop="cancelreason" label="取消原因" />
        <!-- 操作列 -->
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="scope">
            <el-button
                v-if="isAuth('cancelcourseenrollment', '查看')"
                type="primary"
                size="small"
                @click="addOrUpdateHandler(scope.row.id, 'info')"
            >
              查看
            </el-button>
            <el-button
                v-if="isAuth('cancelcourseenrollment', '修改')"
                type="warning"
                size="small"
                @click="addOrUpdateHandler(scope.row.id)"
            >
              修改
            </el-button>
            <el-button
                v-if="isAuth('cancelcourseenrollment', '删除')"
                type="danger"
                size="small"
                @click="deleteHandler([scope.row.id])"
            >
              删除
            </el-button>
            <el-button
                v-if="isAuth('cancelcourseenrollment', '私聊')"
                type="success"
                size="small"
                @click="chatOpen(scope.row)"
            >
              私聊
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


    <!-- 新增/修改弹窗 -->
    <add-or-update
        v-if="addOrUpdateVisible"
        ref="addOrUpdateRef"
        @refreshDataList="getDataList"
    />



    <!-- 图片预览 -->
    <el-dialog v-model="previewVisible" title="预览图" width="50%">
        <img :src="previewImg" alt="" style="width: 100%;">
    </el-dialog>

    <!-- 聊天对话框（管理端） -->
    <el-dialog v-model="chatVisible" :title="`与 ${chatTitle} 私聊`" width="600px" @close="chatClose">
      <div ref="chatContentRef" class="chat-content" style="height: 420px; overflow: auto; padding: 4px 2px;border: 1px solid #ebeef5; border-radius: 4px; background: #f5f5f5;">
        <div class="p-2">
          <div v-for="item in chatList" :key="item.id" style="margin: 8px 0; display:flex;"
               :style="{ justifyContent: isMine(item) ? 'flex-end' : 'flex-start' }">
            <el-alert v-if="item.format===1" class="text-content" :title="item.content" :closable="false"
                      :type="isMine(item) ? 'success' : 'primary'" style="max-width: 70%; width: auto;" :effect="'dark'"/>
            <el-image v-else fit="cover" :src="item.content?.startsWith('http')? item.content : (baseUrl + item.content)"
                      style="width: 100px;height: 100px;"
                      :preview-src-list="[item.content?.startsWith('http')? item.content : (baseUrl + item.content)]" />
          </div>
        </div>
      </div>
      <template #footer>
        <div style="display:flex; align-items:center; width:100%;">
          <el-input v-model="chatForm.content" placeholder="请输入内容" @keydown.enter="addChat(null)"
                    style="flex:1; margin-right: 8px;" />
          <el-button :disabled="!chatForm.content" type="primary" @click="addChat(null)">发送</el-button>
          <el-upload style="margin-left:6px;" :action="uploadUrl" :on-success="uploadSuccess" :show-file-list="false">
            <el-button type="success">上传图片</el-button>
          </el-upload>
        </div>
      </template>
    </el-dialog>
  </div>

</template>

<script setup lang="ts">
  import {  ref, reactive, onMounted , nextTick, watch, computed } from 'vue'
  import { http } from '@/utils/request'
  import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
  import { isAuth } from '@/utils/auth'
  import type { CancelcourseenrollmentItem } from '@/types/common'
  import AddOrUpdate from './AddOrUpdate.vue'
  import { useAuthStore } from '@/stores/auth'
  type MusicTrack = {
    id: string | number
    name: string
    artist: string
    cover?: string
    url: string
    lyrics?: string
  }

  const baseUrl = import.meta.env.VITE_API_BASE_URL || ''

  const musicConfig = {
    enabled: false,
    nameField: '',
    artistField: '',
    coverField: '',
    urlField: '',
    lyricsField: ''
  }

  const musicPlayerRef = ref<any>(null)
  const currentMusic = ref<MusicTrack | null>(null)

  const resolveFileUrl = (path: string) => {
    if (!path) return ''
    if (path.startsWith('http')) return path
    if (!baseUrl) {
      return path.startsWith('/') ? path : `/`
    }
    return baseUrl + (path.startsWith('/') ? path : `/`)
  }

  const formatFileName = (path: string, index: number) => {
    if (!path) return `附件1`
    const segments = path.split('/')
    const raw = decodeURIComponent(segments[segments.length - 1] || '')
    if (!raw) return `附件1`
    const dot = raw.lastIndexOf('.')
    const extension = dot > 0 ? raw.slice(dot) : ''
    const basename = dot > 0 ? raw.slice(0, dot) : raw
    const limit = 12
    const truncated = basename.length > limit ? basename.slice(0, limit) + '…' : basename
    return truncated + extension
  }

  const extractFirstValue = (input: unknown): string => {
    if (input === null || input === undefined) return ''
    if (Array.isArray(input)) {
      for (const item of input) {
        const candidate = extractFirstValue(item)
        if (candidate) return candidate
      }
      return ''
    }
    const text = String(input).trim()
    if (!text) return ''
    if (text.includes(',')) {
      const parts = text.split(',').map(part => part.trim()).filter(Boolean)
      if (parts.length) return parts[0]
    }
    return text
  }

  const openFileLink = (path: string) => {
    const url = resolveFileUrl(path)
    if (!url) return
    const win = window.open(url, '_blank')
    if (win) {
      win.focus()
    }
  }
  const chatContentRef = ref<HTMLElement>()
  const uploadUrl = (baseUrl.endsWith('/') ? baseUrl : baseUrl + '/') + 'file/upload'
  const authStore = useAuthStore()
  const myid = computed(() => { const rawIdStr = String(authStore.userInfo?.id || localStorage.getItem('userid') || '0'); const role = authStore.userInfo?.role || localStorage.getItem('sessionTable') || ''; return role === 'coach' ? String(BigInt(rawIdStr) + 100000n) : rawIdStr })
  const chatVisible = ref(false)
  const chatTitle = ref('')
  const nowfid = ref<number | null>(null)
  const chatList = ref<any[]>([])
  const chatForm = reactive({ content: '' })
  const chatTargetTable = ref('')
  const chatBackendUserIdField = 'userid'
  const chatBackendUserLabelField = ''
  const chatBackendTargetTable = 'cancelcourseenrollment'
  let websock: WebSocket | null = null

  // 响应式数据
  const dataList = ref<CancelcourseenrollmentItem[]>([])
  const dataListLoading = ref(false)
  const dataListSelections = ref<CancelcourseenrollmentItem[]>([])
  const addOrUpdateVisible = ref(false)
  const addOrUpdateRef = ref()

  const musicTracks = computed(() => {
    if (!musicConfig.enabled || !musicConfig.urlField) return [] as MusicTrack[]
    return dataList.value
      .map(row => {
        const record = row as Record<string, any>
        const urlRaw = extractFirstValue(record?.[musicConfig.urlField])
        if (!urlRaw) return null
        const url = resolveFileUrl(urlRaw)
        if (!url) return null
        const nameRaw = musicConfig.nameField ? extractFirstValue(record?.[musicConfig.nameField]) : ''
        const artistRaw = musicConfig.artistField ? extractFirstValue(record?.[musicConfig.artistField]) : ''
        const coverRaw = musicConfig.coverField ? extractFirstValue(record?.[musicConfig.coverField]) : ''
        const lyricsRaw = musicConfig.lyricsField ? record?.[musicConfig.lyricsField] : ''
        return {
          id: record?.id ?? url,
          name: nameRaw || String(record?.name || '取消课程报名记录'),
          artist: artistRaw,
          cover: coverRaw ? resolveFileUrl(coverRaw) : '',
          url,
          lyrics: typeof lyricsRaw === 'string' ? lyricsRaw : (lyricsRaw ?? '').toString()
        } as MusicTrack
      })
      .filter((item): item is MusicTrack => item !== null)
  })

  watch(
    musicTracks,
    tracks => {
      if (!tracks.length) {
        currentMusic.value = null
        return
      }
      if (currentMusic.value) {
        const exists = tracks.find(item => item.id === currentMusic.value?.id)
        if (!exists) {
          currentMusic.value = null
        } else {
          currentMusic.value = exists
        }
      }
    },
    { immediate: true }
  )

  const playMusic = (row: CancelcourseenrollmentItem) => {
    if (!musicConfig.enabled) return
    const track = (() => {
      const record = row as Record<string, any>
      const urlRaw = extractFirstValue(record?.[musicConfig.urlField])
      if (!urlRaw) return null
      const url = resolveFileUrl(urlRaw)
      if (!url) return null
      const nameRaw = musicConfig.nameField ? extractFirstValue(record?.[musicConfig.nameField]) : ''
      const artistRaw = musicConfig.artistField ? extractFirstValue(record?.[musicConfig.artistField]) : ''
      const coverRaw = musicConfig.coverField ? extractFirstValue(record?.[musicConfig.coverField]) : ''
      const lyricsRaw = musicConfig.lyricsField ? record?.[musicConfig.lyricsField] : ''
      return {
        id: record?.id ?? url,
        name: nameRaw || String(record?.name || '取消课程报名记录'),
        artist: artistRaw,
        cover: coverRaw ? resolveFileUrl(coverRaw) : '',
        url,
        lyrics: typeof lyricsRaw === 'string' ? lyricsRaw : (lyricsRaw ?? '').toString()
      } as MusicTrack
    })()
    if (!track) {
      ElMessage.warning('未找到可播放的音频资源')
      return
    }
    currentMusic.value = track
  }

  const padNumber = (value: number) => String(value).padStart(2, '0')

  const exportFields: Record<string, string> = {
    
    '课程名称': 'coursename'
    ,
    '课程封面': 'courseimage'
    ,
    '课程分类': 'coursetype'
    ,
    '上课时间': 'classtime'
    ,
    '教练': 'coachname'
    ,
    '取消原因': 'cancelreason'
  }

  const exportRows = computed(() =>
    dataList.value.map((row) => {
      const plain: Record<string, unknown> = {
        ...row
      }
      if (plain.courseimage !== undefined && plain.courseimage !== null) {
        const images = String(plain.courseimage)
          .split(',')
          .map((item) => item.trim())
          .filter(Boolean)
          .map((item) => resolveFileUrl(item))
        plain.courseimage = images.join('; ')
      }
      return plain
    })
  )

  const exportFileName = computed(() => {
    const now = new Date()
    const stamp =
      now.getFullYear().toString() +
      padNumber(now.getMonth() + 1) +
      padNumber(now.getDate()) +
      padNumber(now.getHours()) +
      padNumber(now.getMinutes()) +
      padNumber(now.getSeconds())
    return '取消课程报名记录数据_' + stamp + '.xls'
  })

  const mapConfig = {
    enabled: false,
    title: '',
    nameField: '',
    descField: '',
    imageField: '',
    latField: '',
    lngField: ''
  }
  const viewMode = ref<'map' | 'table'>(mapConfig.enabled ? 'map' : 'table')
  const toCoordinateNumber = (value: unknown): number | null => {
    if (value === null || value === undefined) return null
    const str = String(value).trim()
    if (!str) return null
    const num = Number.parseFloat(str)
    if (Number.isFinite(num)) {
      return Number(num.toFixed(6))
    }
    return null
  }
  const mapMarkers = computed(() => {
    if (!mapConfig.enabled) {
      return []
    }
    return dataList.value
      .map((item, index) => {
        const row = item as Record<string, any>
        const lng = toCoordinateNumber(row?.[mapConfig.lngField])
        const lat = toCoordinateNumber(row?.[mapConfig.latField])
        if (lng === null || lat === null) {
          return null
        }
        const nameRaw = row?.[mapConfig.nameField]
        const descRaw = row?.[mapConfig.descField]
        const imageRaw = mapConfig.imageField ? row?.[mapConfig.imageField] : ''
        return {
          id: row?.id ?? `--`,
          name: nameRaw ? String(nameRaw) : '未命名地点',
          description: descRaw ? String(descRaw) : '',
          image: imageRaw ? String(imageRaw) : '',
          lng,
          lat
        }
      })
      .filter((item): item is {
        id: string | number
        name: string
        description: string
        image: string
        lng: number
        lat: number
      } => item !== null)
  })




  // 分页数据
  const pageIndex = ref(1)
  const pageSize = ref(10)
  const totalPage = ref(0)

  // 搜索表单
  const searchForm = reactive({
    coursename: undefined,
    coursetype: undefined,
  })

  // 图片预览
  const previewImg = ref('')
  const previewVisible = ref(false)

  // 获取数据列表
  const getDataList = () => {
    dataListLoading.value = true
    const params: any = {
      page: pageIndex.value,
      limit: pageSize.value,
      sort: 'id',
      order: 'desc'
    }
    if (searchForm.coursename) {
      params.coursename = '%' + searchForm.coursename + '%'
    }
    if (searchForm.coursetype) {
      params.coursetype = '%' + searchForm.coursetype + '%'
    }
    http.get('cancelcourseenrollment/page', { params }).then((response: any) => {
      if (response && response.code === 0) {
        dataList.value = response.data.list || []
        // 确保 total 传入分页组件为数字类型，避免字符串导致分页不显示
        totalPage.value = Number(response.data.total ?? 0)
      } else {
        dataList.value = []
        totalPage.value = 0
      }
      dataListLoading.value = false
    }).catch((error) => {
      console.error("获取数据失败:", error)
      dataList.value = []
      totalPage.value = 0
      dataListLoading.value = false
    })
  }

  // 搜索
  const search = () => {
    pageIndex.value = 1
    getDataList()
  }

  // 重置
  const reset = () => {
    searchForm.coursename = undefined
    searchForm.coursetype = undefined
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

  // 多选
  const selectionChangeHandler = (val: CancelcourseenrollmentItem[]) => {
    dataListSelections.value = val
  }

  // 新增 / 修改
  const addOrUpdateHandler = (id?: number, type?: string) => {
    addOrUpdateVisible.value = true
    nextTick(() => {
      addOrUpdateRef.value.init(id, type)
    })
  }

  // 删除
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
      await http.post('cancelcourseenrollment/delete', deleteIds)
      ElMessage.success('操作成功')
      getDataList()
    } catch {
      // 用户取消操作
    }
  }

  // 打开私聊
  const chatOpen = (row: any) => {
    chatTargetTable.value = localStorage.getItem('sessionTable') || chatBackendTargetTable || ''
    const userIdField = chatBackendUserIdField || 'id'
    const rawId = row?.[userIdField] ?? (userIdField === 'id' ? row?.id : undefined)
    let toId: number | null = null
    if (rawId !== undefined && rawId !== null) {
      const rawText = String(rawId).trim()
      if (rawText) {
        const parsed = Number(rawText)
        if (Number.isFinite(parsed)) {
          toId = Number(parsed)
        }
      }
    }
    if (!toId) {
      ElMessage.warning('未找到聊天对象')
      return
    }
    const labelField = chatBackendUserLabelField
    let title = ''
    if (labelField) {
      const rawTitle = row?.[labelField]
      if (rawTitle !== undefined && rawTitle !== null) {
        const text = String(rawTitle).trim()
        if (text) title = text
      }
    }
    if (!title) {
      title = `用户`
    }
    chatTitle.value = title
    nowfid.value = Number(toId)
    chatVisible.value = true
    initWebSocket(toId)
    getChatList()
  }

  const initWebSocket = (toId: number) => {
    try { websock?.close() } catch {}
    websock = null
    const wsBase = (baseUrl || '').replace(/^http/, 'ws')
    let url = wsBase.endsWith('/') ? wsBase : (wsBase + '/')
    url += `ws?user_id=${myid.value}&to_id=${toId}`
    websock = new WebSocket(url)
    websock.onmessage = () => getChatList()
    websock.onerror = () => {}
  }

  const chatClose = () => {
    try { websock?.close() } catch {}
    websock = null
    chatList.value = []
    nowfid.value = null
    chatForm.content = ''
    chatTargetTable.value = ''
  }

  const getChatList = async () => {
    if (!nowfid.value) return
    const params: any = { page: 1, limit: 100, sort: 'id', order: 'asc' }
    const payload = { uid: myid.value, fid: nowfid.value }
    const res: any = await http.get('chatmessage/mlist', { params: { ...params, ...payload } })
    if (res?.code === 0) {
      chatList.value = res.data?.list || []
      scrollToBottom()
    }
  }

  const uploadSuccess = (res: any) => {
    if (res.code === 0) {
      addChat(res.file)
    }
  }

  const addChat = async (ask: string | null) => {
    if (!nowfid.value) return

    // 检查是否给自己发消息
    if (String(nowfid.value) === String(myid.value)) {
      ElMessage.warning('不能给自己发消息')
      return
    }

    const content = ask || chatForm.content
    if (!content) return
    const payload: any = { uid: Number(myid.value), fid: nowfid.value, content, format: ask ? 2 : 1 }
    if (chatTargetTable.value) {
      payload.tablename = chatTargetTable.value
    }
    const rs: any = await http.post('chatmessage/add', payload)
    if (rs?.code === 0) {
      try { websock?.send(content) } catch {}
      chatForm.content = ''
      getChatList()
    }
  }
  // 是否本人消息
  const isMine = (item: any) => String(item?.uid) === String(myid.value)

  // 滚动到底部
  const scrollToBottom = () => {
    nextTick(() => {
      if (chatContentRef.value) {
        chatContentRef.value.scrollTop = chatContentRef.value.scrollHeight
      }
    })
  }



  // 图片预览
  const imgPreView = (url: string) => {
    previewImg.value = url
    previewVisible.value = true
  }

  // 通用内联跨表示例：不打开目标弹窗，直接更新当前行状态并调整其他表库存
  const inlineCrossActionHandler = async (
    row: any,
    tips: string,
    statusColumnName: string,
    statusColumnValue: string,
    stockTargetTable: string,
    stockMode: string,             // 'plus' | 'minus'
    stockField: string,            // 源表数量字段
    amountField?: string,          // 可选数量字段
    btnName?: string
  ) => {
    const successMessage = tips?.trim() || '操作成功'
    const confirmTitle = (btnName || '').trim() || '提示'
    const confirmMessage = successMessage.endsWith('？')
      ? successMessage
      : `，是否继续？`
    const statusKey = (statusColumnName || '').trim()
    const targetStatus = (statusColumnValue || '').trim()
    if (statusKey && targetStatus) {
      const currentStatus = String(row?.[statusKey] ?? '').trim()
      if (currentStatus === targetStatus) {
        ElMessage.warning('当前状态已为目标状态，无需重复操作')
        return
      }
    }
    const extraPayload: Record<string, any> = {}
    const needsLogistics = btnName === '发货'
    if (needsLogistics) {
      const defaultValue = String(row?.logistics || '').trim()
      try {
        const { value } = await ElMessageBox.prompt(
          '请输入物流公司、运单号等信息（必填）',
          '发货处理',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputType: 'textarea',
            inputPlaceholder: '例如：顺丰速运 SF1234567890',
            inputValue: defaultValue,
            autofocus: false
          }
        )
        const logisticsText = String(value ?? '').trim()
        if (!logisticsText) {
          ElMessage.warning('请填写物流信息')
          return
        }
        extraPayload.logistics = logisticsText
      } catch {
        return
      }
    } else {
      try {
        await ElMessageBox.confirm(confirmMessage, confirmTitle)
      } catch {
        return
      }
    }
    try {
      // 1) 可选：更新当前行状态
      if (statusColumnName && statusColumnValue) {
        const curr: any = { ...row, ...extraPayload }
        curr[statusColumnName] = statusColumnValue
        await http.post(`cancelcourseenrollment/update`, curr)
      } else if (Object.keys(extraPayload).length) {
        const curr: any = { ...row, ...extraPayload }
        await http.post(`cancelcourseenrollment/update`, curr)
      }

      // 2) 库存调整：必须提供目标表、crossrefid 和数量
      if (!stockTargetTable) {
        // 无需库存操作
        ElMessage.success(successMessage)
        getDataList()
        return
      }
      // 兼容：记录表（使用 crossrefid）或当前表自更新（使用 id）
      const refId = row?.crossrefid || (stockTargetTable === name ? row?.id : undefined)
      if (!refId) {
        ElMessage.error('缺少 crossrefid，无法定位源记录')
        return
      }

      const amountKey = (amountField || stockField || '').trim()
      let amount: number
      if (!amountKey) {
        amount = 1
      } else {
        amount = Number(row?.[amountKey] ?? 0)
        if (!isFinite(amount) || amount <= 0) {
          ElMessage.error('数量必须为大于0的数字')
          return
        }
      }

      const fieldKey = (stockField || amountKey).trim()
      // 查询源记录
      const { data: info } = await http.get(`${stockTargetTable}/info/${refId}`)
      if (!info) {
        ElMessage.error('未找到源记录')
        return
      }
      const current = Number(info?.[fieldKey] ?? 0)
      if (!isFinite(current)) {
        ElMessage.error('源表数量无效')
        return
      }
      const next = stockMode === 'minus' ? current - amount : current + amount
      if (next < 0) {
        ElMessage.error('操作后数量小于0，已阻止')
        return
      }
      const payload = { ...info, [fieldKey]: next }
      await http.post(`${stockTargetTable}/update`, payload)

      ElMessage.success(successMessage)
      getDataList()
    } catch (e) {
      console.error('内联跨表操作失败:', e)
      ElMessage.error('操作失败')
    }
  }

  // 课程名称 选项数据
  const coursenameOptions = ref<string[]>([])
  // 加载课程名称选项（支持条件联动）
  const getCoursenameOptions = async () => {
    try {
      const params: any = {}
      const response = await http.get('option/course/coursename', { params })
      if (response && response.code === 0) {
        coursenameOptions.value = response.data || []
      } else {
        coursenameOptions.value = []
      }
    } catch (error) {
      console.error('获取课程名称选项失败:', error)
      coursenameOptions.value = []
    }
  }

  // 课程分类 选项数据
  const coursetypeOptions = ref<string[]>([])
  // 加载课程分类选项（支持条件联动）
  const getCoursetypeOptions = async () => {
    try {
      const params: any = {}
      const response = await http.get('option/coursetype/coursetypename', { params })
      if (response && response.code === 0) {
        coursetypeOptions.value = response.data || []
      } else {
        coursetypeOptions.value = []
      }
    } catch (error) {
      console.error('获取课程分类选项失败:', error)
      coursetypeOptions.value = []
    }
  }

  // 组件挂载时获取数据
  onMounted(() => {
    getCoursenameOptions()
    getCoursetypeOptions()
    getDataList()
  })
</script>
