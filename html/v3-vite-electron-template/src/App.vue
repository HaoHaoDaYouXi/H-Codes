<template>
  <div id="app">
    <el-config-provider
      :locale="elLocaleMap[useLocaleStoreHook().defaultLocale]"
    >
      <router-view />
    </el-config-provider>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue"
import { elLocaleMap, useLocaleStoreHook } from "@/store/modules/locale"
const { ipcRenderer } = require("electron")

onMounted(() => {
  const winState: WinStateDTO = {
    width: 1440,
    height: 900,
    center: true,
    maximizable: true,
    resizable: true
  }
  ipcRenderer.send("set_win_size", winState)
})
</script>

<style lang="scss">
* {
  margin: 0;
  padding: 0;
}

@media screen and (max-width: 768px) {
  .el-message {
    min-width: 300px !important;
  }
  .el-message-box {
    width: 300px !important;
  }
  .el-dialog__wrapper .el-dialog {
    width: 95% !important;

    .el-dialog__body {
      padding: 10px 20px !important;
    }
  }
  .el-date-range-picker {
    left: 0 !important;
  }

  .el-form-item__label {
    display: block;
    width: 100% !important;
    text-align: left;
  }
  .el-form-item__content {
    margin-left: 0 !important;
  }
}
</style>
