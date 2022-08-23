<script setup>
import { ref , computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  event: {
    type: Object,
    require: true
  }
})

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

</script>
 
<template>
    <div class="basis-1/2 p-10">
      <div class="mb-4">
        Appointment Date / Time <br />
        <input
          id="date"
          type="datetime-local"
          v-model="eventStartTime"
          class="border-2 border-gray-200 rounded-md p-1 px-2 mt-1"
        />
      </div>

      <div class="mb-16">
        Add Note : <span class="text-sm">(No more than 100 characters / ไม่เกิน 100 ตัวอักษร)</span>
        <input
          class="form-control"
          type="text"
          id="bookingname"
          maxlength="100"
          v-model="note"
        />
      </div>
      
      <div id="button" class="">
        <button id="reschedule" class="bg-green-600 rounded-lg p-2 px-3 hover:bg-green-700 mr-2">
          Comfirm
        </button>
        <button id="reschedule" class="bg-red-600 rounded-lg p-2 px-3 hover:bg-red-700" @click="goBack">
          Cancel
        </button>
      </div>
    </div>
    
  
</template>
 
<style scoped>
#button {
    padding-left: 85%;
    width: 125%;
    color: white;
}

</style>