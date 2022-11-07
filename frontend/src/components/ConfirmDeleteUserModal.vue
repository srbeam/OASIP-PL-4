<script setup>
defineEmits(['closeModal', 'confirm'])
const props = defineProps({
	userSelected: {
		type: Object,
		default: {}
	}
})
</script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="modal-header">
					<div class="modal-content">
						<i class="fa fa-exclamation-circle fa-4x"></i>

						<!-- <h2>Are you sure ?</h2> -->
						<!-- <p>You will not be able to recover this</p> -->
						<h2>Are you sure ?</h2>
						<p class="text-gray-500 m-0">You want to delete user</p>
						<p class="text-blue-500 m-0">{{ userSelected.name }}</p>
					</div>
					<div class="modal-btn-area">
						<div class="modal-btn">
							<button class="no" @click="$emit('closeModal')">NO</button>
							<button class="yes" @click="$emit('confirm')">YES</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.no {
	background-color: gray;
	width: 50%;
	border-bottom-left-radius: 10px;
	cursor: pointer;
}
.no:hover {
	background-color: rgb(165, 165, 165);
}
.yes {
	background-color: rgb(219, 1, 1);
	width: 50%;
	border-bottom-right-radius: 10px;
	cursor: pointer;
}
.yes:hover {
	background-color: rgb(223, 27, 27);
}
button {
	outline: none;
	border: none;
	padding: 15px 0;
	color: white;

	font-size: 14px;
}
.modal {
	top: 0;
	left: 0;
	background-color: rgba(0, 0, 0, 0.8);
	position: fixed;
	width: 100%;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
}
.modal-container {
	/* border: 1px solid black; */
	background-color: white;
	border-radius: 10px;
	min-width: 300px;
}
.modal-content {
	text-align: center;
	/* margin-bottom: 25px; */
	/* margin-top: 18px; */
	padding: 20px 10px;
	border-radius: 10px;
}
/* .modal-content p {
	color: gray;

	margin: 0;
} */
h2 {
	color: black;
}
i {
	color: gray;
}
.modal-header {
	display: flex;
	flex-direction: column;

	padding: 0;
}
.modal-btn-area {
	width: 100%;
}
</style>
