<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, onBeforeMount } from 'vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import NoLoginModal from '../components/NoLoginModal.vue'
let { params } = useRoute()

const currentPage = Number(params.page)
const totalPage = ref(0)

const appRouter = useRouter()

const response = ref([])
const events = ref([])
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const getEvents = async () => {
	const res = await fetch(
		`${import.meta.env.VITE_BACK_URL}/events/page?page=${
			currentPage - 1
		}&pageSize=6`,
		{
			method: 'GET',
			headers: {
				Authorization: `Bearer ${author}`
			}
		}
	)
	if (res.status === 200) {
		response.value = await res.json()
		events.value = response.value.content
		totalPage.value = response.value.totalPages
		for (let event of events.value) {
			event.eventStartTime = new Date(event.eventStartTime)
			// console.log(event.eventStartTime)
		}
	} else if (res.status == 401) {
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
			appRouter.go(0)
			console.log('deleted successfully')
		} else console.log('error, cannot delete data')
	}
}
const is401 = ref()
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
</script>

<template>
	<div>
		<NoLoginModal v-if="is401" />
		<BaseNavBar />
		<div class="content" v-if="!is401">
			<div id="no-events" v-show="events.length == 0">
				<h1 class="">No Scheduled Events</h1>
				<router-link :to="{ name: 'AddEvent' }" @click="checkToken">
					<button class="menu">Let's Reserve</button>
				</router-link>
			</div>

			<div>
				<div id="have-events" v-show="events.length > 0">
					<div
						id="showall"
						v-for="(event, index) in events"
						:key="index"
						class="rounded-md p-4 my-2 w-96"
					>
						<div id="detailevents" class="">
							<p class="text-xl font-bold">{{ event.bookingName }}</p>
							<p>{{ event.eventCategoryName }}</p>
							<p class="font-semibold">Appointment Date / Time</p>
							<div>
								<p>Date : {{ formatDate(event.eventStartTime) }}</p>
								<p>Time : {{ formatTime(event.eventStartTime) }} min.</p>
							</div>
							<div id="duration">
								<p>Duration : {{ event.eventDuration }} min</p>
							</div>
							<div class="detail-and-bin-btn">
								<router-link
									:to="{ name: 'EventDetail', params: { id: event.id } }"
									class="detail"
								>
									<button
										class="border-2 border-gray-500 p-2 rounded-lg text-xs text-gray-500 hover:bg-gray-500 hover:text-white"
									>
										see more >
									</button>
								</router-link>

								<div
									id="bin"
									@click="deleteEvent(event.id, event.bookingName, event.eventStartTime)"
								>
									<img src="../assets/images/trash.png" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="paging" v-if="events.length > 0">
		<router-link :to="{ name: 'Page', params: { page: currentPage - 1 } }">
			<button class="prev" :disabled="currentPage == 1">&lt;&lt;</button>
		</router-link>
		<div v-for="page in totalPage">
			<router-link :to="{ name: 'Page', params: { page: page } }">
				<button
					class="px-3 p-1 mx-2"
					:class="currentPage == page ? 'active' : 'pageBtn'"
				>
					{{ page }}
				</button>
			</router-link>
		</div>
		<div class="page2">
			<router-link :to="{ name: 'Page', params: { page: currentPage + 1 } }">
				<button class="next" :disabled="currentPage == totalPage">>></button>
			</router-link>
		</div>
	</div>
	<br />
	<br />
</template>

<style scoped>
.content {
	display: flex;
	justify-content: center;
	padding-top: 2%;
	min-height: 720px;
}
#no-events {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	border-radius: 10px;
	color: #495ab6;
}

#have-events {
	display: grid;
	grid-template-columns: auto auto auto;
	grid-row-gap: 10px;
	grid-column-gap: 25px;
}

#paging {
	margin-top: 2%;
	color: white;
	display: flex;
	justify-content: center;
	align-items: center;
}

.pageBtn {
	border: white 0.1em solid;
	margin: 0.5%;
	border-radius: 0.2em;
	font-size: 1.1em;
	color: white;
	background-color: lightgray;
}

.active {
	border: white 0.1em solid;
	margin-top: 0.5%;
	border-radius: 0.2em;
	font-size: 1.1em;
	/* color: black; */
	/* background-color: whitesmoke; */
	background-color: #495ab6;
	color: white;
}
#showall {
	color: black;
	box-shadow: 2px 1px 6px 0 rgba(0, 0, 0, 0.2);
	font-size: 14px;
	border-radius: 10px;
	border: 1px solid lightgray;
}
#duration p {
	margin: 0;
	background-color: #eee385;
	padding: 5px;
	text-align: center;
	border-radius: 20px;
	width: 50%;
}
.detail-and-bin-btn {
	display: flex;
	justify-content: center;
	margin-top: 15px;
}
.detail {
	width: 100%;
	display: flex;
	align-items: center;
}
#bin {
	width: 100%;
	display: flex;
	justify-content: right;
	align-items: center;
}
#bin img {
	width: 20%;
	cursor: pointer;
}

.next,
.prev {
	color: #495ab6;
	cursor: pointer;
}
.next:hover,
.prev:hover {
	color: #5466cb;
}
.menu {
	background-color: #495ab6;
	color: white;
	padding: 8px 20px;
	border-radius: 20px;
	margin-top: 10px;
}
.menu:hover {
	background-color: #5668d0;
}
</style>
