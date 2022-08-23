<script setup></script>

<template>
	<div id="gameWindow">
		<div class="flex flex-row">
			<router-link :to="{ name: 'Home' }" id="logo">
				<img src="./assets/images/background/logo.png" style="width: 62%" />
			</router-link>

			<div id="addhome" class="flex flex-row gap-4 my-auto -ml-36">
				<router-link :to="{ name: 'AddEvent' }">
					<button class="navMenu p-2 px-3 hover:rounded-lg">Reserve</button>
				</router-link>
				<router-link :to="{ name: 'Page', params: { page: 1 } }">
					<!-- <router-link :to="{ name: 'ListAll'}"> -->
					<button class="navMenu p-2 px-3 hover:rounded-lg">List All Event</button>
				</router-link>
				<router-link :to="{ name: 'Category' }">
					<button class="navMenu p-2 px-3 hover:rounded-lg">Category</button>
				</router-link>
				<router-link :to="{ name: 'ListByDate' }">
					<button class="navMenu p-2 px-3 hover:rounded-lg">List By Date</button>
				</router-link>
				<router-link :to="{ name: 'ListUser' }">
					<button class="navMenu p-2 px-3 hover:rounded-lg">List All Users</button>
				</router-link>
				<router-link :to="{ name: 'AboutUs' }">
					<button class="navMenu p-2 px-3 hover:rounded-lg">About Us</button>
				</router-link>
			</div>
		</div>

		<div>
			<div id="listallevent" class="mt-6">
				<div>
					<!-- <router-view> </router-view> -->
					<router-view :key="$route.fullPath"> </router-view>
				</div>
			</div>
		</div>
	</div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Mitr&display=swap');
.router-link-active {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
}
#logo.router-link-active {
	background-color: rgba(0, 0, 0, 0);
}

#gameWindow {
	font-family: 'Mitr';
	font-size: 16px;
	color: white;
	overflow: hidden;
	background-repeat: no-repeat;
	background-position: bottom;
	background-attachment: fixed;
}

html,
body {
	background: url(./assets/images/background/background11.gif);
	background-size: 100%;
	background-repeat: no-repeat;
	background-attachment: fixed;
	padding-top: 0.3%;
}
/* #box {
	width: 300px;
	height: 550px;
	padding: 20px;
	background-color: rgba(255, 255, 255, 0.5);
	margin-left: 20px;
	margin-top: 25px;
	font-size: 15px;
} */

.navMenu {
	color: white;
}

.navMenu:hover {
	background-color: rgba(255, 255, 255, 0.2);
}

.selectcategory {
	margin-top: 25px;
}
#addhome {
	color: #383838;
}
#option {
	color: #383838;
}

#button {
	size: 5px;
}

ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
}

li {
	display: inline;
}
</style>
