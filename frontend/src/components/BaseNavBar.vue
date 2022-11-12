<script setup>
import { onBeforeMount, ref } from 'vue'
import VueJwtDecode from 'vue-jwt-decode'
import { useRouter } from 'vue-router'
defineEmits(['signOut'])
const author = localStorage.getItem('token')
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
	appRouter.push({ name: 'Home' })
}
</script>

<template>
	<div>
		<header class="h-auto lg:flex bg-[#495ab6] p-0">
			<nav class="w-full h-auto">
				<div class="hamburger" @click="toggleMenu">
					<div class="bar1"></div>
					<div class="bar2"></div>
					<div class="bar3"></div>
				</div>

				<div class="nav-container">
					<div class="nav-container-grid">
						<div id="logo">
							<router-link
								:to="{ name: 'Home' }"
								class="flex justify-center"
								v-if="getUserFromToken === undefined"
							>
								<img src="../assets/images/background/logo.png" />
							</router-link>
							<img src="../assets/images/background/logo.png" v-else />
						</div>
						<div class="flex flex-col lg:flex-row items-center">
							<ul
								class="flex flex-col lg:flex-row text-center lg:text-left m-0 p-0 list-none"
							>
								<li class="lg:inline lg:mr-5 lg:mt-0 lg:mb-0">
									<router-link
										:to="{ name: 'AddEvent' }"
										v-if="
											getUserFromToken === undefined ||
											getUserFromToken.Roles != 'ROLE_lecturer'
										"
										class="py-2.5 px-4"
									>
										<button class="text-white">Reserve</button>
									</router-link>
								</li>
								<li
									class="lg:inline lg:mr-5 lg:mt-0 lg:mb-0"
									v-if="getUserFromToken != undefined"
								>
									<router-link :to="{ name: 'ListAll' }" class="py-2.5 px-4">
										<button class="text-white">List All Event</button>
									</router-link>
								</li>
								<li class="lg:inline lg:mr-5 lg:mb-0">
									<router-link :to="{ name: 'Category' }" class="py-2.5 px-4">
										<button class="text-white">Category</button>
									</router-link>
								</li>
								<li
									class="lg:inline lg:mr-5 lg:mt-0 lg:mb-0"
									v-if="getUserFromToken != undefined"
								>
									<router-link :to="{ name: 'ListByDate' }" class="py-2.5 px-4">
										<button class="text-white">List By Date</button>
									</router-link>
								</li>
								<li
									class="lg:inline lg:mr-5 lg:mt-0 lg:mb-0"
									v-if="
										getUserFromToken != undefined &&
										getUserFromToken.Roles === 'ROLE_admin'
									"
								>
									<router-link
										:to="{ name: 'ListUser' }"
										v-if="
											getUserFromToken != undefined &&
											getUserFromToken.Roles === 'ROLE_admin'
										"
										class="py-2.5 px-4"
									>
										<button class="text-white">List All Users</button>
									</router-link>
								</li>
								<li class="lg:inline lg:mr-5 lg:mb-0">
									<router-link :to="{ name: 'AboutUs' }" class="py-2.5 px-4">
										<button class="text-white">About Us</button>
									</router-link>
								</li>
							</ul>
							<div id="sign-out-btn" v-if="isHasToken">
								<router-link :to="{ name: 'Home' }" class="py-2.5 px-4">
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
				</div>
			</nav>
		</header>
	</div>
</template>

<style scoped>
.router-link-active {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
	padding: 10px 16px;
}

#logo a.router-link-active,
#sign-out-btn a.router-link-active,
#sign-out-btn a:hover,
#logo a:hover {
	background-color: rgba(0, 0, 0, 0);
	padding: 0;
}

.nav-container-grid {
	display: grid;
	grid-template-columns: 200px auto;
	text-align: center;
	align-items: center;
}

a:hover {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 10px;
	padding: 10px 16px;
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
	padding-bottom: 40px;
}

header {
	min-height: 70px;
}

@media screen and (max-width: 1024px) {
	.hamburger {
		display: inline-block;
		margin-top: 1rem;
		margin-bottom: -10px;
	}

	.nav-container {
		display: none;
		justify-content: center;
	}
	a:hover {
		background-color: lightgray;
		border-radius: 10px;
	}
	.nav-container-grid {
		justify-content: center;
		align-items: center;
		display: flex;
		flex-direction: column;
	}

	#logo img {
		width: 50%;
	}
	#logo {
		display: flex;
		justify-content: center;
		margin-bottom: 14px;
	}
	li {
		margin-bottom: 25px;
	}
}
</style>
