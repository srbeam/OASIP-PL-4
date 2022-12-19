<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import ListCategory from '../components/ListCateComponent.vue'
import BaseNavBar from '../components/BaseNavBar.vue'
import NoLoginModal from '../components/NoLoginModal.vue'

const categories = ref([])
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const is401 = ref()
const getCategory = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/categories`, {
		method: 'GET'
	})
	if (res.status === 200) {
		categories.value = await res.json()
	} else if (res.status === 401) {
		// getRefreshToken()
	} else console.log('error, cannot get data')
}

onBeforeMount(async () => {
	await getCategory()
})

// const getRefreshToken = async () => {
// 	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/refresh`, {
// 		method: 'GET',
// 		headers: {
// 			Authorization: `Bearer ${refreshToken}`
// 		}
// 	})
// 	if (res.status === 200) {
// 		is401.value = false
// 		token.value = await res.json()
// 		saveLocal()
// 	} else if (res.status === 401) {
// 		is401.value = true
// 	} else {
// 		console.log('Error,cannot get refresh token from backend')
// 	}
// }
// const saveLocal = () => {
// 	localStorage.setItem('token', `${token.value.accessToken}`)
// 	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
// }
</script>
<template>
	<div>
		<BaseNavBar />
		<!-- <NoLoginModal v-if="is401" /> -->
		<ListCategory :categories="categories"></ListCategory>
	</div>
</template>

<style scoped></style>
