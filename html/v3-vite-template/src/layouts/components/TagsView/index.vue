<template>
  <div class="box">
    <Hamburger
      :is-active="appStore.sidebar.opened"
      class="hamburger-container"
      :class="props.exData.hideHamburger ? 'hidden' : ''"
      @toggleClick="toggleSideBar"
    />
    <div
      id="tags-view-container"
      class="tags-view-container hidden-sm-and-down"
    >
      <ScrollPane
        ref="scrollPaneRef"
        class="tags-view-wrapper"
        :tag-refs="tagRefs"
      >
        <router-link
          v-for="tag in tagsViewStore.visitedViews"
          ref="tagRefs"
          class="tags-view-item"
          :key="tag.path"
          :class="{ active: isActive(tag) }"
          :to="{ path: tag.path, query: tag.query }"
          @click.middle="!isAffix(tag) && closeSelectedTag(tag)"
          @contextmenu.prevent="openMenu(tag, $event)"
        >
          <span>{{ tag.meta?.title }}</span>
          <span
            v-if="!isAffix(tag)"
            class="el-icon-close"
            @click.prevent.stop="closeSelectedTag(tag)"
          />
        </router-link>
      </ScrollPane>
      <ul
        v-show="visible"
        :style="{ left: left + 'px', top: top + 'px' }"
        class="contextmenu"
      >
        <li @click="refreshSelectedTag(selectedTag)">刷新</li>
        <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
          关闭
        </li>
        <li @click="closeOthersTags">关闭其它</li>
        <li @click="closeAllTags(selectedTag)">关闭所有</li>
      </ul>
    </div>
    <Screenfull
      v-if="settingsStore.showScreenFull"
      :content="true"
      class="screenfull"
    />
  </div>
</template>

<script lang="ts" setup>
import { getCurrentInstance, onMounted, ref, watch } from "vue"
import {
  type RouteLocationNormalizedLoaded,
  type RouteRecordRaw,
  RouterLink,
  useRoute,
  useRouter
} from "vue-router"
import { type TagView, useTagsViewStore } from "@/store/modules/tags-view"
import { useSettingsStore } from "@/store/modules/settings"
import { useAppStore } from "@/store/modules/app"
import { usePermissionStore } from "@/store/modules/permission"
import { useRouteListener } from "@/hooks/useRouteListener"
import ScrollPane from "./ScrollPane.vue"
import Hamburger from "../Hamburger/index.vue"
import Screenfull from "../Screenfull/index.vue"
import path from "path-browserify"

const instance = getCurrentInstance()
const router = useRouter()
const route = useRoute()
const tagsViewStore = useTagsViewStore()
const settingsStore = useSettingsStore()
const appStore = useAppStore()
const permissionStore = usePermissionStore()
const { listenerRouteChange } = useRouteListener()

interface Props {
  exData: any
}

const props = withDefaults(defineProps<Props>(), {
  exData: {
    hideHamburger: false
  }
})

/** 切换侧边栏 */
const toggleSideBar = () => {
  appStore.toggleSidebar(false)
}

/** 标签页组件元素的引用数组 */
const tagRefs = ref<InstanceType<typeof RouterLink>[]>([])

/** 右键菜单的状态 */
const visible = ref(false)
/** 右键菜单的 top 位置 */
const top = ref(0)
/** 右键菜单的 left 位置 */
const left = ref(0)
/** 固定的标签页 */
let affixTags: TagView[] = []
/** 当前正在右键操作的标签页 */
const selectedTag = ref<TagView>({})

/** 判断标签页是否激活 */
const isActive = (tag: TagView) => {
  return tag.path === route.path
}

/** 判断标签页是否固定 */
const isAffix = (tag: TagView) => {
  return tag.meta?.affix
}

/** 筛选出固定标签页 */
const filterAffixTags = (routes: RouteRecordRaw[], basePath = "/") => {
  const tags: TagView[] = []
  routes.forEach((route) => {
    if (isAffix(route)) {
      const tagPath = path.resolve(basePath, route.path)
      tags.push({
        fullPath: tagPath,
        path: tagPath,
        name: route.name,
        meta: { ...route.meta }
      })
    }
    if (route.children) {
      const childTags = filterAffixTags(route.children, route.path)
      tags.push(...childTags)
    }
  })
  return tags
}

/** 初始化标签页 */
const initTags = () => {
  affixTags = filterAffixTags(permissionStore.routes)
  for (const tag of affixTags) {
    // 必须含有 name 属性
    tag.name && tagsViewStore.addVisitedView(tag)
  }
}

/** 添加标签页 */
const addTags = (route: RouteLocationNormalizedLoaded) => {
  if (route.name) {
    tagsViewStore.addVisitedView(route)
    tagsViewStore.addCachedView(route)
  }
}

/** 刷新当前正在右键操作的标签页 */
const refreshSelectedTag = (view: TagView) => {
  tagsViewStore.delCachedView(view)
  router.replace({ path: "/redirect" + view.path, query: view.query })
}

/** 关闭当前正在右键操作的标签页 */
const closeSelectedTag = (view: TagView) => {
  tagsViewStore.delVisitedView(view)
  tagsViewStore.delCachedView(view)
  isActive(view) && toLastView(tagsViewStore.visitedViews, view)
}

/** 关闭其他标签页 */
const closeOthersTags = () => {
  const fullPath = selectedTag.value.fullPath
  if (fullPath !== route.path && fullPath !== undefined) {
    router.push(fullPath)
  }
  tagsViewStore.delOthersVisitedViews(selectedTag.value)
  tagsViewStore.delOthersCachedViews(selectedTag.value)
}

/** 关闭所有标签页 */
const closeAllTags = (view: TagView) => {
  tagsViewStore.delAllVisitedViews()
  tagsViewStore.delAllCachedViews()
  if (affixTags.some((tag) => tag.path === route.path)) return
  toLastView(tagsViewStore.visitedViews, view)
}

/** 跳转到最后一个标签页 */
const toLastView = (visitedViews: TagView[], view: TagView) => {
  const latestView = visitedViews.slice(-1)[0]
  const fullPath = latestView?.fullPath
  if (fullPath !== undefined) {
    router.push(fullPath)
  } else {
    // 如果 TagsView 全部被关闭了，则默认重定向到主页
    if (view.name === "Dashboard") {
      // 重新加载主页
      router.push({ path: "/redirect" + view.path, query: view.query })
    } else {
      router.push("/")
    }
  }
}

/** 打开右键菜单面板 */
const openMenu = (tag: TagView, e: MouseEvent) => {
  const menuMinWidth = 105
  // 当前组件距离浏览器左端的距离
  const offsetLeft = instance!.proxy!.$el.getBoundingClientRect().left
  // 当前组件宽度
  const offsetWidth = instance!.proxy!.$el.offsetWidth
  // 面板的最大左边距
  const maxLeft = offsetWidth - menuMinWidth
  // 面板距离鼠标指针的距离
  const left15 = e.clientX - offsetLeft + 15
  left.value = left15 > maxLeft ? maxLeft : left15
  top.value = e.clientY - appStore.topbarHeight
  // 显示面板
  visible.value = true
  // 更新当前正在右键操作的标签页
  selectedTag.value = tag
}

/** 关闭右键菜单面板 */
const closeMenu = () => {
  visible.value = false
}

watch(visible, (value) => {
  value
    ? document.body.addEventListener("click", closeMenu)
    : document.body.removeEventListener("click", closeMenu)
})

onMounted(() => {
  initTags()
  /** 监听路由变化 */
  listenerRouteChange(async (route) => {
    addTags(route)
  }, true)
})
</script>

<style lang="scss" scoped>
.box {
  width: 100%;
  height: calc(var(--tag-view-height) + var(--tag-view-border-bottom));
  background: #fff;
  border-bottom: var(--tag-view-border-bottom) solid #d8d9e1;
  overflow: hidden;
}

.hamburger-container {
  line-height: var(--tag-view-height);
  height: 100%;
  float: left;
  cursor: pointer;
  transition: background 0.3s;
  -webkit-tap-highlight-color: transparent;

  &:hover {
    background: rgba(0, 0, 0, 0.025);
  }
}

.tags-view-container {
  float: left;
  width: calc(100% - 172px);
  height: var(--tag-view-height);

  .tags-view-wrapper {
    :deep(.el-scrollbar__bar.is-vertical) {
      visibility: hidden;
    }

    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 30px;
      line-height: 30px;
      color: #495060;
      background: #f1f4f9;
      border-radius: 4px 4px 0 0;
      padding: 0 10px;
      font-size: 14px;
      margin-left: 4px;
      margin-top: 5px;
      border: 1px solid #fff;
      border-bottom: none;

      &:last-of-type {
        margin-right: 15px;
      }

      &.active {
        background-color: #fff;
        color: #2f2f2f;
        border-color: #d0d5db;
      }
    }
  }

  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);

    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;

      &:hover {
        background: #eee;
      }
    }
  }
}

.screenfull {
  float: right;
  width: 40px;
  height: var(--tag-view-height);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-sizing: border-box;
}

.tags-view-tools {
  float: right;
  height: var(--tag-view-height);
  box-sizing: border-box;

  li {
    position: relative;
    left: 0;
    top: 0;
    display: inline-block;
    border-left: 1px solid #d8d8d8;
  }

  :deep(.el-button) {
    width: 42px;
    border: none;

    &:active,
    &:hover {
      &.el-button--text {
        color: #2f2f2f;
      }
    }

    &.el-button--text {
      color: #7e828b;
    }
  }
}
</style>
