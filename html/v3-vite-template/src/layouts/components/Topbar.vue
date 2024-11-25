<template>
  <div class="top-nav flex align-center" ref="topNav">
    <router-link class="top-nav-logo" to="/" ref="topNavLogoRef">
      <img src="/vite.svg" class="logo" alt="Vite logo" />
    </router-link>

    <div class="el-menu-box" ref="elMenuBoxRef">
      <el-menu
        ref="elMenuRef"
        v-if="layoutMode !== LayoutModeEnum.Left"
        :active-text-color="variables.menuActiveText"
        :default-active="activeMenu"
        mode="horizontal"
      >
        <div
          v-for="item in permission_routes"
          :key="item.path"
          class="nav-item"
        >
          <SidebarItemLink :to="resolvePath(item)">
            <el-menu-item v-if="!item.meta?.hidden" :index="item.path">
              <i v-if="item.meta?.icon" :class="item.meta?.icon" />
              <el-icon v-if="item.meta?.elIcon">
                <component :is="item.meta?.elIcon" />
              </el-icon>
              <span>{{ item.meta?.title }}</span>
            </el-menu-item>
          </SidebarItemLink>
        </div>
      </el-menu>
    </div>

    <div class="right-menu" ref="rightMenuRef">
      <div class="flex align-center">
        <el-dropdown
          ref="dropdownRef"
          style="padding: 0"
          :hide-on-click="false"
          trigger="click"
          @visible-change="booRoleList = false"
        >
          <!-- 用户头像 -->
          <div class="avatar-wrapper pr-4">
            <img class="user-avatar" :src="user_info.avatar" alt="" />
            <span>{{ user_info.userName }}</span>
            <i class="el-icon-arrow-down" />
          </div>
          <template v-slot:dropdown>
            <el-dropdown-menu style="min-width: 270px; padding: 0">
              <el-dropdown-item class="top-nav-user-box">
                <p class="top-nav-user-name">{{ user_info.userName }}</p>
                <div class="top-nav-user-roles">
                  <p>{{ user_info.roleName }}</p>
                  <!-- 按钮：角色选择 -->
                  <i
                    class="iconfont icon-jiantou_zuoyouqiehuan"
                    @click="booRoleList = !booRoleList"
                  />
                </div>
              </el-dropdown-item>
              <li class="top-nav-role-list list-style-none" v-if="booRoleList">
                <!-- 可选角色列表 -->
                <el-dropdown-item
                  class="hover-focus-cancel"
                  v-for="item in userStore.roleList"
                  :key="item.value"
                  :disabled="item.value === user_info.roleId"
                >
                  <span class="fs-14" @click="handleRoleSelect(item.value)">
                    {{ item.label }}
                  </span>
                </el-dropdown-item>
              </li>
              <li class="list-style-none" v-if="!booRoleList">
                <el-dropdown-item class="hover-focus-cancel">
                  <div class="top-nav-logout" @click="logout">
                    <span class="fs-16" style="padding-top: 3px">退出</span>
                    <i class="iconfont icon-tuichu" />
                  </div>
                </el-dropdown-item>
              </li>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, ref, watch } from "vue"
import {
  type RouteRecordRaw,
  RouterLink,
  useRoute,
  useRouter
} from "vue-router"
import { storeToRefs } from "pinia"
import { useSettingsStore } from "@/store/modules/settings"
import { useAppStore } from "@/store/modules/app"
import { useUserStore } from "@/store/modules/user"
import { usePermissionStore } from "@/store/modules/permission"
import { LayoutModeEnum } from "@/constants/app-key"
import variables from "@/styles/variables.module.scss"
import { ElMenu } from "element-plus"
import { debounce } from "lodash-es"
import SidebarItemLink from "./Sidebar/SidebarItemLink.vue"
import { isExternal } from "@/utils/validate"
import { useRouteListener } from "@/hooks/useRouteListener"

const route = useRoute()
const router = useRouter()
const { listenerRouteChange } = useRouteListener()
const settingsStore = useSettingsStore()
const appStore = useAppStore()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const { layoutMode } = storeToRefs(settingsStore)
const { resizeCount } = storeToRefs(appStore)

const topNavLogoRef = ref<typeof RouterLink | null>()
const rightMenuRef = ref<HTMLDivElement | null>()

const elMenuBoxRef = ref<HTMLDivElement | null>()
const elMenuRef = ref<typeof ElMenu | null>()

const activeMenu = computed(() => {
  const { path } = route
  initCurrentRoutes()
  // 如果不是首页，高亮一级菜单
  return "/" + path.split("/")[1]
})

const permission_routes = computed(() => {
  return permissionStore.routes.filter((route) => !route.meta?.hidden)
})

const user_info = computed(() => {
  return {
    ...userStore.user_info,
    avatar: new URL(userStore.user_info.avatar, import.meta.url).href
  }
})

const booRoleList = ref<boolean>(false)

const resolvePath = (item: RouteRecordRaw) => {
  // 如果是个完成的url直接返回
  if (isExternal(item.path)) {
    return item.path
  }
  // 如果是首页，就返回重定向路由
  if (item.path === "/") {
    return item.redirect || item.path
  }
  // 如果有子项，默认跳转第一个子项路由
  let path = ""
  /**
   * item 路由子项
   * parent 路由父项
   */
  const getDefaultPath = (item: RouteRecordRaw, parent: RouteRecordRaw) => {
    // 如果path是个外部链接（不建议），直接返回链接，存在个问题：如果是外部链接点击跳转后当前页内容还是上一个路由内容
    if (isExternal(item.path)) {
      path = item.path
      return
    }
    // 第一次需要父项路由拼接，所以只是第一个传parent
    if (parent) {
      path += parent.path + "/" + item.path
    } else {
      path += "/" + item.path
    }
    // 如果还有子项，继续递归
    if (item.children?.length) {
      getDefaultPath(item.children[0], item)
    }
  }
  if (item.children) {
    getDefaultPath(item.children[0], item)
    return path
  }
  return item.path
}

const handleRoleSelect = (roleId: number) => {
  userStore.changeRole(roleId)
}

const logout = () => {
  userStore.logout()
  router.push("/login")
}

onMounted(() => {
  initTopbar()
})

const initTopbar = async () => {
  initCurrentRoutes()
  await nextTick()
  initScroll()
}

const initCurrentRoutes = () => {
  const { path } = route
  let currentRoutes = permission_routes.value.find(
    (item) => item.path === "/" + path.split("/")[1]
  )
  if (!currentRoutes) {
    // 找不到，认定为首页
    currentRoutes = permission_routes.value.find((item) => item.path === "/")
  }
  if (currentRoutes) {
    permissionStore.currentRoutes = currentRoutes
    if (!currentRoutes.children || currentRoutes.children.length === 1) {
      appStore.closeSidebar(true)
    } else {
      appStore.openSidebar(true)
    }
  }
}

const initScroll = () => {
  const topNavLogoWidth = topNavLogoRef.value?.$el.offsetWidth
  const rightMenuWidth = rightMenuRef.value?.offsetWidth
  if (elMenuBoxRef.value) {
    elMenuBoxRef.value.style.width = `calc(100% - ${topNavLogoWidth + rightMenuWidth + 2}px)`
  }
}

const debounceInitScroll = debounce(initScroll, 200)

watch(resizeCount, () => debounceInitScroll)
</script>
