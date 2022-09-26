<script setup>
import { ref, onBeforeMount, computed } from 'vue'

function toggleMenu() {
	const nav = document.querySelector('.nav-container')
	const hamburger = document.querySelector('.hamburger')

	if (hamburger.classList.contains('change')) {
		hamburger.classList.remove('change')
		console.log('close')
		nav.classList.remove('open')
		nav.style.display = 'none'
	} else {
		hamburger.classList.add('change')
		console.log('open')
		nav.classList.toggle('open')
		nav.style.display = 'grid'
	}
}
</script>

<template>
	<div id="gameWindow">
		<!-- <div class="flex flex-row"> -->
		<header>
			<nav id="navBar">
				<div class="hamburger" @click="toggleMenu">
					<div class="bar1"></div>
					<div class="bar2"></div>
					<div class="bar3"></div>
				</div>

				<div class="nav-container">
					<div class="nav-container-grid">
						<div id="logo">
							<router-link :to="{ name: 'Home' }">
								<img src="./assets/images/background/logo.png" id="logo" />
							</router-link>
						</div>
						<router-link :to="{ name: 'Home' }" id="menu-home">
							<button class="menu">Home</button>
						</router-link>
						<router-link :to="{ name: 'AddEvent' }">
							<button class="menu">Reserve</button>
						</router-link>
						<router-link :to="{ name: 'Page', params: { page: 1 } }">
							<!-- <router-link :to="{ name: 'ListAll'}"> -->
							<button class="menu">List All Event</button>
						</router-link>
						<router-link :to="{ name: 'Category' }">
							<button class="menu">Category</button>
						</router-link>
						<router-link :to="{ name: 'ListByDate' }">
							<button class="menu">List By Date</button>
						</router-link>
						<router-link :to="{ name: 'ListUser' }">
							<button class="menu">List All Users</button>
						</router-link>
						<router-link :to="{ name: 'AboutUs' }">
							<button class="menu">About Us</button>
						</router-link>
					</div>
				</div>
			</nav>
		</header>
		<div>
			<div id="listallevent" class="mt-6">
				<div class="body">
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
	padding: 10px 0px;
}

#logo.router-link-active {
	background-color: rgba(0, 0, 0, 0);
}

#gameWindow {
	font-family: 'Mitr';
	font-size: 16px;
	color: white;
	overflow: hidden;
	/* background-repeat: no-repeat; */
	/* background-position: bottom; */
	/* background-attachment: fixed; */
}

html,
body {
	background: url(./assets/images/background/background11.gif);
	/* background-size: 100%; */
	background-size: 135%;
	background-repeat: no-repeat;
	/* background-attachment: fixed; */
	padding-top: 0.3%;
}
#navBar {
	width: 80%;
}
.nav-container-grid {
	display: grid;
	grid-template-columns: 250px auto auto auto auto auto auto;
	text-align: center;
	align-items: center;
	grid-gap: 15px;
}
.menu {
	color: white;
}
a:hover {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
	padding: 10px 0px;
}
.menu:hover {
	/* background-color: rgba(255, 255, 255, 0.2); */
}

.navMenu {
	color: white;
}

.navMenu:hover {
	/* background-color: rgba(255, 255, 255, 0.2); */
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
.hamburger {
	display: none;
	cursor: pointer;
}

.bar1,
.bar2,
.bar3 {
	width: 35px;
	height: 5px;
	background-color: #232323;
	margin: 6px 20px;
	transition: 0.4s;
	border-radius: 10px;
}

.change .bar1 {
	-webkit-transform: rotate(-45deg) translate(-9px, 6px);
	transform: rotate(-45deg) translate(-9px, 6px);
}

.change .bar2 {
	opacity: 0;
}

.change .bar3 {
	-webkit-transform: rotate(45deg) translate(-8px, -8px);
	transform: rotate(45deg) translate(-8px, -8px);
}
.open {
	height: auto !important;
	transform: translateY(5px);
	transition: 0.5s;
}
.logo-mobile {
	display: none;
}
#menu-home {
	display: none;
}
@media screen and (max-width: 1024px) {
	html,
	body {
		background-size: 150%;
	}
	#navBar {
		width: 100%;
		padding: 0 20px 0 0;
	}
	.nav-container-grid {
		grid-gap: 25px;
	}
}
@media screen and (max-width: 768px) {
	html,
	body {
		background: none;
		padding-top: 0;
	}
	header {
		height: auto;
		padding: 0;
	}
	.hamburger {
		display: inline-block;
		margin-top: 1rem;
		margin-bottom: -10px;
	}
	#menu-home {
		display: inline;
		padding: 0;
	}
	nav {
		height: auto;
		/* flex-direction: column; */
	}
	.nav-container {
		display: none;
		justify-content: center;
	}

	.menu {
		color: black;
		padding: 8px 0px;
	}
	a:hover {
		/* background-color: rgba(255, 255, 255, 0.2); */
		background-color: lightgray;
		border-radius: 10px;
		/* padding: 0 30px; */
	}
	.nav-container-grid {
		grid-template-columns: none;
		grid-template-rows: repeat(7, auto);
		grid-row-gap: 20px;
		grid-column-gap: 0;

		justify-content: center;
	}
	.body {
		background: url(./assets/images/background/background11.gif);
		background-size: cover;
		background-repeat: no-repeat;
		/* background-attachment: fixed; */
		padding-top: 0.3%;
	}
	#logo {
		display: none;
	}
}
</style>
