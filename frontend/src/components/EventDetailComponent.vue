<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import RescheduleSuccess from './RescheduleSuccess.vue'

import VueJwtDecode from 'vue-jwt-decode'

const getUserFromToken = ref()
const getUser = () => {
	getUserFromToken.value = VueJwtDecode.decode(localStorage.getItem('token'))
}

onBeforeMount(async () => {
	await getUser()
})
defineEmits(['back'])
const props = defineProps({
	pevent: {
		type: Object,
		default: {}
	},
	pevents: {
		type: Array,
		default: []
	}
})

const event = computed(() => props.pevent)
const events = computed(() => props.pevents)
// const event = computed(() => props.event)
// const events = computed(() => props.events)

const isEditMode = ref(false)

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let currentDateTime = ref('')
const newStartTime = ref('')
const newNote = ref('')

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
		// const date = new Date(newStartTime.value)
		const date = new Date(newStartTime.value).toLocaleString('en-GB')
		// const updateStartTime = [
		// 	date.getFullYear(),
		// 	date.getMonth() + 1,
		// 	date.getDate(),
		// 	date.getHours(),
		// 	date.getMinutes()
		// ]
		// const updateData = {
		// 	id: event.value.id,
		// 	eventStartTime: updateStartTime,
		// 	eventNote: newNote.value,
		// 	eventDuration: event.value.eventDuration,
		// 	eventCategory: event.value.eventCategory
		// }
		const updateData = {
			id: event.value.id,
			bookingName: event.value.bookingName.trim(),
			bookingEmail: event.value.bookingEmail.trim(),
			eventStartTime: date,
			eventDuration: event.value.eventDuration,
			eventNote: newNote.value,
			eventCategory: {
				id: event.value.eventCategory.id
			}
		}
		console.log(updateData)
		rescheduleEvent(updateData)
		// rescheduleEvent(
		// 	updateStartTime,
		// 	newNote.value,
		// 	event.value.eventCategory,
		// 	event.value.id
		// )
	}
}
const isRescheduleSuccess = ref()
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()
const filePath = ref()
const rescheduleEvent = async (updateData) => {
	let formData = new FormData()
	const blob = new Blob([JSON.stringify(updateData)], {
		type: 'application/json'
	})
	formData.append('file', newFile.value)
	formData.append('event', blob)

	if (isChang.value) {
		const res = await fetch(
			`${import.meta.env.VITE_BACK_URL}/events/${updateData.id}`,
			{
				method: 'PUT',
				headers: {
					Authorization: `Bearer ${author}`
				},
				body: formData
			}
		)

		if (res.status === 200) {
			isEditMode.value = false
			isRescheduleSuccess.value = true
			setTimeout(toggleRescheduleSuccess, 3000)
		} else if (res.status == 401) {
			getRefreshToken()
			isRescheduleSuccess.value = false
		} else console.log('error, cannot be added')
	} else {
		isEditMode.value = false
		isRescheduleSuccess.value = true
		setTimeout(toggleRescheduleSuccess, 3000)
	}
}
// const rescheduleEvent = async (
// 	updateStartTime,
// 	updateNote,
// 	eventCategory,
// 	eventId
// ) => {
// 	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/${eventId}`, {
// 		method: 'PUT',
// 		headers: {
// 			'content-type': 'application/json',
// 			Authorization: `Bearer ${author}`
// 		},
// 		body: JSON.stringify({
// 			eventStartTime: updateStartTime,
// 			eventNote: updateNote,
// 			eventDuration: event.value.eventDuration,
// 			eventCategory: eventCategory
// 		})
// 	})
// 	console.log(updateStartTime)
// 	console.log(updateNote)

// 	if (res.status === 200) {
// 		console.log('edited successfully')
// 		// cancel()

// 		isEditMode.value = false
// 		isRescheduleSuccess.value = true
// 		setTimeout(toggleRescheduleSuccess, 3000)
// 	} else if (res.status == 401) {
// 		getRefreshToken()
// 		isRescheduleSuccess.value = false
// 	} else console.log('error, cannot be added')
// }
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
	// console.log(newStartTime.value)
	isEditMode.value = true
	newNote.value = event.value.eventNote
	isChang.value = false
	isFileToobig.value = false
	newFile.value = null
	isAttachFile.value = false
}

const toggleRescheduleSuccess = () => {
	if (isRescheduleSuccess.value === true) {
		isRescheduleSuccess.value = false
		goBack()
	} else {
		isRescheduleSuccess.value = true
	}
}

const downloadFile = async (eventId, fileName) => {
	const res = await fetch(
		`${import.meta.env.VITE_BACK_URL}/downloadFile/${eventId}/${fileName}`,
		{
			method: 'GET',
			headers: {
				Authorization: `Bearer ${author}`
			}
		}
	)
	if (res.status === 200) {
		let file = await res.blob()
		let tempUrl = URL.createObjectURL(file)
		let a = document.createElement('a')
		document.body.appendChild(a)
		a.style = 'display: none'
		a.href = tempUrl
		a.setAttribute('download', fileName)
		a.click()
		window.URL.revokeObjectURL(tempUrl)
	} else if (res.status == 401) {
		getRefreshToken()
	} else {
		console.log('Error,cannot download file')
	}
}
const isFileToobig = ref()
const editFile = (e) => {
	let maxFileSize = 10 * 1024 * 1024 //10MB
	if (e.target.files[0] != undefined) {
		if (e.target.files[0].size > maxFileSize) {
			isFileToobig.value = true
			setTimeout(() => (isFileToobig.value = false), 4000)
			if (newFile.value === undefined || newFile.value === '') {
				clearFileInput()
				isAttachFile.value = false
				isFileToobig.value = true
				isChang.value = false
				setTimeout(() => (isFileToobig.value = false), 4000)
			}
		} else {
			fileupload.value = e.target.files[0]
			isAttachFile.value = true
			newFile.value = fileupload.value
			isFileToobig.value = false
			isChang.value = true
		}
	} else {
		isAttachFile.value = false
		isChang.value = false
	}
}
// const editFile = (e) => {
// 	let maxFileSize = 10 * 1024 * 1024 //10MB
// 	if (e.target.files[0] != undefined) {
// 		if (e.target.files[0].size > maxFileSize) {
// 			isFileToobig.value = true
// 			setTimeout(() => (isFileToobig.value = false), 4000)
// 			if (fileupload.value === undefined || fileupload.value === '') {
// 				clearFileInput()
// 				isAttachFile.value = false
// 				isFileToobig.value = true
// 				isChang.value = false
// 				setTimeout(() => (isFileToobig.value = false), 4000)
// 			}
// 		} else {
// 			fileupload.value = e.target.files[0]
// 			isAttachFile.value = true
// 			newFile.value = fileupload.value
// 			isFileToobig.value = false
// 			isChang.value = true
// 		}
// 	} else {
// 		isAttachFile.value = false
// 		isChang.value = false
// 	}
// }
let dataTransfer = new DataTransfer()
const clearFileInput = () => {
	fileupload.value = ''
	isAttachFile.value = false
	dataTransfer.items.clear()
	isFileToobig.value = false
	// newFile.value = ''
	isChang.value = false
}

const isAttachFile = ref()
const fileupload = ref()
const newFile = ref()

// const dragOver = (e) => {
// 	e.stopPropagation()
// 	e.preventDefault()
// }
// const drop = (e) => {
// 	e.stopPropagation()
// 	e.preventDefault()
// 	let maxFileSize = 10 * 1024 * 1024 //10MB
// 	if (e.dataTransfer.files[0] != undefined) {
// 		if (e.dataTransfer.files[0] > maxFileSize) {
// 			isFileToobig.value = true
// 			setTimeout(() => (isFileToobig.value = false), 4000)
// 			if (fileupload.value === undefined || fileupload.value === '') {
// 				clearFileInput()
// 				isAttachFile.value = false
// 				isFileToobig.value = true
// 				isChang.value = false
// 				setTimeout(() => (isFileToobig.value = false), 4000)
// 			}
// 		} else {
// 			fileupload.value = e.dataTransfer.files[0]
// 			isAttachFile.value = true
// 			newFile.value = fileupload.value
// 			isFileToobig.value = false
// 			isChang.value = true
// 		}
// 	} else {
// 		isAttachFile.value = false
// 		isChang.value = false
// 	}
// let maxFileSize = 10 * 1024 * 1024 //10MB
// if (e.dataTransfer.files[0] != undefined) {
// 	if (e.dataTransfer.files[0].size > maxFileSize) {
// 		isFileToobig.value = true
// 		setTimeout(() => (isFileToobig.value = false), 4000)

// 		if (fileupload.value === undefined || fileupload.value === '') {
// 			clearFileInput()
// 			isAttachFile.value = false
// 			isFileToobig.value = true
// 			setTimeout(() => (isFileToobig.value = false), 4000)
// 		} else {
// 		}
// 	} else {
// 		fileupload.value = e.dataTransfer.files[0]
// 		isAttachFile.value = true
// 		newFile.value = fileupload.value
// 		isFileToobig.value = false
// 	}
// } else {
// 	if (fileupload.value === undefined || fileupload.value === '') {
// 		clearFileInput()
// 		isAttachFile.value = false
// 	} else {
// 	}
// }
// }
const isRemoveFile = ref(false)
const isChang = ref(false)
const deleteFile = () => {
	isRemoveFile.value = true
	newFile.value = null
	isChang.value = true
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
					<h5 class="text-sm" v-if="event.eventCategory != undefined">
						{{ event.eventCategory.eventCategoryName }}
					</h5>
				</div>

				<div class="basis-1/2 p-10 ml-4" v-if="!isEditMode">
					<p class="font-semibold">Appointment Date / Time</p>
					<div v-if="event.eventStartTime != null">
						<p>Date : {{ formatDate(event.eventStartTime) }}</p>
						<p>Time : {{ formatTime(event.eventStartTime) }} min</p>
					</div>
					<div id="duration" class="rounded-full w-40 h-8">
						<p class="ml-3 text-sm">Duration : {{ event.eventDuration }} min</p>
					</div>
					<br />
					<div class="note-area">
						<p>Note :&nbsp;</p>
						<p v-if="event.eventNote != undefined && event.eventNote.length > 0">
							{{ event.eventNote }}
						</p>
						<p v-else>No note</p>
					</div>
					<div class="file" v-if="event.fileName != null">
						<span>Attachment : </span>

						<a
							class="text-blue-500 cursor-pointer"
							@click="downloadFile(event.id, event.fileName)"
							>{{ event.fileName }}</a
						>
					</div>

					<button
						id="reschedule"
						class="rounded-lg p-2 px-3"
						@click="editMode"
						v-if="
							getUserFromToken != undefined &&
							getUserFromToken.Roles != 'ROLE_lecturer'
						"
					>
						Reschedule
					</button>
				</div>
				<!------------ edit Mode ------------>
				<div class="basis-1/2 p-10" v-else>
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

					<div class="mb-4">
						Note :
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
					<div class="mb-4">
						<div>
							<span>Attachment : </span>

							<span v-if="isRemoveFile" class="line-through">
								{{ event.fileName }}</span
							>
							<span v-else> {{ event.fileName }}</span>
							<span
								class="text-red-600 cursor-pointer ml-2 text-xs"
								@click="deleteFile"
								v-if="event.fileName != null"
								>remove</span
							>
						</div>

						<div class="max-w-xl">
							<div
								class="flex flex-col justify-center items-center h-32 px-4 bg-white border-2 border-gray-300 hover:border-gray-500 border-dashed rounded-md appearance-none focus:outline-none"
							>
								<label
									class="flex justify-center items-center w-full h-full cursor-pointer"
								>
									<!-- <label
									class="flex justify-center items-center w-full"
									@dragover="dragOver($event)"
									@drop="drop($event)"
								> -->
									<span class="flex flex-col items-center space-x-2">
										<span v-if="isFileToobig" class="text-red-600"
											>The file size cannot be larger than 10 MB.</span
										>
										<div class="flex">
											<svg
												xmlns="http://www.w3.org/2000/svg"
												class="w-6 h-6 text-gray-600"
												fill="none"
												viewBox="0 0 24 24"
												stroke="currentColor"
												stroke-width="2"
											>
												<path
													stroke-linecap="round"
													stroke-linejoin="round"
													d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
												/>
											</svg>

											<span class="font-medium text-gray-600 ml-2">
												<span class="text-blue-600 underline cursor-pointer">Click</span>
												to upload new file
											</span>
										</div>
										<div
											v-if="isAttachFile"
											class="border-2 border-gray-300 rounded-md py-1 px-3 mt-2 flex items-center"
										>
											<i class="fa fa-file" aria-hidden="true"></i>
											<span class="text-sm ml-3">{{ newFile.name }}</span>
										</div>
									</span>
									<input
										type="file"
										name="file_upload"
										class="hidden"
										id="fileInput"
										@change="editFile($event)"
									/>
								</label>
								<span
									@click="clearFileInput"
									class="text-gray-500 hover:text-red-700 hover:cursor-pointer hover:underline text-sm"
									v-if="isAttachFile"
								>
									Remove Selected File
								</span>
							</div>
						</div>
					</div>
					<!-- <div class="mb-4">
						Attachment :

						<input type="file" id="fileInput" @change="uploadFile($event)" />
						<p
							@click="clearFileInput"
							class="text-gray-500 hover:text-red-700 hover:cursor-pointer hover:underline text-sm"
							v-if="isAttachFile"
						>
							Remove Selected File
						</p>
					</div> -->

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
		<RescheduleSuccess v-if="isRescheduleSuccess" />
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
