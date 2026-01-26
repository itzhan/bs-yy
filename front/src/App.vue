<template>
  <router-view />
    <AIWidget />
  <!-- 全局挂载通用登录/注册弹窗 -->
  <AuthModal />
</template>

<script setup lang="ts">
import AIWidget from '@/components/AIWidget.vue'
import AuthModal from '@/components/AuthModal.vue'
import { onMounted } from 'vue'
import { useAuthModal } from '@/composables/useAuthModal'

const { openLogin } = useAuthModal()

onMounted(() => {
  // 未授权时由 Request 触发该事件，统一打开登录弹窗
  window.addEventListener('front-auth-open', () => openLogin())
})
</script>

<style>
html, body, #app { height: 100%; }
</style>
