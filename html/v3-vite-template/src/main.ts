import { createApp } from "vue"
import App from "@/App.vue"
import store from "@/store"
import router from "@/router"
import "@/router/permission"

// load
import { loadPlugins } from "@/plugins"
import { loadIcons } from "@/icons"
import { loadDirectives } from "@/directives"
import commonFormRules from "@/utils/rules"

// css
import "uno.css"
import "normalize.css"
import "element-plus/dist/index.css"
import "element-plus/theme-chalk/dark/css-vars.css"
import "vxe-table/lib/style.css"
import "vxe-table-plugin-element/dist/style.css"

import "@/styles/index.scss"

const app = createApp(App)

app.config.globalProperties.commonFormRules = commonFormRules

// 加载插件
loadPlugins(app)
// 加载全局 icon（el-icon、svgIcon）
loadIcons(app)
// 加载自定义指令
loadDirectives(app)

app.use(store).use(router)
router.isReady().then(() => {
  app.mount("#app")
})
