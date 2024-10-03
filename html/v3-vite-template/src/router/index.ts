import { type RouteRecordRaw, createRouter,
    createWebHashHistory,createWebHistory } from 'vue-router'
import { history } from "./util"

/**
 * 常驻路由
 */
export const constantRoutes: RouteRecordRaw[] = [
    {
        path: '/',
        component: () => import('@/views/index.vue'),
        meta: {
            hidden: true
        }
    },
    {
        path: '/login',
        component: () => import('@/views/login/index.vue'),
        meta: {
            hidden: true
        }
    }
]

export const dynamicRoutes: RouteRecordRaw[] = [

]


const router = createRouter({
    history,
    routes: constantRoutes
})

export default router
