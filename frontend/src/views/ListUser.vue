<script setup>
import ListAllUser from './../components/ListAllUser.vue'
import { ref, onBeforeMount, computed } from 'vue'
import BaseNavBar from '../components/BaseNavBar.vue'
</script>

<template>
	<div>
		<BaseNavBar />
		<ListAllUser :users="users"></ListAllUser>
	</div>
</template>

<style scoped></style>
