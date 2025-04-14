<template>
  <div
    style="width: 100%; height: 100%; background-color: #f1f4f9"
    v-loading="true"
    element-loading-text="跳转中"
    element-loading-spinner="el-icon-loading"
  />
</template>

<script setup lang="ts">
import { onMounted } from "vue"
import { useRouter } from "vue-router"
import { usePermissionStore } from "@/store/modules/permission"

const router = useRouter()
const permissionStore = usePermissionStore()

onMounted(() => {
  if (
    permissionStore.defaultOpenRoute &&
    permissionStore.defaultOpenRoute.name
  ) {
    router.push({ name: permissionStore.defaultOpenRoute.name })
  } else {
    router.push("/login")
  }
})
</script>
