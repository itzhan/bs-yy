<template>
  <el-dialog v-model="visible" :title="dataForm.id ? (isInfo ? '查看支付设置' : '修改支付设置') : '新增支付设置'" width="600px" :close-on-click-modal="false" @close="visible = false">
    <el-form ref="formRef" :model="dataForm" :rules="rules" label-width="120px" :disabled="isInfo">
      <el-form-item label="支付方式名称" prop="payname">
        <el-input v-model="dataForm.payname" placeholder="请输入支付方式名称" />
      </el-form-item>
      <el-form-item label="编码" prop="paycode">
        <el-input v-model="dataForm.paycode" placeholder="请输入编码（如 weixin, zhifubao）" />
      </el-form-item>
      <el-form-item label="图标" prop="payicon">
        <el-upload :action="uploadUrl" :show-file-list="false" :on-success="uploadSuccess" accept="image/*">
          <el-image v-if="dataForm.payicon" :src="imgSrc(dataForm.payicon)" style="width:80px;height:80px" fit="cover" />
          <el-button v-else size="small" type="primary">上传图标</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="是否启用" prop="enabled">
        <el-radio-group v-model="dataForm.enabled">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sortorder">
        <el-input-number v-model="dataForm.sortorder" :min="0" :step="1" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button v-if="!isInfo" type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { http } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'

const emit = defineEmits(['refreshDataList'])
const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const uploadUrl = (baseUrl.endsWith('/') ? baseUrl : baseUrl + '/') + 'file/upload'
const imgSrc = (p: string) => { if (!p) return ''; return p.startsWith('http') ? p : baseUrl + (p.startsWith('/') ? p : '/' + p) }

const visible = ref(false)
const isInfo = ref(false)
const formRef = ref<FormInstance>()
const dataForm = reactive<Record<string, any>>({
  id: null,
  payname: '',
  paycode: '',
  payicon: '',
  enabled: '是',
  sortorder: 0
})

const rules = {
  payname: [{ required: true, message: '请输入支付方式名称', trigger: 'blur' }],
  paycode: [{ required: true, message: '请输入编码', trigger: 'blur' }]
}

const init = (id?: number, type?: string) => {
  isInfo.value = type === 'info'
  Object.assign(dataForm, { id: null, payname: '', paycode: '', payicon: '', enabled: '是', sortorder: 0 })
  visible.value = true
  if (id) {
    http.get('paymentsetting/info/' + id).then((res: any) => {
      if (res?.code === 0 && res.data) {
        Object.assign(dataForm, res.data)
      }
    })
  }
}

const uploadSuccess = (res: any) => {
  if (res?.code === 0 && res.file) {
    dataForm.payicon = res.file
  }
}

const submitForm = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid) return
    const url = dataForm.id ? 'paymentsetting/update' : 'paymentsetting/save'
    const res: any = await http.post(url, dataForm)
    if (res?.code === 0) {
      ElMessage.success('操作成功')
      visible.value = false
      emit('refreshDataList')
    } else {
      ElMessage.error(res?.msg || '操作失败')
    }
  })
}

defineExpose({ init })
</script>
