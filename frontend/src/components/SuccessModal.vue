<script setup>
const props = defineProps({
	typeOfModal: {
		type: String,
		default: ''
	}
})
</script>

<template>
	<div>
		<div
			class="top-0 left-0 bg-black/70 fixed w-full h-full flex justify-center items-center"
		>
			<div class="border-1 bg-white rounded-xl min-w-[250px]">
				<div class="p-6 my-6 text-center text-black border-y-2 border-gray-300">
					<i class="fa fa-check-circle fa-4x text-green-700"></i>
					<h3 v-if="typeOfModal === 'addUser'">Add User Success</h3>
					<h3 v-if="typeOfModal === 'editUser'">Edit User Success</h3>
					<h3 v-if="typeOfModal === 'deleteUser'">Delete User Success</h3>
					<h3 v-if="typeOfModal === 'addEvent'">Add Event Success</h3>
					<h3 v-if="typeOfModal === 'deleteEvent'">Delete Event Success</h3>
					<h3 v-if="typeOfModal === 'login'">Login Successful</h3>
					<h3 v-if="typeOfModal === 'reschedule'">Reschedule Success</h3>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
