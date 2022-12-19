<script setup>
import { useRoute } from 'vue-router'
import { ref, onBeforeMount, onBeforeUpdate, onUpdated } from 'vue'
import EventDetailComponent from '../components/EventDetailComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import { useRouter } from 'vue-router'
const appRouter = useRouter()
let { params } = useRoute()
const eventId = params.id
const event = ref({})
const events = ref([])
const token = ref()
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')

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
	} else if (res.status == 404 || res.status == 403) {
		appRouter.push({ name: 'AddEvent' })
	} else if (res.status == 401) {
		getRefreshToken()
	} else console.log('error, cannot get data')
}

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
		token.value = await res.json()
		saveLocal()
	} else if (res.status === 401) {
		localStorage.clear()
		appRouter.push({ name: 'Home' })
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}
const getUserFromToken = ref()
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
	getUserFromToken.value = VueJwtDecode.decode(token.value.accessToken)
	localStorage.setItem('role', `${getUserFromToken.value.Roles}`)
}
onBeforeMount(async () => {
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
	</div>
</template>

<style></style>
