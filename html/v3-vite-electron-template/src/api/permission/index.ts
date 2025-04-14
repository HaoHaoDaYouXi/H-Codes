import { request } from "@/utils/service"
import { PermissionRes } from "./types/permission"

/** 获取用户路由 */
export function getRouterByUserApi() {
  return request<PermissionRes>({
    url: "res/getRouterByUser",
    method: "get"
  })
}
