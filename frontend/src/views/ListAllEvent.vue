<script setup>
import { ref, onBeforeMount } from 'vue'
import ListallComponent from '../components/ListallComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'

const events = ref([])
const author = localStorage.getItem('token')
const getEvents = async () => {
	// const res = await fetch('http://localhost:8080/api/events')
	// const res = await fetch('http://10.4.56.124:8081/api/events')
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
	} else console.log('error, cannot get data')
}
onBeforeMount(async () => {
	await getEvents()
	console.log(events)
})

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

const deleteEvent = async (eventId, bookingName, eventStartTime) => {
	let confirms = confirm(
		`Do you want to delete? \n"${bookingName}" \nAppointment : ${formatDate(
			eventStartTime
		)} ${formatTime(eventStartTime)}`
	)
	console.log(eventId)
	console.log(confirms)
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
			events.value = events.value.filter((event) => event.id !== eventId)
			console.log('deleted successfully')
		} else console.log('error, cannot delete data')
	}
}
</script>

<template>
	<div>
		<BaseNavBar />
		<ListallComponent
			:events="events"
			@deleteEvent="deleteEvent"
		></ListallComponent>
		<!-- <router-view> </router-view> -->
	</div>
</template>

<style scoped></style>
