import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import type { RoleConfig } from '@/types/auth'
import { menuConfig as staticMenuConfig } from '@/utils/menu'

// 菜单配置（在登录后设置）
let menuConfig: RoleConfig[] = []

/**
 * 设置菜单配置
 */
export function setMenuConfig(config: RoleConfig[]) {
  menuConfig = config
}

/**
 * 权限检查函数
 * @param tableName 表名
 * @param action 操作名
 * @returns 是否有权限
 */
export function isAuth(tableName: string, action: string): boolean {
  const authStore = useAuthStore()
  
  if (!authStore.currentRole) {
    console.log('No current role')
    return false
  }

  // 查找当前角色的配置
  const currentRoleConfig = menuConfig.find(role => role.tableName === authStore.currentRole)
  if (!currentRoleConfig) {
    console.log('No role config found for:', authStore.currentRole)
    return false
  }

  // 在后台菜单中查找对应表的权限
  if (currentRoleConfig.backMenu) {
    for (const menuGroup of currentRoleConfig.backMenu) {
      if (menuGroup.child) {
        const menuItem = menuGroup.child.find(item => item && item.tableName === tableName)
        if (menuItem) {
          // console.log('Found menu item:', menuItem, 'Has permission:', menuItem.buttons.includes(action))
          return menuItem.buttons.includes(action)
        }
      }
    }
  }
  console.log('No permission found for:', tableName, action)
  return false
}

/**
 * Vue 3 Composition API 权限检查
 */
export function useAuth(tableName: string, action: string) {
  return computed(() => isAuth(tableName, action))
}

/**
 * 获取表的所有权限按钮
 */
export function getTableButtons(tableName: string): string[] {
  const authStore = useAuthStore()
  
  if (!authStore.currentRole) return []
  
  const currentRoleConfig = menuConfig.find(role => role.tableName === authStore.currentRole)
  if (!currentRoleConfig) return []
  
  for (const menuGroup of currentRoleConfig.backMenu) {
    const menuItem = menuGroup.child.find(item => item.tableName === tableName)
    if (menuItem) {
      return menuItem.buttons
    }
  }
  
  return []
}

/**
 * 全局属性注入（兼容Vue2写法）
 */
export function setupGlobalAuth(app: any) {
  app.config.globalProperties.isAuth = isAuth
  app.provide('isAuth', isAuth)
}

/**
 * 实用：列出支持后台登录的角色
 */
export function listBackLoginRoles(): RoleConfig[] {
  // static menu for unauthenticated pages
  return (staticMenuConfig as unknown as RoleConfig[]).filter((r: any) => r.hasBackLogin === '是')
}

/**
 * 实用：列出支持后台注册的角色
 */
export function listBackRegisterRoles(): RoleConfig[] {
  return (staticMenuConfig as unknown as RoleConfig[]).filter((r: any) => r.hasBackRegister === '是')
}