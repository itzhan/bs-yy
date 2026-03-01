<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">{{ isEdit ? '修改计划明细' : '新增计划明细' }}</div>
      </template>
      <el-alert
        title="训练计划明细由教练维护，前台仅支持查看"
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
        <el-form-item label="序号" prop="dayno">
          <el-input-number v-model="form.dayno" :min="1" />
        </el-form-item>
        <el-form-item label="训练内容" prop="itemcontent">
          <el-input v-model="form.itemcontent" type="textarea" :rows="4" placeholder="请输入训练内容" />
        </el-form-item>
        <el-form-item label="目标" prop="target">
          <el-input v-model="form.target" placeholder="请输入目标" />
        </el-form-item>
        <el-form-item label="排序" prop="sortno">
          <el-input-number v-model="form.sortno" :min="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
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
  dayno: 1,
  itemcontent: '',
  target: '',
  sortno: 0,
  remark: ''
})

const isEdit = computed(() => Boolean(route.query.id))

const rules: FormRules = {
  planid: [{ required: true, message: '请选择计划', trigger: 'change' }],
  itemcontent: [{ required: true, message: '请输入训练内容', trigger: 'blur' }]
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
    const res: any = await http.get(`trainingplanitem/info/${id}`)
    if (res?.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // ignore
  }
}

const handleSubmit = async () => {
  ElMessage.warning('训练计划明细由教练维护')
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
