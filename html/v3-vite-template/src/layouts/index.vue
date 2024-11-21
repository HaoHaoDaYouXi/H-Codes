<template>
  <div :class="classObj" class="app-wrapper">
    <Topbar />
    <div
      v-if="appStore.device === DeviceEnum.Mobile && appStore.sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <Sidebar class="sidebar-container" v-if="!classObj.noSidebar" />
    <div class="main-container">
      <div :class="fixedHeader ? 'fixed-header' : 'unfixed-header'">
        <TagsView
          :exData="{ hideHamburger: classObj.noSidebar }"
          v-if="showTagsView"
        />
        <Navbar />
      </div>
      <AppMain />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watch } from "vue"
import { storeToRefs } from "pinia"
import { useSettingsStore } from "@/store/modules/settings"
import { useAppStore } from "@/store/modules/app"
import { AppMain, Topbar, Sidebar, TagsView, Navbar } from "./components"
import { DeviceEnum } from "@/constants/app-key"

const settingsStore = useSettingsStore()
const appStore = useAppStore()

const { fixedHeader, showTagsView } = storeToRefs(settingsStore)

const classObj = computed(() => {
  return {
    hideSidebar: !appStore.sidebar.opened,
    openSidebar: appStore.sidebar.opened,
    withoutAnimation: appStore.sidebar.withoutAnimation,
    mobile: appStore.device === DeviceEnum.Mobile,
    noSidebar: appStore.sidebar.hide
  }
})
const handleClickOutside = () => {
  appStore.closeSidebar(false)
}
</script>

<style lang="scss" scoped>
@import "@/styles/mixin.scss";
@import "@/styles/variables.module.scss";

.app-wrapper {
  @include relative;
  @include clearfix;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.fixed-header {
  position: fixed;
  left: var(--sidebar-width);
  top: var(--topbar-height);
  right: 0;
  z-index: 2001;
}

.unfixed-header {
  padding-top: var(--topbar-height);
}

.hideSidebar {
  .fixed-header {
    left: 54px;
  }
}

.mobile .fixed-header {
  left: 0;
}

.noSidebar .fixed-header {
  left: 0;
}

.noSidebar .main-container {
  margin-left: 0 !important;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}
</style>
