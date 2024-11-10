import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { type RouteRecordRaw } from "vue-router"
import { componentsMap, constantRoutes } from "@/router"
import { getRouterByUserApi } from "@/api/permission"
import { PermissionRes, PermissionData} from "@/api/permission/types/permission"

export const usePermissionStore = defineStore("permission", () => {
  /** 可访问的路由 */
  const routes = ref<any[]>(constantRoutes)
  /** 是否已经添加路由 */
  const booAddRoutes = ref<boolean>()
  /** 添加路由 */
  const addRouter = ref<any[]>([])
  /** 当前使用路由 */
  const currentRoutes = ref()

  /** 获取路由 */
  const getRouterByUser = async () => {
    // const { data } = await getRouterByUserApi() // 调用接口获取路由信息
    const { data } = {
      data: {
        routerDetails: [
          {
            id: "1",
            parentId: "0",
            resType: 2,
            path: "/",
            // component: "Index",
            component: "Layout",
            // redirect: "/",
            disabled: 0,
            meta: {
              title: "首页",
              hidden: false
            },
            children: []
          }
        ]
      }
    }
    // 格式化路由对象
    addRouter.value = generateRoutes(data.routerDetails)
    // data.routerDetails.forEach((route) => {
    //   const tmp: RouteRecordRaw = {
    //     ...route,
    //     component: componentsMap[route.component],
    //     name: route.component === "Layout" ? route.path.replace(/^\//, "") : route.component
    //   }
    //   addRouter.value.push(tmp)
    // })
    routes.value = routes.value.concat(addRouter.value)
  }
  /** 格式化路由 */
  const generateRoutes = (routers: PermissionData[]) => {
    const res: any[] = []
    routers.forEach((route) => {
      const tmp = {
        ...route,
        component: componentsMap[route.component],
        name:
          route.component === "Layout"
            ? route.path.replace(/^\//, "")
            : route.component
      }
      if (route.children) {
        tmp.children = generateRoutes(route.children)
      }
      res.push(tmp)
    })
    return res
  }

  return { routes, booAddRoutes, addRouter, currentRoutes, getRouterByUser }
})

/** 在 setup 外使用 */
export function usePermissionStoreHook() {
  return usePermissionStore(store)
}
