<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">{{ isEdit ? '修改绑定' : '新增绑定' }}</div>
      </template>
      <el-alert
        title="绑定关系由课程报名自动生成，前台不支持手动新增或修改"
        type="info"
        :closable="false"
        show-icon
        class="mb-4"
      />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="教练" prop="coachid">
          <el-select v-model="form.coachid" placeholder="请选择教练" filterable @change="onCoachChange">
            <el-option v-for="opt in coachOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="绑定状态" prop="bindstatus">
          <el-select v-model="form.bindstatus" placeholder="请选择状态">
            <el-option label="已绑定" value="已绑定" />
            <el-option label="已解绑" value="已解绑" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
  coachid: undefined,
  coachname: '',
  coachaccount: '',
  bindstatus: '已绑定',
  remark: ''
})

const isEdit = computed(() => Boolean(route.query.id))

const rules: FormRules = {
  coachid: [{ required: true, message: '请选择教练', trigger: 'change' }]
}

const coachOptions = ref<Array<{ label: string; value: number; coachaccount?: string }>>([])

const fetchCoaches = async () => {
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
    coachOptions.value = []
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
    const res: any = await http.get(`coachmember/info/${id}`)
    if (res?.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // ignore
  }
}

const handleSubmit = async () => {
  ElMessage.warning('绑定关系由课程报名自动生成')
}

const handleCancel = () => router.back()

onMounted(async () => {
  await fetchCoaches()
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
