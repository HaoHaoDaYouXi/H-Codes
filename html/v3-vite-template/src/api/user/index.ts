import { request } from "@/utils/service"
import type * as User from "./types/user"

/** 获取用户详情 */
export function getUserInfoApi() {
  return request<User.UserInfoRes>({
    url: "user/getInfo",
    method: "get"
  })
}
