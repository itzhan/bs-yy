<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">{{ isEdit ? '修改训练记录' : '新增训练记录' }}</div>
      </template>
      <el-alert
        title="训练记录由教练填写，前台仅支持查看"
        type="info"
        :closable="false"
        show-icon
        class="mb-4"
      />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="所属计划" prop="planid">
          <el-select v-model="form.planid" placeholder="请选择计划" filterable @change="onPlanChange">
            <el-option v-for="opt in planOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="记录时间" prop="recorddate">
          <el-date-picker v-model="form.recorddate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择记录时间" />
        </el-form-item>
        <el-form-item label="训练时长(分钟)" prop="duration">
          <el-input-number v-model="form.duration" :min="0" />
        </el-form-item>
        <el-form-item label="完成度(%)" prop="completionrate">
          <el-input-number v-model="form.completionrate" :min="0" :max="100" />
        </el-form-item>
        <el-form-item label="训练内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入训练内容" />
        </el-form-item>
        <el-form-item label="教练点评" prop="coachcomment">
          <el-input v-model="form.coachcomment" type="textarea" :rows="3" placeholder="请输入教练点评" />
        </el-form-item>
        <div class="form-actions">
          <el-button @click="handleCancel">返回</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { http } from '@/utils/request'

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()
const saving = ref(false)

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

const isEdit = computed(() => Boolean(route.query.id))

const rules: FormRules = {
  planid: [{ required: true, message: '请选择计划', trigger: 'change' }],
  recorddate: [{ required: true, message: '请选择记录时间', trigger: 'change' }],
  content: [{ required: true, message: '请输入训练内容', trigger: 'blur' }]
}

const planOptions = ref<Array<{ label: string; value: number }>>([])

const fetchPlans = async () => {
  try {
    const res: any = await http.get('trainingplan/page', { params: { page: 1, limit: 999 } })
    const payload = res?.data
    const list = Array.isArray(payload?.data?.list)
      ? payload.data.list
      : Array.isArray(payload?.list)
        ? payload.list
        : []
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

const loadDetail = async () => {
  const id = route.query.id
  if (!id) return
  try {
    const res: any = await http.get(`trainingrecord/info/${id}`)
    if (res?.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // ignore
  }
}

const handleSubmit = async () => {
  ElMessage.warning('训练记录由教练填写')
}

const handleCancel = () => router.back()

onMounted(async () => {
  await fetchPlans()
  await loadDetail()
})
</script>

<style scoped>
.form-card-header {
  font-weight: 600;
}
.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}
</style>
