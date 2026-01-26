<template>
  <div class="main-content bg-gray-50 p-6">
    <!-- 搜索表单区域 -->
    <div class="bg-white p-4 rounded-lg shadow mb-4">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">套餐名称</label>
            <el-input
            v-model="searchForm.packagename"
            placeholder="套餐名称"
            clearable
            @keydown.enter="search()"
            />
        </div>
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">套餐类型</label>
            <div>
                <el-select v-model="searchForm.packagetype" placeholder="请选择套餐类型" clearable class="w-40">
                    <el-option label="月卡" value="月卡"/>
                    <el-option label="季卡" value="季卡"/>
                    <el-option label="年卡" value="年卡"/>
                </el-select>
            </div>
        </div>
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">最小价格</label>
            <el-input
            v-model="searchForm.packageprice_start"
            placeholder="最小价格"
            clearable
            @keydown.enter="search()"
            />
        </div>
        <div class="inline-block mr-4">
            <label class="inline-block mr-2 leading-10">最大价格</label>
            <el-input
            v-model="searchForm.packageprice_end"
            placeholder="最大价格"
            clearable
            @keydown.enter="search()"
            />
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
            v-if="!false && isAuth('membershippackage', '新增')"
            type="success"
            @click="addOrUpdateHandler()"
          >
            <el-icon><Plus /></el-icon>
            新增
          </el-button>
          <el-button
            v-if="!false && isAuth('membershippackage', '删除')"
            type="danger"
            :disabled="!dataListSelections.length"
            @click="deleteHandler()"
          >
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
          <download-excel
            v-if="isAuth('membershippackage', '导出')"
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
        v-if="isAuth('membershippackage', '查看')"
        class="w-full"
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandler"
        stripe
        border
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column prop="packagename" label="套餐名称" />
        <el-table-column prop="packageimage" label="套餐图片" width="200">
        <template #default="scope">
            <div v-if="scope.row.packageimage">
            <img
                v-if="scope.row.packageimage.substring(0,4)=='http'"
                :src="scope.row.packageimage.split(',')[0]"
                width="100"
                height="100"
                style="object-fit: cover"
                @click="imgPreView(scope.row.packageimage.split(',')[0])"
                class="cursor-pointer"
            >
            <img
                v-else
                :src="baseUrl + scope.row.packageimage.split(',')[0]"
                width="100"
                height="100"
                style="object-fit: cover"
                @click="imgPreView(baseUrl + scope.row.packageimage.split(',')[0])"
                class="cursor-pointer"
            >
            </div>
            <div v-else class="text-gray-400">无图片</div>
        </template>
        </el-table-column>
        <el-table-column prop="packagetype" label="套餐类型" />
        <el-table-column prop="packageprice" label="价格" />
        <el-table-column prop="validdays" label="有效期" />
        <el-table-column prop="includedcourses" label="包含课时" />
        <!-- 操作列 -->
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="scope">
            <el-button
                v-if="isAuth('membershippackage', '查看')"
                type="primary"
                size="small"
                @click="addOrUpdateHandler(scope.row.id, 'info')"
            >
              查看
            </el-button>
            <el-button
                v-if="isAuth('membershippackage', '修改')"
                type="warning"
                size="small"
                @click="addOrUpdateHandler(scope.row.id)"
            >
              修改
            </el-button>
            <el-button
                v-if="isAuth('membershippackage', '删除')"
                type="danger"
                size="small"
                @click="deleteHandler([scope.row.id])"
            >
              删除
            </el-button>
        <el-button
                v-if="isAuth('membershippackage', '办卡')"
                type="success"
                size="small"
                @click="cardapplicationCrossAddOrUpdateHandler(scope.row, 'cross', '办卡成功', 'orderstatus', '未支付', '办卡')"
            >
            办卡
            </el-button>
        <el-button
                v-if="isAuth('membershippackage', '续卡')"
                type="success"
                size="small"
                @click="cardrenewalCrossAddOrUpdateHandler(scope.row, 'cross', '续卡成功', 'orderstatus', '未支付', '续卡')"
            >
            续卡
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


    <!-- cardapplication 跨表组件 -->
    <cardapplication-cross-add-or-update
    v-if="cardapplicationCrossAddOrUpdateFlag"
    ref="cardapplicationCrossAddOrUpdateRef"
    @refreshDataList="getDataList"
    ></cardapplication-cross-add-or-update>
    <!-- cardrenewal 跨表组件 -->
    <cardrenewal-cross-add-or-update
    v-if="cardrenewalCrossAddOrUpdateFlag"
    ref="cardrenewalCrossAddOrUpdateRef"
    @refreshDataList="getDataList"
    ></cardrenewal-cross-add-or-update>

    <!-- 图片预览 -->
    <el-dialog v-model="previewVisible" title="预览图" width="50%">
        <img :src="previewImg" alt="" style="width: 100%;">
    </el-dialog>

  </div>

</template>

<script setup lang="ts">
  import {  ref, reactive, onMounted , nextTick, watch, computed } from 'vue'
  import { http } from '@/utils/request'
  import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
  import { isAuth } from '@/utils/auth'
  import type { MembershippackageItem } from '@/types/common'
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
  import cardapplicationCrossAddOrUpdate from '../cardapplication/AddOrUpdate.vue'
  import cardrenewalCrossAddOrUpdate from '../cardrenewal/AddOrUpdate.vue'

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

  // 响应式数据
  const dataList = ref<MembershippackageItem[]>([])
  const dataListLoading = ref(false)
  const dataListSelections = ref<MembershippackageItem[]>([])
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
          name: nameRaw || String(record?.name || '会员卡套餐'),
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

  const playMusic = (row: MembershippackageItem) => {
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
        name: nameRaw || String(record?.name || '会员卡套餐'),
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
    
    '套餐名称': 'packagename'
    ,
    '套餐图片': 'packageimage'
    ,
    '套餐类型': 'packagetype'
    ,
    '价格': 'packageprice'
    ,
    '有效期': 'validdays'
    ,
    '包含课时': 'includedcourses'
  }

  const exportRows = computed(() =>
    dataList.value.map((row) => {
      const plain: Record<string, unknown> = {
        ...row
      }
      if (plain.packageimage !== undefined && plain.packageimage !== null) {
        const images = String(plain.packageimage)
          .split(',')
          .map((item) => item.trim())
          .filter(Boolean)
          .map((item) => resolveFileUrl(item))
        plain.packageimage = images.join('; ')
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
    return '会员卡套餐数据_' + stamp + '.xls'
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



  // 跨表组件显示标志（去重）
  const cardapplicationCrossAddOrUpdateFlag = ref(false)
  const cardapplicationCrossAddOrUpdateRef = ref()
  const cardrenewalCrossAddOrUpdateFlag = ref(false)
  const cardrenewalCrossAddOrUpdateRef = ref()

  // 分页数据
  const pageIndex = ref(1)
  const pageSize = ref(10)
  const totalPage = ref(0)

  // 搜索表单
  const searchForm = reactive({
    packagename: undefined,
    packagetype: undefined,
    packageprice_start: undefined,
    packageprice_end: undefined,
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
    if (searchForm.packagename) {
      params.packagename = '%' + searchForm.packagename + '%'
    }
    if (searchForm.packagetype) {
      params.packagetype = '%' + searchForm.packagetype + '%'
    }
    if (searchForm.packageprice_start) {
      params.packageprice_start = searchForm.packageprice_start
    }
    if (searchForm.packageprice_end) {
      params.packageprice_end = searchForm.packageprice_end
    }
    http.get('membershippackage/page', { params }).then((response: any) => {
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
    searchForm.packagename = undefined
    searchForm.packagetype = undefined
    searchForm.packageprice_start = undefined
    searchForm.packageprice_end = undefined
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
  const selectionChangeHandler = (val: MembershippackageItem[]) => {
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
      await http.post('membershippackage/delete', deleteIds)
      ElMessage.success('操作成功')
      getDataList()
    } catch {
      // 用户取消操作
    }
  }




  // 图片预览
  const imgPreView = (url: string) => {
    previewImg.value = url
    previewVisible.value = true
  }
  
  // 办卡
  const cardapplicationCrossAddOrUpdateHandler = (row: any, type: string, tips: string, statusColumnName: string, statusColumnValue: string, buttonName: string) => {
    // 显示办卡弹窗
    cardapplicationCrossAddOrUpdateFlag.value = true

    localStorage.setItem('crossObj', JSON.stringify(row))
    localStorage.setItem('crossTable', 'membershippackage')
    localStorage.setItem('statusColumnName', statusColumnName)
    localStorage.setItem('statusColumnValue', statusColumnValue)
    localStorage.setItem('tips', tips)
    try {
      ;['stockUpdate.targetTable','stockUpdate.mode','stockUpdate.field','crossrefid'].forEach(k=>localStorage.removeItem(k))
    } catch (e) {}
    // 不能再次进行办卡的检查
    if (statusColumnName && !statusColumnName.startsWith('[')) {
      const crossObj = JSON.parse(localStorage.getItem('crossObj') || '{}')
      if (crossObj[statusColumnName] === statusColumnValue) {
        ElMessage({
          message: tips || '状态无需更新',
          type: 'warning',
          duration: 1500,
          onClose: () => {
            getDataList()
          }
        })
        cardapplicationCrossAddOrUpdateFlag.value = false
        return
      }
    }
    // 调用跨表组件
    nextTick(() => {
      cardapplicationCrossAddOrUpdateRef.value?.init(undefined, type)
    })
  }
  
  // 续卡
  const cardrenewalCrossAddOrUpdateHandler = (row: any, type: string, tips: string, statusColumnName: string, statusColumnValue: string, buttonName: string) => {
    // 显示续卡弹窗
    cardrenewalCrossAddOrUpdateFlag.value = true

    localStorage.setItem('crossObj', JSON.stringify(row))
    localStorage.setItem('crossTable', 'membershippackage')
    localStorage.setItem('statusColumnName', statusColumnName)
    localStorage.setItem('statusColumnValue', statusColumnValue)
    localStorage.setItem('tips', tips)
    try {
      ;['stockUpdate.targetTable','stockUpdate.mode','stockUpdate.field','crossrefid'].forEach(k=>localStorage.removeItem(k))
    } catch (e) {}
    // 不能再次进行续卡的检查
    if (statusColumnName && !statusColumnName.startsWith('[')) {
      const crossObj = JSON.parse(localStorage.getItem('crossObj') || '{}')
      if (crossObj[statusColumnName] === statusColumnValue) {
        ElMessage({
          message: tips || '状态无需更新',
          type: 'warning',
          duration: 1500,
          onClose: () => {
            getDataList()
          }
        })
        cardrenewalCrossAddOrUpdateFlag.value = false
        return
      }
    }
    // 调用跨表组件
    nextTick(() => {
      cardrenewalCrossAddOrUpdateRef.value?.init(undefined, type)
    })
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
        await http.post(`membershippackage/update`, curr)
      } else if (Object.keys(extraPayload).length) {
        const curr: any = { ...row, ...extraPayload }
        await http.post(`membershippackage/update`, curr)
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

  // 组件挂载时获取数据
  onMounted(() => {
    getDataList()
  })
</script>
