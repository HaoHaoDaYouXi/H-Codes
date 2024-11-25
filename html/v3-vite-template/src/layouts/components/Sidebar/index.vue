<template>
  <div :class="{ 'has-logo': showLogo }">
    <Logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <SidebarItem
          v-for="item in routes"
          :key="item.path"
          :item="item"
          :base-path="
            layoutMode === LayoutModeEnum.Left
              ? item.path
              : permissionStore.currentRoutes?.path + '/' + item.path
          "
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue"
import { useRoute } from "vue-router"
import { storeToRefs } from "pinia"
import { useSettingsStore } from "@/store/modules/settings"
import { LayoutModeEnum } from "@/constants/app-key"
import { useAppStore } from "@/store/modules/app"
import { usePermissionStore } from "@/store/modules/permission"
import variables from "@/styles/variables.module.scss"
import Logo from "./Logo.vue"
import SidebarItem from "./SidebarItem.vue"

const route = useRoute()
const settingsStore = useSettingsStore()
const appStore = useAppStore()
const permissionStore = usePermissionStore()

const { showLogo, layoutMode } = storeToRefs(settingsStore)
const { sidebar } = storeToRefs(appStore)

const isCollapse = computed(() => {
  return !sidebar.value.opened
})
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})
const routes = computed(() => {
  if (layoutMode.value !== LayoutModeEnum.Left) {
    return permissionStore.currentRoutes?.children || []
  } else {
    return permissionStore.routes
  }
})
</script>
