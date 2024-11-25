import { LayoutModeEnum } from "@/constants/app-key"
import { getConfigLayout } from "@/utils/cache/local-storage"

/** 项目配置类型 */
export interface LayoutSettings {
  /** 是否显示 Settings Panel */
  showSettings: boolean
  /** 布局模式 */
  layoutMode: LayoutModeEnum
  /** 是否显示 Logo */
  showLogo: boolean
  /** 是否固定 Header */
  fixedHeader: boolean
  /** 是否显示标签栏 */
  showTagsView: boolean
  /** 是否缓存标签栏 */
  cacheTagsView: boolean
  /** 是否显示全屏按钮 */
  showScreenFull: boolean
  /** 是否显示页脚 Footer */
  showFooter: boolean
}

/** 默认配置 */
const defaultSettings: LayoutSettings = {
  showSettings: true,
  layoutMode: LayoutModeEnum.LeftTop,
  showLogo: false,
  fixedHeader: true,
  showTagsView: true,
  cacheTagsView: false,
  showScreenFull: true,
  showFooter: true
}

/** 项目配置 */
export const layoutSettings: LayoutSettings = {
  ...defaultSettings,
  ...getConfigLayout()
}
