<script setup>
import { onBeforeMount, ref } from 'vue';

const events = ref([])
const getEvents = async () => {
  const res = await fetch('http://localhost:80/api/events')
  if (res.status === 200) {
    events.value = await res.json()
    console.log(events.value)
  } else console.log('error, cannot get data')
}
onBeforeMount(async () => {
  await getEvents()
  console.log(typeof events.value[0].eventStartTime)
})



</script>
 
<template>
<h1 class="font-black text-xl">Test</h1>

<!-- {{ events }} -->
<div v-for="(event, index) in events" :key="index" class="border-2 border-black rounded-md p-2 my-3"> 
  <p>Booking Name : {{ event.bookingName }}</p>
  <p>Event Category : {{ event.eventCategory.id }}</p>
  <p>Date : {{ event.eventStartTime.slice(0,10) }}</p>
  <p>Time : {{ event.eventStartTime.slice(11) }}</p>
  <p>Duration : {{ event.eventDuration }}</p>
</div>

</template>
 
<style>
body {
  padding: 3em;
}
</style>