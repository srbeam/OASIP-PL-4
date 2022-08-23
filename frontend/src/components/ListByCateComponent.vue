<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

defineEmits(['deleteEvent'])
const props = defineProps({
  category: {
    type: String,
    require: true
  },
  events: {
    type: String,
    require: true
  }
})

const events = computed(() => props.events)
const filterEvents = computed(() =>
  events.value.filter((event) => {
    return event.eventCategoryName == props.category
  })
)

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

const appRouter = useRouter()
const goBack = () => appRouter.push({ name: 'Category' })
</script>

<template>
  <div class="ml-44">
    <div v-show="filterEvents.length == 0" class="ml-32">
      <button @click="goBack">&lt; back</button>
      <div id="no-events" class="mt-2">
        <h1 class="pt-60">No Scheduled Events</h1>
      </div>
    </div>

    <div v-show="filterEvents.length > 0">
      <button @click="goBack">&lt; back</button>
      <div id="have-events">
        <div
          id="showall"
          v-for="(event, index) in filterEvents"
          :key="index"
          class="rounded-md p-4 my-1 w-96 color mr-2"
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
            <div id="bin" @click="$emit('deleteEvent', event.id, event.bookingName, event.eventStartTime)">
              <img src="../assets/images/trash.png" />
            </div>
          </div>
        </div>
        <div></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#no-events {
  width: 180%;
  text-align: center;
}

</style>
