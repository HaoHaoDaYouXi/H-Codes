<template>
  <h1>{{ msg }}</h1>

  <div class="card">
    <button type="button" @click="count++">count is {{ count }}</button>
    <p>
      Edit
      <code>components/HelloWorld.vue</code>
      to test HMR
    </p>

    <p
      v-for="(item, index) in LOCALE_OPTIONS"
      :key="index"
      @click="changeLocale(item.value)"
    >
      {{ item.label }}
    </p>
    <p>{{ $t("test") }}</p>
  </div>

  <p>
    Check out
    <a href="https://vuejs.org/guide/quick-start.html#local" target="_blank">
      create-vue
    </a>
    , the official Vue + Vite starter
  </p>
  <p>
    Learn more about IDE Support for Vue in the
    <a
      href="https://vuejs.org/guide/scaling-up/tooling.html#ide-support"
      target="_blank"
    >
      Vue Docs Scaling up Guide
    </a>
    .
  </p>
  <p class="read-the-docs">Click on the Vite and Vue logos to learn more</p>
</template>

<script setup lang="ts">
import { ref } from "vue"
import { useI18n } from "vue-i18n"
import { LOCALE_OPTIONS, useLocaleStoreHook } from "@/store/modules/locale"

defineProps<{ msg: string }>()

const count = ref(0)

const i18n = useI18n()

const changeLocale = (value: string) => {
  useLocaleStoreHook().changeLocale(value)
  i18n.locale.value = value
}
</script>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
