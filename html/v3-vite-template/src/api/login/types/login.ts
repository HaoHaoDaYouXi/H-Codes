export interface LoginReq {
  /** 账号 */
  username: "admin"
  /** 密码 */
  password: "123456"
}

export type LoginRes = ApiRes<{ token: string }>

export type UserInfoRes = ApiRes<{ username: string }>
