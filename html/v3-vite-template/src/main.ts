import { createApp } from 'vue'
import '@/style.css'
import App from '@/App.vue'

// load
import { loadPlugins } from "@/plugins"
import { loadSvg } from "@/icons"
import { loadDirectives } from "@/directives"

const app = createApp(App)

// 加载插件
loadPlugins(app)
// 加载全局 SVG
loadSvg(app)
// 加载自定义指令
loadDirectives(app)

app.mount("#app")
