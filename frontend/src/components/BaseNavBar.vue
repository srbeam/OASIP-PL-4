<script setup>
import { computed } from '@vue/reactivity'
import { onBeforeMount, ref } from 'vue'
import VueJwtDecode from 'vue-jwt-decode'
import { useRouter } from 'vue-router'
defineEmits(['signOut'])
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const getUserFromToken = ref()
const getUser = () => {
	if (author != undefined) {
		getUserFromToken.value = VueJwtDecode.decode(author)
	}
}

const isHasToken = ref()
const checkToken = () => {
	if (
		localStorage.getItem('token') != null ||
		localStorage.getItem('token') != undefined
	) {
		isHasToken.value = true
	} else {
		isHasToken.value = false
	}
}

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
onBeforeMount(async () => {
	await checkToken()
	getUser()
})
const appRouter = useRouter()
const signOut = () => {
	localStorage.clear()
	checkToken()
	// window.location.reload()
	// window.location = 'http://localhost:3000/pl4/'
	appRouter.push({ name: 'Home' })
}
</script>

<template>
	<div>
		<header>
			<nav id="navBar">
				<div class="hamburger" @click="toggleMenu">
					<div class="bar1"></div>
					<div class="bar2"></div>
					<div class="bar3"></div>
				</div>

				<div class="nav-container" v-if="getUserFromToken === undefined">
					<div class="nav-container-grid">
						<div id="logo">
							<router-link :to="{ name: 'Home' }">
								<img src="../assets/images/background/logo.png" id="logo-img" />
							</router-link>
						</div>
						<router-link :to="{ name: 'AddEvent' }">
							<button class="menu">Reserve</button>
						</router-link>
						<router-link :to="{ name: 'Category' }">
							<button class="menu">Category</button>
						</router-link>
						<router-link :to="{ name: 'AboutUs' }">
							<button class="menu">About Us</button>
						</router-link>
						<div id="sign-out-btn" v-if="isHasToken">
							<router-link :to="{ name: 'Home' }">
								<button
									class="btn btn-warning rounded-md text-black"
									@click="$emit('signOut'), signOut()"
								>
									SignOut
								</button>
							</router-link>
						</div>
					</div>
				</div>
				<div class="nav-container" v-else>
					<div class="nav-container-grid">
						<div id="logo">
							<router-link :to="{ name: 'Home' }">
								<img src="../assets/images/background/logo.png" id="logo-img" />
							</router-link>
						</div>
						<router-link
							:to="{ name: 'AddEvent' }"
							v-if="getUserFromToken.Roles != 'ROLE_lecturer'"
						>
							<button class="menu">Reserve</button>
						</router-link>
						<router-link :to="{ name: 'ListAll' }">
							<button class="menu">List All Event</button>
						</router-link>
						<router-link :to="{ name: 'Category' }">
							<button class="menu">Category</button>
						</router-link>
						<router-link :to="{ name: 'ListByDate' }">
							<button class="menu">List By Date</button>
						</router-link>
						<router-link
							:to="{ name: 'ListUser' }"
							v-if="
								getUserFromToken != undefined && getUserFromToken.Roles === 'ROLE_admin'
							"
						>
							<button class="menu">List All Users</button>
						</router-link>
						<router-link :to="{ name: 'AboutUs' }">
							<button class="menu">About Us</button>
						</router-link>
						<div id="sign-out-btn" v-if="isHasToken">
							<router-link :to="{ name: 'Home' }">
								<button
									class="btn btn-warning rounded-md text-black"
									@click="$emit('signOut'), signOut()"
								>
									SignOut
								</button>
							</router-link>
						</div>
					</div>
				</div>
			</nav>
		</header>
	</div>
</template>

<style scoped>
.router-link-active {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
	padding: 10px 0px;
}

#logo.router-link-active {
	background-color: rgba(0, 0, 0, 0);
}
#sign-out-btn a.router-link-active {
	padding: 0;
}
#sign-out-btn a:hover {
	background-color: rgba(0, 0, 0, 0);
}
#navBar {
	width: 100%;
}
.nav-container-grid {
	display: grid;
	/* grid-template-columns: 250px 80px 150px 100px 150px 150px 150px 100px; */
	/* grid-template-columns: repeat(8, 1fr); */
	grid-template-columns: 200px auto auto auto auto auto auto auto;
	/* grid-template-columns: auto; */
	text-align: center;
	align-items: center;
	grid-gap: 5px;
}

.menu {
	color: white;
}
a:hover {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
	padding: 10px 0px;
}

.navMenu {
	color: white;
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
	/* background-color: #232323; */
	background-color: white;
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
header {
	display: flex;
	background-color: #495ab6;
	min-height: 70px;
}
#logo img {
	/* display: none; */
	/* width: 80%; */
}
@media screen and (max-width: 1024px) {
	#navBar {
		width: 100%;
		padding: 0 20px 0 0;
	}
	.nav-container-grid {
		grid-gap: 25px;
	}
	nav#navBar {
		padding: 0;
	}
	#logo img {
		/* display: none; */
		/* width: 100%; */
	}
}
@media screen and (max-width: 768px) {
	header {
		height: auto;
		padding: 0;
		padding-bottom: 20px;
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
		/* color: black; */
		padding: 8px 0px;
		color: white;
	}
	a:hover {
		/* background-color: rgba(255, 255, 255, 0.2); */
		background-color: lightgray;
		border-radius: 10px;
		/* padding: 0 30px; */
	}
	.nav-container-grid {
		grid-template-columns: none;
		grid-template-rows: repeat(8, auto);
		grid-row-gap: 20px;
		grid-column-gap: 0;

		justify-content: center;
		align-items: center;
	}

	#logo a {
		/* display: none; */
		display: flex;
		justify-content: center;
	}

	#logo .router-link-active,
	#logo a:hover {
		background-color: rgba(255, 255, 255, 0);
	}
	#logo img {
		/* display: none; */
		width: 50%;
	}
}
</style>
