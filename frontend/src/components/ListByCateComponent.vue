<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import VueJwtDecode from 'vue-jwt-decode'

defineEmits(['deleteEvent'])
const props = defineProps({
	category: {
		type: String,
		require: true
	},
	events: {
		type: String,
		require: true
	}
})

const events = computed(() => props.events)
const filterEvents = computed(() =>
	events.value.filter((event) => {
		return event.eventCategoryName == props.category
	})
)

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

const appRouter = useRouter()
const goBack = () => appRouter.push({ name: 'Category' })
const getUserFromToken = ref()
const getUser = () => {
	getUserFromToken.value = VueJwtDecode.decode(localStorage.getItem('token'))
}
onBeforeMount(async () => {
	await getUser()
})
</script>

<template>
	<div>
		<div class="py-8">
			<h4 class="text-gray-500 text-center">{{ category }}</h4>

			<div class="flex justify-center px-10">
				<div v-show="filterEvents.length == 0">
					<button @click="goBack">&lt; back</button>
					<div class="text-[#495ab6] justify-center items-center">
						<h1>No Scheduled Events</h1>
					</div>
				</div>

				<div v-show="filterEvents.length > 0">
					<button @click="goBack">&lt; back</button>

					<div class="md:grid md:grid-cols-2 md:gap-x-14 lg:grid-cols-3">
						<div
							v-for="(event, index) in filterEvents"
							:key="index"
							class="rounded-md p-4 my-2 shadow mb-4 sm:w-80"
						>
							<div class="text-[#383838]">
								<p class="text-xl font-bold text-[#495ab6]">{{ event.bookingName }}</p>
								<p>{{ event.eventCategoryName }}</p>
								<!-- <p class="font-semibold">Appointment Date / Time</p> -->
								<div>
									<p>Date : {{ formatDate(event.eventStartTime) }}</p>
									<p>Time : {{ formatTime(event.eventStartTime) }} min.</p>
								</div>

								<p class="bg-[#eee385] p-1 w-3/4 text-center rounded-2xl">
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
											$emit(
												'deleteEvent',
												event.id,
												event.bookingName,
												event.eventStartTime
											)
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
		</div>
	</div>
</template>

<style scoped></style>
