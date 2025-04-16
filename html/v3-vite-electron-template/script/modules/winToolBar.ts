/**
 * 工具窗口
 */
import { BrowserWindow, type BrowserWindowConstructorOptions, screen } from "electron"
import GlobalConfig from "./GlobalConfig"

class WinToolBar {
  /** 窗口实例 */
  private static TOOL_INST: BrowserWindow | null = null

  /** 窗口配置 */
  private static TOOL_CONFIG: BrowserWindowConstructorOptions = {
    width: 60,
    height: 60,
    type: "toolbar", //创建的窗口类型为工具栏窗口
    frame: false, //要创建无边框窗口
    resizable: false, //禁止窗口大小缩放
    transparent: true, //设置透明
    alwaysOnTop: true,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
      webSecurity: false
    }
  }

  /** 获取窗口实例 */
  static getToolInst() {
    return this.TOOL_INST
  }
  /** 创建窗口 */
  static create() {
    if (this.TOOL_INST) return

    this.TOOL_INST = new BrowserWindow(this.TOOL_CONFIG)
    this.TOOL_INST.removeMenu()

    const { left, top } = {
      left: screen.getPrimaryDisplay().workAreaSize.width - 60,
      top: screen.getPrimaryDisplay().workAreaSize.height - 60
    }
    this.TOOL_INST.setPosition(left, top) // 设置悬浮球位置
    this.TOOL_INST.setVisibleOnAllWorkspaces(true) // 显示在所有工作区

    if (GlobalConfig.IS_DEV_MODE) {
      this.TOOL_INST.loadURL(GlobalConfig.getWinToolUrl())
    } else {
      this.TOOL_INST.loadFile(GlobalConfig.getWinToolUrl())
    }

    this.TOOL_INST.once("ready-to-show", () => {
      this.TOOL_INST.show()
    })
  }
}

export default WinToolBar
