<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import SignUpForm from '../components/SignUpForm.vue'
import SignInForm from '../components/SignInForm.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
</script>

<template>
	<div>
		<BaseNavBar />

		<SignInForm />

		<!-- <SignUpForm></SignUpForm> -->
	</div>
</template>

<style></style>
