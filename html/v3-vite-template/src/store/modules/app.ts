import { reactive, ref, watch } from "vue"
import { defineStore } from "pinia"
import { getSidebarStatus, setSidebarStatus } from "@/utils/cache/local-storage"
import { DeviceEnum, SIDEBAR_OPENED, SIDEBAR_CLOSED } from "@/constants/app-key"

interface Sidebar {
  opened: boolean
  withoutAnimation: boolean
  hide: boolean
}

/** 设置侧边栏状态本地缓存 */
function handleSidebarStatus(opened: boolean) {
  opened ? setSidebarStatus(SIDEBAR_OPENED) : setSidebarStatus(SIDEBAR_CLOSED)
}

export const useAppStore = defineStore("app", () => {
  /** 侧边栏状态 */
  const sidebar: Sidebar = reactive({
    opened: getSidebarStatus() !== SIDEBAR_CLOSED,
    withoutAnimation: false,
    hide: false
  })
  /** 设备类型 */
  const device = ref<DeviceEnum>(DeviceEnum.Desktop)
  /** 浏览器窗口缩放操作计数器 */
  const resizeCount = ref(0)
  /** 顶部导航高度 顶部导航栏 68 + tags偏移 7 */
  const topbarHeight = ref(68 + 7)
  /** 是否全屏 */
  const isFullscreen = ref<boolean>(false)
  /** 内容区放大 */
  const isContentLarge = ref<boolean>(false)

  /** 监听侧边栏 opened 状态 */
  watch(
    () => sidebar.opened,
    (opened) => handleSidebarStatus(opened)
  )

  /** 切换侧边栏 */
  const toggleSidebar = (withoutAnimation: boolean) => {
    sidebar.opened = !sidebar.opened
    sidebar.withoutAnimation = withoutAnimation
  }
  /** 打开侧边栏 */
  const openSidebar = (withoutAnimation: boolean) => {
    sidebar.opened = true
    sidebar.withoutAnimation = withoutAnimation
  }
  /** 关闭侧边栏 */
  const closeSidebar = (withoutAnimation: boolean) => {
    sidebar.opened = false
    sidebar.withoutAnimation = withoutAnimation
  }
  /** 切换设备类型 */
  const toggleDevice = (value: DeviceEnum) => {
    device.value = value
  }

  return {
    device,
    sidebar,
    resizeCount,
    topbarHeight,
    isFullscreen,
    isContentLarge,
    toggleSidebar,
    openSidebar,
    closeSidebar,
    toggleDevice
  }
})
