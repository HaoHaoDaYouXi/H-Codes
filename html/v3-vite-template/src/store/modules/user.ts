import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { getToken, removeToken, setToken } from "@/utils/cache/cookies"

export const useUserStore = defineStore("user", () => {
    const token = ref<string>(getToken() || "")
    const username = ref<string>("")
    const booAddRoutes = ref<boolean>()

    /** 登录 */
    const login = async (username:string, password:string) => {
        // await login({ username, password }) 调用接口登录
        const { data } = {data:{token:"testToken"}}
        setToken(data.token)
        token.value = data.token
    }

    /** 获取用户详情 */
    const getUserInfo = async () => {
        // await getUserInfo() 调用接口获取用户信息
        username.value = "admin"
    }

    /** 获取路由 */
    const getRouterByUser = async () => {
        // await getRouterByUser() 调用接口获取路由信息
        booAddRoutes.value = true
        return [
            {
                path: "/",
                component: () => import('@/views/index.vue'),
                meta: {
                    hidden: true
                }
            }
        ];
    }

    return { token, username, booAddRoutes, login, getUserInfo, getRouterByUser }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
    return useUserStore(store)
}
