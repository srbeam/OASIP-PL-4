<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, onBeforeMount } from 'vue'
let { params } = useRoute()

const currentPage = Number(params.page)
const totalPage = ref(0)

const appRouter = useRouter()

const response = ref([])
const events = ref([])
const getEvents = async () => { 
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/page?page=${currentPage - 1}&pageSize=6`)
  if (res.status === 200) {
    response.value = await res.json()
    events.value = response.value.content
    totalPage.value = response.value.totalPages
    for (let event of events.value) {
      event.eventStartTime = new Date(event.eventStartTime)
      console.log(event.eventStartTime)
    }
  } else console.log('error, cannot get data')
}
onBeforeMount(async () => {
  await getEvents()
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
  <div class="">
  <div class="ml-36">
    <div id="no-events" v-show="events.length == 0">
      <h1 class="mx-60 pt-60">No Scheduled Events</h1>
    </div>

    <div>
      <div id="have-events" v-show="events.length > 0">
        <div
          id="showall"
          v-for="(event, index) in events"
          :key="index"
          class="rounded-md p-4 my-2 w-96 color mr-2"
        >
          <div id="detailevents" class="">
            <p class="text-xl font-bold">{{ event.bookingName }}</p>
            <p>{{ event.eventCategoryName }}</p>
            <p class="font-semibold">Appointment Date / Time</p>
            <div>
              <p>Date : {{ formatDate(event.eventStartTime) }}</p>
              <p>Time : {{ formatTime(event.eventStartTime) }} min.</p>
            </div>
            <div id="duration" class="rounded-full w-44 h-9 p-1">
              <p class="ml-3">Duration : {{ event.eventDuration }} min</p>
            </div>

            <router-link
              :to="{ name: 'EventDetail', params: { id: event.id } }"
            >
              <button
                class="border-2 border-gray-500 mt-3 p-2 rounded-lg text-xs text-gray-500 hover:bg-gray-500 hover:text-white"
              >
                see more >
              </button>
            </router-link>

            <div id="bin" @click="deleteEvent(event.id, event.bookingName, event.eventStartTime)">
              <img src="../assets/images/trash.png" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>  
  </div>
  <div id="paging" class="flex">
    <router-link :to="{ name: 'Page', params: { page: currentPage-1 } }">
      <button class="mr-3 text-white mt-1" :disabled="currentPage == 1">&lt;&lt;</button>
    </router-link>
    <div v-for="page in totalPage">
      <router-link :to="{ name: 'Page', params: { page: page } }">
        <button class=" px-3 p-1 mx-2" :class="currentPage == page ? 'active' : 'pageBtn'">{{ page }}</button>
      </router-link>
    </div>
    <div class="page2">
    <router-link :to="{ name: 'Page', params: { page: currentPage+1 } }">
      <button class="ml-3 text-white mt-1" :disabled="currentPage == totalPage">>></button>
    </router-link>
    </div>
  </div>
  <br>
  <br>
</template>

<style scoped>
#no-events {
  /* padding-right: auto; */
  /* margin-left: auto; */
  margin-left: 18%;
  /* background-color: rgba(255, 255, 255, 0.4);
  color: #5c5c5c;
  width: 900px;
  height: 550px; */
}

#have-events {
  display: grid;
  grid-template-columns: auto auto auto;
}

#paging {
  /* margin-left: 50%; */
  padding-left: 650px;
  width: 55em;
  margin-top: 3%;
  color: white;
  justify-content: center
}

.pageBtn {
  border: white 0.1em solid;
  margin: 0.5%;
  border-radius: 0.2em;
  font-size: 1.1em;
  color: white;
  background-color: orange;
}

.active {
  border: white 0.1em solid;
  margin: 0.5%;
  border-radius: 0.2em;
  font-size: 1.1em;
  color: black;
  background-color: whitesmoke;
}

</style>
