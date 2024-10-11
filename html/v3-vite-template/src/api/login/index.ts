import { request } from "@/utils/service"
import type * as Login from "./types/login"

/** 登录并返回 Token */
export function loginApi(data: Login.LoginReq) {
  return request<Login.LoginRes>({
    url: "user/login",
    method: "post",
    data
  })
}

/** 获取用户详情 */
export function getUserInfoApi() {
  return request<Login.UserInfoRes>({
    url: "user/getInfo",
    method: "get"
  })
}
