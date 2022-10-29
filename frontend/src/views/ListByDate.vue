<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ListallComponent from '../components/ListallComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import NoLoginModal from '../components/NoLoginModal.vue'
import SuccessModal from '../components/SuccessModal.vue'

const events = ref([])
const filterEvents = ref([])
const showNoEvent = ref('Please select your filter')
const appRouter = useRouter()
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()
const getEvents = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${author}`
		}
	})
	if (res.status === 200) {
		events.value = await res.json()
		filterEvents.value = events.value.reverse()
		for (let event of events.value) {
			event.eventStartTime = new Date(event.eventStartTime)
			// const dd = new Date(event.eventStartTime)
			// console.log(event.eventStartTime)
		}
		console.log(filterEvents.value)
	} else if (res.status === 401) {
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
onBeforeMount(async () => {
	await getEvents()
})

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

const isDeleteEventSuccess = ref()
const typeOfModal = ref()
const deleteEvent = async (eventId, bookingName, eventStartTime) => {
	let confirms = confirm(
		`Do you want to delete? \n"${bookingName}" \nAppointment : ${formatDate(
			eventStartTime
		)} ${formatTime(eventStartTime)}`
	)

	if (confirms) {
		const res = await fetch(
			`${import.meta.env.VITE_BACK_URL}/events/${eventId}`,
			{
				// method: 'DELETE',
				// Authorization: `Bearer ${author}`
				method: 'DELETE',
				headers: {
					Authorization: `Bearer ${author}`
				}
			}
		)
		if (res.status === 200) {
			typeOfModal.value = 'deleteEvent'
			isDeleteEventSuccess.value = true
			setTimeout(toggleDeleteEventSuccess, 3000)
			getEvents()
			events.value = events.value.filter((event) => event.id !== eventId)
			// appRouter.go(0)
			// console.log('deleted successfully')
		} else if (res.status === 401) {
			getRefreshToken()
		} else console.log('error, cannot delete data')
	}
}
const toggleDeleteEventSuccess = () => {
	if (isDeleteEventSuccess.value === true) {
		isDeleteEventSuccess.value = false
		// getUsers()
	} else {
		isDeleteEventSuccess.value = true
	}
}
const optionSelected = ref('')
const dateSelected = ref('')

const search = () => {
	// console.log(optionSelected.value)
	const currentDateTime = new Date()
	// console.log(currentDateTime)
	if (optionSelected.value == 'past') {
		filterEvents.value = events.value.filter(
			(item) => item.eventStartTime < currentDateTime
		)
		filterEvents.value = filterEvents.value.reverse()

		if (filterEvents.value.length == 0) {
			showNoEvent.value = 'No Past Events'
		}
	} else if (optionSelected.value == 'upcomming') {
		filterEvents.value = events.value.filter(
			(item) => item.eventStartTime > currentDateTime
		)

		if (filterEvents.value.length == 0) {
			showNoEvent.value = 'No On-Going or Upcomming Events'
		}
	} else {
		dateSelected.value = new Date(dateSelected.value)
		filterEvents.value = events.value.filter((item) => {
			return (
				dateSelected.value.getYear() == item.eventStartTime.getYear() &&
				dateSelected.value.getMonth() == item.eventStartTime.getMonth() &&
				dateSelected.value.getDate() == item.eventStartTime.getDate()
			)
		})

		if (filterEvents.value.length == 0) {
			showNoEvent.value = 'No Schedule Events'
		}
	}
}

const cancel = () => {
	optionSelected.value = ''
	filterEvents.value = events.value
	showNoEvent.value = 'Please select your filter'
	// console.log(filterEvents.value)
}
</script>

<template>
	<div>
		<BaseNavBar />
		<div id="content">
			<NoLoginModal v-if="is401" />
			<div class="listByDate-container" v-else>
				<div id="searchBar">
					<div id="searchdate">
						<a> Search by date : </a>
						<select
							v-model="optionSelected"
							class="border-2 border-gray-200 rounded-md p-1 text text-black w-64"
						>
							<option value="" disabled>Select your filter</option>
							<option value="past">Past Event</option>
							<option value="upcomming">Upcomming Event</option>
							<option value="day">Choose by day</option>
						</select>
						<input
							type="date"
							class="border-2 border-gray-200 rounded-md p-1 text ml-4 text-black"
							v-if="optionSelected == 'day'"
							v-model="dateSelected"
						/>
					</div>

					<div class="" id="buttonsearch">
						<button
							class="bg-green-600 hover:bg-green-700 p-2 px-3 rounded-md"
							@click="search"
						>
							search
						</button>
						<button
							class="bg-red-600 hover:bg-red-700 p-2 px-3 ml-3 rounded-md"
							@click="cancel"
						>
							cancel
						</button>
					</div>
				</div>

				<div id="no-events" class="mt-7" v-if="filterEvents.length == 0">
					<h1>{{ showNoEvent }}</h1>
				</div>

				<div v-if="filterEvents.length > 0">
					<ListallComponent
						:events="filterEvents"
						@deleteEvent="deleteEvent"
					></ListallComponent>
				</div>
			</div>
		</div>
		<SuccessModal v-if="isDeleteEventSuccess" :typeOfModal="typeOfModal" />
	</div>
</template>

<style scoped>
#content {
	padding: 2% 0 3% 0;
}
#no-events {
	height: 32em;
	width: 100%;
	text-align: center;
	border-radius: 10px;
	display: flex;
	justify-content: center;
	align-items: center;
	color: #495ab6;
}
.listByDate-container {
	/* padding: 0 8%; */
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
#searchBar {
	/* width: 170vh; */
	margin-bottom: 12px;
	width: 100%;
	display: flex;
	/* padding-left: 11%; */

	width: 80%;
	justify-content: right;
}
#buttonsearch {
	margin-left: 20px;
}
#searchdate {
	color: #495ab6;
}
</style>
