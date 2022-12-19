<script setup>
import BaseNavBar from './components/BaseNavBar.vue'
</script>

<template>
	<div id="gameWindow">
		<div>
			<div id="content">
				<div class="body">
					<router-view class="router-view" :key="$route.fullPath"> </router-view>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Mitr&display=swap');
#gameWindow {
	font-family: 'Mitr';
	font-size: 16px;
	color: white;
	overflow: hidden;
}
</style>
