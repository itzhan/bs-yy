export interface MenuItem {
  menu: string
  menuJump: string
  tableName: string
  buttons: string[]
  allButtons: string[]
}

export interface MenuGroup {
  menu: string
  child: MenuItem[]
}

export interface RoleConfig {
  // 角色标识与可见性
  roleName: string
  tableName: string
  hasBackLogin?: string
  hasBackRegister?: string
  hasFrontLogin?: string
  hasFrontRegister?: string

  // 菜单配置
  backMenu: MenuGroup[]
  frontMenu?: MenuGroup[]
}

export interface LoginResponse {
  code: number
  msg: string
  token?: string
  role?: string
}

export interface UserInfo {
  id: number
  username: string
  role: string
  avatar?: string
  email?: string
  phone?: string
}