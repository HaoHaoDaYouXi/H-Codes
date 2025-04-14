<!-- svg-icon 组件 -->
<template>
  <svg class="svg-icon">
    <use :href="symbolId" />
  </svg>
</template>

<script lang="ts" setup>
import { computed } from "vue"

interface Props {
  prefix?: string
  name: string
}

const props = withDefaults(defineProps<Props>(), {
  prefix: "icon"
})

const symbolId = computed(() => `#${props.prefix}-${props.name}`)
</script>

<style lang="scss" scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  overflow: hidden;
}
</style>
