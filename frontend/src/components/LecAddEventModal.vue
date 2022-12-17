<script setup>
defineEmits(['closeModal'])
</script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="close">
					<p @click="$emit('closeModal')">X</p>
				</div>
				<div class="modal-content">
					<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
					<h3>Sorry</h3>
					<p>Lecturer can not add event</p>
					<router-link :to="{ name: 'ListAll' }">
						<button class="view-event-btn">View Event</button>
					</router-link>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
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
	border: 1px solid black;
	background-color: white;
	border-radius: 10px;
	min-width: 250px;
}
.modal-content {
	text-align: center;
	margin: 0px 0 25px 0;
	padding: 25px;
}
i {
	color: brown;
	font-size: 50px;
	margin-bottom: 20px;
}
h3 {
	color: black;
}
p {
	color: brown;
}
button {
	background-color: orange;
	border-radius: 5px;
	padding: 5px 10px;
	width: 100%;
	color: white;
}
button:hover {
	background-color: rgb(255, 183, 48);
}
a:hover {
	padding: 0;
}
.close {
	display: flex;
	justify-content: right;
	align-items: center;
	padding: 10px;
	padding-bottom: 0;
}
.close p {
	width: 5%;
	text-align: center;
	/* margin: 0; */
}
.view-event-btn {
	margin-bottom: 10px;
	background-color: #3b9afa;
}
.view-event-btn:hover {
	background-color: #499ff5;
}
</style>
