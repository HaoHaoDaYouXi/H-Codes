import { ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { getToken, removeToken, setToken } from "@/utils/cache/cookies"
import { resetRouter } from "@/router"
import { loginApi, getUserInfoApi } from "@/api/login"
import { type LoginReq } from "@/api/login/types/login"

export const useUserStore = defineStore("user", () => {
  const token = ref<string>(getToken() || "")
  const user_name = ref<string>("")

  /** 登录 */
  const login = async ({ user_name, user_password }: LoginReq) => {
    // const { data } = await loginApi({ user_name, user_password })
    const { data } = { data: { token: "testToken" } }
    setToken(data.token)
    token.value = data.token
  }

  /** 获取用户详情 */
  const getUserInfo = async () => {
    // await getUserInfoApi() 调用接口获取用户信息
    user_name.value = "admin"
  }

  /** 登出 */
  const logout = () => {
    removeToken()
    token.value = ""
    resetRouter()
  }

  return { token, user_name, login, getUserInfo, logout }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
  return useUserStore(store)
}
