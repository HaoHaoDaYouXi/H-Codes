import { type App } from "vue"
import "virtual:svg-icons-register"
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import SvgIcon from "@/components/SvgIcon/index.vue"

export function loadIcons(app: App) {
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component("el-icon-" + key, component)
  }
  app.component("SvgIcon", SvgIcon)
}
