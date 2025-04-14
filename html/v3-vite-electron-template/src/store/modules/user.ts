import { computed, reactive, ref } from "vue"
import store from "@/store"
import { defineStore } from "pinia"
import { usePermissionStoreHook } from "./permission"
import { useTagsViewStore } from "./tags-view"
import { useSettingsStore } from "./settings"
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
  /** 设置用户信息 */
  const setUserInfo = (userInfo: UserInfoData) => {
    user_info.userName = userInfo.userName || ""
    user_info.avatar = userInfo.avatar || ""
    user_info.roleId = userInfo.roleId || 0
    user_info.roleName = userInfo.roleName || ""
  }
  /** 重置状态 */
  const resetState = () => {
    token.value = ""
    setUserInfo({ userName: "", avatar: "", roleId: 0, roleName: "" })
  }

  const permissionStore = usePermissionStoreHook()
  const tagsViewStore = useTagsViewStore()
  const settingsStore = useSettingsStore()

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
    setUserInfo({
      userName: "admin",
      avatar: "../../assets/vue.svg",
      roleId: 1,
      roleName: "系统管理员"
    })
  }

  /** 登出 */
  const logout = () => {
    removeToken()
    resetState()
    permissionStore.resetState()
    resetRouter()
    if (settingsStore.cacheTagsView) {
      tagsViewStore.delAllVisitedViews()
      tagsViewStore.delAllCachedViews()
    }
  }

  const roleList = [
    {
      value: 1,
      label: "系统管理员"
    },
    {
      value: 2,
      label: "测试人员1"
    },
    {
      value: 3,
      label: "测试人员2"
    },
    {
      value: 4,
      label: "测试人员3"
    }
  ]
  /** 更改角色 */
  const changeRole = async (roleId: number) => {
    roleList.forEach((r) => {
      if (r.value === roleId) {
        user_info.roleId = r.value
        user_info.roleName = r.label
        return
      }
    })
  }

  return { token, user_info, login, getUserInfo, logout, roleList, changeRole }
})

/** 在 setup 外使用 */
export function useUserStoreHook() {
  return useUserStore(store)
}
