import { computed, ref } from 'vue'
import { menuConfig as defaultMenuConfig } from '@/config/menu'

interface FrontMenuItem {
  menu: string
  tableName: string
  buttons: string[]
  allButtons?: string[]
  menuJump?: string
  cateRefTable?: string
  cateRefColumn?: string
}

interface FrontMenuGroup {
  menu: string
  child: FrontMenuItem[]
}

interface RoleConfig {
  tableName: string
  roleName: string
  hasFrontLogin?: string
  hasFrontRegister?: string
  frontMenu?: FrontMenuGroup[]
}

let menuConfig: RoleConfig[] = Array.isArray(defaultMenuConfig)
  ? (defaultMenuConfig as unknown as RoleConfig[])
  : []

const authVersion = ref(0)

function bumpAuthVersion() {
  authVersion.value += 1
}

if (typeof window !== 'undefined') {
  window.addEventListener('front-auth-updated', () => bumpAuthVersion())
  window.addEventListener('storage', (event) => {
    if (!event.key) return
    if ([
      'frontSessionTable',
      'frontToken',
      'UserTableName',
      'username',
      'frontUserid',
      'userid'
    ].includes(event.key)) {
      bumpAuthVersion()
    }
  })
}

export function useAuthVersion() {
  return computed(() => authVersion.value)
}

export function notifyAuthChanged() {
  bumpAuthVersion()
}

export function setMenuConfig(config: RoleConfig[]) {
  if (Array.isArray(config)) {
    menuConfig = config
    bumpAuthVersion()
  }
}

function normalizeRoleName(role?: string | null): string {
  if (!role) return ''
  return String(role).replace(/^"|"$/g, '').trim()
}

function resolveCurrentRole(): string {
  if (typeof window === 'undefined') return ''
  const direct = window.localStorage.getItem('frontSessionTable')
    || window.localStorage.getItem('UserTableName')
  if (direct && direct !== 'null' && direct !== 'undefined') {
    return normalizeRoleName(direct)
  }
  const session = window.localStorage.getItem('frontSession')
  if (session) {
    try {
      const parsed = JSON.parse(session)
      if (parsed?.role) return normalizeRoleName(parsed.role)
      if (parsed?.tablename) return normalizeRoleName(parsed.tablename)
    } catch (e) {
      // ignore
    }
  }
  return ''
}

function listCandidateRoles(): RoleConfig[] {
  const configs = Array.isArray(menuConfig) ? menuConfig : []
  const currentRole = resolveCurrentRole()
  if (currentRole) {
    const matched = configs.find(role => normalizeRoleName(role.tableName) === currentRole)
    if (matched) return [matched]
  }
  const frontRoles = configs.filter(role =>
    Array.isArray(role.frontMenu)
    && role.frontMenu.length > 0
    && (role.hasFrontLogin === '是' || role.hasFrontRegister === '是')
  )
  if (frontRoles.length) return frontRoles
  return configs.filter(role => Array.isArray(role.frontMenu) && role.frontMenu.length > 0)
}

function findMenuItems(tableName: string, roles: RoleConfig[]): FrontMenuItem[] {
  const items: FrontMenuItem[] = []
  for (const role of roles) {
    const groups = Array.isArray(role.frontMenu) ? role.frontMenu : []
    for (const group of groups) {
      const children = Array.isArray(group.child) ? group.child : []
      for (const child of children) {
        if (child && child.tableName === tableName) {
          items.push(child)
        }
      }
    }
  }
  return items
}

export function isAuth(tableName: string|undefined, action: string): boolean {
  if (!tableName || !action) return false
  const normalizedAction = action.trim()
  const roles = listCandidateRoles()
  if (!roles.length) {
    return normalizedAction === '查看'
  }
  const items = findMenuItems(tableName, roles)
  if (!items.length) {
    return normalizedAction === '查看'
  }
  return items.some(item => Array.isArray(item.buttons) && item.buttons.includes(normalizedAction))
}

export function getTableButtons(tableName: string): string[] {
  const roles = listCandidateRoles()
  if (!roles.length) return []
  const items = findMenuItems(tableName, roles)
  const set = new Set<string>()
  for (const item of items) {
    if (Array.isArray(item.buttons)) {
      for (const btn of item.buttons) {
        set.add(btn)
      }
    }
  }
  return Array.from(set)
}

export function useAuth(tableName: string, action: string) {
  return computed(() => {
    authVersion.value
    return isAuth(tableName, action)
  })
}

export function useTableButtons(tableName: string) {
  return computed(() => {
    authVersion.value
    return getTableButtons(tableName)
  })
}

export function getCurrentRole(): string {
  return resolveCurrentRole()
}

