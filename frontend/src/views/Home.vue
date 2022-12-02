<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import SignInForm from '../components/SignInForm.vue'
import BaseNavBar from '../components/BaseNavBar.vue'

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
		<BaseNavBar @signOut="checkToken" />
		<SignInForm />
	</div>
</template>

<style></style>
