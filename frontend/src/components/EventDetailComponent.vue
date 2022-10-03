<script setup>
import { ref, computed } from 'vue'
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
	return dateTime.toLocaleString('en-US', {
		day: '2-digit',
		month: 'long',
		year: 'numeric'
	})
}

const formatTime = (dateTime) => {
	return dateTime.toLocaleString('th-Th', { hour: '2-digit', minute: '2-digit' })
}

const formatDateTime = (dateTime) => {
	let options = {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit'
	}
	let tzoffset = new Date().getTimezoneOffset() * 60000 //offset in milliseconds
	const formatDateTime = new Date(dateTime - tzoffset).toISOString().slice(0, -8)
	return formatDateTime
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
	const startDangerRange = new Date(
		new Date(startDTOld.getTime() - Number(durationNew) * 60000)
	)

	console.log(startDTOld)

	if (startDTNew > endDTOld) {
		return true
	} else {
		if (startDTNew < startDangerRange) {
			return true
		} else {
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
	if (newStartTime.value == '') {
		isBlank.value = true
		isInvalidDateFuture.value = false
		isInvalidOverLab.value = false
	} else if (!validateDateFuture(newStartTime.value)) {
		isInvalidDateFuture.value = true
		isBlank.value = false
		isInvalidOverLab.value = false
	} else if (
		!validateNonOverlab(
			event.value.eventCategory.eventCategoryName,
			newStartTime.value,
			event.value.eventDuration
		)
	) {
		isInvalidOverLab.value = true
		isInvalidDateFuture.value = false
		isBlank.value = false
	} else {
		const date = new Date(newStartTime.value)
		const updateStartTime = [
			date.getFullYear(),
			date.getMonth() + 1,
			date.getDate(),
			date.getHours(),
			date.getMinutes()
		]
		const updateNote = newNote.value
		event.value.eventStartTime = date
		event.value.eventNote = newNote.value
		rescheduleEvent(
			updateStartTime,
			updateNote,
			event.value.eventCategory,
			event.value.id
		)
	}
}
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()
const rescheduleEvent = async (
	updateStartTime,
	updateNote,
	eventCategory,
	eventId
) => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/${eventId}`, {
		method: 'PUT',
		headers: {
			'content-type': 'application/json',
			Authorization: `Bearer ${author}`
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
	} else if (res.status == 401) {
		getRefreshToken()
	} else console.log('error, cannot be added')
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
	<div id="root">
		<div class="container">
			<div id="back">
				<p @click="goBack">&lt;&lt; back</p>
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
					<div class="note-area">
						<p>Note :&nbsp;</p>
						<p v-if="event.eventNote.length > 0">{{ event.eventNote }}</p>
						<p v-else>No note</p>
					</div>

					<button id="reschedule" class="rounded-lg p-2 px-3" @click="editMode">
						Reschedule
					</button>
				</div>
				<div class="basis-1/2 p-10" v-if="isEditMode">
					<p class="text-red-600" v-if="isBlank">
						Please select date and time or click cancel. |
						กรุณาเลือกระบุเวลาใหม่ที่ต้องการเลือก หรือ กดยกเลิก
					</p>
					<p class="text-red-600" v-if="isInvalidDateFuture">
						Appointment time in the past. | วันเวลานัดหมายไม่ถูกต้อง
					</p>
					<p class="text-red-600" v-if="isInvalidOverLab">
						Have an appointment during this time. | มีการนัดในช่วงเวลานี้
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
						Add Note :
						<span class="text-sm"
							>(No more than 100 characters / ไม่เกิน 100 ตัวอักษร)</span
						>
						<input
							class="form-control"
							type="text"
							id="bookingname"
							maxlength="100"
							v-model="newNote"
						/>
					</div>

					<div id="button" class="">
						<button
							class="bg-green-600 rounded-lg p-2 px-3 hover:bg-green-700 mr-2"
							@click="confirm"
						>
							Confirm
						</button>
						<button
							class="bg-red-600 rounded-lg p-2 px-3 hover:bg-red-700"
							@click="cancel"
						>
							Cancel
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
#root {
	height: 80vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
.container {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
#back {
	/* margin-left: 2%; */
	/* border: #383838 1px solid; */

	/* width: 100%; */
	/* background-color: #383838; */
	width: 100%;
	margin-left: 200px;
}
#back p {
	color: #495ab6;
	cursor: pointer;
}
#back p:hover {
	color: #6578e4;
}
#showdetail {
	/* background-color: rgba(255, 255, 255, 0.4); */
	color: #383838;
	width: 1100px;
	padding: 20px;
	/* margin-left: 3%; */
	/* margin-top: 1%; */
	/* height: px; */
	border: 1px solid lightgrey;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	border-radius: 10px;
}

#duration {
	background-color: #eee385;
	padding: 3.5px;
}

#reschedule {
	margin-left: 85%;
	color: white;
	background-color: #ff9f0f;
}
#reschedule:hover {
	background-color: #ffa825;
}

#button {
	padding-left: 80%;
	width: 125%;
	color: white;
}
.note-area {
	display: flex;
}
.note-area p {
	font-weight: 400;
}
</style>
