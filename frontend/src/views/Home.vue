<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import SignUpForm from '../components/SignUpForm.vue'
import SignInForm from '../components/SignInForm.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import Welcome from './Welcome.vue'
const hasToken = ref()
const checkToken = () => {
	localStorage.getItem('token') === null
		? (hasToken.value = true)
		: (hasToken.value = false)
}
onBeforeMount(async () => {
	await checkToken()
})
</script>

<template>
	<div>
		<BaseNavBar />

		<SignInForm v-if="hasToken" />
		<Welcome v-else />

		<!-- <SignUpForm></SignUpForm> -->
	</div>
</template>

<style></style>
