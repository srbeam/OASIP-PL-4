<script setup>
import { ref, onBeforeMount, resolveDirective } from 'vue'
import ListallComponent from '../components/ListallComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import NoLoginModal from '../components/NoLoginModal.vue'
import SuccessModal from '../components/SuccessModal.vue'

const events = ref([])
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()
const is403 = ref()
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
	} else if (res.status == 401) {
		getRefreshToken()
	} else if (res.status === 403) {
		is403.value = true
	} else console.log('error, cannot get data')
}
onBeforeMount(async () => {
	await getEvents()
})

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
		// window.location.reload()
		is401.value = true
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}

const formatDate = (dateTime) => {
	return dateTime.toLocaleString('en-US', {
		day: '2-digit',
		month: 'long',
		year: 'numeric'
	})
}

const formatTime = (dateTime) => {
	return dateTime.toLocaleString('th-TH', { hour: '2-digit', minute: '2-digit' })
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
</script>

<template>
	<div>
		<BaseNavBar />
		<NoLoginModal v-if="is401" />
		<ListallComponent
			v-else
			:events="events"
			@deleteEvent="deleteEvent"
		></ListallComponent>
		<SuccessModal v-if="isDeleteEventSuccess" :typeOfModal="typeOfModal" />
	</div>
</template>

<style scoped></style>
