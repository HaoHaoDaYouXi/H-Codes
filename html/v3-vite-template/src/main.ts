import { createApp } from 'vue'
import '@/style.css'
import App from '@/App.vue'

// load
import { loadDirectives } from "@/directives"
import { loadSvg } from "@/icons"

const app = createApp(App)

// 加载自定义指令
loadDirectives(app)
// 加载全局 SVG
loadSvg(app)

app.mount("#app")
