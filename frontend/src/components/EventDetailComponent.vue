<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import VueJwtDecode from 'vue-jwt-decode'
import SuccessModal from './SuccessModal.vue'

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

const isEditMode = ref(false)

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let currentDateTime = ref('')
const newStartTime = ref('')
const newNote = ref('')

const isBlank = ref(false)
const isInvalidDateFuture = ref(false)
const isInvalidOverLab = ref(false)

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
	console.log(category)
	console.log(startDTNew)
	console.log(durationNew)
	filterCategory(category)
	console.log(filterEvents.value)
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

	if (startDTNew >= endDTOld) {
		return true
	} else {
		if (startDTNew <= startDangerRange) {
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
const isDeleteFile = ref(false)

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
		const date = new Date(newStartTime.value).toLocaleString('en-GB')
		const updateData = {
			id: event.value.id,
			bookingName: event.value.bookingName.trim(),
			bookingEmail: event.value.bookingEmail.trim(),
			eventStartTime: date,
			eventDuration: event.value.eventDuration,
			eventNote: newNote.value,
			eventCategory: {
				id: event.value.eventCategory.id
			},
			isDeleteFile: isDeleteFile.value
		}
		rescheduleEvent(updateData)
	}
}
const isRescheduleSuccess = ref()
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()

const rescheduleEvent = async (updateData) => {
	let formData = new FormData()
	const blob = new Blob([JSON.stringify(updateData)], {
		type: 'application/json'
	})
	formData.append('file', newFile.value)
	formData.append('event', blob)

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
	newFile.value = null
	isBlank.value = false
	isInvalidDateFuture.value = false
	isRemoveFile.value = false
}
const editMode = () => {
	newStartTime.value = formatDateTime(event.value.eventStartTime)
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

let dataTransfer = new DataTransfer()
const clearFileInput = () => {
	fileupload.value = ''
	isAttachFile.value = false
	dataTransfer.items.clear()
	isFileToobig.value = false
	isChang.value = false
}

const isAttachFile = ref()
const fileupload = ref()
const newFile = ref()

const isRemoveFile = ref(false)
const isChang = ref(false)
const deleteFile = () => {
	isRemoveFile.value = true
	newFile.value = null
	isChang.value = true
	isDeleteFile.value = true
}
const cancelRemoveFile = () => {
	isRemoveFile.value = false
	isDeleteFile.value = false
}
</script>

<template>
	<div>
		<div class="flex flex-col justify-center items-center p-4">
			<p
				@click="goBack"
				class="text-[#495ab6] cursor-pointer hover:text-[#6578e4] w-full lg:hidden"
			>
				&lt;&lt; back
			</p>
			<p
				@click="goBack"
				class="text-[#495ab6] cursor-pointer hover:text-[#6578e4] w-full hidden lg:flex ml-36 xl:hidden"
			>
				&lt;&lt; back
			</p>
			<p
				@click="goBack"
				class="text-[#495ab6] cursor-pointer hover:text-[#6578e4] w-full hidden lg:hidden xl:flex ml-96"
			>
				&lt;&lt; back
			</p>
			<div
				class="text-[#383838] border border-solid border-zinc-300 shadow-md rounded-lg w-full lg:w-[85%] xl:w-8/12 2xl:w-7/12"
			>
				<div class="lg:h-full text-center p-4 flex flex-col lg:flex-row">
					<div class="lg:border-r-4 lg:border-zinc-300 lg:px-10">
						<img
							src="../assets/images/user2.png"
							alt="user"
							width="180"
							class="mx-auto"
						/>

						<div class="mt-4">
							<div class="flex flex-col">
								<h5>{{ event.bookingName }}</h5>
								<span>{{ event.bookingEmail }}</span>
							</div>
							<span class="text-sm" v-if="event.eventCategory != undefined">
								{{ event.eventCategory.eventCategoryName }}
							</span>
						</div>
					</div>

					<div
						class="lg:flex lg:flex-col lg:w-full mt-3 border-t-2 border-zinc-300 text-left lg:border-0 lg:pl-10"
						v-if="!isEditMode"
					>
						<div>
							<p class="mt-3 mb-0 font-semibold lg:hidden">Appointment Date / Time</p>
							<p class="m-0 font-semibold hidden lg:flex">Appointment Date / Time</p>
							<div v-if="event.eventStartTime != null" class="lg:hidden">
								<p class="m-0">
									<span class="font-semibold">Date : </span>
									{{ formatDate(event.eventStartTime) }}
								</p>
								<p class="m-0">
									<span class="font-semibold">Time : </span
									>{{ formatTime(event.eventStartTime) }} min
								</p>
							</div>
							<div
								v-if="event.eventStartTime != null"
								class="hidden lg:flex lg:flex-col"
							>
								<p class="my-3">
									<span class="font-semibold">Date : </span>
									{{ formatDate(event.eventStartTime) }}
								</p>
								<p class="mb-3">
									<span class="font-semibold">Time : </span
									>{{ formatTime(event.eventStartTime) }} min
								</p>
							</div>

							<span
								class="lg:bg-[#eee385] lg:p-1 lg:px-4 lg:rounded-full lg:text-center"
							>
								<span class="font-semibold lg:font-normal">Duration : </span
								>{{ event.eventDuration }} min
							</span>

							<div class="lg:mt-4">
								<span class="font-semibold">Note : </span>
								<span v-if="event.eventNote != undefined && event.eventNote.length > 0">
									{{ event.eventNote }}
								</span>
								<span v-else>No note</span>
							</div>
							<div v-if="event.fileName != null" class="lg:my-4">
								<span class="font-semibold">Attachment : </span>

								<a
									class="text-blue-500 cursor-pointer"
									@click="downloadFile(event.id, event.fileName)"
									>{{ event.fileName }}</a
								>
							</div>
						</div>
						<div class="lg:flex lg:justify-end lg:items-end lg:h-full">
							<button
								class="bg-[#ff9f0f] hover:bg-[#ffa825] text-white rounded-lg p-2 px-3 mt-3 w-full lg:w-auto"
								@click="editMode"
								v-if="
									getUserFromToken != undefined &&
									getUserFromToken.Roles != 'ROLE_lecturer'
								"
							>
								Reschedule
							</button>
						</div>
					</div>

					<!-- edit mode -->
					<div
						class="mt-4 border-t-2 border-zinc-300 lg:w-full lg:px-10 lg:pb-8 text-left lg:border-0"
						v-else
					>
						<p class="text-red-600 mt-4" v-if="isBlank">
							Please select date and time or click cancel. |
							กรุณาเลือกระบุเวลาใหม่ที่ต้องการเลือก หรือ กดยกเลิก
						</p>
						<p class="text-red-600 mt-4" v-if="isInvalidDateFuture">
							Appointment time in the past. | วันเวลานัดหมายไม่ถูกต้อง
						</p>
						<p class="text-red-600 mt-4" v-if="isInvalidOverLab">
							Have an appointment during this time. | มีการนัดในช่วงเวลานี้
						</p>
						<div class="mb-3 mt-3">
							Appointment Date / Time <span class="text-red-600 text-xl">*</span><br />
							<input
								id="date"
								type="datetime-local"
								:min="currentDateTime"
								v-model="newStartTime"
								class="form-control"
							/>
						</div>

						<div class="mb-3">
							Note :
							<span class="text-sm"
								>(No more than 100 characters / ไม่เกิน 100 ตัวอักษร)</span
							>
							<input
								class="form-control"
								type="text"
								maxlength="100"
								v-model="newNote"
							/>
						</div>
						<div class="mb-3">
							<div>
								<span>Attachment : </span>

								<span v-if="isRemoveFile" class="line-through">
									{{ event.fileName }}</span
								>
								<span v-else> {{ event.fileName }}</span>
								<span v-if="event.fileName != null">
									<span
										class="text-blue-600 cursor-pointer ml-2 text-xs"
										@click="cancelRemoveFile"
										v-if="isRemoveFile"
										>recover</span
									>
									<span
										v-else
										class="text-red-600 cursor-pointer ml-2 text-xs"
										@click="deleteFile"
										>remove</span
									>
								</span>
							</div>

							<div class="">
								<div
									class="flex flex-col justify-center items-center h-32 px-4 bg-white border-2 border-gray-300 hover:border-gray-500 border-dashed rounded-md appearance-none focus:outline-none"
								>
									<label
										class="flex justify-center items-center w-full h-full cursor-pointer"
									>
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

						<div id="button" class="text-right text-white">
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
		<SuccessModal v-if="isRescheduleSuccess" typeOfModal="reschedule" />
	</div>
</template>

<style scoped></style>
