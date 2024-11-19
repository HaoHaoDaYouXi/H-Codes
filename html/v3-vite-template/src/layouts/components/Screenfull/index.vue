<template>
  <div>
    <!-- 全屏 -->
    <el-tooltip
      v-if="!content"
      effect="dark"
      :content="fullscreenTips"
      placement="bottom"
    >
      <SvgIcon :name="fullscreenSvgName" @click="handleFullscreenClick" />
    </el-tooltip>
    <!-- 内容区 -->
    <el-dropdown v-else :disabled="appStore.isFullscreen">
      <SvgIcon :name="contentLargeSvgName" />
      <template #dropdown>
        <el-dropdown-menu>
          <!-- 内容区放大 -->
          <el-dropdown-item @click="handleContentLargeClick">
            {{ contentLargeTips }}
          </el-dropdown-item>
          <!-- 内容区全屏 -->
          <el-dropdown-item @click="handleContentFullClick">
            内容区全屏
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watchEffect } from "vue"
import { useAppStore } from "@/store/modules/app"
import { ElMessage } from "element-plus"
import screenfull from "screenfull"

interface Props {
  /** 全屏的元素，默认是 html */
  element?: string
  /** 打开全屏提示语 */
  openTips?: string
  /** 关闭全屏提示语 */
  exitTips?: string
  /** 是否只针对内容区 */
  content?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  element: "html",
  openTips: "全屏",
  exitTips: "退出全屏",
  content: false
})

const appStore = useAppStore()

// 全屏
const fullscreenTips = computed(() => {
  return appStore.isFullscreen ? props.exitTips : props.openTips
})
const fullscreenSvgName = computed(() => {
  return appStore.isFullscreen ? "fullscreen-exit" : "fullscreen"
})
const handleFullscreenClick = () => {
  const dom = document.querySelector(props.element) || undefined
  screenfull.isEnabled ? screenfull.toggle(dom) : ElMessage.warning("您的浏览器无法工作")
}
const handleFullscreenChange = () => {
  appStore.isFullscreen = screenfull.isFullscreen
  // 退出全屏时清除所有的 class
  appStore.isFullscreen || (document.body.className = "")
}
watchEffect((onCleanup) => {
  // 挂载组件时自动执行
  screenfull.isEnabled && screenfull.on("change", handleFullscreenChange)
  // 卸载组件时自动执行
  onCleanup(() => {
    screenfull.isEnabled && screenfull.off("change", handleFullscreenChange)
  })
})

// 内容区
const contentLargeTips = computed(() => {
  return appStore.isContentLarge ? "内容区复原" : "内容区放大"
})
const contentLargeSvgName = computed(() => {
  return appStore.isContentLarge ? "fullscreen-exit" : "fullscreen"
})
const handleContentLargeClick = () => {
  appStore.isContentLarge = !appStore.isContentLarge
  // 内容区放大时，将不需要的组件隐藏
  document.body.className = appStore.isContentLarge ? "content-large" : ""
  if (appStore.isContentLarge) {
    appStore.topbarHeight = 0
  } else {
    appStore.topbarHeight = 68 + 7
  }
}
const handleContentFullClick = () => {
  // 取消内容区放大
  appStore.isContentLarge && handleContentLargeClick()
  // 内容区全屏时，将不需要的组件隐藏
  document.body.className = "content-full"
  // 开启全屏
  handleFullscreenClick()
}
//#endregion
</script>

<style lang="scss" scoped>
.svg-icon {
  font-size: 20px;
  &:focus {
    outline: none;
  }
}
</style>
