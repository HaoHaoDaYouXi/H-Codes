export interface UserInfoData {
  userName: string
  avatar: string
  roleId: number
  roleName: string
}

export type UserInfoRes = ApiRes<UserInfoData>
