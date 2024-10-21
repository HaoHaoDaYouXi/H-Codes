export interface LoginReq {
  /** 账号 */
  user_name: string
  /** 密码 */
  user_password: string
}

export type LoginRes = ApiRes<{ token: string }>
