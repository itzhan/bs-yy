import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例（读取 .env 中的 VITE_API_BASE_URL）
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' }
})

// 请求拦截器：携带 frontToken
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('frontToken')
    if (token) {
      (config.headers as any).Token = token
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器：统一处理业务码
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    if (data.code === 401 || data.code === 403) {
      ElMessage.error('请先登录')
      // 触发全局事件由 App.vue 打开登录弹窗
      window.dispatchEvent(new CustomEvent('front-auth-open'))
      return Promise.reject(new Error(data.msg || '未登录'))
    }
    return data
  },
  (error) => {
    // 处理HTTP状态码401/403
    if (error?.response?.status === 401 || error?.response?.status === 403) {
        ElMessage.error('请先登录')
        window.dispatchEvent(new CustomEvent('front-auth-open'))
        return Promise.reject(new Error('未登录'))
    }
    ElMessage.error(error?.response?.data?.msg || '请求失败')
    return Promise.reject(error)
  }
)

export default service
export const http = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> { return service.get(url, config) },
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> { return service.post(url, data, config) },
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> { return service.put(url, data, config) },
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> { return service.delete(url, config) },
}
