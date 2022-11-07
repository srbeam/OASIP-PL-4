<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRouter } from 'vue-router'
import BaseNavBar from '../components/BaseNavBar.vue'
import NoLoginModal from '../components/NoLoginModal.vue'
import VueJwtDecode from 'vue-jwt-decode'
import LecAddEventModal from '../components/LecAddEventModal.vue'
// import AddEventSuccessModal from '../components/AddEventSuccessModal.vue'
import SuccessModal from '../components/SuccessModal.vue'

const events = ref([])
let filterEvents = ref([])
const categories = ref([])
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
const isAddEventSuccess = ref(false)
const getUserFromToken = ref()
const token = ref()
let author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const is401 = ref()
const isAttachFile = ref()
const isLecAddEvent = ref()
const typeOfModal = ref()
const fileupload = ref()

onBeforeMount(async () => {
	await getCategory()
	getUser()
	getCurDateTime()
})

const getUser = () => {
	if (author != undefined || author != null) {
		getUserFromToken.value = VueJwtDecode.decode(author)
		author = localStorage.getItem('token')
	}
}

const getCategory = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/categories`, {
		method: 'GET'
	})
	if (res.status === 200) {
		categories.value = await res.json()
	} else if (res.status == 401) {
		getRefreshToken()
	} else console.log('error, cannot get data')
}

const getRefreshToken = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/refresh`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${refreshToken}`
		}
	})
	if (res.status === 200) {
		is401.value = false
		token.value = await res.json()
		saveLocal()
	} else if (res.status === 401) {
		is401.value = true
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}

const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}

const addEvent = () => {
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
		isInvalidEmail.value = true
		isBlank.value = false
		isInvalidDateFuture.value = false
		isInvalidOverLab.value = false
	} else if (!validateDateFuture(eventStartTime.value)) {
		isInvalidDateFuture.value = true
		isInvalidOverLab.value = false
		isInvalidEmail.value = false
		isBlank.value = false
	} else if (
		!validateNonOverlab(
			eventCategory.value.eventCategoryName,
			eventStartTime.value,
			eventDuration.value
		)
	) {
		isInvalidOverLab.value = true
		isInvalidDateFuture.value = false
		isInvalidEmail.value = false
		isBlank.value = false
	} else {
		const date = new Date(eventStartTime.value).toLocaleString('en-GB')
		const newEvent = {
			bookingName: bookingName.value.trim(),
			bookingEmail: bookingEmail.value.trim(),
			eventStartTime: date,
			eventDuration: eventCategory.value.eventDuration,
			eventNote: note.value,
			eventCategory: {
				id: eventCategory.value.id
			}
		}
		if (getUserFromToken.value === undefined) {
			guestAddEventToDB(newEvent)
		} else {
			if (getUserFromToken.value.Roles === 'ROLE_student') {
				bookingEmail.value = getUserFromToken.value.sub
			}
			addEventToDB(newEvent)
		}
	}
}

//fetch สำหรับ guest
const guestAddEventToDB = async (newEvent) => {
	let formData = new FormData()
	const blob = new Blob([JSON.stringify(newEvent)], { type: 'application/json' })
	formData.append('file', fileupload.value)
	formData.append('event', blob)
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`, {
		method: 'POST',
		body: formData
	})
	if (res.status === 201) {
		console.log('added sucessfully')
		console.log(newEvent)
		typeOfModal.value = 'addEvent'
		isAddEventSuccess.value = true
		setTimeout(toggleAddEventSuccess, 3000)
		clearInput()
		clearFileInput()
		clearError()
	} else if (res.status == 401) {
		// getRefreshToken()
		typeOfModal.value = ''
	} else console.log('error, cannot be added')
}

//fetch สำหรับ user
const addEventToDB = async (newEvent) => {
	let formData = new FormData()
	const blob = new Blob([JSON.stringify(newEvent)], { type: 'application/json' })
	formData.append('file', fileupload.value)
	formData.append('event', blob)
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`, {
		method: 'POST',
		headers: {
			Authorization: `Bearer ${author}`
		},
		body: formData
	})
	if (res.status === 201) {
		console.log('added sucessfully')
		console.log(newEvent)
		// goSuccess()
		typeOfModal.value = 'addEvent'
		isAddEventSuccess.value = true
		setTimeout(toggleAddEventSuccess, 3000)
		clearInput()
		clearFileInput()
		clearError()
	} else if (res.status == 401) {
		getRefreshToken()
		typeOfModal.value = ''
	} else console.log('error, cannot be added')
}

const clearInput = () => {
	bookingName.value = ''
	bookingEmail.value = ''
	eventCategory.value = ''
	eventStartTime.value = ''
	note.value = ''
	clearFileInput()
}

const clearFileInput = () => {
	let fileInput = document.getElementById('fileInput')
	fileInput.type = 'text'
	fileInput.type = 'file'
	fileupload.value = ''
	isAttachFile.value = false
}
const clearError = () => {
	isBlank.value = false
	isInvalidEmail.value = false
	isInvalidDateFuture.value = false
	isInvalidOverLab.value = false
}

const uploadFile = (e) => {
	fileupload.value = e.target.files[0]
	let maxFileSize = 10 * 1024 * 1024 //10MB
	if (fileupload.value != undefined && fileupload.value.size > maxFileSize) {
		console.log('too big')
		let fileInput = document.getElementById('fileInput')
		fileInput.setCustomValidity('The file size cannot be larger than 10 MB.')
		fileInput.reportValidity()
		fileInput.type = 'text'
		fileInput.type = 'file'
		isAttachFile.value = false
	} else {
		fileInput.setCustomValidity('')
		isAttachFile.value = true
	}
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

	startDTNew = new Date(startDTNew)

	for (let event of filterEvents.value) {
		if (
			!checkOverLab(
				startDTNew,
				event.eventStartTime,
				durationNew,
				event.eventDuration
			)
		)
			return false
	}
	return true
}

const checkOverLab = (startDTNew, startDTOld, durationNew, durationOld) => {
	const endDTOld = new Date(
		new Date(startDTOld.getTime() + Number(durationOld) * 60000)
	)
	const startDanger = new Date(
		new Date(startDTOld.getTime() - Number(durationNew) * 60000)
	)

	if (startDTNew > endDTOld) {
		return true
	} else {
		if (startDTNew < startDanger) {
			return true
		} else {
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
const goSuccess = () => appRouter.push({ name: 'AddSuccess' })
const goAllEvent = () => appRouter.push({ name: 'Page', params: { page: 1 } })
const closeLecAddEventModal = () => {
	isLecAddEvent.value = false
}
const currentDateTime = ref()
const getCurDateTime = () => {
	let fullTime = new Date()
	let date = fullTime.getDate()
	date < 10 ? (date = '0' + date) : ''
	let month = fullTime.getMonth() + 1
	month < 10 ? (month = '0' + month) : ''
	let year = fullTime.getFullYear()
	let hours = fullTime.getHours()
	hours < 10 ? (hours = '0' + hours) : ''
	let min = fullTime.getMinutes()
	min < 10 ? (min = '0' + min) : ''
	currentDateTime.value =
		year + '-' + month + '-' + date + 'T' + hours + ':' + min
}

const toggleAddEventSuccess = () => {
	if (isAddEventSuccess.value === true) {
		isAddEventSuccess.value = false
		// getUsers()
	} else {
		isAddEventSuccess.value = true
	}
}
</script>

<template>
	<div>
		<NoLoginModal v-if="is401" />
		<BaseNavBar />
		<div id="add-event-container" v-if="!is401">
			<div id="add">
				<div class="error pb-2" v-if="isBlank">
					Please fill out the information completely. | กรุณากรอกข้อมูลให้ครบด้วยค่ะ
				</div>
				<div class="error pb-2" v-if="isInvalidEmail">
					Enter incorrect email information. | กรุณากรอก email ให้ถูกต้อง (ตัวอย่าง:
					example@gmail.com)
				</div>
				<div class="error pb-2" v-if="isInvalidDateFuture">
					Appointment time in the past. | วันเวลานัดหมายไม่ถูกต้อง
				</div>
				<div class="error pb-2" v-if="isInvalidOverLab">
					Have an appointment during this time. | มีการนัดในช่วงเวลานี้
				</div>
				<div>
					<label for="name">Booking nameInput</label><span class="error"> * </span>
					<span class="lenght">100 character | 100 ตัวอักษร</span>
					<input
						class="form-control mb-3"
						type="text"
						v-model="bookingName"
						maxlength="100"
					/>

					<div>
						<label for="email">Booking Email address</label>
						<span class="error"> * </span>
						<span class="lenght">100 character | 100 ตัวอักษร</span>
						<div class="flex">
							<input
								v-if="
									getUserFromToken != undefined &&
									getUserFromToken.Roles === 'ROLE_student'
								"
								id="email"
								name="email"
								type="text"
								class="form-control mb-3"
								:value="getUserFromToken.sub"
								readonly
							/>
							<input
								v-else
								id="email"
								name="email"
								type="email"
								class="form-control mb-3"
								v-model="bookingEmail"
								maxlength="100"
							/>
						</div>
					</div>
				</div>
				<div class="selectcategory">
					<div>
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

						<span class="error"> *</span>&emsp; Duration
						<span class="border-2 border-gray-200 rounded-md p-1 px-2 bg-gray-100">
							{{ eventDuration }}
						</span>
						<br />
						<br />
					</div>

					Appointment Date&Time <span class="error">*</span> <br />
					<input
						id="date"
						type="datetime-local"
						v-model="eventStartTime"
						class="border-2 border-gray-200 rounded-md p-1 px-2 mt-1 bg-white"
						:min="currentDateTime"
					/>
				</div>
				<br />
				Add Note
				<span class="lenght">500 character | 500 ตัวอักษร</span>
				<input
					class="form-control"
					type="text"
					id="bookingname"
					v-model="note"
					maxlength="500"
				/>
				<div class="file">
					<span>Attach File : </span>
					<input type="file" id="fileInput" @change="uploadFile($event)" />
					<p
						@click="clearFileInput"
						class="text-gray-500 hover:text-red-700 hover:cursor-pointer hover:underline text-sm"
						v-if="isAttachFile"
					>
						Remove Selected File
					</p>
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
						@click="clearInput"
					>
						CLEAR INPUT
					</button>
				</div>
			</div>
		</div>
		<LecAddEventModal v-if="isLecAddEvent" @closeModal="closeLecAddEventModal" />
		<!-- <AddEventSuccessModal v-if="isAddEventSuccess" /> -->
		<SuccessModal v-if="isAddEventSuccess" :typeOfModal="typeOfModal" />
	</div>
</template>

<style>
#add-event-container {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
	padding: 3% 0;
}
#add {
	width: 78%;
	min-height: 550px;
	padding: 30px 40px;
	color: black;
	border-radius: 10px;
}

#click {
	display: flex;
	justify-content: center;
	margin-top: 45px;
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
input {
	margin-top: 5px;
}
.file {
	margin-top: 16px;
}
</style>
