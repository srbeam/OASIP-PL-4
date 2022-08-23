<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRouter } from 'vue-router'

const categories = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/categories`)
  if (res.status === 200) {
    categories.value = await res.json()
    console.log(categories.value)
  } else console.log('error, cannot get data')
}
onBeforeMount(async () => {
  await getCategory()
  await getEvents()
  console.log(events)
  console.log(categories)
})

const events = ref([])
let filterEvents = ref([]) 
const getEvents = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`)
  if (res.status === 200) {
    events.value = await res.json()
    for (let event of events.value) {
      event.eventStartTime = new Date(event.eventStartTime)
      console.log(event.eventStartTime)
    }
  } else console.log('error, cannot get data')
}

const bookingName = ref('')
const bookingEmail = ref('')
const eventCategory = ref('')
const eventStartTime = ref('')
const eventDuration = computed(() => {
  if (eventCategory.value == '') {
    return 0
  } else {
    return eventCategory.value.eventDuration
  }
})
const note = ref('')

let isBlank = ref(false)
let isInvalidEmail = ref(false)
let isInvalidDateFuture = ref(false)
let isInvalidOverLab = ref(false)

const addEvent = () => {
  console.log(validateDateFuture(eventStartTime.value))
  if (
    bookingName.value == '' ||
    bookingEmail.value == '' ||
    eventCategory.value == '' ||
    eventStartTime.value == ''
  ) {
    isBlank.value = true
    isInvalidEmail.value = false
    isInvalidDateFuture.value = false
    isInvalidOverLab.value = false
    console.log(isBlank.value)
  } else if (!validateEmail(bookingEmail.value)) {
    console.log('เข้า false')
    console.log(bookingEmail.value)
    isInvalidEmail.value = true
    isBlank.value = false
    isInvalidDateFuture.value = false
    isInvalidOverLab.value = false
  } else if(!validateDateFuture(eventStartTime.value)) {
    console.log('เข้า false date')
    isInvalidDateFuture.value = true
    isInvalidOverLab.value = false
    isInvalidEmail.value = false
    isBlank.value = false
  }else if(!validateNonOverlab(eventCategory.value.eventCategoryName, eventStartTime.value, eventDuration.value)) {
    console.log('เข้า false date')
    isInvalidOverLab.value = true
    isInvalidDateFuture.value = false
    isInvalidEmail.value = false
    isBlank.value = false
  }else {
    console.log('add')
    const date = new Date(eventStartTime.value).toLocaleString('en-GB')
    const newEvent = {
      bookingName: bookingName.value.trim(),
      bookingEmail: bookingEmail.value,
      eventCategory: eventCategory.value,
      eventStartTime: date,
      eventDuration: eventCategory.value.eventDuration,
      eventNote: note.value
    }
    console.log(newEvent)
    addEventToDB(newEvent)
  }
}

//ส่ง fetch
const addEventToDB = async (newEvent) => {
  console.log(newEvent)
  //ใช้ตัวแปร env แทนการเขียน path
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`, {
    method: 'POST',
    headers: {
      'content-type': 'application/json'
    },
    //ยัด newEvent ลงใน body ส่งให้ backend
    body: JSON.stringify(newEvent)
  })
  console.log(res.status)
  if (res.status === 201) {
    console.log('added sucessfully')
    console.log(res)
    goSuccess()
  } else console.log('error, cannot be added')
}

const validateEmail = (email) => {
  const re =
    /^(([^<>()[\]\\.,;:\s*$&!#?@"]+(\.[^<>()[\]\\.,;:\s*$&!#?@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(String(email).toLocaleLowerCase())
}

const validateDateFuture = (dateTime) => {
  const currentDateTime = new Date()
  dateTime = new Date(dateTime)
  return dateTime > currentDateTime ? true : false

}

const validateNonOverlab = (category, startDTNew, durationNew) => {
  filterCategory(category)
  console.log(filterEvents.value)

  startDTNew = new Date(startDTNew)
  
  for(let event of filterEvents.value) {
    console.log(checkOverLab(startDTNew, event.eventStartTime, durationNew, event.eventDuration))
    if(!checkOverLab(startDTNew, event.eventStartTime, durationNew, event.eventDuration)) return false
  }
  return true
}

const checkOverLab = (startDTNew, startDTOld, durationNew, durationOld) => {
  const endDTOld = new Date(new Date(startDTOld.getTime() + Number(durationOld)*60000))
  const startDanger = new Date(new Date(startDTOld.getTime() - Number(durationNew)*60000))

    if(startDTNew > endDTOld) {
      return true
    }else {
      if(startDTNew < startDanger) {
        return true
      }else {
        return false
      }
    }

}

const filterCategory = (category) => {
    filterEvents.value = events.value.filter((event) => {
      return event.eventCategoryName == category
    })
}

const appRouter = useRouter()
const goSuccess = () => appRouter.push({ name: 'AddSuccess'})
const goAllEvent = () => appRouter.push({name: 'Page', params: {page: 1}})

</script>

<template>
  <div class="ml-44">
    <div id="add" class="">
      <div class="error pb-2" v-if="isBlank">
        Please fill out the information completely. |
        กรุณากรอกข้อมูลให้ครบด้วยค่ะ
      </div>
      <div class="error pb-2" v-if="isInvalidEmail">
        Enter incorrect email information. |
        กรุณากรอก email ให้ถูกต้อง (ตัวอย่าง: example@gmail.com)
      </div>
      <div class="error pb-2" v-if="isInvalidDateFuture">
      Appointment time in the past. |
      วันเวลานัดหมายไม่ถูกต้อง 
      </div>
      <div class="error pb-2" v-if="isInvalidOverLab">
      Have an appointment during this time. |
      มีการนัดในช่วงเวลานี้
      </div>
      <div>
        Booking Name <span class="error">*</span> <span class="lenght">100 character | 100 ตัวอักษร</span>
        <input
          class="form-control mb-3"
          type="text"
          v-model="bookingName"
          maxlength="100"
        />

        <div>
          <label for="email">Booking Email address</label>
          <span class="error"> *</span>
          <span class="lenght"> 100 character | 100 ตัวอักษร</span>
          <div class="flex">
            <input
              id="email"
              name="email"
              type="email"
              class="form-control mb-3"
              v-model="bookingEmail"
              maxlength="100"
            />
            <span class="ml-3 mt-2 text-xl"> </span>
          </div>
        </div>
      </div>
      <div class="selectcategory">
        <select
          v-model="eventCategory"
          class="border-2 border-gray-200 rounded-md p-1"
        >
          <option value="" disabled>Please Select Clinic Category</option>
          <option
            v-for="category in categories"
            :key="category.id"
            :value="category"
          >
            {{ category.eventCategoryName }}
          </option>
        </select>
        <span class="error"> *</span> &emsp; &emsp; Duration
        <span class="border-2 border-gray-200 rounded-md p-1 px-2 bg-gray-100">
          {{ eventDuration }}
        </span>
        <br />
        <br />
        Appointment Date&Time <span class="error">*</span> <br />
        <input
          id="date"
          type="datetime-local"
          v-model="eventStartTime"
          class="border-2 border-gray-200 rounded-md p-1 px-2 mt-1 bg-white"
        />
      </div>
      <br>
      Add Note 
      <span class="lenght">500 character | 500 ตัวอักษร</span>
      <input
        class="form-control"
        type="text"
        id="bookingname"
        v-model="note"
        maxlength="500"
      />
    </div>

    <div id="click">
      <button
        type="button"
        class="btn btn-dark"
        style="opacity: 1; color: white"
        @click="addEvent"
      >
        ADD NEW EVENT
      </button>
      &ensp;
      <button
        type="button"
        class="btn btn-danger"
        style="opacity: 1; color: white"
        @click="goAllEvent"
      >
        CANCEL
      </button>
    </div>
  </div>
</template>

<style>
#add {
  position: fixed;
  width: 1200px;
  height: 550px;
  padding: 30px 40px;
  background-color: rgb(255, 255, 255, 0.5);
  color: black;
}
.selectcategory {
  margin-top: 25px;
}

#click {
  position: absolute;
  margin-left: 60px;
  margin-top: 470px;
}

.error {
  color: red;
}
input:invalid + span::before {
  content: '✘';
  color: red;
}
input:valid + span::before {
  content: '✔';
  color: green;
}

.lenght {
  color: grey;
  font-size: 0.8em;
}

</style>
