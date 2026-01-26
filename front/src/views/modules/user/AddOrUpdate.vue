<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">
          {{ isEdit ? `修改用户` : `新增用户` }}
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="form-main"
      >
    <el-form-item label="账号" prop="useraccount">
      <el-input
        v-model="form.useraccount"
        :placeholder="'请输入账号'"
        :readonly="read.useraccount"
        clearable
      />
    </el-form-item>
    <el-form-item label="密码" prop="userpassword">
      <el-input
        v-model="form.userpassword"
        :placeholder="'请输入密码'"
        :readonly="read.userpassword"
        clearable
      />
    </el-form-item>
    <el-form-item label="姓名" prop="name">
      <el-input
        v-model="form.name"
        :placeholder="'请输入姓名'"
        :readonly="read.name"
        clearable
      />
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-input
        v-model="form.sex"
        :placeholder="'请输入性别'"
        :readonly="read.sex"
        clearable
      />
    </el-form-item>
    <el-form-item label="年龄" prop="age">
      <el-input-number v-model="form.age" placeholder="请输入年龄" :step="1" :controls="false" :disabled="read.age" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phone">
      <el-input
        v-model="form.phone"
        :placeholder="'请输入手机号码'"
        :readonly="read.phone"
        clearable
      />
    </el-form-item>
    <el-form-item label="头像" prop="image">
      <UploadField
        v-model="form.image"
        upload-type="image"
        :readonly="read.image"
      />
    </el-form-item>
    <el-form-item label="余额" prop="money">
      <el-input-number v-model="form.money" placeholder="请输入余额" :step="1" :controls="false" :disabled="read.money" />
    </el-form-item>
    <el-form-item label="会员卡号" prop="cardno">
      <el-input
        v-model="form.cardno"
        :placeholder="'请输入会员卡号'"
        :readonly="read.cardno"
        clearable
      />
    </el-form-item>
    <el-form-item label="会员等级" prop="memberlevel">
      <el-select v-model="form.memberlevel" placeholder="请选择会员等级" clearable :disabled="read.memberlevel">
        <el-option
          v-for="opt in dictOptions.memberlevel"
          :key="opt"
          :label="opt"
          :value="opt"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="会员到期时间" prop="expiretime">
      <el-date-picker
        v-model="form.expiretime"
        type="datetime"
        value-format="YYYY-MM-DD HH:mm:ss"
        placeholder="请选择会员到期时间"
        :disabled="read.expiretime"
        clearable
      />
    </el-form-item>
    <el-form-item label="剩余课时" prop="remainingcourses">
      <el-input-number v-model="form.remainingcourses" placeholder="请输入剩余课时" :step="1" :controls="false" :disabled="read.remainingcourses" />
    </el-form-item>
        <div class="form-actions">
          <el-button class="confirm-btn" type="primary" :loading="saving" @click="handleSubmit">提交</el-button>
          <el-button class="cancel-btn" @click="handleCancel">取消</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { http } from '@/utils/request'
import { recognizeImage } from '@/utils/imageRecognition'

const route = useRoute()
const router = useRouter()
const emit = defineEmits(['success'])
const formRef = ref<FormInstance>()
const saving = ref(false)

const isEdit = ref(false)
const pageType = ref<string>(String(route.query.type || ''))
const isCross = computed(() => (pageType.value || '').toLowerCase() === 'cross')
const embedMode = computed(() => (pageType.value || '').toLowerCase() === 'embed')
const sessionRaw = localStorage.getItem('frontSession')
let sessionObj: any = {}
try {
  sessionObj = sessionRaw ? JSON.parse(sessionRaw) : {}
} catch (error) {
  sessionObj = {}
}
const currentUserId = (() => {
  const direct = Number(localStorage.getItem('frontUserid') || localStorage.getItem('userid'))
  if (Number.isFinite(direct) && direct) return direct
  const fallback = Number(sessionObj.id || sessionObj.userid || sessionObj.userId || 0)
  return Number.isFinite(fallback) ? fallback : 0
})()

const initialFormValues: Record<string, any> = {
    useraccount: 
      '',
    userpassword: 
      '',
    name: 
      '',
    sex: 
      '',
    age: 
      undefined,
    phone: 
      '',
    image: 
      '',
    money: 
      undefined,
    addtime: 
      '',
    cardno: 
      '',
    memberlevel: 
      '普通会员',
    expiretime: 
      '',
    remainingcourses: 
      undefined,
}

const form = reactive<any>({ ...initialFormValues })
const imageRecognitionConfig = {
  enabled: false,
  sourceField: '',
  targetField: '',
  targetLabel: ''
}
const imageRecognitionState = reactive({
  ready: false,
  lastImage: '',
  requestId: 0
})
const normalizeImageValue = (value: unknown): string => {
  if (value === null || value === undefined) return ''
  if (Array.isArray(value)) {
    const filtered = value.map((item) => String(item ?? '').trim()).filter((item) => item.length > 0)
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
const handleImageRecognition = async (imagePath: string) => {
  if (!imageRecognitionConfig.enabled || !imageRecognitionConfig.targetField) return
  if (!Object.prototype.hasOwnProperty.call(form, imageRecognitionConfig.targetField)) return
  const trimmed = imagePath.trim()
  if (!trimmed) {
    imageRecognitionState.lastImage = ''
    return
  }
  const requestId = ++imageRecognitionState.requestId
  imageRecognitionState.lastImage = trimmed
  try {
    const result = await recognizeImage(trimmed)
    if (requestId !== imageRecognitionState.requestId) return
    const keyword = (result?.keyword || '').trim()
    if (keyword) {
      form[imageRecognitionConfig.targetField] = keyword
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
  }
}

if (imageRecognitionConfig.enabled && imageRecognitionConfig.sourceField) {
  watch(() => form[imageRecognitionConfig.sourceField], (val) => {
    if (!imageRecognitionState.ready) return
    const latest = normalizeImageValue(val as any)
    if (!latest) {
      imageRecognitionState.lastImage = ''
      return
    }
    if (latest === imageRecognitionState.lastImage) return
    handleImageRecognition(latest)
  })
}

let currentEditId = 0

const read = reactive<Record<string, boolean>>({
  'useraccount': false,
  'userpassword': false,
  'name': false,
  'sex': false,
  'age': false,
  'phone': false,
  'image': false,
  'money': false,
  'addtime': false,
  'cardno': false,
  'memberlevel': false,
  'expiretime': false,
  'remainingcourses': false,
})
function resetFormValues() {
  Object.keys(initialFormValues).forEach((key) => {
    form[key] = initialFormValues[key]
  })
  if (imageRecognitionConfig.enabled) {
    imageRecognitionState.lastImage = ''
  }
}

function resetReadState() {
  Object.keys(read).forEach((key) => {
    read[key] = false
  })
}

const dictOptions = reactive<Record<string, string[]>>({
  'sex': ['男', '女'],
  'memberlevel': ['普通会员', '银卡会员', '金卡会员', '钻石会员'],
})

const categoryOptions = reactive<Record<string, string[]>>({
})

const rules = reactive<FormRules>({
    'useraccount': [
      { required: false, message: '请输入账号', trigger: 'blur' }
    ],
    'userpassword': [
      { required: false, message: '请输入密码', trigger: 'blur' }
    ],
    'name': [
      { required: false, message: '请输入姓名', trigger: 'blur' }
    ],
    'sex': [
      { required: false, message: '请输入性别', trigger: 'blur' }
    ],
    'age': [
      { required: false, message: '请输入年龄', trigger: 'blur' }
    ],
    'phone': [
      { required: false, message: '请输入手机号码', trigger: 'blur' }
    ],
    'image': [
      { required: false, message: '请输入头像', trigger: 'blur' }
    ],
    'money': [
      { required: false, message: '请输入余额', trigger: 'blur' }
    ],
    'addtime': [
      { required: false, message: '请输入创建时间', trigger: 'blur' }
    ],
    'cardno': [
      { required: false, message: '请输入会员卡号', trigger: 'blur' }
    ],
    'memberlevel': [
      { required: false, message: '请输入会员等级', trigger: 'blur' }
    ],
    'expiretime': [
      { required: false, message: '请输入会员到期时间', trigger: 'blur' }
    ],
    'remainingcourses': [
      { required: false, message: '请输入剩余课时', trigger: 'blur' }
    ],
})

// 计算两个日期的相差天数（start < end 返回正整数，否则返回0）
const diffDays = (start?: string, end?: string): number => {
  if (!start || !end) return 0
  const a = new Date(start as any).getTime()
  const b = new Date(end as any).getTime()
  if (isNaN(a) || isNaN(b) || b <= a) return 0
  const oneDay = 24 * 3600 * 1000
  return Math.floor((b - a) / oneDay)
}

// 自动计算：日期差与表达式

async function loadCategoryOptions() {
}

let categoriesLoaded = false
async function ensureCategoryOptions() {
  if (categoriesLoaded) return
  await loadCategoryOptions()
  categoriesLoaded = true
}

function applyCrossPrefill() {
  try {
    const raw = localStorage.getItem('crossObj')
    if (!raw) return
    resetReadState()
    const crossObj = JSON.parse(raw)
    let amountFieldName = ''
    try {
      const af = (localStorage.getItem('stockUpdate.field') || '').trim()
      const f = (localStorage.getItem('stockUpdate.field') || '').trim()
      amountFieldName = af || f
    } catch (error) {}
    Object.keys(form).forEach((key) => {
      if (key === 'id') return
      if (key === 'crossuserid') {
        if (currentUserId) (form as any)[key] = currentUserId
        return
      }
      if (key === 'crossrefid') {
        let refVal: any = localStorage.getItem('crossrefid')
        if (!refVal || refVal === 'null' || refVal === 'undefined') {
          refVal = crossObj.id
        }
        if (refVal !== undefined && refVal !== null && refVal !== '') {
          const numeric = Number(refVal)
          ;(form as any)[key] = Number.isFinite(numeric) ? numeric : refVal
        }
        return
      }
      const value = crossObj[key]
      if (value !== undefined && value !== null) {
        (form as any)[key] = value
        if (Object.prototype.hasOwnProperty.call(read, key)) {
          if (!amountFieldName || key !== amountFieldName) {
            read[key] = true
          }
        }
      }
    })
    if (pointRecordConfig.enabled) {
      const userField = (pointRecordConfig.recordUserField || '').trim()
      if (userField && Object.prototype.hasOwnProperty.call(form, userField)) {
        const currentVal = (form as any)[userField]
        const needsFill = currentVal === undefined || currentVal === null || String(currentVal).trim() === ''
        if (needsFill) {
          const fallbackUser = crossObj[userField]
            ?? localStorage.getItem('userid')
            ?? localStorage.getItem('frontUserid')
            ?? currentUserId
          if (fallbackUser !== undefined && fallbackUser !== null && fallbackUser !== '') {
            const parsed = Number(fallbackUser as any)
            (form as any)[userField] = Number.isFinite(parsed) ? parsed : fallbackUser
            if (Object.prototype.hasOwnProperty.call(read, userField)) {
              read[userField] = true
            }
          }
        }
      }
    }
    if (amountFieldName && (form as any)[amountFieldName] === undefined) {
      (form as any)[amountFieldName] = 1 as any
    }
  } catch (error) {
    console.warn('跨表数据预填失败', error)
  }
}

async function applyCrossStatusAfterSave(): Promise<string> {
  const crossTable = localStorage.getItem('crossTable')
  const statusColumnName = localStorage.getItem('statusColumnName')
  const statusColumnValueRaw = localStorage.getItem('statusColumnValue')
  const tips = localStorage.getItem('tips')
  let message = tips || '操作成功'

  if (crossTable && statusColumnName && !statusColumnName.startsWith('[')) {
    const raw = localStorage.getItem('crossObj')
    if (raw) {
      try {
        const payload = JSON.parse(raw)
        let statusValue: any = statusColumnValueRaw
        if (statusColumnValueRaw !== null && statusColumnValueRaw !== undefined) {
          try {
            statusValue = JSON.parse(statusColumnValueRaw)
          } catch (e) {
            statusValue = statusColumnValueRaw
          }
        }
        payload[statusColumnName] = statusValue
        await http.post(`${crossTable}/update`, payload)
      } catch (error) {
        console.warn('跨表状态更新失败', error)
      }
    }
  }

  return message
}

function clearCrossCache() {
  ;['crossObj','crossTable','statusColumnName','statusColumnValue','tips','crossrefid','stockUpdate.targetTable','stockUpdate.mode','stockUpdate.field','stockUpdatePlan'].forEach(key => localStorage.removeItem(key))
  resetReadState()
}


// 与 admin 端一致的“库存数量”校验与回写逻辑
// 1) 提交前：根据 localStorage 中的 stockUpdate.* 对源表数量做预演校验并缓存回写计划
async function handleCrossStockBeforeSave(): Promise<boolean> {
  try {
    const targetTable = (localStorage.getItem('stockUpdate.targetTable') || '').trim()
    const field = (localStorage.getItem('stockUpdate.field') || '').trim()
    const mode = (localStorage.getItem('stockUpdate.mode') || 'minus').trim()
    const amountField = (localStorage.getItem('stockUpdate.field') || '').trim()
    if (!targetTable || !field) return true

    // 关联源记录ID：优先 crossrefid，其次 crossObj.id
    let refId: any = localStorage.getItem('crossrefid')
    if (!refId || refId === 'undefined' || refId === 'null') {
      try { refId = JSON.parse(localStorage.getItem('crossObj') || '{}')?.id } catch (e) { refId = undefined }
    }
    if (!refId || refId === 'undefined' || refId === 'null') {
      ElMessage.error('缺少源记录ID，无法更新库存')
      return false
    }

    // 数量：未配置时默认 1；配置了 amountField 则读取表单中的数量
      let amount: number
      if (!amountField) {
          amount = 1
      } else {
          const parsed = Number((form as any)[amountField])
          if (!isFinite(parsed) || String((form as any)[amountField]).trim() === '') {
              amount = 1
          } else if (parsed <= 0) {
              ElMessage.error('数量必须为大于0的数字')
              return false
          } else {
              amount = parsed
          }
      }

    // 拉取源记录做校验
    const { data: info }: any = await http.get(`${targetTable}/info/${refId}`)
    if (!info) { ElMessage.error('未找到源记录'); return false }
    const current = Number(info?.[field] ?? 0)
    if (!isFinite(current)) { ElMessage.error('源表数量无效'); return false }
    const next = mode === 'minus' ? current - amount : current + amount
    if (next < 0) { ElMessage.error('数量不足，无法提交'); return false }

    // 预演计划存储，提交成功后统一回写
    const payload = { ...info, [field]: next }
    localStorage.setItem('stockUpdatePlan', JSON.stringify({ targetTable, payload }))
    return true
  } catch (e) {
    console.error('跨表数量校验失败:', e)
    return false
  }
}

// 2) 提交成功后：按预演计划回写目标表库存
async function applyCrossStockUpdateAfterSave(): Promise<void> {
  const raw = localStorage.getItem('stockUpdatePlan')
  if (!raw) return
  try {
    const plan = JSON.parse(raw)
    const targetTable = plan?.targetTable
    const payload = plan?.payload
    if (!targetTable || !payload) return
    await http.post(`${targetTable}/update`, payload)
  } catch (e) {
    console.error('更新源表库存失败:', e)
  }
}

async function ensurePointRequirementBeforeSave(): Promise<boolean> {
  pointAdjustmentPlan.value = null
  if (!pointRecordConfig.enabled || !isCross.value) {
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

  try {
    const res = await http.get(pointRecordConfig.userTable + '/info/' + currentUserId)
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
      id: info?.id ?? currentUserId,
      points: nextPoints
    }
    pointAdjustmentPlan.value = { userId:currentUserId, payload: updatePayload }
    return true
  } catch (error) {
    console.error('校验用户积分失败:', error)
    ElMessage.error('校验用户积分失败')
    return false
  }
}

async function applyPointAdjustmentAfterSave(): Promise<void> {
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


async function loadDetail(id: number) {
  try {
    resetReadState()
    const res: any = await http.get('user/detail/' + id)
    Object.assign(form, res?.data || {})
    currentEditId = id
  } catch (error) {
    ElMessage.error('加载详情失败')
  }
}

function handleCancel() {
  if (isCross.value) {
    clearCrossCache()
  }
  if (embedMode.value) {
    if (currentEditId) {
      loadDetail(currentEditId)
    } else {
      resetFormValues()
    }
    return
  }
  router.back()
}

async function handleSubmit() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  saving.value = true
  try {
    // 跨表场景：提交前执行“库存/数量”校验与积分校验
    if (isCross.value) {
      const ok = await handleCrossStockBeforeSave()
      if (!ok) { saving.value = false; return }
      const pointOk = await ensurePointRequirementBeforeSave()
      if (!pointOk) { saving.value = false; return }
    }

    const api = isEdit.value && !isCross.value ? 'user/update' : 'user/add'
    const payload = { ...form }
    const res: any = await http.post(api, payload)
    if (res?.code === 0) {
      if (isCross.value) {
        try {
          // 按预演计划回写源表库存
          const message = await applyCrossStatusAfterSave()
          await applyCrossStockUpdateAfterSave()
          await applyPointAdjustmentAfterSave()
          ElMessage.success(message)
          if (false && typeof updateSeatSoldSeatsAfterSave === 'function') {
            await updateSeatSoldSeatsAfterSave()
          }
        } catch (error) {
          ElMessage.success('操作成功')
          await applyPointAdjustmentAfterSave()
        } finally {
          clearCrossCache()
        }
      } else {
        await applyPointAdjustmentAfterSave()
        ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
      }
      if (embedMode.value) {
        emit('success')
      } else {
        router.back()
      }
    } else {
      ElMessage.error(res?.msg || '操作失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    pointAdjustmentPlan.value = null
    saving.value = false
  }
}

async function init(id?: number | null, type?: string | null) {
  resetReadState()
  resetFormValues()
  await ensureCategoryOptions()
  pageType.value = typeof type === 'string' ? type : ''
  const normalizedId = Number(id)
  currentEditId = Number.isFinite(normalizedId) && normalizedId > 0 ? normalizedId : 0
  if (imageRecognitionConfig.enabled) {
    imageRecognitionState.ready = false
    imageRecognitionState.lastImage = ''
    imageRecognitionState.requestId = 0
  }
  if (Number.isFinite(normalizedId) && normalizedId > 0) {
    isEdit.value = true
    await loadDetail(normalizedId)
  } else {
    isEdit.value = false
    if (isCross.value) {
      applyCrossPrefill()
    }
  }
  // 自动计算字段默认只读
  if (imageRecognitionConfig.enabled) {
    imageRecognitionState.ready = true
  }
}

onMounted(async () => {
  const queryId = Number(route.query.id)
  const validId = Number.isFinite(queryId) && queryId > 0 ? queryId : undefined
  const typeParam = typeof route.query.type === 'string' ? route.query.type : ''
  await init(validId, typeParam)
})

defineExpose({ init })
</script>
