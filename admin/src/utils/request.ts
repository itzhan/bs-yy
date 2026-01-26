import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Token = authStore.token
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response.config?.responseType === 'blob') {
      return response.data
    }
    const { data } = response
    const headers = response.config?.headers as Record<string, string | undefined> | undefined
    const muteSuccessToast = Boolean(headers && (headers['X-Mute-Success-Toast'] === 'true' || headers['x-mute-success-toast'] === 'true'))
    
    // 检查业务状态码
    if (data.code === 401 || data.code === 403) {
      ElMessage.error('登录已过期，请重新登录')
      const authStore = useAuthStore()
      authStore.logout()
      // 跳转到登录页面
      window.location.href = '/login'
      return Promise.reject(new Error(data.msg || '登录已过期'))
    } else if (data.code === 0) {
      if (!muteSuccessToast && response?.config?.method !== 'get'){
        console.log(response.config.method)
        ElMessage.success('操作成功')
      }
      return data;
    } else {
      // 其他状态码都当作错误处理
      ElMessage.error(data.msg || '服务器内部错误')
      return Promise.reject(new Error(data.msg || '服务器内部错误'))
    }
  },
  (error) => {
    console.error('响应错误:', error)
    if (error.response) {
      const { status, data } = error.response
      switch (status) {
        case 401:
          ElMessage.error('未授权访问')
          break
        case 403:
          ElMessage.error('没有权限访问')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.msg || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络请求失败，请检查网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)
// 导出axios实例
export default service

// 通用请求方法
export const http = {
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config)
  },
  
  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config)
  },
  
  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config)
  },
  
  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config)
  }
}
