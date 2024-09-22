import { createApp } from 'vue'
import '@/styles/style.css'
import App from '@/App.vue'
import store from "@/store"
import router from "@/router"

// load
import { loadPlugins } from "@/plugins"
import { loadSvg } from "@/icons"
import { loadDirectives } from "@/directives"

// css
import "uno.css"
import "element-plus/dist/index.css"
import "element-plus/theme-chalk/dark/css-vars.css"
import "vxe-table/lib/style.css"
import "vxe-table-plugin-element/dist/style.css"

const app = createApp(App)

// 加载插件
loadPlugins(app)
// 加载全局 SVG
loadSvg(app)
// 加载自定义指令
loadDirectives(app)

app.use(store).use(router)
router.isReady().then(() => {
    app.mount("#app")
})
