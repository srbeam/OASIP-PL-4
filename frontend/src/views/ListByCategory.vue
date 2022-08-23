<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, onBeforeMount } from 'vue'
import ListByCategory from './../components/ListByCateComponent.vue'
let { params } = useRoute()

const id = ref(Number(params.id))
const category = ref('')

switch (id.value) {
  case 1:
    category.value = 'Project Management Clinic'
    break;
  case 2:
    category.value = 'DevOps/Infra Clinic'
    break;
  case 3:
    category.value = 'Database Clinic'
    break;
  case 4:
    category.value = 'Client-side Clinic'
    break;
  case 5:
    category.value = 'Server-side Clinic'
    break;
}

const events = ref([])
const getEvents = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`)
  if (res.status === 200) {
    events.value = await res.json()
    for (let event of events.value) {
      event.eventStartTime = new Date(event.eventStartTime)
      console.log(event.eventStartTime)
    }
    console.log(events.value)
  
  } else console.log('error, cannot get data')
}
onBeforeMount(async () => {
  await getEvents()
  console.log(events)
  
})

const formatDate = (dateTime) => {
  return dateTime.toLocaleString('en-US', {
    day: '2-digit',
    month: 'long',
    year: 'numeric'
  })
}

const formatTime = (dateTime) => {
  return dateTime.toLocaleString('th-TH', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const deleteEvent = async (eventId, bookingName, eventStartTime) => {
  let confirms = confirm(`Do you want to delete? \n"${bookingName}" \nAppointment : ${formatDate(eventStartTime)} ${formatTime(eventStartTime)}`)
  console.log(eventId)
  console.log(confirms)
  if (confirms) {
    const res = await fetch(
      `${import.meta.env.VITE_BACK_URL}/events/${eventId}`,
      {
        method: 'DELETE'
      }
    )
    if (res.status === 200) {
      events.value = events.value.filter((event) => event.id !== eventId)
      appRouter.go(0)
      console.log('deleted successfully')
    } else console.log('error, cannot delete data')
  }
}

</script>
 
<template>
    <ListByCategory :category="category" :events="events" @deleteEvent="deleteEvent"></ListByCategory>

</template>
 
<style>

</style>