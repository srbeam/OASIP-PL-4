<script setup>
import { ref , computed } from 'vue'
import { useRouter } from 'vue-router'

// defineEmits(['formatDate', 'formatTime', 'formatDateTime'])
const props = defineProps({
  event: {
    type: Object,
    require: true
  },
  events: {
    type: Array,
    require: true
  }
})

const event = computed(() => props.event)
const events = computed(() => props.events)
const isEditMode = ref(false)
console.log(events.value)
console.log(event.value)
console.log(props.events)

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let currentDateTime = ref('')
const newStartTime = ref('')
// const newStartTime = computed(() => formatDateTime(event.value.eventStartTime))
const newNote = ref(event.value.eventNote)
let isBlank = ref(false)
let isInvalidDateFuture = ref(false)
let isInvalidOverLab = ref(false)

const formatDate = (dateTime) => {
  return dateTime.toLocaleString('en-US', {day: '2-digit', month: 'long', year: 'numeric'})
}

const formatTime = (dateTime) => {
  return dateTime.toLocaleString('th-Th', {hour: '2-digit', minute: '2-digit', })
}

const formatDateTime = (dateTime) => {
  let options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit'}
  let tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
  const formatDateTime = new Date(dateTime-tzoffset).toISOString().slice(0, -8);
  return formatDateTime
}

const validateDateFuture = (dateTime) => {
  const currentDateTime = new Date()
  console.log(currentDateTime)
  dateTime = new Date(dateTime)
  console.log(dateTime)

  return dateTime > currentDateTime ? true : false

}

const validateNonOverlab = (category, startDTNew, durationNew) => {
  filterCategory(category)
  console.log(filterEvents.value)

  startDTNew = new Date(startDTNew)
  // let endDTNew = new Date(startDTNew.getTime() + Number(durationNew)*60000)
  
  for(let event of filterEvents.value) {
    console.log(checkOverLab(startDTNew, event.eventStartTime, durationNew, event.eventDuration))
    if(!checkOverLab(startDTNew, event.eventStartTime, durationNew, event.eventDuration)) return false
  }
  return true
}

const checkOverLab = (startDTNew, startDTOld, durationNew, durationOld) => {
  const endDTOld = new Date(new Date(startDTOld.getTime() + Number(durationOld)*60000))
  const startDangerRange = new Date(new Date(startDTOld.getTime() - Number(durationNew)*60000))

  console.log(startDTOld)

    if(startDTNew > endDTOld) {
      // console.log('true วันไม่ตรงกัน')
      return true
    }else {
      if(startDTNew < startDangerRange) {
        // console.log('true วันไม่ตรงกัน')    
        return true
      }else {
        // console.log('flase วันตรงกัน')
        return false
      }
    }

}


let filterEvents = ref([]) 
const filterCategory = (category) => {
    filterEvents.value = events.value.filter((e) => {
      return e.eventCategoryName == category && e.id !== event.value.id
    })
}

const confirm = () => {
  // console.log(formatDateTime(event.value.eventStartTime))
  if(newStartTime.value == ''){
    isBlank.value = true
    isInvalidDateFuture.value = false
    isInvalidOverLab.value = false
  }else if(!validateDateFuture(newStartTime.value)) {
    isInvalidDateFuture.value = true
    isBlank.value = false 
    isInvalidOverLab.value = false
  }else if(!validateNonOverlab(event.value.eventCategory.eventCategoryName, newStartTime.value, event.value.eventDuration)) {
    console.log('เข้า')
    isInvalidOverLab.value = true
    isInvalidDateFuture.value = false
    isBlank.value = false
  }else {
    console.log(typeof newStartTime.value)
    const date = new Date(newStartTime.value)
    console.log(date)
    const updateStartTime = [
      date.getFullYear(),
      date.getMonth()+1,
      date.getDate(),
      date.getHours(),
      date.getMinutes()
    ]
    const updateNote = newNote.value
    console.log(updateStartTime)
    console.log(updateNote)
    console.log(event.value.id)

    event.value.eventStartTime = date
    event.value.eventNote = newNote.value 
    rescheduleEvent(updateStartTime, updateNote, event.value.eventCategory, event.value.id)
  }
}

const rescheduleEvent = async (updateStartTime, updateNote, eventCategory, eventId) => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/reschedule/${eventId}`, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
      eventStartTime: updateStartTime,
      eventNote: updateNote,
      eventDuration: event.value.eventDuration,
      eventCategory: eventCategory
    })
  })
  if (res.status === 200) {
    console.log('edited successfully')
    cancel()
  } else console.log('error, cannot be added')
  
}

const cancel = () => {
  newStartTime.value = ''
  newNote.value = event.value.eventNote
  isEditMode.value = false
  isBlank.value = false
  isInvalidDateFuture = false
}
const editMode = () => {
  newStartTime.value = formatDateTime(event.value.eventStartTime)
  console.log(newStartTime.value)
  isEditMode.value = true
}

</script>

<template>
  <div id="back">
    <button type="button" class="btn btn-default btn-xs text-white" @click="goBack">
      &lt; back
    </button>
  </div>
  <div id="showdetail" class="flex flex-row">
    <div class="basis-2/5 text-center p-4 border-r-4 border-zinc-300">
      <img
        src="../assets/images/user2.png"
        alt="user"
        width="180"
        class="mx-auto"
      />
      <br />
      <h3>{{ event.bookingName }}</h3>
      <p>{{ event.bookingEmail }}</p>
      <h5 class="text-sm">{{ event.eventCategory.eventCategoryName }}</h5>
    </div>

    <div class="basis-1/2 p-10 ml-4" v-if="!isEditMode">
      <p class="font-semibold">Appointment Date / Time</p>
      <div>
        <p>Date : {{ formatDate(event.eventStartTime) }}</p>
        <p>Time : {{ formatTime(event.eventStartTime) }} min</p>
      </div>
      <div id="duration" class="rounded-full w-40 h-8">
        <p class="ml-3 text-sm">Duration : {{ event.eventDuration }} min</p>
      </div>
      <br />

      <p class="font-semibold">Note :</p>

      <!-- <p v-if="event.eventNote != undefined || event.eventNote != '' || event.eventNote != null">{{ event.eventNote }}</p>
      <p v-else>no note</p> -->
      <p>{{ event.eventNote }}</p>

      <button
        id="reschedule"
        class="bg-red-600 rounded-lg p-2 px-3 hover:bg-red-700"
        @click="editMode"
      >
        Reschedule
      </button>
    </div>

    <!-- edit mode -->
    <!-- <Reschedule :event="event" v-if="isEditMode"></Reschedule> -->
    <div class="basis-1/2 p-10" v-if="isEditMode">
    <p class="text-red-600" v-if="isBlank">
    Please select date and time or click cancel. |
    กรุณาเลือกระบุเวลาใหม่ที่ต้องการเลือก หรือ กดยกเลิก
    </p>
    <p class="text-red-600" v-if="isInvalidDateFuture">
      Appointment time in the past. |
      วันเวลานัดหมายไม่ถูกต้อง
    </p>
    <p class="text-red-600" v-if="isInvalidOverLab">
      Have an appointment during this time. |
      มีการนัดในช่วงเวลานี้
    </p>
      <div class="mb-4">
        Appointment Date / Time <span class="text-red-600 text-xl">*</span><br />
        <input
          id="date"
          type="datetime-local"
          :min="currentDateTime"
          v-model="newStartTime"
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
          v-model="newNote"
        />
      </div>
      
      <div id="button" class="">
        <button class="bg-green-600 rounded-lg p-2 px-3 hover:bg-green-700 mr-2" @click="confirm">
          Confirm
        </button>
        <button class="bg-red-600 rounded-lg p-2 px-3 hover:bg-red-700" @click="cancel">
          Cancel
        </button>
      </div>
    </div>
    <!-- {{events}} -->
   
  </div>
</template>

<style scoped>
#back {
  margin-left: 2%;
  /* border: #383838 1px solid; */
}
#showdetail {
  background-color: rgba(255, 255, 255, 0.4);
  color: #383838;
  width: 1100px;
  padding: 20px;
  margin-left: 3%;
  margin-top: 1%;
  /* height: px; */
}

#duration {
  background-color: #eee385;
  padding: 3.5px;
}

#reschedule {
  margin-left: 85%;
  color: white;
}

#button {
    padding-left: 80%;
    width: 125%;
    color: white;
}
</style>
