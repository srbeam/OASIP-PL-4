<script setup>
const signOut = () => {
	localStorage.clear()
}
</script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="modal-content">
					<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
					<h3>Admin Only</h3>
					<p>Please sing in with admin account to access this page</p>
					<router-link :to="{ name: 'ListAll' }">
						<button class="view-event-btn">View Event</button>
					</router-link>
					<router-link :to="{ name: 'Home' }">
						<button @click="signOut">Sing in again with admin accout</button>
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
	margin: 25px 0;
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
	background-color: red;
	display: flex;
	justify-content: right;
	align-items: center;
}
.close p {
	background-color: green;
	width: 5%;
	text-align: center;
	margin: 0;
}
.view-event-btn {
	margin-bottom: 10px;
	background-color: #3b9afa;
}
.view-event-btn:hover {
	background-color: #499ff5;
}
</style>
