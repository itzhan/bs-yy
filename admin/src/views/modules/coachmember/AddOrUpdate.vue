<template>
  <div class="add-edit-dialog">
    <el-dialog v-model="visible" :title="id ? (type === 'info' ? '查看绑定' : '修改绑定') : '新增绑定'" width="60%" :close-on-click-modal="false" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="教练" prop="coachid">
          <el-select v-model="form.coachid" placeholder="请选择教练" filterable :disabled="type === 'info'" @change="onCoachChange">
            <el-option v-for="opt in coachOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="会员" prop="userid">
          <el-select v-model="form.userid" placeholder="请选择会员" filterable :disabled="type === 'info'" @change="onUserChange">
            <el-option v-for="opt in userOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="绑定状态" prop="bindstatus">
          <el-select v-model="form.bindstatus" placeholder="请选择" :disabled="type === 'info'">
            <el-option label="已绑定" value="已绑定" />
            <el-option label="已解绑" value="已解绑" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :readonly="type === 'info'" />
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
  coachid: undefined,
  coachname: '',
  coachaccount: '',
  userid: undefined,
  username: '',
  useraccount: '',
  bindstatus: '已绑定',
  remark: ''
})

const rules: FormRules = {
  coachid: [{ required: true, message: '请选择教练', trigger: 'change' }],
  userid: [{ required: true, message: '请选择会员', trigger: 'change' }]
}

const coachOptions = ref<Array<{ label: string; value: number; coachaccount?: string }>>([])
const userOptions = ref<Array<{ label: string; value: number; useraccount?: string }>>([])

const fetchOptions = async () => {
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
      useraccount: item.useraccount
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
    form.username = opt.label
    form.useraccount = opt.useraccount || ''
  }
}

const init = async (newId?: number, newType?: string) => {
  id.value = newId
  type.value = newType || ''
  visible.value = true
  await fetchOptions()
  if (id.value) {
    const res: any = await http.get(`coachmember/info/${id.value}`)
    Object.assign(form, res?.data || {})
  } else {
    Object.assign(form, {
      id: undefined,
      coachid: undefined,
      coachname: '',
      coachaccount: '',
      userid: undefined,
      username: '',
      useraccount: '',
      bindstatus: '已绑定',
      remark: ''
    })
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const api = id.value ? 'coachmember/update' : 'coachmember/save'
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
