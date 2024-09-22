import {createWebHashHistory, createWebHistory} from "vue-router";

/** 路由模式 */
export const history =
    import.meta.env.VITE_ROUTER_HISTORY === "hash"
        ? createWebHashHistory(import.meta.env.VITE_PUBLIC_PATH)
        : createWebHistory(import.meta.env.VITE_PUBLIC_PATH)
