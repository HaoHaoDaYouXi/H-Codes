import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { componentsMap, constantRoutes } from "@/router"
import { getRouterByUserApi } from "@/api/permission"
import {
  PermissionRes,
  PermissionData
} from "@/api/permission/types/permission"

export const usePermissionStore = defineStore("permission", () => {
  /** 可访问的路由 */
  const routes = ref<any[]>([])
  /** 是否已经添加路由 */
  const booAddRoutes = ref<boolean>(false)
  /** 添加路由 */
  const addRouter = ref<any[]>([])
  /** 当前使用路由 */
  const currentRoutes = ref(undefined)
  /** 默认打开的路由 */
  const defaultOpenRoute = ref(undefined)
  /** 面包屑列表 */
  const breadcrumbList = ref<any[]>([])
  /** 重置状态 */
  const resetState = () => {
    routes.value = []
    booAddRoutes.value = false
    addRouter.value = []
    currentRoutes.value = undefined
    defaultOpenRoute.value = undefined
    breadcrumbList.value = []
  }

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
            path: "/test",
            component: "Layout",
            // redirect: "/",
            disabled: 0,
            meta: {
              title: "首页",
              icon: "el-icon-s-home",
              hidden: false,
              breadcrumb: true,
              alwaysShow: true
            },
            children: [
              {
                id: "2",
                parentId: "1",
                resType: 2,
                path: "index",
                component: "Index",
                disabled: 0,
                meta: {
                  title: "首页1",
                  icon: "el-icon-s-home",
                  breadcrumb: true,
                  // affix: false,
                  hidden: false
                },
                children: []
              }
            ]
          },
          {
            id: "11",
            parentId: "0",
            resType: 2,
            path: "/table",
            component: "Layout",
            // redirect: "/table/element-plus",
            disabled: 0,
            meta: {
              title: "表格",
              elIcon: "Grid",
              breadcrumb: true,
              alwaysShow: true
            },
            children: [
              {
                id: "22",
                parentId: "11",
                resType: 2,
                path: "element-plus",
                component: "Table",
                disabled: 0,
                meta: {
                  title: "Element Plus",
                  elIcon: "List",
                  breadcrumb: true
                  // ,cachedView: true
                },
                children: []
              }
            ]
          }
        ]
      }
    }
    // 格式化路由对象
    addRouter.value = generateRoutes(data.routerDetails)
    defaultOpenRoute.value = addRouter.value[0].children[0]
    routes.value = constantRoutes.concat(addRouter.value)
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

  return {
    resetState,
    routes,
    booAddRoutes,
    addRouter,
    currentRoutes,
    defaultOpenRoute,
    breadcrumbList,
    getRouterByUser
  }
})

/** 在 setup 外使用 */
export function usePermissionStoreHook() {
  return usePermissionStore(store)
}
