import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import * as ElementPlusIcons from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import './style.css'
import { setMenuConfig } from '@/utils/auth'
import { menuConfig } from '@/config/menu'
import UploadField from '@/components/UploadField.vue'
import MapCoordinateField from '@/components/MapCoordinateField.vue'
import MapDistribution from '@/components/MapDistribution.vue'
import MusicPlayer from '@/components/MusicPlayer.vue'

const app = createApp(App)

app.use(ElementPlus, { locale: zhCn })

for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component)
}

app.component('UploadField', UploadField)
app.component('MapCoordinateField', MapCoordinateField)
app.component('MapDistribution', MapDistribution)
app.component('MusicPlayer', MusicPlayer)

app.use(router)

setMenuConfig(menuConfig as any)

// 全局属性
app.config.globalProperties.$base = {
  url: import.meta.env.VITE_API_BASE_URL || ''
}

app.mount('#app')

// 如需“换肤”，直接修改 src/style.css 中的 CSS 变量或样式
