'use strict'

import { app, protocol, BrowserWindow, screen, ipcMain, Tray, Menu } from 'electron'
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'
import installExtension, { VUEJS_DEVTOOLS } from 'electron-devtools-installer'
import path from 'path'

const isDevelopment = process.env.NODE_ENV !== 'production'

// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])

let tray = null;
let win = null;

function createTray() {
  // 创建任务栏图标
  tray = new Tray(path.join(__static, 'favicon.ico'));
  const contextMenu = Menu.buildFromTemplate([
    {
      label: '显示窗口',
      click: () => {
        win.show();
      }
    },
    {
      label: '退出',
      click: () => {
        app.isQuiting = true;
        app.quit(); // 退出程序
      }
    }
  ]);
  tray.setToolTip('我的应用'); // 设置任务栏图标的提示文字
  tray.setContextMenu(contextMenu); // 设置任务栏图标的右键菜单
}

async function createWindow() {
  // Create the browser window.
  win = new BrowserWindow({
    width: 350,
    height: 230,
    resizable: false,
    maximizable: false,
    fullscreen: false,
    autoHideMenuBar: true,
    alwaysOnTop: true,
    icon: path.join(__static, 'favicon.ico'),
    frame: false,

    webPreferences: {
      spellcheck: false, // 禁用拼写检查器
      disableBlinkFeatures: "SourceMap", // 以 "," 分隔的禁用特性列表
      devTools: true, // 是否开启 DevTools, 如果设置为 false（默认值为 true）, 则无法使用 BrowserWindow.webContents.openDevTools()
      webSecurity: false, // 当设置为 false, 将禁用同源策略
      nodeIntegration: false, // 是否启用 Node 集成
      contextIsolation: true, // 是否在独立 JavaScript 环境中运行 Electron API 和指定的 preload 脚本，默认为 true
      backgroundThrottling: false, // 是否在页面成为背景时限制动画和计时器，默认值为 true
      nodeIntegrationInWorker: true, // 是否在 Web 工作器中启用了 Node 集成

      preload: path.join(__dirname, 'preload.js'), // 预加载脚本
    }
  })
  console.log("__dirname",__dirname)

  createTray()

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    // Load the url of the dev server if in development mode
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    // if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    // Load the index.html when not in development
    win.loadURL('app://./index.html')
  }

  let isHidden = false;
  let originalBounds = win.getBounds();

  win.on('move', () => {
    const { x, y, width, height } = win.getBounds();
    const { width: screenWidth, height: screenHeight } = screen.getPrimaryDisplay().workAreaSize;

    if (x <= 0 && !isHidden) {
      win.setBounds({ x: 0, y, width: 20, height });
      isHidden = true;
    } else if (x + width >= screenWidth && !isHidden) {
      win.setBounds({ x: screenWidth - 20, y, width: 20, height });
      isHidden = true;
    } else if (y <= 0 && !isHidden) {
      win.setBounds({ x, y: 0, width, height: 20 });
      isHidden = true;
    } else if (y + height >= screenHeight && !isHidden) {
      win.setBounds({ x, y: screenHeight - 20, width, height: 20 });
      isHidden = true;
    }
  });

  // 监听窗口关闭事件
  win.on('close', (event) => {
    console.log('close-window')
    if (!app.isQuiting) {
      event.preventDefault(); // 阻止窗口关闭
      win.hide(); // 隐藏窗口
    }
  });

  // 监听渲染进程的消息，恢复窗口
  ipcMain.on('restore-window', (event, { screenX }) => {
    console.log('restore-window')
    if (isHidden) {
      const { width: screenWidth } = screen.getPrimaryDisplay().workAreaSize;
      const { x, y, width, height } = originalBounds;

      if (screenX < screenWidth / 2) {
        win.setBounds({ x: 0, y, width, height });
      } else {
        win.setBounds({ x: screenWidth - width, y, width, height });
      }
      isHidden = false;
    }
  });

  ipcMain.on('minimize-window', () => {
    console.log('minimize-window')
    win.minimize();
  });

  ipcMain.on('close-window', () => {
    console.log('close-window')
    win.hide(); // 隐藏窗口
  });

  // 监听鼠标移动
  // win.on('mousemove', (event) => {
  //   const { x, y } = event.screen;
  //   const { x: winX, y: winY, width, height } = win.getBounds();
  //
  //   // 如果窗口隐藏且鼠标移动到贴边区域，恢复窗口
  //   if (isHidden && (x >= winX && x <= winX + width && y >= winY && y <= winY + height)) {
  //     win.setBounds(originalBounds);
  //     isHidden = false;
  //   }
  // });
}

app.whenReady().then(() => {
  if (isDevelopment && !process.env.IS_TEST) {
    // Install Vue Devtools
    // try {
    //   await installExtension(VUEJS_DEVTOOLS)
    // } catch (e) {
    //   console.error('Vue Devtools failed to install:', e.toString())
    // }
  }
  createWindow();
});

// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
// app.on('ready', async () => {
//   if (isDevelopment && !process.env.IS_TEST) {
//     // Install Vue Devtools
//     // try {
//     //   await installExtension(VUEJS_DEVTOOLS)
//     // } catch (e) {
//     //   console.error('Vue Devtools failed to install:', e.toString())
//     // }
//   }
//   createWindow()
// })

// Exit cleanly on request from parent process in development mode.
if (isDevelopment) {
  if (process.platform === 'win32') {
    process.on('message', (data) => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}
