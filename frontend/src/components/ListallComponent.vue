<script setup>
import { ref, onBeforeMount } from 'vue'
import NoLoginModal from './NoLoginModal.vue'
import VueJwtDecode from 'vue-jwt-decode'
const getUserFromToken = ref()
const getUser = () => {
	getUserFromToken.value = VueJwtDecode.decode(localStorage.getItem('token'))
}
onBeforeMount(async () => {
	await getUser()
})
defineEmits(['deleteEvent'])
const props = defineProps({
	events: {
		type: Array,
		require: true
	}
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
</script>

<template>
	<div class="py-8">
		<h4 class="lg:hidden text-gray-500 text-center" v-show="events.length != 0">
			List All Users
		</h4>
		<div class="flex justify-center">
			<div class="text-[#495ab6]" v-show="events.length == 0">
				<h1>No Scheduled Events</h1>
			</div>

			<div
				v-show="events.length > 0"
				class="md:grid md:grid-cols-2 md:gap-x-14 lg:grid-cols-3 lg:gap-x-10 lg:px-10 xl:px-0"
			>
				<div
					v-for="(event, index) in events"
					:key="index"
					class="rounded-md p-4 my-2 shadow mb-4 w-80"
				>
					<div class="text-[#383838]">
						<p class="text-xl font-bold text-[#495ab6]">{{ event.bookingName }}</p>
						<p>{{ event.eventCategoryName }}</p>
						<div>
							<p>Date : {{ formatDate(event.eventStartTime) }}</p>
							<p>Time : {{ formatTime(event.eventStartTime) }} min.</p>
						</div>

						<p class="bg-[#eee385] p-1 w-2/3 text-center rounded-2xl">
							Duration : {{ event.eventDuration }} min
						</p>

						<div class="flex justify-center mt-3.5">
							<router-link
								:to="{ name: 'EventDetail', params: { id: event.id } }"
								class="w-full justify-center items-center"
							>
								<button
									class="border-2 border-gray-500 p-2 rounded-lg text-xs text-gray-500 hover:bg-gray-500 hover:text-white"
								>
									see more >
								</button>
							</router-link>
							<div
								class="w-full flex justify-end items-center"
								@click="
									$emit('deleteEvent', event.id, event.bookingName, event.eventStartTime)
								"
								v-if="
									getUserFromToken != undefined &&
									getUserFromToken.Roles != 'ROLE_lecturer'
								"
							>
								<img class="cursor-pointer w-1/5" src="../assets/images/trash.png" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
