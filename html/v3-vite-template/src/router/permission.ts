import router from "@/router"
import { setRouteChange } from "@/hooks/useRouteListener"
import { getToken } from "@/utils/cache/cookies"
import isWhiteList from "@/config/white-list"
import { useUserStoreHook } from "@/store/modules/user"
import { usePermissionStoreHook } from "@/store/modules/permission"
import NProgress from "nprogress"
import "nprogress/nprogress.css"

NProgress.configure({ showSpinner: false })

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  const token = getToken()
  // console.log("token: "+token)
  // console.log(router.getRoutes())
  // 如果没有登陆
  if (!token) {
    // 如果在免登录的白名单中，则直接进入
    if (isWhiteList(to)) return next()
    // 其他没有访问权限的页面将被重定向到登录页面
    return next("/login")
  }

  const userStore = useUserStoreHook()
  // 用户信息是否存在
  if (!userStore.user_name) {
    await userStore.getUserInfo()
  }

  const permissionStore = usePermissionStoreHook()
  // 路由信息是否获取并添加过
  if (!userStore.booAddRoutes) {
    const routerData = await userStore.getRouterByUser()
    routerData.forEach((route) => router.addRoute(route))
    if (to.path == "/404" && to.redirectedFrom) {
      return next({ path: to.redirectedFrom.fullPath, replace: true })
    } else {
      return next({ ...to, replace: true })
    }
    // return next({ ...to, replace: true })
  }
  return next()
})

router.afterEach((to) => {
  setRouteChange(to)
  NProgress.done()
})
