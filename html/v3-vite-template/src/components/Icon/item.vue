<!-- icon 组件 -->
<template>
  <template v-if="props.icon.includes('el-icon') || props.icon.includes('svg-icon') || props.icon.includes('iconfont')">
    <SvgIcon v-if="props.icon.includes('svg-icon')" :name="props.icon" class="el-icon sub-el-icon" />
    <i v-else :class="props.icon.includes('el-icon')?props.icon+' el-icon sub-el-icon mr-8':props.icon+' el-icon sub-el-icon mr-5 text-18'" />
  </template>
  <component v-else :is="props.icon" class="el-icon sub-el-icon" />
</template>

<script lang="ts" setup>
interface Props {
  icon?: string
}

const props = withDefaults(defineProps<Props>(), {
  icon: ""
})
</script>

<style lang="scss" scoped>
.sub-el-icon {
  width: 24px;
  display: inline-block;
  text-align: center;
  color: currentColor;
}
.mr-8 {
  margin-right: 8px;
}
.mr-5 {
  margin-right: 5px;
}
.text-18 {
  font-size: 18px;
}
</style>
