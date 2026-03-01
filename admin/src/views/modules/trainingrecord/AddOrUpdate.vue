<template>
  <div class="add-edit-dialog">
    <el-dialog v-model="visible" :title="id ? (type === 'info' ? '查看训练记录' : '修改训练记录') : '新增训练记录'" width="60%" :close-on-click-modal="false" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="所属计划" prop="planid">
          <el-select v-model="form.planid" placeholder="请选择计划" filterable :disabled="type === 'info'" @change="onPlanChange">
            <el-option v-for="opt in planOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录时间" prop="recorddate">
          <el-date-picker v-model="form.recorddate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" :disabled="type === 'info'" />
        </el-form-item>
        <el-form-item label="训练时长(分钟)" prop="duration">
          <el-input-number v-model="form.duration" :min="0" :disabled="type === 'info'" />
        </el-form-item>
        <el-form-item label="完成度(%)" prop="completionrate">
          <el-input-number v-model="form.completionrate" :min="0" :max="100" :disabled="type === 'info'" />
        </el-form-item>
        <el-form-item label="训练内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" :readonly="type === 'info'" />
        </el-form-item>
        <el-form-item label="教练点评" prop="coachcomment">
          <el-input v-model="form.coachcomment" type="textarea" :rows="3" :readonly="type === 'info'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取消</el-button>
        <el-button v-if="type !== 'info'" type="primary" :loading="loading" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { http } from '@/utils/request'

const emit = defineEmits(['refreshDataList'])
const visible = ref(false)
const loading = ref(false)
const id = ref<number | undefined>()
const type = ref<string>('')
const formRef = ref<FormInstance>()

const form = reactive<any>({
  id: undefined,
  planid: undefined,
  planname: '',
  recorddate: '',
  duration: 0,
  completionrate: 0,
  content: '',
  coachcomment: ''
})

const rules: FormRules = {
  planid: [{ required: true, message: '请选择计划', trigger: 'change' }],
  recorddate: [{ required: true, message: '请选择记录时间', trigger: 'change' }],
  content: [{ required: true, message: '请输入训练内容', trigger: 'blur' }]
}

const planOptions = ref<Array<{ label: string; value: number }>>([])

const fetchPlans = async () => {
  try {
    const res: any = await http.get('trainingplan/page', { params: { page: 1, limit: 999 } })
    const list = res?.data?.list || []
    planOptions.value = list.map((item: any) => ({
      label: item.planname || String(item.id),
      value: Number(item.id)
    }))
  } catch {
    planOptions.value = []
  }
}

const onPlanChange = (val: number) => {
  const opt = planOptions.value.find((item) => item.value === Number(val))
  if (opt) {
    form.planname = opt.label
  }
}

const init = async (newId?: number, newType?: string) => {
  id.value = newId
  type.value = newType || ''
  visible.value = true
  await fetchPlans()
  if (id.value) {
    const res: any = await http.get(`trainingrecord/info/${id.value}`)
    Object.assign(form, res?.data || {})
  } else {
    Object.assign(form, {
      id: undefined,
      planid: undefined,
      planname: '',
      recorddate: '',
      duration: 0,
      completionrate: 0,
      content: '',
      coachcomment: ''
    })
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const api = id.value ? 'trainingrecord/update' : 'trainingrecord/save'
      await http.post(api, form)
      ElMessage.success('操作成功')
      handleClose()
      emit('refreshDataList')
    } finally {
      loading.value = false
    }
  })
}

const handleClose = () => {
  visible.value = false
}

defineExpose({ init })
</script>
