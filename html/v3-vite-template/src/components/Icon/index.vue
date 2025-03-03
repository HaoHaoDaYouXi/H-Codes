<!-- icon 组件 -->
<template>
  <template v-if="props.icon">
    <template v-if="props.icon.includes('el-icon')">
      <i v-if="iIcon(props.icon) != ''" :class="iIcon(props.icon)" />
      <SvgIcon v-else :name="props.icon" class="el-icon sub-el-icon" />
    </template>
    <component v-else :is="props.icon" class="el-icon sub-el-icon" />
  </template>
  <i v-else class="round" />
</template>

<script lang="ts" setup>
interface Props {
  icon?: string
}

const props = withDefaults(defineProps<Props>(), {
  icon: ""
})

const iIcon = (icon: string) => {
  if (icon.includes("el-icon")) {
    return icon + " el-icon sub-el-icon mr-8"
  } else if (icon.includes("iconfont")) {
    return icon + " el-icon sub-el-icon mr-5 text-18"
  } else {
    return ""
  }
}
</script>

<style lang="scss" scoped>
.round {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  border: 1px solid #7e828b;
  margin-right: 14px;
  margin-left: -12px;
}
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
