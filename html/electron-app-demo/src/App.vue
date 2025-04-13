<template>
  <div id="app">
    <!-- 自定义标题栏 -->
    <div class="title-bar">
      <div class="title">我的应用</div>
      <div class="window-controls">
        <button class="minimize" @click="minimizeWindow">-</button>
        <button class="close" @click="closeWindow">×</button>
      </div>
    </div>
    <!-- 应用内容 -->
    <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  mounted() {
    console.log('App component mounted'); // 检查组件是否加载
  },
  methods: {
    minimizeWindow() {
      console.log('minimizeWindow')
      window.electron.send('minimize-window');
    },
    closeWindow() {
      window.electron.send('close-window');
    }
  }
}
</script>

<style>
/* 自定义标题栏样式 */
.title-bar {
  -webkit-app-region: drag; /* 允许拖拽 */
  height: 30px;
  background-color: #2c3e50;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 10px;
}

.title {
  font-size: 14px;
}

.window-controls {
  display: flex;
  gap: 10px;
}

.window-controls button {
  -webkit-app-region: no-drag; /* 禁止拖拽 */
  background: none;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.window-controls button:hover {
  color: #ccc;
}

/* 应用内容样式 */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 20px;
}
</style>
