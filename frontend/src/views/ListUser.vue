<script setup>
import ListAllUser from './../components/ListAllUser.vue'
import { ref, onBeforeMount, computed } from 'vue'
</script>

<template>
	<div>
		<ListAllUser :users="users"></ListAllUser>
	</div>
</template>

<style></style>
