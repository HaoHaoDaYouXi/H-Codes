<template>
  <section class="app-main">
    <div class="app-scrollbar">
      <!-- key 采用 route.path 和 route.fullPath 有着不同的效果，大多数时候 path 更通用 -->
      <router-view v-slot="{ Component, route }" :key="key">
        <transition name="fade-transform" mode="out-in">
          <keep-alive :include="tagsViewStore.cachedViews">
            <component
              :is="Component"
              :key="route.path"
              class="app-container-grow"
            />
          </keep-alive>
        </transition>
      </router-view>
      <!-- 页脚 -->
      <Footer v-if="settingsStore.showFooter" />
    </div>
    <!-- 返回顶部 -->
    <el-backtop />
    <!-- 返回顶部（固定 Header 情况下） -->
    <el-backtop target=".app-scrollbar" />
  </section>
</template>

<script lang="ts" setup>
import { useTagsViewStore } from "@/store/modules/tags-view"
import { useRoute } from "vue-router"
import { useSettingsStore } from "@/store/modules/settings"
import Footer from "./Footer/index.vue"
import { computed } from "vue"

const tagsViewStore = useTagsViewStore()
const route = useRoute()
const settingsStore = useSettingsStore()

const key = computed(() => {
  return route.path + Math.random()
})
</script>

<style lang="scss" scoped>
@import "@/styles/variables.module.scss";
@import "@/styles/mixin.scss";
.app-main {
  min-height: calc(100vh - var(--app-main-paddingT));
  width: 100%;
  position: relative;
  overflow: hidden;
}

/* 143 = top-nav + tag-view + navbar 68 + 38 + 37  */
.fixed-header + .app-main {
  padding-top: var(--app-main-paddingT);
  min-height: 100vh;
}

.app-scrollbar {
  flex-grow: 1;
  overflow: auto;
  display: flex;
  flex-direction: column;
  @include scrollBar;
  .app-container-grow {
    flex-grow: 1;
  }
}
</style>
