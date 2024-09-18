import { type App } from "vue"
import { loadI18n } from "./i18n"
import { loadElementPlus } from "./element-plus"
import { loadElementPlusIcon } from "./element-plus-icon"
import { loadVxeTable } from "./vxe-table"

export function loadPlugins(app: App) {
  loadI18n(app)
  loadElementPlus(app)
  loadElementPlusIcon(app)
  loadVxeTable(app)
}
