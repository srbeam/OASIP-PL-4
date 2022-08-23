<script setup>
import { ref } from 'vue'
defineEmits(['deleteEvent'])
const props = defineProps({
  events: {
    type: Array,
    require: true
  }
})
// console.log(props.events.value)


</script>
 
<template>
<div>
    <div id="no-events" v-show="events.length == 0">
      <h1 class="mx-60 pt-60">No Scheduled Events</h1>
    </div>

    <div>
      <div id="have-events" v-show="events.length > 0">
        <!-- <h1 style="color: #5c5c5c">Have Scheduled Events</h1> -->
        
        <div
          id="showall"
          v-for="(event, index) in events"
          :key="index"
          class="rounded-md p-4 my-3 w-96 color mr-2"
        >
          <div id="detailevents" class="">
            <p class="text-xl font-bold">{{ event.bookingName }}</p>
            <p>{{ event.eventCategoryName }}</p>
            <p class="font-semibold">Appointment Date</p>
            <p>Date : {{ event.eventStartTime.slice(0, 11) }}</p> 
            <p>Time : {{ event.eventStartTime.slice(13,18) }} min.</p>
            <div id="duration" class="rounded-full w-44 h-9 p-1">
              <p class="ml-3">Duration : {{ event.eventDuration }} min</p>
            </div>

            <router-link :to="{ name: 'EventDetail', params: {id: event.id} }" >
              <button
                class="border-2 border-gray-500 mt-3 p-2 rounded-lg text-xs text-gray-500 hover:bg-gray-500 hover:text-white"
              >
                see more >
              </button>
            </router-link>
            <div id="bin" @click="$emit('deleteEvent', event.id)">
                <img src="../assets/images/trash.png">
            </div>
          </div>
        </div>
        <div></div>
      </div>
    </div>

    <!-- <router-view :key="$route.fullPath"> </router-view> -->
    
  </div>
</template>
 
<style>
#no-events {
  background-color: rgba(255, 255, 255, 0.4);
  color: #5c5c5c;
  width: 900px;
  height: 550px;

  /* position: fixed; */
  /* margin-left: 400px;
  margin-top: 150px; */
}

#have-events {
  /* margin-top: -567px;
  margin-left: 260px;
   */

  display: grid;
  grid-template-columns: auto auto auto;
  /* grid-column-gap: 5px; */
}
#showall {
  background-color: rgba(255, 255, 255, 0.4);
  /* background-color: white;
  opacity: 0.4; */
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
</style>