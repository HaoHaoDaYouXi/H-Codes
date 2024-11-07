import { reactive, ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { getToken, removeToken, setToken } from "@/utils/cache/cookies"
import { resetRouter } from "@/router"
import { loginApi } from "@/api/login"
import { type LoginReq } from "@/api/login/types/login"
import { getUserInfoApi } from "@/api/user"
import { type UserInfoData } from "@/api/user/types/user"

export const useUserStore = defineStore("user", () => {
  const token = ref<string>(getToken() || "")
  const user_info: UserInfoData = reactive({
    userName: "",
    avatar: "",
    roleId: 0,
    roleName: ""
  })

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
    user_info.userName = "admin"
    user_info.avatar = "../../assets/vue.svg"
    user_info.roleId = 1
    user_info.roleName = "系统管理员"
  }

  /** 登出 */
  const logout = () => {
    removeToken()
    token.value = ""
    resetRouter()
  }

  return { token, user_info, login, getUserInfo, logout }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
  return useUserStore(store)
}
