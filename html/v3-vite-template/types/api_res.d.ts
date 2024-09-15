/**
 * 接口返回对象
 */
interface ApiRes<T> {
  code: number
  message: string
  data: T
}
