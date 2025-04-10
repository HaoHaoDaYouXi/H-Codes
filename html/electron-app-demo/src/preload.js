const { ipcRenderer } = 'electron';

// 监听渲染进程的鼠标移动事件
window.addEventListener('mousemove', (event) => {
    const { screenX } = event;
    ipcRenderer.send('restore-window', { screenX });
});
