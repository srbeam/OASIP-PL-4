<script setup>
import { useRoute } from 'vue-router'
import { ref, onBeforeMount, onBeforeUpdate, onUpdated } from 'vue'
import EventDetailComponent from '../components/EventDetailComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
let { params } = useRoute()

const eventId = params.id
const event = ref({})
const author = localStorage.getItem('token')
const getEventById = async (eventId) => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/events/${eventId}`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${author}`
		}
	})
	if (res.status === 200) {
		event.value = await res.json()
		event.value.eventStartTime = new Date(event.value.eventStartTime)
	} else if (res.status == 401) {
		getRefreshToken()
	} else console.log('error, cannot get data')
}

const events = ref([])
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
		for (let event of events.value) {
			event.eventStartTime = new Date(event.eventStartTime)
		}
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
onBeforeMount(async () => {
	// await getEventById(eventId)
	await getEvents()
	await getEventById(eventId)
})
</script>

<template>
	<div>
		<BaseNavBar />
		<EventDetailComponent
			:pevent="event"
			:pevents="events"
		></EventDetailComponent>
		<!-- <EventDetailComponent :event="event" :events="events" @formatDate="formatDate" @formatTime="formatTime" @formatDateTiem="formatDateTime"></EventDetailComponent> -->
	</div>
</template>

<style></style>
