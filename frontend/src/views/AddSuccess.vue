<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRouter } from 'vue-router'

const appRouter = useRouter()
const goAllEvent = () => appRouter.push({ name: 'Page', params: { page: 1 } })
</script>

<template>
  <div id="success" class="pl-96">
    <div class="bg-white/30 grid place-content-center py-16">
      <h1 class="text-center">Add Sucessful !</h1>
      <img src="../assets/images/success.png" width="500" />
      <button @click="goAllEvent" class="border-2 border-white p-2 rounded-md hover:bg-orange-500">
        Go to All Event
      </button>
    </div>
  </div>
</template>

<style scoped>
#success {
  /* padding-left: 10%; */
  width: 160vh;
}
</style>
