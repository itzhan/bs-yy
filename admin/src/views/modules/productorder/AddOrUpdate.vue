<template>
  <div class="add-edit-dialog">
    <el-dialog
        v-model="visible"
        :title="id ? (type === 'info' ? '查看商品订单' : '修改商品订单') : '新增商品订单'"
        width="60%"
        :close-on-click-modal="false"
        @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="space-y-4"
      >
        <el-form-item v-if="type === 'info' || true || read.productname" label="商品名称" prop="productname">
            <el-select v-model="form.productname" placeholder="请选择商品名称" :readonly="type === 'info' || read.productname">
                <el-option
                    v-for="(item, index) in productnameOptions"
                    :key="index"
                    :label="item"
                    :value="item"
                />
            </el-select>
        </el-form-item>
        <el-form-item v-if="type === 'info' || true || read.productimage" label="商品图片" prop="productimage">
            <UploadField
                v-model="form.productimage"
                upload-type="image"
                :readonly="type === 'info' || read.productimage"
            />
        </el-form-item>
        <el-form-item v-if="type === 'info' || true || read.producttype" label="商品分类" prop="producttype">
            <el-select v-model="form.producttype" placeholder="请选择商品分类" :readonly="type === 'info' || read.producttype">
                <el-option
                    v-for="(item, index) in producttypeOptions"
                    :key="index"
                    :label="item"
                    :value="item"
                />
            </el-select>
        </el-form-item>
        <el-form-item v-if="type === 'info' || true || read.productprice" label="商品单价" prop="productprice">
            <el-input-number v-model="form.productprice" placeholder="请输入商品单价" controls-position="right" :readonly="type === 'info' || read.productprice"/>
        </el-form-item>
        <el-form-item v-if="type === 'info' || true || read.quantity" label="购买数量" prop="quantity">
            <el-input-number v-model="form.quantity" placeholder="请输入购买数量" controls-position="right" :readonly="type === 'info' || read.quantity"/>
        </el-form-item>
        <el-form-item v-if="type === 'info' || true || read.totalprice" label="订单总价" prop="totalprice">
            <el-input-number v-model="form.totalprice" placeholder="请输入订单总价" controls-position="right" :readonly="type === 'info' || read.totalprice"/>
        </el-form-item>
        <el-form-item v-if="type === 'info' || false || read.orderstatus" label="状态" prop="orderstatus">
            <el-select v-model="form.orderstatus" placeholder="请选择状态" :readonly="type === 'info' || read.orderstatus">
                <el-option label="未支付" value="未支付"/>
                <el-option label="已支付" value="已支付"/>
                <el-option label="已取消" value="已取消"/>
                <el-option label="已退款" value="已退款"/>
                <el-option label="已发货" value="已发货"/>
                <el-option label="已完成" value="已完成"/>
            </el-select>
        </el-form-item>
        <el-form-item v-if="type === 'info' || false || read.logistics" label="物流信息" prop="logistics">
            <el-input
                v-model="form.logistics"
                type="textarea"
                placeholder="请输入物流信息"
                :readonly="type === 'info' || read.logistics"
                clearable
            />
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="space-x-2">
          <el-button @click="handleClose">取消</el-button>
          <el-button
            v-if="type !== 'info'"
            type="primary"
            :loading="loading"
            @click="handleSave"
          >
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, watch, computed } from 'vue'
  import { ElMessage, type FormInstance } from 'element-plus'
  import { InfoFilled } from '@element-plus/icons-vue'
  import { useAuthStore } from '@/stores/auth'
  import { http } from '@/utils/request'
  import { recognizeImage } from '@/utils/imageRecognition'
  import { validateNumber, validateMobile, validateEmail } from '@/utils/validate'

  const emit = defineEmits(['refreshDataList'])
  const authStore = useAuthStore()
  const visible = ref(false)
  const loading = ref(false)
  const id = ref<number | undefined>()
  const type = ref<string>('')
  const formRef = ref<FormInstance>()
  const baseUrl = import.meta.env.VITE_API_BASE_URL || ''

  // 商品名称选项
  const productnameOptions = ref<string[]>([])
  // 商品分类选项
  const producttypeOptions = ref<string[]>([])

  // 表单数据
  const form = reactive({
    productname: '',
    productimage: '',
    producttype: '',
    productprice: undefined,
    quantity: undefined,
    totalprice: undefined,
    orderstatus: '未支付',
    logistics: '',
  })
  const imageRecognitionConfig = {
    enabled: false,
    sourceField: '',
    targetField: '',
    targetLabel: ''
  }
  const imageRecognitionState = reactive({
    ready: false,
    loading: false,
    lastImage: '',
    requestId: 0
  })
  const normalizeImageValue = (value: unknown): string => {
    if (value === null || value === undefined) return ''
    if (Array.isArray(value)) {
      const filtered = value
        .map((item) => String(item ?? '').trim())
        .filter((item) => item.length > 0)
      return filtered.length ? filtered[filtered.length - 1] : ''
    }
    const text = String(value).trim()
    if (!text) return ''
    if (text.includes(',')) {
      const parts = text.split(',').map((item) => item.trim()).filter((item) => item.length > 0)
      if (parts.length) {
        return parts[parts.length - 1]
      }
    }
    return text
  }

  const pointRecordConfig = {
    enabled: false,
    mode: "",
    recordPointsField: "",
    recordUserField: "",
    userTable: "user"
  }

  const pointAdjustmentPlan = ref<{ userId: number; payload: Record<string, any> } | null>(null)

  // 是否只读
  const read = reactive({
    productname: false,
    productimage: false,
    producttype: false,
    productprice: false,
    quantity: false,
    totalprice: false,
    orderstatus: false,
    logistics: false,
  })
  const isCoupon = false
  const merchantMappingConfig = {
    table: '',
    accountField: '',
    nameField: '',
    idField: ''
  }
  const hasMerchantAccountField = Object.prototype.hasOwnProperty.call(form, 'merchantaccount')
  const hasMerchantNameField = Object.prototype.hasOwnProperty.call(form, 'merchantname')
  const hasMerchantIdField = Object.prototype.hasOwnProperty.call(form, 'merchantid')
  const hasMerchantMapping = Boolean(merchantMappingConfig.table && merchantMappingConfig.accountField)
  const adminRoleNames = ['admin']
  const isAdminRole = computed(() => {
    const role = (authStore.currentRole || localStorage.getItem('sessionTable') || '').toString().toLowerCase()
    return adminRoleNames.includes(role)
  })
  const merchantReadOnly = computed(() => isCoupon && hasMerchantMapping && !isAdminRole.value)
  const showDiscountField = computed(() => isCoupon && String(form.coupontype || '').trim() === '折扣券')
  const showFullReductionFields = computed(() => isCoupon && String(form.coupontype || '').trim() !== '折扣券')

  // 表单验证规则
  const rules: Record<string, any[]> = {
    productname: [ { trigger: 'blur' , message: '商品名称不能为空' , required: true } ],
    productprice: [ { trigger: 'blur' , message: '商品单价不能为空' , required: true }, { validator: validateNumber , trigger: 'blur' } ],
    quantity: [ { trigger: 'blur' , message: '购买数量不能为空' , required: true }, { validator: validateNumber , trigger: 'blur' } ],
    totalprice: [ { trigger: 'blur' , message: '订单总价不能为空' , required: true }, { validator: validateNumber , trigger: 'blur' } ],
  }

  watch(merchantReadOnly, (val) => {
    if (!isCoupon) return
    if (hasMerchantAccountField) read.merchantaccount = val
    if (hasMerchantNameField) read.merchantname = val
    if (hasMerchantIdField) read.merchantid = val
  }, { immediate: true })

  if (isCoupon) {
    watch(() => form.coupontype, (val) => {
      const typeText = String(val || '').trim()
      if (typeText === '折扣券') {
        if (form.coupondiscountrate === undefined || form.coupondiscountrate === null) {
          form.coupondiscountrate = 0.9 as any
        }
      } else {
        form.coupondiscountrate = undefined as any
      }
    }, { immediate: true })
  }

  const prefillMerchantInfo = async (force = false) => {
    if (!isCoupon || !hasMerchantMapping) return
    if (!force && id.value) return
    let profile: Record<string, any> | null = null
    if (merchantMappingConfig.table) {
      try {
        const res: any = await http.get(`${merchantMappingConfig.table}/session`)
        if (res && res.data) {
          profile = res.data
        }
      } catch (error) {
        profile = null
      }
    }
    if (!profile && typeof authStore.getUserProfile === 'function') {
      try {
        const result = await authStore.getUserProfile()
        if (result && result.success && result.data) {
          profile = result.data
        }
      } catch (error) {
        profile = null
      }
    }
    if (!profile) return
    if (hasMerchantIdField && merchantMappingConfig.idField) {
      const val = profile[merchantMappingConfig.idField]
      if (val !== undefined && val !== null && String(val).trim() !== '') {
        const parsed = Number(val)
        if (!Number.isNaN(parsed)) {
          form.merchantid = parsed as any
        }
      }
    }
    if (hasMerchantAccountField) {
      const account = profile[merchantMappingConfig.accountField] ?? profile.merchantaccount ?? profile.username
      if (account !== undefined && account !== null && String(account).trim() !== '') {
        form.merchantaccount = String(account)
      }
    }
    if (hasMerchantNameField && merchantMappingConfig.nameField) {
      const nameVal = profile[merchantMappingConfig.nameField]
      if (nameVal !== undefined && nameVal !== null && String(nameVal).trim() !== '') {
        form.merchantname = String(nameVal)
      }
    } else if (hasMerchantNameField) {
      const fallbackName = profile.merchantname || profile.shopname || profile.name
      if (fallbackName !== undefined && fallbackName !== null && String(fallbackName).trim() !== '') {
        form.merchantname = String(fallbackName)
      }
    }
  }

  const handleImageRecognition = async (imagePath: string) => {
    if (!imageRecognitionConfig.enabled || !imageRecognitionConfig.targetField) {
      return
    }
    if (!(imageRecognitionConfig.targetField in form)) {
      return
    }
    const trimmed = imagePath.trim()
    if (!trimmed) {
      return
    }
    const requestId = ++imageRecognitionState.requestId
    imageRecognitionState.loading = true
    imageRecognitionState.lastImage = trimmed
    try {
      const result = await recognizeImage(trimmed)
      if (requestId !== imageRecognitionState.requestId) {
        return
      }
      const keyword = (result?.keyword || '').trim()
      if (keyword) {
        ;(form as any)[imageRecognitionConfig.targetField] = keyword as any
        const label = imageRecognitionConfig.targetLabel || imageRecognitionConfig.targetField
        const suffix = keyword ? '：' + keyword : ''
        ElMessage.success('已自动填充' + label + suffix)
      } else {
        ElMessage.warning('未识别到有效结果，请手动填写')
        imageRecognitionState.lastImage = ''
      }
    } catch (error) {
      console.warn('图片识别失败', error)
      if (requestId === imageRecognitionState.requestId) {
        ElMessage.error('图片识别失败，请手动填写')
        imageRecognitionState.lastImage = ''
      }
    } finally {
      if (requestId === imageRecognitionState.requestId) {
        imageRecognitionState.loading = false
      }
    }
  }

  if (imageRecognitionConfig.enabled && imageRecognitionConfig.sourceField) {
    watch(() => (form as any)[imageRecognitionConfig.sourceField], (val) => {
      if (!imageRecognitionState.ready || type.value === 'info') {
        return
      }
      const latest = normalizeImageValue(val as any)
      if (!latest) {
        imageRecognitionState.lastImage = ''
        return
      }
      if (latest === imageRecognitionState.lastImage) {
        return
      }
      handleImageRecognition(latest)
    })
  }


  // 初始化
  const init = async (itemId?: number, itemType?: string) => {
    visible.value = true
    id.value = itemId
    type.value = itemType || ''
    if (imageRecognitionConfig.enabled) {
      imageRecognitionState.ready = false
      imageRecognitionState.lastImage = ''
      imageRecognitionState.requestId = 0
    }
    
    // 重置表单
    Object.assign(form, {
      productname: '',
      productimage: '',
      producttype: '',
      productprice: undefined,
      quantity: undefined,
      totalprice: undefined,
      orderstatus: '未支付',
      logistics: '',
    })
    
    // 初始化商品名称下拉选项
    getProductnameOptions()
    
    // 初始化商品分类下拉选项
    getProducttypeOptions()
    
    // 表单回显
    if (itemId) {
      await getInfo(itemId)
    }

    if (isCoupon && !itemId && !isAdminRole.value) {
      await prefillMerchantInfo(true)
    }
    if (isCoupon && !itemId) {
      if (form.couponfullamount === undefined || form.couponfullamount === null) {
        form.couponfullamount = 0 as any
      }
      if (form.coupondeductamount === undefined || form.coupondeductamount === null) {
        form.coupondeductamount = 0 as any
      }
    }

    if (imageRecognitionConfig.enabled) {
      imageRecognitionState.ready = true
    }

    if (type.value === 'cross') {
      const crossObj = JSON.parse(localStorage.getItem('crossObj') || '{}')
      let amountFieldName = ''
      try {
        const af = (localStorage.getItem('stockUpdate.field') || '').trim()
        const f = (localStorage.getItem('stockUpdate.field') || '').trim()
        amountFieldName = (af || f)
      } catch (e) {}
      // 重置只读标记
      for (const key in read) {
        ;(read as any)[key] = false
      }
      for (const key in crossObj) {
        if (key === 'productname') {
            if (!amountFieldName || key !== amountFieldName) {
              form.productname=crossObj[key]
              read.productname=true
            }
            continue
        }
        if (key === 'productimage') {
            if (!amountFieldName || key !== amountFieldName) {
              form.productimage=crossObj[key]
              read.productimage=true
            }
            continue
        }
        if (key === 'producttype') {
            if (!amountFieldName || key !== amountFieldName) {
              form.producttype=crossObj[key]
              read.producttype=true
            }
            continue
        }
        if (key === 'productprice') {
            if (!amountFieldName || key !== amountFieldName) {
              form.productprice=crossObj[key]
              read.productprice=true
            }
            continue
        }
        if (key === 'quantity') {
            if (!amountFieldName || key !== amountFieldName) {
              form.quantity=crossObj[key]
              read.quantity=true
            }
            continue
        }
        if (key === 'totalprice') {
            if (!amountFieldName || key !== amountFieldName) {
              form.totalprice=crossObj[key]
              read.totalprice=true
            }
            continue
        }
        if (key === 'orderstatus') {
            if (!amountFieldName || key !== amountFieldName) {
              form.orderstatus=crossObj[key]
              read.orderstatus=true
            }
            continue
        }
        if (key === 'logistics') {
            if (!amountFieldName || key !== amountFieldName) {
              form.logistics=crossObj[key]
              read.logistics=true
            }
            continue
        }
      }

      if (pointRecordConfig.enabled && Object.prototype.hasOwnProperty.call(form, pointRecordConfig.recordUserField)) {
        const fieldName = pointRecordConfig.recordUserField
        const currentValue = (form as any)[fieldName]
        const needsFill = currentValue === undefined || currentValue === null || String(currentValue).trim() === ''
        if (needsFill) {
          const fallbackUser = (crossObj && (crossObj.userid ?? crossObj.userId))
            ?? localStorage.getItem('userid')
            ?? (authStore as any)?.userInfo?.id
          if (fallbackUser !== undefined && fallbackUser !== null && fallbackUser !== '') {
            const parsed = Number(fallbackUser as any)
            if (!Number.isNaN(parsed)) {
              (form as any)[fieldName] = parsed
              if (Object.prototype.hasOwnProperty.call(read, fieldName)) {
                ;(read as any)[fieldName] = true
              }
            }
          }
        }
      }

      // 设置跨表关联字段
      const currentUserId = (authStore as any)?.userInfo?.id
        || localStorage.getItem('frontUserid')
        || localStorage.getItem('userid')
        || crossObj.userid
      if (currentUserId !== undefined && currentUserId !== null) {
        (form as any).crossuserid = Number(currentUserId as any)
      }
      if (crossObj && crossObj.id !== undefined && crossObj.id !== null) {
        (form as any).crossrefid = Number(crossObj.id as any)
      }
      // 若配置了数量字段，默认本次数量为1（可按需修改）
      if (amountFieldName && (form as any)[amountFieldName] === undefined) {
        (form as any)[amountFieldName] = 1 as any
      }
    }
    // 自动计算字段默认只读
    read.totalprice = true
    // 级联监听：父变更 -> 清空自己并重载
    // 自动计算：日期差与表达式
    watch(() => [
        (form as any).productprice,
        (form as any).quantity
    ], () => {
        try {
            const val = (() => { return form.productprice * form.quantity; })()
            const num = Number(val);
            (form as any).totalprice = isFinite(num) ? Number(num.toFixed(2)) : 0
        } catch (e) {
            (form as any).totalprice = 0
        }
      }, { immediate: true })
  }

  // 获取详情
  const getInfo = async (itemId: number) => {
    try {
      const { data } = await http.get(`productorder/info/${itemId}`)
      if (data){
        Object.assign(form, data);
      }
    } catch (error) {
      console.error('获取详情失败:', error)
    }
  }

  // 保存
  const handleSave = async () => {
    if (!formRef.value) return

    try {
      const valid = await formRef.value.validate()
      if (!valid) return

      loading.value = true


      if (type.value === 'cross') {
        const updateSuccess = await handleCrossStatusUpdate()
        if (!updateSuccess) {
          loading.value = false
          return
        }
        // 库存/数量变更校验
        const stockOk = await handleCrossStockBeforeSave()
        if (!stockOk) {
          loading.value = false
          return
        }
      }

      const pointReady = await ensurePointRequirementBeforeSave()

      const url = id.value ? 'productorder/update' : 'productorder/save'
      const res: any = await http.post(url, form)

      if (pointReady) {
          await applyPointAdjustmentAfterSave()
      }

      visible.value = false
      // 保存成功后再回写数量（与状态更新错开，保证记录成功后再扣减）
      if (type.value === 'cross') {
        try {
          await applyCrossStockUpdateAfterSave()
        } catch (e) {
          console.error('回写源表库存失败:', e)
        }
      }
      if (false && typeof updateSeatSoldSeatsAfterSave === 'function') {
        await updateSeatSoldSeatsAfterSave()
      }
      emit('refreshDataList')

      // 跨表操作完成后清理跨表缓存
      if (type.value === 'cross') {
        try {
          ['crossObj','crossTable','statusColumnName','statusColumnValue','tips','crossCleanType','stockUpdate.targetTable','stockUpdate.mode','stockUpdate.field','stockUpdatePlan']
                  .forEach(k => localStorage.removeItem(k))
        } catch (e) {}
      }
    } catch (error) {
      console.error('保存失败:', error)
      ElMessage.error('保存失败')
    } finally {
      pointAdjustmentPlan.value = null
      loading.value = false
    }
  }

  // 关闭对话框
  const handleClose = () => {
    visible.value = false
  }
  
  // 获取商品名称选项（支持联动条件）
  const getProductnameOptions = async () => {
    try {
      const params: any = {}
      const response = await http.get('option/product/productname', { params })
      if (response && response.code === 0) {
        productnameOptions.value = response.data || []
      } else {
        productnameOptions.value = []
      }
    } catch (error) {
      console.error('获取商品名称选项失败:', error)
      productnameOptions.value = []
    }
  }
  
  // 获取商品分类选项（支持联动条件）
  const getProducttypeOptions = async () => {
    try {
      const params: any = {}
      const response = await http.get('option/producttype/producttypename', { params })
      if (response && response.code === 0) {
        producttypeOptions.value = response.data || []
      } else {
        producttypeOptions.value = []
      }
    } catch (error) {
      console.error('获取商品分类选项失败:', error)
      producttypeOptions.value = []
    }
  }
  const handleCrossStatusUpdate = async () => {
    const statusColumnName = localStorage.getItem('statusColumnName')
    const statusColumnValue = localStorage.getItem('statusColumnValue')
    const crossTable = localStorage.getItem('crossTable')
    const tips = localStorage.getItem('tips')
    if (statusColumnName && statusColumnValue) {
      try {
        const crossObj = localStorage.getItem('crossObj')
        if (crossObj) {
          const crossData = JSON.parse(crossObj)
          if (statusColumnName && !statusColumnName.startsWith('[')) {
            for (const key in crossData) {
              if (key === statusColumnName) {
                // 检查是否已经是目标状态
                if (crossData[key] === statusColumnValue) {
                  ElMessage.warning(tips || '状态无需更新')
                  return false
                }
                crossData[key] = statusColumnValue
              }
            }
            await http.post(`${crossTable}/update`, crossData)
            console.log('源表状态更新成功')
          } else {
            // 数量限制模式
            const rawUserId = (authStore as any)?.user?.id || localStorage.getItem('userid') || crossData.userid
            const crossuserid = rawUserId ? Number(rawUserId as any) : undefined
            const crossrefid = crossData.id
            const rawLimit = localStorage.getItem('statusColumnName') || ''
            const crossoptnum = rawLimit.replace(/\[/, '').replace(/\]/, '')
            if (crossrefid && crossuserid) {
              ;(form as any).crossuserid = crossuserid
              ;(form as any).crossrefid = crossrefid
              // 检查已有数量
              const params = {
                page: 1,
                limit: 10,
                crossuserid: crossuserid,
                crossrefid: crossrefid,
              }

              const { data } = await http.get('productorder/page', { params })
              if (data.total >= Number(crossoptnum)) {
                ElMessage.error(tips || '操作次数已达上限')
                return false
              }
            }
          }
        }
      } catch (error) {
        console.error('处理跨表状态更新失败:', error)
        return false
      }
    }
    return true
  }

  // 执行数量检查
  const handleCrossStockBeforeSave = async (): Promise<boolean> => {
    try {
      const targetTable = (localStorage.getItem('stockUpdate.targetTable') || '').trim()
      const field = (localStorage.getItem('stockUpdate.field') || '').trim()
      const mode = (localStorage.getItem('stockUpdate.mode') || 'minus').trim()
      const amountField = (localStorage.getItem('stockUpdate.field') || '').trim()
      if (!targetTable || !field || !amountField) return true
      let refId = localStorage.getItem('crossrefid')
      if (!refId || refId === 'undefined' || refId === 'null') {
        refId = JSON.parse(localStorage.getItem('crossObj') || '{}')?.id
      }
      if (!refId || refId === 'undefined' || refId === 'null') {
        ElMessage.error('缺少源记录ID，无法更新库存')
        return false
      }

      let amount: number
      if (!amountField) {
        amount = 1
      } else {
        amount = Number((form as any)[amountField])
        if (!isFinite(amount) || amount <= 0) {
          ElMessage.error('数量必须为大于0的数字')
          return false
        }
      }

      // 获取最新源记录
      const { data: info } = await http.get(`${targetTable}/info/${refId}`)
      if (!info) {
        ElMessage.error('未找到源记录')
        return false
      }
      const current = Number(info?.[field] ?? 0)
      if (!isFinite(current)) {
        ElMessage.error('源表数量无效')
        return false
      }
      const next = mode === 'minus' ? current - amount : current + amount
      if (next < 0) {
        ElMessage.error('数量不足，无法提交')
        return false
      }

      // 预演：缓存完整payload，提交成功后回写
      const payload = { ...info, [field]: next }
      localStorage.setItem('stockUpdatePlan', JSON.stringify({ targetTable, payload }))
      return true
    } catch (e) {
      console.error('跨表数量校验失败:', e)
      return false
    }
  }

  const ensurePointRequirementBeforeSave = async (): Promise<boolean> => {
    if(isAdminRole.value) return false;
    pointAdjustmentPlan.value = null
    if (!pointRecordConfig.enabled || type.value !== 'cross') {
      return true
    }

    const pointsField = (pointRecordConfig.recordPointsField || '').trim()
    if (!pointsField || !Object.prototype.hasOwnProperty.call(form, pointsField)) {
      return true
    }
    const rawDelta = (form as any)[pointsField]
    const deltaValue = Number(rawDelta ?? 0)
    const delta = Math.abs(deltaValue)
    if (!Number.isFinite(delta) || delta === 0) {
      return true
    }

    const userId = (authStore as any)?.userInfo?.id || localStorage.getItem('userid')
    try {
      const res = await http.get(pointRecordConfig.userTable + '/info/' + userId)
      const info = (res && (res as any).data) || {}
      const currentPoints = Number(info.points ?? 0)
      if (!Number.isFinite(currentPoints)) {
        ElMessage.error('用户积分数据异常')
        return false
      }

      let nextPoints = currentPoints
      if (pointRecordConfig.mode === 'redeem') {
        if (delta <= 0) {
          ElMessage.error('所需积分必须大于0')
          return false
        }
        if (currentPoints < delta) {
          ElMessage.error('积分不足，无法兑换')
          return false
        }
        nextPoints = currentPoints - delta
      } else {
        const earn = delta > 0 ? delta : 0
        if (earn === 0) {
          return true
        }
        nextPoints = currentPoints + earn
      }

      const updatePayload: Record<string, any> = {
        ...info,
        id: info?.id ?? userId,
        points: nextPoints
      }
      pointAdjustmentPlan.value = { userId, payload: updatePayload }
      return true
    } catch (error) {
      console.error('校验用户积分失败:', error)
      ElMessage.error('校验用户积分失败')
      return false
    }
  }

  const applyPointAdjustmentAfterSave = async () => {
    if (!pointRecordConfig.enabled || !pointAdjustmentPlan.value) {
      return
    }
    const plan = pointAdjustmentPlan.value
    try {
      await http.post(pointRecordConfig.userTable + '/update', plan.payload)
    } catch (error) {
      console.error('更新用户积分失败:', error)
      ElMessage.error('更新用户积分失败')
    } finally {
      pointAdjustmentPlan.value = null
    }
  }

  // 提交后：按预演计划回写目标表库存
  const applyCrossStockUpdateAfterSave = async () => {
    const planRaw = localStorage.getItem('stockUpdatePlan')
    if (!planRaw) return
    try {
      const plan = JSON.parse(planRaw)
      const targetTable = plan?.targetTable
      const payload = plan?.payload
      if (!targetTable || !payload) return
      await http.post(`${targetTable}/update`, payload)
    } catch (e) {
      console.error('更新源表库存失败:', e)
    }
  }
  // 计算两个日期的相差天数（start < end 返回正整数，否则返回0）
  const diffDays = (start?: string, end?: string): number => {
    if (!start || !end) return 0
    const a = new Date(start as any).getTime()
    const b = new Date(end as any).getTime()
    if (isNaN(a) || isNaN(b) || b <= a) return 0
    const oneDay = 24 * 3600 * 1000
    return Math.floor((b - a) / oneDay)
  }

  // 暴露方法给父组件
  defineExpose({
    init
  })
</script>

