<template>
  <div class="p-6 px-10 md:px-48">
    <el-alert title="确认支付前请核对订单信息" type="success" :closable="false" class="mb-4" />

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div
        v-for="item in payOptions"
        :key="item.label"
        class="border rounded p-4 flex items-center justify-between bg-white"
      >
        <el-radio v-model="payType" :label="item.label">{{ item.label }}</el-radio>
        <img :src="item.img" :alt="item.label"/>
      </div>
    </div>

    <div class="mt-6 space-x-2">
      <el-button type="primary" @click="submitPay">确认支付</el-button>
      <el-button @click="router.back()">返回</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/request'
import { Money, CreditCard, Wallet } from '@element-plus/icons-vue'
import weixinImg from '@/assets/img/pay/weixin.png'
import zhifubaoImg from '@/assets/img/pay/zhifubao.png'
import jiansheImg from '@/assets/img/pay/jianshe.png'
import jiaotongImg from '@/assets/img/pay/jiaotong.png'
import nongyeImg from '@/assets/img/pay/nongye.png'
import zhongguoImg from '@/assets/img/pay/zhongguo.png'

const router = useRouter()

const payType = ref('')
const table = ref('')
const obj = ref<any>({})

type PayConfig = { tableName: string; statusField: string; paidValue: string; unpaidValue: string; auditRequired: boolean }
const payConfigs: PayConfig[] = [
  { tableName: 'courseenrollment', statusField: 'ispay', paidValue: '已支付', unpaidValue: '未支付', auditRequired: false },
  { tableName: 'productorder', statusField: 'ispay', paidValue: '已支付', unpaidValue: '未支付', auditRequired: false },
  { tableName: 'cardapplication', statusField: 'ispay', paidValue: '已支付', unpaidValue: '未支付', auditRequired: false },
  { tableName: 'cardrenewal', statusField: 'ispay', paidValue: '已支付', unpaidValue: '未支付', auditRequired: false }
]
const fallbackPayConfig: PayConfig = { tableName: '', statusField: 'ispay', paidValue: '已支付', unpaidValue: '未支付', auditRequired: false }
const getPayConfig = (tableName: string): PayConfig => {
  return payConfigs.find(item => item.tableName === tableName) ?? { ...fallbackPayConfig, tableName }
}

const payOptions = [
  { label: '微信支付', icon: Wallet, img: weixinImg },
  { label: '支付宝支付', icon: CreditCard, img: zhifubaoImg },
  { label: '建设银行', icon: Money, img: jiansheImg },
  { label: '交通银行', icon: CreditCard, img: jiaotongImg },
  { label: '农业银行', icon: Money, img: nongyeImg },
  { label: '中国银行', icon: CreditCard, img: zhongguoImg }
]

onMounted(() => {
  // 兼容：从本地存储读取待支付表与对象
  table.value = localStorage.getItem('paytable') || ''
  try {
    obj.value = JSON.parse(localStorage.getItem('payObject') || '{}')
  } catch (e) {
    obj.value = {}
  }
})

const submitPay = async () => {
  if (!payType.value) {
    ElMessage.error('请选择支付方式')
    return
  }
  if (!table.value || !obj.value || !obj.value.id) {
    ElMessage.error('支付信息不完整')
    return
  }
  try {
    await ElMessageBox.confirm('确定支付?', '提示', { type: 'warning' })
  } catch {
    return
  }
  const config = getPayConfig(table.value)
  const payload = { ...obj.value, [config.statusField]: config.paidValue }
  try {
    const { code, msg } = await http.post(`${table.value}/update`, payload)
    if (code === 0) {
      ElMessage.success('支付成功')
      router.back()
    } else {
      ElMessage.error(msg || '支付失败')
    }
  } catch (e) {
    ElMessage.error('网络错误，请稍后重试')
  }
}
</script>

<style scoped>
</style>
