<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <h2 class="text-white text-center py-4">{{ appTitle }}</h2>
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :router="false"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        
        <!-- 动态菜单 -->
        <template v-for="menuGroup in menuGroups" :key="menuGroup.menu">
          <!-- 如果菜单组只有一个子项，直接显示为一级菜单 -->
          <el-menu-item
            v-if="menuGroup.child && menuGroup.child.length === 1 && isAuth(menuGroup.child[0].tableName, '查看')"
            :index="`/dashboard/modules/${menuGroup.child[0].tableName}`"
          >
            <el-icon><Menu /></el-icon>
            <span>{{ menuGroup.child[0].menu }}</span>
          </el-menu-item>
          
          <!-- 如果菜单组有多个子项，显示为二级菜单 -->
          <el-sub-menu 
            v-else-if="menuGroup.child && menuGroup.child.length > 1 && hasVisibleChildren(menuGroup)"
            :index="menuGroup.menu"
          >
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>{{ menuGroup.menu }}</span>
            </template>
            <template v-for="item in menuGroup.child" :key="item?.tableName || ''">
              <el-menu-item
                v-if="item && item.tableName && isAuth(item.tableName, '查看')"
                :index="`/dashboard/modules/${item?.tableName}`"
              >
                {{ item.menu }}
              </el-menu-item>
            </template>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="flex justify-between items-center h-full px-4">
          <div class="flex items-center">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="$route.meta.title">
                {{ $route.meta.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="flex items-center space-x-4">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link flex items-center cursor-pointer">
                <el-avatar :size="30" :src="baseUrl + userInfo?.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="ml-2">{{ userInfo?.username || '用户' }}</span>
                <el-icon class="ml-1"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                  <el-dropdown-item command="updatePassword">修改密码</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <!-- 主体内容 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  House,
  Menu,
  User,
  ArrowDown
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { isAuth } from '@/utils/auth'

const router = useRouter()
const authStore = useAuthStore()

// 应用标题
const appTitle = import.meta.env.VITE_APP_TITLE || '管理系统'
const baseUrl = import.meta.env.VITE_API_BASE_URL

// 计算属性
const userInfo = computed(() => authStore.userInfo)
const menuGroups = computed(() => {
  // console.log('Auth store menuConfig:', authStore.menuConfig)
  console.log('Current role:', authStore.currentRole)
  
  if (!authStore.menuConfig || !Array.isArray(authStore.menuConfig)) {
    console.log('No menu config or invalid format')
    return []
  }
  
  // 获取当前角色的菜单配置
  const currentRoleConfig = authStore.menuConfig.find(
    config => config && config.tableName === authStore.currentRole
  )
  
  // console.log('Found role config:', currentRoleConfig)
  const rawMenus = currentRoleConfig?.backMenu || []
  
  // 过滤并清理数据，确保每个菜单项和子项都是有效的
  const result = rawMenus.filter(menuGroup => {
    if (!menuGroup || !menuGroup.menu) return false
    
    // 如果有子菜单，创建新的数组而不是修改原始数组
    if (menuGroup.child && Array.isArray(menuGroup.child)) {
      const validChildren = menuGroup.child.filter(item => 
        item && item.tableName && item.menu
      )
      // 创建新对象，避免修改原始数据
      return { ...menuGroup, child: validChildren }
    }
    
    return true
  }).map(menuGroup => {
    // 确保返回的是新对象，避免响应式问题
    if (menuGroup.child && Array.isArray(menuGroup.child)) {
      const validChildren = menuGroup.child.filter(item => 
        item && item.tableName && item.menu
      )
      return { ...menuGroup, child: validChildren }
    }
    return menuGroup
  })
  
  // console.log('Menu groups result:', result)
  return result
})

// 检查菜单组是否有可见的子项
const hasVisibleChildren = (menuGroup: any) => {
  if (!menuGroup || !menuGroup.child || !Array.isArray(menuGroup.child)) {
    return false
  }
  
  // 对于系统管理菜单，直接返回true，跳过权限检查
  if (menuGroup.menu === '系统管理') {
    return true
  }
  
  return menuGroup.child.some((item: any) => {
    if (!item || !item.tableName) return false
    return isAuth(item.tableName, '查看')
  })
}

// 菜单选择处理
const handleMenuSelect = (index: string) => {
  console.log('Menu selected:', index)
  console.log('Current route:', router.currentRoute.value.path)
  
  // 如果路径不匹配，手动导航
  if (router.currentRoute.value.path !== index) {
    router.push(index).catch(err => {
      console.error('Navigation error:', err)
    })
  }
}

// 下拉菜单命令处理
const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/dashboard/profile')
      break
    case 'updatePassword':
      router.push('/dashboard/update-password')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        authStore.logout()
        router.push('/login')
        ElMessage.success('已退出登录')
      } catch {
        // 用户取消操作
      }
      break
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
}

.logo {
  border-bottom: 1px solid #434c5e;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.main-content {
  background-color: #f5f5f5;
  padding: 20px;
}

.el-menu-vertical {
  border-right: none;
}

.el-dropdown-link {
  color: #606266;
  font-size: 14px;
}
</style>