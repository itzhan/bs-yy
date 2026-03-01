<template>
  <div class="form-container front-add-page">
    <el-card shadow="hover" class="form-card">
      <template #header>
        <div class="form-card-header">{{ isEdit ? '修改训练计划' : '新增训练计划' }}</div>
      </template>
      <el-alert
        title="训练计划由教练制定，前台仅支持查看"
        type="info"
        :closable="false"
        show-icon
        class="mb-4"
      />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="计划名称" prop="planname">
          <el-input v-model="form.planname" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="训练目标" prop="plangoal">
          <el-input v-model="form.plangoal" placeholder="请输入训练目标" />
        </el-form-item>
        <el-form-item label="计划内容" prop="plancontent">
          <el-input v-model="form.plancontent" type="textarea" :rows="5" placeholder="请输入计划内容" />
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-date-picker v-model="form.starttime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-date-picker v-model="form.endtime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" />
        </el-form-item>
        <el-form-item label="提交教练" prop="coachid">
          <el-select v-model="form.coachid" placeholder="请选择教练" filterable @change="onCoachChange">
            <el-option v-for="opt in coachOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-input v-model="form.auditstatus" readonly />
        </el-form-item>
        <el-form-item label="计划状态">
          <el-input v-model="form.planstatus" readonly />
        </el-form-item>
        <el-form-item label="审核回复" v-if="form.auditreply">
          <el-input v-model="form.auditreply" type="textarea" :rows="2" readonly />
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
  planname: '',
  plangoal: '',
  plancontent: '',
  starttime: '',
  endtime: '',
  coachid: undefined,
  coachname: '',
  coachaccount: '',
  auditstatus: '待审核',
  auditreply: '',
  planstatus: '待审核'
})

const isEdit = computed(() => Boolean(route.query.id))

const rules: FormRules = {
  planname: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  plangoal: [{ required: true, message: '请输入训练目标', trigger: 'blur' }],
  plancontent: [{ required: true, message: '请输入计划内容', trigger: 'blur' }],
  coachid: [{ required: true, message: '请选择教练', trigger: 'change' }]
}

const coachOptions = ref<Array<{ label: string; value: number; coachaccount?: string }>>([])

const fetchCoachMembers = async () => {
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
    const res: any = await http.get(`trainingplan/info/${id}`)
    if (res?.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // ignore
  }
}

const handleSubmit = async () => {
  ElMessage.warning('训练计划由教练制定')
}

const handleCancel = () => router.back()

onMounted(async () => {
  await fetchCoachMembers()
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
