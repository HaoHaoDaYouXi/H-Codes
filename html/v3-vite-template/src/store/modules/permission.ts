import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { type RouteRecordRaw } from "vue-router"
import { constantRoutes } from "@/router"

export const usePermissionStore = defineStore("permission", () => {
  /** 可访问的路由 */
  const routes = ref<RouteRecordRaw[]>([])
  /** 是否已经添加路由 */
  const booAddRoutes = ref<boolean>()
  /** 添加路由 */
  const addRoutes = ref<RouteRecordRaw[]>([])

  /** 获取路由 */
  const getRouterByUser = async () => {
    // await getRouterByUser() 调用接口获取路由信息
    addRoutes.value = [
      {
        path: "/",
        component: () => import("@/views/index.vue"),
        meta: {
          hidden: true
        }
      }
    ]
    booAddRoutes.value = true
    return addRoutes.value
  }

  return { routes, booAddRoutes, addRoutes, getRouterByUser }
})

/** 在 setup 外使用 */
export function usePermissionStoreHook() {
  return usePermissionStore(store)
}
