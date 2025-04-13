const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: './',
  pluginOptions: {
    electronBuilder: {
      chainWebpackMainProcess: (config) => {
        config.output.filename('background.js');
      },
      preload: path.resolve(__dirname, 'src/preload.js'),
    }
  },
  configureWebpack: {
    resolve: {
      fallback: {
        path: require.resolve('path-browserify'),
        fs: false, // 如果需要使用 fs，可以设置为 require.resolve('fs')
      },
    },
  },
})
