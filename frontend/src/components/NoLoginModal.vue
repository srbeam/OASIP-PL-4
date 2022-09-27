<script setup></script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="modal-content">
					<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
					<h3>Login required</h3>
					<p>Please login first to access this page.</p>
					<router-link :to="{ name: 'Home' }">
						<button>Go to Sing in</button>
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
	background-color: #3b9afa;
	border-radius: 5px;
	padding: 5px 0;
	width: 100%;
	color: white;
}
button:hover {
	background-color: #499ff5;
}
a:hover {
	padding: 0;
}
</style>
