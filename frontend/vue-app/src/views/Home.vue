<template>
  <header class="mb-auto">    
    <Naivation></Naivation>
  </header>
  <main class="px-3">
<!--     
    <router-view v-slot="{ Splash }" >
       <transition name="fade">
          <component :is="Splash" />
       </transition>
    </router-view> -->
    <!-- <h1>Cover your page.</h1>
    <p class="lead">Cover is a one-page template for building simple and beautiful home pages. Download, edit the text, and add your own fullscreen background photo to make it your own.</p>
    <p class="lead">
      <a href="#" class="btn btn-lg btn-secondary fw-bold border-white bg-white">Learn more</a>
    </p> -->
  </main>

  <footer class="mt-auto text-white-50">
    <p>Cover template for <a href="https://getbootstrap.com/" class="text-white">Bootstrap</a>, by <a href="https://twitter.com/mdo" class="text-white">@mdo</a>.</p>
  </footer>
</template>

<script setup lang="ts">
import Navigation from '../components/Navigation.vue'
</script>

<style scoped>
       .bd-placeholder-img {
          font-size: 1.125rem;
          text-anchor: middle;
          -webkit-user-select: none;
          -moz-user-select: none;
          user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
</style>
