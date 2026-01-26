import { defineStore } from 'pinia'
import { ref } from 'vue'
import { setMenuConfig } from '@/utils/auth'
import { menuConfig as localMenuConfig } from '@/utils/menu'
import { http } from '@/utils/request'
import { userInfoMapping } from '@/types/common'
import type { UserInfo, RoleConfig } from '@/types/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const currentRole = ref<string>('')
  const menuConfig = ref<RoleConfig[]>([])

  /**
   * 登录
   */
  async function login(username: string, password: string, roleName?: string): Promise<boolean> {
    try {
      // 使用FormData因为后端接受form参数
      const formData = new FormData()
      formData.append('username', username)
      formData.append('password', password)
      
      // 根据角色名查找对应的表名
      const roleConfig = localMenuConfig.find(config => config.roleName === roleName)
      const tableName = roleConfig?.tableName || 'admin'
      
      // 根据角色调用对应的登录接口
      const loginUrl = `${import.meta.env.VITE_API_BASE_URL}/${tableName}/login`
      
      const response = await fetch(loginUrl, {
        method: 'POST',
        body: formData,
        mode: 'cors',
        credentials: 'include'
      }).then(res => res.json())
      
      if (response.code === 0 && response.token) {
        token.value = response.token
        currentRole.value = tableName
        
        // 保存到localStorage
        localStorage.setItem('token', response.token)
        localStorage.setItem('role', roleName || '管理员')
        localStorage.setItem('sessionTable', tableName)
        localStorage.setItem('adminName', username)
        localStorage.setItem('currentRole', tableName)

        
        // 获取用户信息和菜单配置
        await getUserInfo()
        await getMenuConfig()
        
        return true
      } else {
        throw new Error(response.msg || '登录失败')
      }
    } catch (error) {
      console.error('登录失败:', error)
      return false
    }
  }

  /**
   * 注册
   */
  async function register(
    registerData: {
      username: string
      password: string
      [key: string]: any
    },
    roleName: string
  ): Promise<{ success: boolean; message: string }> {
    try {
      const roleConfig = localMenuConfig.find(cfg => cfg.roleName === roleName)
      if (!roleConfig) {
        return { success: false, message: '未找到对应的注册角色配置' }
      }
      if (roleConfig.hasBackRegister !== '是') {
        return { success: false, message: '该角色未开启后台注册' }
      }

      const tableName = roleConfig.tableName
      const registerUrl = `${tableName}/register`

      // 使用角色映射，组装用户名/密码字段（JSON）
      const roleMapping = userInfoMapping[tableName as keyof typeof userInfoMapping]
      const payload: Record<string, any> = {}
      if (roleMapping) {
        payload[roleMapping.usernameField] = registerData.username
        payload[roleMapping.passwordField] = registerData.password
      } else {
        return { success: false, message: '该角色未开启后台注册' }
      }

      const response = await http.post(registerUrl, payload)
      
      if (response.code === 0) {
        return { success: true, message: '注册成功' }
      } else {
        return { success: false, message: response.msg || '注册失败' }
      }
    } catch (error) {
      console.error('注册失败:', error)
      return { success: false, message: '网络错误，请重试' }
    }
  }

  /**
   * 修改密码
   */
  async function updatePassword(passwordData: string | { oldPassword?: string; newPassword: string }): Promise<{ success: boolean; message: string }> {
    try {
      // 提取新旧密码
      const newPassword = typeof passwordData === 'string' ? passwordData : passwordData.newPassword
      const oldPassword = typeof passwordData === 'object' ? passwordData.oldPassword : undefined
      
      // 先获取当前用户信息
      const sessionResponse = await http.get(`${currentRole.value}/session`)
      const currentUser = sessionResponse.data
      
      // 根据角色映射获取密码字段
      const roleMapping = userInfoMapping[currentRole.value as keyof typeof userInfoMapping]
      
      // 验证旧密码（如果提供了旧密码）
      if (oldPassword) {
        let currentPassword
        if (roleMapping) {
          currentPassword = currentUser[roleMapping.passwordField]
        } else {
          // 降级方案：尝试通用密码字段
          currentPassword = currentUser.password || currentUser.mima
        }
        
        if (oldPassword !== currentPassword) {
          return { success: false, message: '原密码错误' }
        }
      }
      
      // 更新密码
      const updatedUser = { ...currentUser }
      
      if (roleMapping) {
        // 使用映射的密码字段
        updatedUser[roleMapping.passwordField] = newPassword
      } else {
        // 降级方案：兼容通用密码字段
        updatedUser.password = newPassword
        updatedUser.mima = newPassword
      }
      
      const formData = new FormData()
      Object.keys(updatedUser).forEach(key => {
        if (updatedUser[key] !== null && updatedUser[key] !== undefined) {
          // 确保值是字符串
          const value = typeof updatedUser[key] === 'object' 
            ? JSON.stringify(updatedUser[key]) 
            : String(updatedUser[key])
          formData.append(key, value)
        }
      })
      
      await http.post(`${currentRole.value}/update`, formData)
      
      return { success: true, message: '密码修改成功' }
    } catch (error) {
      console.error('修改密码失败:', error)
      return { success: false, message: '网络错误，请重试' }
    }
  }

  /**
   * 登出
   */
  function logout() {
    token.value = ''
    userInfo.value = null
    currentRole.value = ''
    menuConfig.value = []
    
    // 清除localStorage
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('sessionTable')
    localStorage.removeItem('adminName')
    localStorage.removeItem('currentRole')
    
    // 清除菜单配置
    setMenuConfig([])
  }

  /**
   * 获取用户信息
   */
  async function getUserInfo() {
    try {
      const result = await getUserProfile()
      if (result.success && result.data) {
        console.log(result)
        // 统一使用 image 字段作为头像
        // 根据角色映射获取用户名字段
        const findUsername = () => {
          const data = result.data
          const roleMapping = userInfoMapping[currentRole.value as keyof typeof userInfoMapping]
          
          if (roleMapping && data[roleMapping.usernameField]) {
            return data[roleMapping.usernameField]
          }
          
          // 降级方案：自动查找包含 account 的字段
          if (data.username) return data.username
          
          for (const key in data) {
            if (key.toLowerCase().includes('account') && data[key]) {
              return data[key]
            }
          }
          
          return '用户'
        }
        
        userInfo.value = {
          id: result.data.id,
          username: findUsername(),
          avatar: result.data.image || '',
          role: currentRole.value
        }
        console.log('Set user info:', userInfo.value)
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }

  /**
   * 获取用户详细信息（用于个人信息页面）
   */
  async function getUserProfile(): Promise<{ success: boolean; message: string; data?: any }> {
    try {
      // 检查token是否存在
      if (!token.value) {
        return { success: false, message: '请先登录' }
      }
      
      // 检查当前角色是否存在
      if (!currentRole.value) {
        return { success: false, message: '用户角色信息缺失' }
      }
      
      const response = await http.get(`${currentRole.value}/session`)
      
      return { success: true, message: '获取成功', data: response.data }
    } catch (error: any) {
      console.error('获取个人信息失败:', error)
      
      // 检查是否是认证相关错误
      if (error.response?.status === 401 || error.message?.includes('登录')) {
        logout() // 清除无效的登录状态
        return { success: false, message: '登录状态已过期，请重新登录' }
      }
      
      return { success: false, message: error.message || '网络错误，请重试' }
    }
  }

  /**
   * 更新用户个人信息
   */
  async function updateUserProfile(profileData: any): Promise<{ success: boolean; message: string }> {
    try {
      // 处理图片路径
      const updatedData = { ...profileData }
      if (updatedData.image && typeof updatedData.image === 'string') {
        updatedData.image = updatedData.image.replace(new RegExp(import.meta.env.VITE_API_BASE_URL, "g"), "")
      }
      
      const formData = new FormData()
      Object.keys(updatedData).forEach(key => {
        if (updatedData[key] !== null && updatedData[key] !== undefined) {
          // 确保值是字符串
          const value = typeof updatedData[key] === 'object' 
            ? JSON.stringify(updatedData[key]) 
            : String(updatedData[key])
          formData.append(key, value)
        }
      })
      
      await http.post(`${currentRole.value}/update`, formData)
      
      // 如果是管理员更新头像，需要更新本地存储
      if (currentRole.value === 'users' && updatedData.image) {
        localStorage.setItem('headportrait', updatedData.image)
      }
      
      return { success: true, message: '修改信息成功' }
    } catch (error) {
      console.error('更新个人信息失败:', error)
      return { success: false, message: '网络错误，请重试' }
    }
  }

  /**
   * 获取菜单配置
   */
  async function getMenuConfig() {
    try {
      // 使用静态导入的本地菜单配置，后续可以改为从API获取
      console.log('Loaded menu config from file:', localMenuConfig)
      menuConfig.value = localMenuConfig
      setMenuConfig(localMenuConfig)
      console.log('Set menu config in store:', menuConfig.value)
    } catch (error) {
      console.error('获取菜单配置失败:', error)
    }
  }

  /**
   * 初始化（从 localStorage 恢复状态）
   */
  function init() {
    const savedToken = localStorage.getItem('token')
    const savedRole = localStorage.getItem('currentRole')
    
    console.log('Initializing auth store, savedToken:', savedToken, 'savedRole:', savedRole)
    
    if (savedToken) {
      token.value = savedToken
      currentRole.value = savedRole || 'users'
      
      // 重新获取用户信息和菜单配置
      getUserInfo()
      getMenuConfig()
    } else {
      // 即使没有token，也要加载菜单配置
      getMenuConfig()
    }
  }

  return {
    token,
    userInfo,
    currentRole,
    menuConfig,
    login,
    register,
    updatePassword,
    logout,
    getUserInfo,
    getUserProfile,
    updateUserProfile,
    getMenuConfig,
    init
  }
})