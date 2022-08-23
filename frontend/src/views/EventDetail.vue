<script setup>
import { useRoute } from 'vue-router'
import { ref, onBeforeMount, onBeforeUpdate, onUpdated } from 'vue'
import EventDetailComponent from '../components/EventDetailComponent.vue'
let { params } = useRoute()

const eventId = params.id
const event = ref({})
const getEventById = async (eventId) => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/${eventId}`)
  if (res.status === 200) {
    event.value = await res.json()
    event.value.eventStartTime = new Date(event.value.eventStartTime)
  } else console.log('error, cannot get data')
}

const events = ref([])
const getEvents = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`)
  if (res.status === 200) {
    events.value = await res.json()
    for (let event of events.value) {
      event.eventStartTime = new Date(event.eventStartTime)
    }
  
  } else console.log('error, cannot get data')
}

onBeforeMount(async () => {  
  await getEventById(eventId)
  await getEvents()
  console.log(events.value)
})


</script>

<template>

  <div class="ml-40">
    <EventDetailComponent :event="event" :events="events"></EventDetailComponent>
    <!-- <EventDetailComponent :event="event" :events="events" @formatDate="formatDate" @formatTime="formatTime" @formatDateTiem="formatDateTime"></EventDetailComponent> -->
  </div>
</template>

<style>

</style>
