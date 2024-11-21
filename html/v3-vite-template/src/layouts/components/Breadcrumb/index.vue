<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item
        v-for="(item, index) in permissionStore.breadcrumbList"
        :key="item.path"
      >
        <span
          v-if="
            item.redirect === 'noRedirect' ||
            index === permissionStore.breadcrumbList.length - 1
          "
          class="no-redirect"
        >
          {{ item.meta.title }}
        </span>
        <a style="cursor: text" @click.prevent="handleLink(item)" v-else>
          {{ item.meta.title }}
        </a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script lang="ts" setup>
import { useRouteListener } from "@/hooks/useRouteListener"
import { usePermissionStore } from "@/store/modules/permission"
import { RouteRecordNameGeneric } from "vue-router"

const { listenerRouteChange } = useRouteListener()
const permissionStore = usePermissionStore()

const handleLink = (item: RouteRecordNameGeneric) => {}

/** 监听路由变化 */
listenerRouteChange(async (route) => {
  permissionStore.breadcrumbList = route.matched
}, true)
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 38px;
  margin-left: 10px;

  a {
    color: #2f2f2f;

    &:hover,
    &:focus {
      color: #2f2f2f;
    }
  }

  .no-redirect {
    color: #7e828b;
    cursor: text;
  }
}
</style>
