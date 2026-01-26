<template>
  <div class="public-container">
    <!-- 顶部标题栏 -->
    <div class="topbar-container">
      <div class="topbar__inner">
        <div class="brand" @click="go('/index/home')">健身房运营管理系统</div>
        <div class="account">
          <template v-if="!token">
            <button class="account-login-btn" @click="openLogin">登录</button>
          </template>
          <el-dropdown v-else>
            <span class="account-username">
              {{username}}<el-icon style="margin-left:4px"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="account-dropdown">
                <el-dropdown-item class="account-dropdown-item" @click.native="go('/index/center')">个人中心</el-dropdown-item>
                <el-dropdown-item class="account-dropdown-item" divided @click.native="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 导航栏 -->
    <div class="menu-container">
      <div class="menu-list">
        <div class="menu-item menu-home" :class="activeMenu=='/index/home'?'menu-active':''" @click="go('/index/home')">
          <div class="title">
            <div class="text">首页</div>
          </div>
        </div>
        <div class="menu-item"
             v-for="(item,index) in menuList" :key="index"
             :class="activeMenu==item.url?'menu-active':''"
             @mouseenter="onMenuEnter(index, $event)"
             @mouseleave="onMenuLeave"
             @click="go(item.url)">
          <div class="title">
            <div class="text">{{item.name}}</div>
          </div>
        </div>
        <div class="menu-item menu-user" :class="activeMenu=='/index/center'?'menu-active':''" @click="go('/index/center')" v-if="token && notAdmin">
          <div class="title">
            <div class="text">个人中心</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 轮播图 -->
    <div class="banner-container" v-if="carouselList.length">
      <el-carousel class="banner-main" :height="String(bannerHeight) + 'px'" indicator-position="outside" :interval="4000" arrow="always">
        <el-carousel-item class="banner-img-item" v-for="item in carouselList" :key="item.id">
          <img style="width: 100%;height: 100%" class="banner-img" :src="resolveImage(item.url)"/>
        </el-carousel-item>
      </el-carousel>
    </div>

    <teleport to="body">
      <transition name="el-zoom-in-top">
        <div
          v-if="currCateList.length > 0"
          class="menu-child-list"
          :style="{ position: 'fixed', left: dropdownPos.left + 'px', top: dropdownPos.top + 'px' }"
          @mouseenter="onChildEnter"
          @mouseleave="onChildLeave"
        >
          <div class="menu-child-item" v-for="(c,ci) in currCateList" :key="ci" @click.stop="cateClick(currItemUrl,c)">{{c}}</div>
        </div>
      </transition>
    </teleport>

    <router-view />
  </div>
  
</template>

<script setup lang="ts">
import { ref, onMounted, computed, onBeforeUnmount, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { menuConfig } from '@/config/menu'
import { useAuthModal } from '@/composables/useAuthModal'
import { http } from '@/utils/request'
import { isAuth, useAuthVersion, getCurrentRole } from '@/utils/auth'

const token = ref(localStorage.getItem('frontToken')||'')
const username = ref(localStorage.getItem('username')||'')
const notAdmin = ref(getCurrentRole() !== 'admin')
// 弹窗触发函数
const { openLogin } = useAuthModal()

type NavItem = { name: string; url: string; cateRefTable?: string; cateRefColumn?: string; cateList?: string[]; hasCate?: boolean }
const menuList = ref<NavItem[]>([])
const router = useRouter()
const route = useRoute()
const apiBase = import.meta.env.VITE_API_BASE_URL || ''
const cartCount = ref(0)

// 轮播图
const carouselList = ref<any[]>([])
const showCateIndex = ref(-1)
const dropdownPos = ref({ left: 0, top: 0 })
const overChild = ref(false)
let hideTimer: any = null
const bannerHeight = Number(getComputedStyle(document.documentElement).getPropertyValue('--front-banner-height').replace('px','')) || 420
const currItem = computed<NavItem | undefined>(() => menuList.value[showCateIndex.value])
const currCateList = computed<string[]>(() => currItem.value?.cateList ?? [])
const currItemUrl = computed<string>(() => currItem.value?.url ?? '')
const authVersion = useAuthVersion()

function buildNav() {
  // 从 menu.ts 的 frontMenu 聚合前台导航
  const cfg = (menuConfig as any[])
  const seen = new Set<string>()
  const result: NavItem[] = []
  for (const role of cfg) {
    const groups = role?.frontMenu || []
    for (const group of groups) {
      if (!group?.child || !group.child.length) continue
      for (const child of group.child) {
        const table = child?.tableName
        const name = child?.menu
        if (!table || ['admin','newstype','config'].includes(table)) continue
        if (child?.hidden === true || child?.hidden === 'true') continue
        if (!isAuth(table, '查看')) continue
        if (!seen.has(table)) {
          seen.add(table)
          const nav: NavItem = { name: name || table, url: `/index/${table}` }
          if (child?.cateRefTable && child?.cateRefColumn) {
            nav.cateRefTable = child.cateRefTable
            nav.cateRefColumn = child.cateRefColumn
          }
          result.push(nav)
        }
      }
    }
  }
  menuList.value = result
  fetchCateForMenu()
}

function resolveImage(val: string) {
  if (!val) return ''
  return /^https?:\/\//.test(val) ? val : apiBase + val
}

async function fetchCarousel() {
  try {
    const res: any = await http.get('config/list', { params: { page: 1, limit: 5 } })
    if (res?.code === 0) {
      carouselList.value = res.data?.list || []
    }
  } catch (e) {
    // ignore
  }
}

async function fetchCateForMenu() {
  const tasks: Promise<void>[] = []
  for (const item of menuList.value) {
    if (item.cateRefTable && item.cateRefColumn) {
      tasks.push(
          http.get(`option/${item.cateRefTable}/${item.cateRefColumn}`)
          .then((res: any) => {
            const list = res?.data
            const arr = Array.isArray(list) ? list : []
            item.cateList = arr
            item.hasCate = arr.length > 0
          })
          .catch(() => { /* ignore */ })
      )
    }
  }
  if (tasks.length) await Promise.allSettled(tasks)
}

function cateClick(url: string, cate: string) {
  router.push({ path: url, query: { homeFenlei: cate } })
}

function go(url: string) { router.push(url) }

function calcDropdownPosFromEl(el?: HTMLElement | null) {
  if (!el) return
  const rect = el.getBoundingClientRect()
  let left = rect.left
  const top = rect.bottom + 6
  // 简单防溢出处理（可按需增强）
  const maxLeft = window.innerWidth - 200
  if (left > maxLeft) left = Math.max(8, maxLeft)
  dropdownPos.value = { left, top }
}

function onMenuEnter(index: number, evt: MouseEvent) {
  showCateIndex.value = index
  overChild.value = false
  const el = (evt.currentTarget as HTMLElement) || (evt.target as HTMLElement)
  calcDropdownPosFromEl(el)
  if (hideTimer) { clearTimeout(hideTimer); hideTimer = null }
}

function onMenuLeave() {
  if (hideTimer) clearTimeout(hideTimer)
  hideTimer = setTimeout(() => {
    if (!overChild.value) {
      showCateIndex.value = -1
    }
  }, 120)
}

function onChildEnter() {
  overChild.value = true
  if (hideTimer) { clearTimeout(hideTimer); hideTimer = null }
}

function onChildLeave() {
  overChild.value = false
  showCateIndex.value = -1
}

function onWindowUpdate() {
  if (showCateIndex.value < 0) return
  // 通过当前激活项重新计算位置
  const menuItems = document.querySelectorAll('.menu-item') as NodeListOf<HTMLElement>
  const el = menuItems[showCateIndex.value]
  if (el) calcDropdownPosFromEl(el)
}

function logout() {
  localStorage.clear()
  token.value = ''
  username.value = ''
  notAdmin.value = true
  window.dispatchEvent(new CustomEvent('front-auth-updated'))
  window.location.href = '/index/home'
}

function syncAuth() {
  token.value = localStorage.getItem('frontToken')||''
  username.value = localStorage.getItem('username')||''
  notAdmin.value = getCurrentRole() !== 'admin'
  buildNav()
}

onMounted(() => {
  buildNav()
  window.addEventListener('front-auth-updated', syncAuth)
  fetchCarousel()
  window.addEventListener('scroll', onWindowUpdate, true)
  window.addEventListener('resize', onWindowUpdate)
  const listEl = document.querySelector('.menu-list') as HTMLElement | null
  if (listEl) listEl.addEventListener('scroll', onWindowUpdate)
})

// 当前激活菜单（用于高亮）
const activeMenu = computed(() => (route.meta?.activeMenu as string) || route.path)

watch(authVersion, () => {
  syncAuth()
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', onWindowUpdate, true)
  window.removeEventListener('resize', onWindowUpdate)
  const listEl = document.querySelector('.menu-list') as HTMLElement | null
  if (listEl) listEl.removeEventListener('scroll', onWindowUpdate)
})

</script>

<style>

</style>
