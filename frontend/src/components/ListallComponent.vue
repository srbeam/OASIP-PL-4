<script setup>
import { ref, VueElement } from 'vue'
import NoLoginModal from './NoLoginModal.vue'

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
	<div>
		<div class="content">
			<div id="no-events" v-show="events.length == 0">
				<h1>No Scheduled Events</h1>
			</div>
			<div>
				<div v-show="events.length > 0" id="have-events">
					<div
						id="showall"
						v-for="(event, index) in events"
						:key="index"
						class="rounded-md p-4 my-2 w-96"
					>
						<div id="detailevents">
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
									@click="
										$emit(
											'deleteEvent',
											event.id,
											event.bookingName,
											event.eventStartTime
										)
									"
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
</template>

<style scoped>
.content {
	display: flex;
	justify-content: center;
	padding: 2% 0;
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

#showall {
	/* background-color: rgba(255, 255, 255, 0.4); */
	box-shadow: 2px 4px 6px 2px rgba(0, 0, 0, 0.2);
	font-size: 14px;
	border-radius: 10px;
}
#showall:hover {
	background-color: rgba(255, 255, 255, 0.8);
}
#detailevents {
	color: #383838;
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
</style>
