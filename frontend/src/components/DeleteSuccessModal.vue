<script setup></script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="modal-content">
					<i class="fa fa-check-circle fa-4x"></i>
					<h3>Delete User Success</h3>
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
	color: green;
}
h3 {
	color: black;
}
</style>
