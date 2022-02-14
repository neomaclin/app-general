<script setup lang="ts">
import { ref, computed } from 'vue'

const currentPath = ref(window.location.hash)

window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash
})

// const currentView = computed(() => {
//   const url = currentPath.value.slice(1)
//   return routes.find(route => url.match(route.path.reg) || route.path === '/')?.component || NotFound
// })
</script>

<template>
  <component :is="currentView" />
</template>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
