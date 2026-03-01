<template>
  <div class="add-edit-dialog">
    <el-dialog v-model="visible" :title="id ? (type === 'info' ? '查看训练计划' : '修改训练计划') : '新增训练计划'" width="70%" :close-on-click-modal="false" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="计划名称" prop="planname">
          <el-input v-model="form.planname" :readonly="type === 'info'" />
        </el-form-item>
        <el-form-item label="训练目标" prop="plangoal">
          <el-input v-model="form.plangoal" :readonly="type === 'info'" />
        </el-form-item>
        <el-form-item label="计划内容" prop="plancontent">
          <el-input v-model="form.plancontent" type="textarea" :rows="4" :readonly="type === 'info'" />
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-date-picker v-model="form.starttime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" :disabled="type === 'info'" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-date-picker v-model="form.endtime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" :disabled="type === 'info'" />
        </el-form-item>
        <el-form-item label="会员" prop="userid">
          <el-select v-model="form.userid" placeholder="请选择会员" filterable :disabled="type === 'info'" @change="onUserChange">
            <el-option v-for="opt in userOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="教练" prop="coachid">
          <el-select v-model="form.coachid" placeholder="请选择教练" filterable :disabled="type === 'info' || isCoachRole" @change="onCoachChange">
            <el-option v-for="opt in coachOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态" prop="auditstatus">
          <el-select v-model="form.auditstatus" placeholder="请选择" :disabled="type === 'info'">
            <el-option label="待审核" value="待审核" />
            <el-option label="通过" value="通过" />
            <el-option label="不通过" value="不通过" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划状态" prop="planstatus">
          <el-select v-model="form.planstatus" placeholder="请选择" :disabled="type === 'info'">
            <el-option label="待审核" value="待审核" />
            <el-option label="进行中" value="进行中" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已驳回" value="已驳回" />
            <el-option label="已暂停" value="已暂停" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核回复" prop="auditreply">
          <el-input v-model="form.auditreply" type="textarea" :rows="2" :readonly="type === 'info'" />
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
import { ref, reactive, computed } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { http } from '@/utils/request'

const emit = defineEmits(['refreshDataList'])
const visible = ref(false)
const loading = ref(false)
const id = ref<number | undefined>()
const type = ref<string>('')
const formRef = ref<FormInstance>()
const isCoachRole = computed(() => {
  const role = (localStorage.getItem('sessionTable') || '').toString().toLowerCase()
  return role === 'coach'
})

const form = reactive<any>({
  id: undefined,
  planname: '',
  plangoal: '',
  plancontent: '',
  starttime: '',
  endtime: '',
  userid: undefined,
  useraccount: '',
  username: '',
  coachid: undefined,
  coachaccount: '',
  coachname: '',
  auditstatus: '待审核',
  auditreply: '',
  planstatus: '待审核'
})

const rules: FormRules = {
  planname: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  plangoal: [{ required: true, message: '请输入训练目标', trigger: 'blur' }],
  plancontent: [{ required: true, message: '请输入计划内容', trigger: 'blur' }],
  userid: [{ required: true, message: '请选择会员', trigger: 'change' }],
  coachid: [{ required: true, message: '请选择教练', trigger: 'change' }]
}

const coachOptions = ref<Array<{ label: string; value: number; coachaccount?: string }>>([])
const userOptions = ref<Array<{ label: string; value: number; useraccount?: string; username?: string }>>([])
const coachProfile = ref<any>(null)

const fetchCoachProfile = async () => {
  if (!isCoachRole.value) return
  try {
    const res: any = await http.get('coach/session')
    coachProfile.value = res?.data || null
  } catch {
    coachProfile.value = null
  }
}

const fetchOptions = async () => {
  if (isCoachRole.value) {
    await fetchCoachProfile()
    if (coachProfile.value) {
      coachOptions.value = [{
        label: coachProfile.value.coachname || coachProfile.value.coachaccount || String(coachProfile.value.id || ''),
        value: Number(coachProfile.value.id),
        coachaccount: coachProfile.value.coachaccount
      }]
    } else {
      coachOptions.value = []
    }
    try {
      const params: Record<string, any> = { page: 1, limit: 999 }
      if (coachProfile.value?.id) {
        params.coachid = coachProfile.value.id
      } else if (coachProfile.value?.coachaccount) {
        params.coachaccount = coachProfile.value.coachaccount
      } else {
        const fallbackAccount = localStorage.getItem('adminName')
        if (fallbackAccount) params.coachaccount = fallbackAccount
      }
      const res: any = await http.get('coachmember/list', { params })
      const list = res?.data?.list || []
      userOptions.value = list.map((item: any) => ({
        label: item.username || item.useraccount || String(item.userid || item.id),
        value: Number(item.userid || item.id),
        useraccount: item.useraccount,
        username: item.username
      }))
    } catch {
      userOptions.value = []
    }
    return
  }
  try {
    const res: any = await http.get('coach/list', { params: { page: 1, limit: 999 } })
    const list = res?.data?.list || []
    coachOptions.value = list.map((item: any) => ({
      label: item.coachname || item.coachaccount || String(item.id),
      value: Number(item.id),
      coachaccount: item.coachaccount
    }))
  } catch {
    coachOptions.value = []
  }
  try {
    const res: any = await http.get('user/list', { params: { page: 1, limit: 999 } })
    const list = res?.data?.list || []
    userOptions.value = list.map((item: any) => ({
      label: item.name || item.useraccount || String(item.id),
      value: Number(item.id),
      useraccount: item.useraccount,
      username: item.name
    }))
  } catch {
    userOptions.value = []
  }
}

const onCoachChange = (val: number) => {
  const opt = coachOptions.value.find((item) => item.value === Number(val))
  if (opt) {
    form.coachname = opt.label
    form.coachaccount = opt.coachaccount || ''
  }
}

const onUserChange = (val: number) => {
  const opt = userOptions.value.find((item) => item.value === Number(val))
  if (opt) {
    form.username = opt.username || opt.label
    form.useraccount = opt.useraccount || ''
  }
}

const init = async (newId?: number, newType?: string) => {
  id.value = newId
  type.value = newType || ''
  visible.value = true
  await fetchOptions()
  if (id.value) {
    const res: any = await http.get(`trainingplan/info/${id.value}`)
    Object.assign(form, res?.data || {})
    if (isCoachRole.value && coachProfile.value?.id) {
      form.coachid = Number(coachProfile.value.id)
      form.coachname = coachProfile.value.coachname || form.coachname
      form.coachaccount = coachProfile.value.coachaccount || form.coachaccount
    }
  } else {
    Object.assign(form, {
      id: undefined,
      planname: '',
      plangoal: '',
      plancontent: '',
      starttime: '',
      endtime: '',
      userid: undefined,
      useraccount: '',
      username: '',
      coachid: undefined,
      coachaccount: '',
      coachname: '',
      auditstatus: '待审核',
      auditreply: '',
      planstatus: '待审核'
    })
    if (isCoachRole.value && coachProfile.value?.id) {
      form.coachid = Number(coachProfile.value.id)
      form.coachname = coachProfile.value.coachname || form.coachname
      form.coachaccount = coachProfile.value.coachaccount || form.coachaccount
    }
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const api = id.value ? 'trainingplan/update' : 'trainingplan/save'
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
