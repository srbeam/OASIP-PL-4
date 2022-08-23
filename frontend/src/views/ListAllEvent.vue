<script setup>
import { ref, onBeforeMount } from 'vue'
import ListallComponent from '../components/ListallComponent.vue'

const events = ref([])
const getEvents = async () => {
  // const res = await fetch('http://localhost:8080/api/events')
  // const res = await fetch('http://10.4.56.124:8081/api/events')
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`)
  if (res.status === 200) {
    events.value = await res.json()
    for (let event of events.value) {
      console.log(new Date("2022-05-24 11:11:00"))
      event.eventStartTime = new Date(event.eventStartTime)
      // const dd = new Date(event.eventStartTime)
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
  return dateTime.toLocaleString('en-US', {day: '2-digit', month: 'long', year: 'numeric'})
}

const formatTime = (dateTime) => {
  return dateTime.toLocaleString('th-TH', {hour: '2-digit', minute: '2-digit', })
}

const deleteEvent = async (eventId, bookingName, eventStartTime) => {
  let confirms = confirm(`Do you want to delete? \n"${bookingName}" \nAppointment : ${formatDate(eventStartTime)} ${formatTime(eventStartTime)}`)
  console.log(eventId)
  console.log(confirms)
  if(confirms){
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/${eventId}`, {
    method: 'DELETE'
  })
  if (res.status === 200) {
    events.value =  events.value.filter((event) =>  event.id !== eventId)
    console.log('deleted successfully')
  } else console.log('error, cannot delete data')}
}


</script>

<template>
  <div>
    <ListallComponent :events="events" @deleteEvent="deleteEvent" ></ListallComponent>
    <router-view> </router-view>
    
  </div>
  
</template>


<style scoped>

#showall {
  background-color: rgba(255, 255, 255, 0.4);
}
#detailevents {
  color: #383838;
}
#duration {
  background-color: #eee385;
}
#bin{
  width: 25px;
  margin-left: 310px;
  margin-top: -30px;
}
.pagination {
  display: inline-block;
  margin-left: 550px;
}

.pagination a {
  color: black;
  float: left;
  padding: 8px 16px;
  text-decoration: none;
}

.pagination a.active {
  background-color: orange;
  color: white;
  border-radius: 5px;
}

.pagination a:hover:not(.active) {
  background-color: #ddd;
  border-radius: 5px;
}

</style>
