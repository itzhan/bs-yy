<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">{{ isEdit ? '修改教练评价' : '新增教练评价' }}</div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="教练" prop="coachid">
          <el-select v-model="form.coachid" placeholder="请选择教练" filterable @change="onCoachChange">
            <el-option v-for="opt in coachOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="form.rating" />
        </el-form-item>
        <el-form-item label="评价内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入评价内容" />
        </el-form-item>
        <div class="form-actions">
          <el-button type="primary" :loading="saving" @click="handleSubmit">提交</el-button>
          <el-button @click="handleCancel">取消</el-button>
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
  coachid: undefined,
  coachname: '',
  coachaccount: '',
  rating: 5,
  content: ''
})

const isEdit = computed(() => Boolean(route.query.id))

const rules: FormRules = {
  coachid: [{ required: true, message: '请选择教练', trigger: 'change' }],
  content: [{ required: true, message: '请输入评价内容', trigger: 'blur' }]
}

const coachOptions = ref<Array<{ label: string; value: number; coachaccount?: string }>>([])

const fetchCoachOptions = async () => {
  try {
    const res: any = await http.get('coachmember/page', { params: { page: 1, limit: 999 } })
    const payload = res?.data
    const list = Array.isArray(payload?.data?.list)
      ? payload.data.list
      : Array.isArray(payload?.list)
        ? payload.list
        : []
    coachOptions.value = list.map((item: any) => ({
      label: item.coachname || item.coachaccount || String(item.coachid),
      value: Number(item.coachid),
      coachaccount: item.coachaccount
    }))
  } catch {
    coachOptions.value = []
  }
  if (!coachOptions.value.length) {
    try {
      const res: any = await http.get('coach/list', { params: { page: 1, limit: 999 } })
      const payload = res?.data
      const list = Array.isArray(payload?.data?.list)
        ? payload.data.list
        : Array.isArray(payload?.list)
          ? payload.list
          : []
      coachOptions.value = list.map((item: any) => ({
        label: item.coachname || item.coachaccount || String(item.id),
        value: Number(item.id),
        coachaccount: item.coachaccount
      }))
    } catch {
      // ignore
    }
  }
}

const onCoachChange = (val: number) => {
  const opt = coachOptions.value.find((item) => item.value === Number(val))
  if (opt) {
    form.coachname = opt.label
    form.coachaccount = opt.coachaccount || ''
  }
}

const loadDetail = async () => {
  const id = route.query.id
  if (!id) return
  try {
    const res: any = await http.get(`coachreview/info/${id}`)
    if (res?.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // ignore
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    saving.value = true
    try {
      const api = isEdit.value ? 'coachreview/update' : 'coachreview/add'
      await http.post(api, form)
      ElMessage.success('提交成功')
      router.back()
    } catch {
      // ignore
    } finally {
      saving.value = false
    }
  })
}

const handleCancel = () => router.back()

onMounted(async () => {
  await fetchCoachOptions()
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
