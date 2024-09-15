import { type Directive } from "vue"

/**
 * 权限判断
 */
export const permission: Directive = {
  mounted(el, binding) {
    console.log("判断是否有权限")
    const { value } = binding
    if (Array.isArray(value) && value.length > 0) {
      const hasPermission = true // permissions.includes(value) 判断当前权限是否在已有权限中
      // hasPermission || (el.style.display = "none") // 隐藏
      hasPermission || el.parentNode?.removeChild(el) // 销毁
    }else {
      console.log("没有权限数据")
      el.parentNode?.removeChild(el) // 销毁
    }
  }
}
