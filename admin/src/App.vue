<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

// 初始化应用
onMounted(() => {
  authStore.init()
})
</script>

<style>
#app {
  width: 100%;
  height: 100vh;
}
</style>