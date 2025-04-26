import router from "@/router"
import { useUserStoreHook } from "@/store/modules/user"
import { usePermissionStoreHook } from "@/store/modules/permission"
import { ElMessage } from "element-plus"
import { setRouteChange } from "@/hooks/useRouteListener"
import { getToken } from "@/utils/cache/cookies"
import isWhiteList from "@/config/white-list"
import NProgress from "nprogress"
import "nprogress/nprogress.css"

NProgress.configure({ showSpinner: false })

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  const userStore = useUserStoreHook()
  const permissionStore = usePermissionStoreHook()
  const token = getToken()
  // console.log("token: "+token)
  // console.log(to.path)
  const loginUrl = "/login"
  // 如果没有登陆
  if (!token) {
    // 如果在免登录的白名单中，则直接进入
    if (isWhiteList(to)) return next()
    // 其他没有访问权限的页面将被重定向到登录页面
    return next(loginUrl)
  }

  // 如果已经登录，并准备进入 Login 页面，则重定向到主页
  if (to.path === loginUrl) return next({ path: "/" })

  // 用户信息是否存在
  if (!userStore.userInfo.userName) await userStore.getUserInfo()

  // 路由信息是否获取并添加过
  if (permissionStore.booAddRoutes) return next()

  try {
    await permissionStore.getRouterByUser()
    permissionStore.addRouter.forEach((route) => router.addRoute(route))
    permissionStore.booAddRoutes = true
    if (to.path == "/404" && to.redirectedFrom) {
      return next({ path: to.redirectedFrom.fullPath, replace: true })
    } else {
      return next({ ...to, replace: true })
    }
  } catch (err: any) {
    // 过程中发生任何错误，都直接重置 Token，并重定向到登录页面
    userStore.resetState()
    ElMessage.error(err.message || "路由守卫过程发生错误")
    next("/login")
  }
})

router.afterEach((to) => {
  setRouteChange(to)
  NProgress.done()
})
