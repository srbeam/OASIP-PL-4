<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, onBeforeMount, computed } from 'vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import SuccessModal from '../components/SuccessModal.vue'
import ListAllEventsComponent from '../components/ListAllEventsComponent.vue'
let { params } = useRoute()
const appRouter = useRouter()
const id = ref(Number(params.id))
const category = ref('')

switch (id.value) {
	case 1:
		category.value = 'Project Management Clinic'
		break
	case 2:
		category.value = 'DevOps/Infra Clinic'
		break
	case 3:
		category.value = 'Database Clinic'
		break
	case 4:
		category.value = 'Client-side Clinic'
		break
	case 5:
		category.value = 'Server-side Clinic'
		break
}

const events = ref([])
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')

const getEvents = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${author}`
		}
	})
	if (res.status === 200) {
		events.value = await res.json()
		for (let event of events.value) {
			event.eventStartTime = new Date(event.eventStartTime)
		}
	} else if (res.status === 401) {
		getRefreshToken()
	} else console.log('error, cannot get data')
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
			console.log('deleted successfully')
		} else if (res.status === 401) {
			getRefreshToken()
		} else console.log('error, cannot delete data')
	}
}
const toggleDeleteEventSuccess = () => {
	if (isDeleteEventSuccess.value === true) {
		isDeleteEventSuccess.value = false
	} else {
		isDeleteEventSuccess.value = true
	}
}
// const is401 = ref()
const getRefreshToken = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/refresh`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${refreshToken}`
		}
	})
	if (res.status === 200) {
		// is401.value = false
		token.value = await res.json()
		saveLocal()
	} else if (res.status === 401) {
		// is401.value = true
		localStorage.clear()
		appRouter.push({ name: 'Home' })
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}
const filterEvents = computed(() =>
	events.value.filter((event) => {
		return event.eventCategoryName == category.value
	})
)
</script>

<template>
	<div>
		<BaseNavBar />
		<!-- <ListByCategory
			:category="category"
			:events="events"
			@deleteEvent="deleteEvent"
		></ListByCategory> -->

		<ListAllEventsComponent
			:category="category"
			:events="filterEvents"
			@deleteEvent="deleteEvent"
		></ListAllEventsComponent>

		<SuccessModal v-if="isDeleteEventSuccess" :typeOfModal="typeOfModal" />
	</div>
</template>

<style></style>
