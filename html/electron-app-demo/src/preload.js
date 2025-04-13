const { contextBridge, ipcRenderer } = require('electron')

/**
 * 给window 暴露 contextBridge 和 quitApp 函数，该函数用于发送 quit-app 事件给主进程
 * electron --这个名字 可以自行修改，在window 下使用时，取相应的名字即可
 */
contextBridge.exposeInMainWorld('electron', {
    contextBridge,
    send: (channel, data) => ipcRenderer.send(channel, data),
    on: (channel, func) => ipcRenderer.on(channel, (event, ...args) => func(...args)),
});

// // 监听渲染进程的鼠标移动事件
// window.addEventListener('mousemove', (event) => {
//     const { screenX } = event
//     ipcRenderer.send('restore-window', { screenX })
// });
