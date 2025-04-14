import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { setLocale, getLocale } from "@/utils/cache/local-storage"
import zhCn from "element-plus/es/locale/lang/zh-cn"
import en from "element-plus/es/locale/lang/en"

export const LOCALE_OPTIONS = [
  { label: "中文", value: "zh_CN" },
  { label: "English", value: "en_US" }
]

export const elLocaleMap: { [key: string]: any } = {
  zh_CN: zhCn,
  en_US: en
}

export const useLocaleStore = defineStore("locale", () => {
  const defaultLocale = ref(getLocale() || "zh_CN")

  /** 切换 */
  const changeLocale = (value: string) => {
    setLocale(value)
    defaultLocale.value = value
  }
  return { defaultLocale, changeLocale }
})

export const useLocaleStoreHook = () => {
  return useLocaleStore(store)
}
