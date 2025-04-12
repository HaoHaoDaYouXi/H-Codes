module.exports = {
  root: true,
  env: {
    node: true,
    browser: true
  },
  parserOptions: {
    // ecmaVersion: 7,
    // sourceType: "module",
    parser: "babel-eslint"
  },
  extends: [
    'plugin:vue/essential'
  ],
  plugins: [
    'vue'
  ],
  // 添加自定义规则
  rules: {
    // allow async-await
    'generator-star-spacing': 'off',
    // allow debugger during development
    indent: 0,
    'no-useless-escape': 0,
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off'
  },
  globals: {
    'array-bracket-spacing': 'off',
    'vue/html-closing-bracket-newline': 'off'
  }
}
