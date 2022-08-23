<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

defineEmits(['deleteEvent'])
const props = defineProps({
  categories: {
    type: Array,
    require: true
  },
})

const showForm = ref(false)
const categories = computed(() => props.categories)

const currentEdit = ref()
const updateName = ref()
const updateDescription = ref()
const updateDuration = ref()

let isBlank = ref(false)
let isInvalidDuration = ref(false)
let isDuplicateName = ref(false)

const editMode = (category) => {
    showForm.value = true
    currentEdit.value = category
    updateName.value = currentEdit.value.eventCategoryName
    updateDescription.value = currentEdit.value.eventCategoryDescription
    updateDuration.value = currentEdit.value.eventDuration
}

const save = () => {
  validateNameDuplicate(updateName.value)
  console.log(updateDuration.value == '' && updateDuration.value != 0)
  console.log(validateDuration(updateDuration.value))
  if(updateName.value == '' || updateDescription.value == '' || (updateDuration.value == '' && updateDuration.value != 0)) {
    isBlank.value = true
    isInvalidDuration.value = false
    isDuplicateName.value = false
  }else if(!validateNameDuplicate(updateName.value)) {
    isDuplicateName.value = true
    isBlank.value = false
    isInvalidDuration.value = false
  }else if(!validateDuration(updateDuration.value)) {
    isInvalidDuration.value = true
    isBlank.value = false
    isDuplicateName.value = false
  }else {
    editCategory()
  }
}

const editCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BACK_URL}/categories/${currentEdit.value.id}`, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
      eventCategoryName: updateName.value.trim(),
      eventCategoryDescription: updateDescription.value,
      eventDuration: updateDuration.value
    })
  })
  if (res.status === 200) {
    console.log('edited successfully')
    // cancelEdit()
    goSuccess()
  } else console.log('error, cannot be added')
  
}

const validateDuration = (duration) => {
  return duration <= 480 && duration >= 1
}


const validateNameDuplicate = (categoryName) => {
  const nameAllCategory = []
  categories.value.forEach((category) => {
      if(category.id != currentEdit.value.id) {
        nameAllCategory.push(category.eventCategoryName.toLowerCase())
      }
  })
  return !nameAllCategory.includes(categoryName.toLowerCase())
}

const cancelEdit = () => {
    showForm.value = false
    isBlank.value = false
    isInvalidDuration.value = false
    isDuplicateName.value = false
    updateName.value = ''
    updateDescription.value = ''
    updateDuration.value = ''
}

const appRouter = useRouter()
const goSuccess = () => appRouter.go(0)

</script>
 
<template>
<!-- <button id="myBtn">Open Modal</button> -->

<!-- The Modal -->
<div class="pop-up" v-if="showForm">
  <div class="modal-content p-5">
    <div class="flex flex-row">
      <img src="../assets/images/category.png" class="" width="50" />
      <h3 class="ml-5 mt-2">{{ currentEdit.eventCategoryName }}</h3>
    </div>
    <div class="error mt-4" v-if="isBlank">
        Please fill out the information completely. |
        กรุณากรอกข้อมูลให้ครบด้วยค่ะ
      </div>
      <div class="error mt-4" v-if="isInvalidDuration">
        The event duration is out of range. (1-480 min) |
        เวลาของนัดอยู่นอกช่วงเวลาที่กำหนด (1-480 นาที)
      </div>
      <div class="error mt-4" v-if="isDuplicateName">
        EventsName must not have duplicate names. | EventsName ต้องไม่มีชื่อซ้ำกัน
        
      </div>
    <div class="py-4">
        Clinic Name : 
        <span class="error"> * </span>
        <span class="lenght"> 100 character | 100 ตัวอักษร </span><br>
        <input type="text" class="rounded-md w-96" v-model="updateName" maxlength="100"><br />
        Clinic Duration 
        <span class="error"> * </span>
        <span class="lenght">between 1 and 480 | ช่วงเวลาระหว่าง 1 ถึง 480</span> <br>
        <input type="number" min="1" max="480" class="rounded-md text-center " v-model="updateDuration"> min.<br />
        Clinic Description : <span class="lenght"> 500 character | 500 ตัวอักษร</span>
        <br><textarea type="text" class="rounded-md" v-model="updateDescription" maxlength="500"></textarea>
    </div>
    <div class="grid grid-cols-2 text-white">
        <button class="p-2 bg-green-500 hover:bg-green-600" @click="save">save</button>
        <button class="p-2 bg-red-500 hover:bg-red-600" @click="cancelEdit">cancel</button>
    </div>
    
  </div>
</div>

  <div>
    <div class="category-box flex flex-row items-center rounded-xl py-40" v-for="category in categories">
      <div class="mr-10 ml-5">
        <img src="../assets/images/category.png" class="" width="55" />
      </div>
      <div class="basis-5/6">
        <p class="text-4xl mb-2">{{ category.eventCategoryName }}</p>
        <p>Description : {{ category.eventCategoryDescription }}</p>
        <span id="duration" class="rounded-full w-44 h-7 p-1 px-3 text-black text-sm">Duration : {{ category.eventDuration }} min.</span>
      </div>
      <div class="Btn">
        <button class="edit bg-blue-500 hover:bg-blue-400 p-2 px-3 rounded-md text-white" @click="editMode(category)">Edit category</button>
        <router-link :to="{ name: 'ListByCategory', params: {id: category.id} }">
          <button class="bg-orange-600 hover:bg-orange-400 p-2 px-3 rounded-md text-white">Show events in category</button>
        </router-link>
      </div>
    </div>
  </div>

</template>
 
<style scoped>
.category-box {
  background-color: rgba(255, 255, 255, 0.5);
  width: 150%;
  height: 12em;
  color: #383838;
  margin-left: 20%;
  margin-bottom: 3%;
  padding: 3%;
}

#duration {
  background-color: #eee385;
}

.edit {
  width: 79.5%;
  margin-bottom: 3%;
}

.Btn {
  text-align: center;
  color: white;
}

/* The Modal (background) */
.pop-up {
    position: fixed;
    z-index: 2;
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */

}

.error {
  font-size: 0.9em;
}


/* Modal Content/Box */
.modal-content {
  background-color: #fefefe;
  position: absolute;
  margin: 6% auto; /* 15% from the top and centered */
  margin-left: 25%;
  padding: 20px;
  border: 1px solid #888;
  width: 50%; /* Could be more or less, depending on screen size */
  color: #383838;
}

input {
    background-color: rgba(220, 220, 220, 0.4);
    padding: 1.5%;
    margin-bottom: 2%;
    font-size: 0.9em;
}

textarea {
    width: 95%;
    height: 7em;
    resize: both;
    background-color: rgba(220, 220, 220, 0.4);
    padding: 1.5%;
    font-size: 0.9em;
}
</style>