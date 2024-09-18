import { type App } from "vue"
import { createI18n } from 'vue-i18n'
import zh from './config/zh_CN'
import en from './config/en_US'

export const LOCALE_OPTIONS = [
  { label: '中文', value: 'zh_CN' },
  { label: 'English', value: 'en_US' },
];

export function loadI18n(app: App) {
  const defaultLocale = localStorage.getItem('locale') || 'zh_CN';
  // VueI18n
  const i18n = createI18n({
    legacy: false,  // 设置为 false，启用 composition API 模式
    locale: defaultLocale,
    messages: {
      'zh_CN': zh,
      'en_US': en,
    }
  })
  app.use(i18n)
}
