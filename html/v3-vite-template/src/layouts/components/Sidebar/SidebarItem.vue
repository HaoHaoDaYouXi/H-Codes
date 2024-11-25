<template>
  <div v-if="!(props.item.meta && props.item.meta.hidden)">
    <template
      v-if="
        !alwaysShowRootMenu &&
        hasOneShowingChild &&
        (!hasOneShowingChild.children ||
          hasOneShowingChild.children?.length == 0)
      "
    >
      <SidebarItemLink
        v-if="hasOneShowingChild.meta"
        :to="resolvePath(hasOneShowingChild.path)"
        :key="resolvePath(hasOneShowingChild.path)"
      >
        <el-menu-item
          :index="resolvePath(hasOneShowingChild.path)"
          :class="{ 'sub-menu-title-noDropdown': !isNest }"
        >
          <template v-if="hasOneShowingChild.meta?.title">
            <template v-if="hasOneShowingChild.meta?.icon">
              <i
                v-if="iIcon(hasOneShowingChild.meta?.icon) != ''"
                :class="iIcon(hasOneShowingChild.meta?.icon)"
              />
              <SvgIcon
                v-else
                :name="hasOneShowingChild.meta?.icon"
                class="el-icon sub-el-icon"
              />
            </template>
            <component
              v-else-if="hasOneShowingChild.meta?.elIcon"
              :is="hasOneShowingChild.meta?.elIcon"
              class="el-icon sub-el-icon"
            />
            <i v-else class="round" />
            <span>{{ hasOneShowingChild.meta?.title }}</span>
          </template>
        </el-menu-item>
      </SidebarItemLink>
    </template>
    <el-sub-menu
      v-else
      ref="subMenu"
      :index="resolvePath(props.item.path)"
      :key="resolvePath(props.item.path)"
      popper-append-to-body
    >
      <template v-if="props.item.meta?.title" #title>
        <template v-if="props.item.meta?.icon">
          <i
            v-if="iIcon(props.item.meta?.icon) != ''"
            :class="iIcon(props.item.meta?.icon)"
          />
          <SvgIcon
            v-else
            :name="props.item.meta?.icon"
            class="el-icon sub-el-icon"
          />
        </template>
        <component
          v-if="props.item.meta?.elIcon"
          :is="props.item.meta?.elIcon"
          class="el-icon sub-el-icon"
        />
        <span>{{ props.item.meta?.title }}</span>
      </template>
      <template v-if="props.item.children && props.item.children?.length != 0">
        <SidebarItem
          v-for="child in props.item.children"
          :key="child.path"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="nest-menu"
        />
      </template>
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue"
import { type RouteRecordRaw } from "vue-router"
import SidebarItemLink from "./SidebarItemLink.vue"
import { isExternal } from "@/utils/validate"
import path from "path-browserify"

interface Props {
  item: RouteRecordRaw
  isNest?: boolean
  basePath?: string
}

const props = withDefaults(defineProps<Props>(), {
  isNest: false,
  basePath: ""
})

/** 是否始终显示根菜单 */
const alwaysShowRootMenu = computed(() => props.item.meta?.alwaysShow)

/** 显示的子菜单 */
const showingChildren = computed(() => {
  return props.item.children?.filter((child) => !child.meta?.hidden) ?? []
})

/** 显示的子菜单数量 */
const showingChildNumber = computed(() => {
  return showingChildren.value.length
})

/** 唯一的子菜单项 */
const hasOneShowingChild = computed(() => {
  const number = showingChildNumber.value
  switch (true) {
    case number > 1:
      return false
    case number === 1:
      return showingChildren.value[0]
    default:
      return { ...props.item, path: "" }
  }
})

const iIcon = (icon: string) => {
  if (icon.includes("el-icon")) {
    return icon + " el-icon sub-el-icon mr-8"
  } else if (icon.includes("iconfont")) {
    return icon + " el-icon sub-el-icon mr-5 text-18"
  } else {
    return ""
  }
}

/** 解析路径 */
const resolvePath = (routePath: string) => {
  switch (true) {
    case isExternal(routePath):
      return routePath
    case isExternal(props.basePath):
      return props.basePath
    default:
      return path.resolve(props.basePath, routePath)
  }
}
</script>

<style scoped>
.round {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  border: 1px solid #7e828b;
  margin-right: 14px;
  margin-left: -12px;
}
.sub-el-icon {
  width: 24px;
  display: inline-block;
  text-align: center;
  color: currentColor;
}
.mr-8 {
  margin-right: 8px;
}
.mr-5 {
  margin-right: 5px;
}
.text-18 {
  font-size: 18px;
}
</style>
