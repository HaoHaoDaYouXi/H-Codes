<template>
  <div class="navbar">
    <Breadcrumb class="breadcrumb-container hidden-sm-and-down" />
  </div>
</template>

<script lang="ts" setup>
import Breadcrumb from "./Breadcrumb/index.vue"
</script>

<style lang="scss" scoped>
.navbar {
  height: 38px;
  overflow: hidden;
  position: relative;
  background-color: #f1f4f9;

  .breadcrumb-container {
    float: left;
  }
}
</style>
