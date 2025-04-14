import { type App } from "vue"
import { createI18n } from "vue-i18n"
import { useLocaleStoreHook } from "@/store/modules/locale"
import zh from "./config/zh_CN"
import en from "./config/en_US"

export const i18nMsgMap: { [key: string]: any } = {
  zh_CN: zh,
  en_US: en
}

export function loadI18n(app: App) {
  // VueI18n
  const i18n = createI18n({
    legacy: false, // 设置为 false，启用 composition API 模式
    locale: useLocaleStoreHook().defaultLocale,
    messages: i18nMsgMap
  })
  app.use(i18n)
}
