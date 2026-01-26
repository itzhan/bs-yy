import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './stores'
import ElementPlus from 'element-plus'
import * as ElementPlusIcons from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import './style.css'

// 权限系统
import { setupGlobalAuth } from '@/utils/auth'

// 全局组件
import RichEditor from '@/components/RichEditor.vue'
import UploadField from '@/components/UploadField.vue'
import MapCoordinateField from '@/components/MapCoordinateField.vue'
import MapDistribution from '@/components/MapDistribution.vue'
import MusicPlayer from '@/components/MusicPlayer.vue'
import JsonExcel from 'vue-json-excel3'

const app = createApp(App)

// 配置 Element Plus 中文语言包
app.use(ElementPlus, {
  locale: zhCn
})

// 注册全局ElementPlusIcons
for (const [key, component] of Object.entries(ElementPlusIcons)) {
    app.component(key, component)
}

// 注册全局组件
app.component('RichEditor', RichEditor)
app.component('UploadField', UploadField)
app.component('MapCoordinateField', MapCoordinateField)
app.component('MapDistribution', MapDistribution)
app.component('MusicPlayer', MusicPlayer)
app.component('downloadExcel', JsonExcel)

// 配置状态管理
app.use(pinia)

// 配置路由
app.use(router)

// 配置全局权限
setupGlobalAuth(app)

// 全局属性
app.config.globalProperties.$base = {
  url: import.meta.env.VITE_API_BASE_URL || ''
}

app.mount('#app')
