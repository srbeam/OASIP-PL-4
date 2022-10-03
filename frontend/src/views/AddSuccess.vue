<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useRouter } from 'vue-router'
import BaseNavBar from '../components/BaseNavBar.vue'

const appRouter = useRouter()
const goAllEvent = () => appRouter.push({ name: 'Page', params: { page: 1 } })
</script>

<template>
	<div id="root">
		<BaseNavBar />
		<div id="success">
			<h1>Add Sucessful !</h1>
			<img src="../assets/images/success.png" width="500" />
			<button @click="goAllEvent">Go to All Event</button>
		</div>
	</div>
</template>

<style scoped>
#root {
	height: 100vh;
}
#success {
	display: flex;
	flex-direction: column;
	height: 90%;
	align-items: center;
	justify-content: center;
}
h1 {
	text-align: center;
	color: #495ab6;
}
button {
	color: gray;
	border: 1px solid gray;
	padding: 5px 50px;
	border-radius: 10px;
	cursor: pointer;
	margin-top: 30px;
}
button:hover {
	border: 0;
	background-color: orange;
	color: white;
}
</style>
