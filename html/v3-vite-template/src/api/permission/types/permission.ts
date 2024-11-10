import { RouteMeta } from "vue-router"

export interface PermissionData {
  id: string
  parentId: string
  resType: number
  path: string
  component: string
  disabled: number
  meta: RouteMeta
  children: PermissionData[]
}

export type PermissionRes = ApiRes<{ routerDetails: PermissionData[] }>
