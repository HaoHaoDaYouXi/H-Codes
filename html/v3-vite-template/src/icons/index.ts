import { type App } from "vue"
import "virtual:svg-icons-register"

import SvgIcon from "@/components/SvgIcon/index.vue"

export function loadSvg(app: App) {
  app.component("SvgIcon", SvgIcon)
}
