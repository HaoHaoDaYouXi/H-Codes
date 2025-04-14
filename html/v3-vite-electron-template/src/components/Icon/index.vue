<!-- icon 组件 -->
<template>
  <Item v-if="props.icon" :icon="props.icon" />
  <i v-else class="round" />
</template>

<script lang="ts" setup>
import Item from "./item.vue"

interface Props {
  icon?: string
}

const props = withDefaults(defineProps<Props>(), {
  icon: ""
})
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
</style>
