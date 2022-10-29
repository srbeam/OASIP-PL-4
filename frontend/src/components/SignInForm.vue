<script setup>
import { ref, onBeforeMount } from 'vue'
import PasswordMatchModal from './PasswordMatchModal.vue'
import { useRouter } from 'vue-router'
defineEmits(['isHasToken'])
const userEmail = ref('')
const userPass = ref('')
const validateMsg = ref('')
const isPasswordMatch = ref(false)
const isInvalidEmail = ref(false)
const token = ref()
const fetchMatchPass = async () => {
	if (userEmail.value === '' || userPass.value === '') {
		isInvalidEmail.value = true
		validateMsg.value = 'Please fill out both fields.'
	} else if (!validateEmail(userEmail.value.trim())) {
		isInvalidEmail.value = true
		validateMsg.value = 'Email is not valid'
	} else {
		isInvalidEmail.value = false
		validateMsg.value = ''
		checklengthPass()
	}

	const user = {
		email: userEmail.value.trim(),
		password: userPass.value
	}
	//ใช้ตัวแปร env แทนการเขียน path
	if (isInvalidEmail.value === false) {
		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/login`, {
			method: 'POST',
			headers: {
				'content-type': 'application/json'
			},

			body: JSON.stringify(user)
		})
		// console.log(res.status)
		if (res.status === 200) {
			// console.log('Password Matched')
			isPasswordMatch.value = true
			setTimeout(togglePassMatch, 3000)
			userEmail.value = ''
			userPass.value = ''
			validateMsg.value = ''
			token.value = await res.json()
			saveLocal()
		} else if (res.status === 401) {
			isPasswordMatch.value = false
			validateMsg.value = 'Password Incorrect'
		} else if (res.status === 404) {
			isPasswordMatch.value = false
			validateMsg.value = 'A user with the specified email DOES NOT exist'
		} else {
			validateMsg.value = ''
		}
	}
}
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`),
		localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}

const togglePassMatch = () => {
	if (isPasswordMatch.value === true) {
		isPasswordMatch.value = false
	} else {
		isPasswordMatch.value = true
	}
	appRouter.push({ name: 'AddEvent' })
}
const validateEmail = (email) => {
	const reg =
		/^(([^<>()[\]\\.,;:\s*$&!#?@"]+(\.[^<>()[\]\\.,;:\s*$&!#?@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	return reg.test(String(email).toLocaleLowerCase())
}
const passLengthNotValid = ref(false)
const passLengthNotValidMsg = ref(' Password must be at least 8')

const checklengthPass = () => {
	if (userPass.value.length < 8) {
		isInvalidEmail.value = true
		validateMsg.value = 'Password must be at least 8'
	} else {
		isInvalidEmail.value = false
		validateMsg.value = ''
	}
}
const appRouter = useRouter()
</script>

<template>
	<div>
		<div id="home">
			<div id="left">
				<p id="web-name">BOOKIING</p>

				<p>
					OASIP-PL4 Website for booking clinics <br />
					INT222 Integrated IT Project II
				</p>

				<img src="../assets/images/home2.png" />
			</div>
			<div id="right">
				<div id="logIn">
					<div class="title">
						<h3>Sign In</h3>
					</div>

					<div id="signIn">
						<div class="input">
							<label for="email">E-MAIL : </label>
							<input
								type="text"
								id="email"
								name="email"
								placeholder="YOUR E-MAIL"
								v-model="userEmail"
								maxlength="50"
							/>
						</div>
						<div class="input">
							<label for="passwd">PASSWORD : </label>
							<input
								type="password"
								id="passwd"
								name="password"
								placeholder="YOUR PASSWORD"
								v-model="userPass"
								maxlength="14"
							/>
						</div>

						<div class="btn-area">
							<p class="error">{{ validateMsg }}</p>
							<div>
								<div>
									<button class="text-white btn btn-primary" @click="fetchMatchPass">
										Sign In
									</button>
								</div>

								<!-- <router-link :to="{ name: 'Page', params: { page: 1 } }">
												<div>
													<button class="text-white btn btn-primary">Sign In</button>
													<button class="text-white btn btn-primary" id="getstart">
														Get Start !
													</button>
												</div>
											</router-link> -->
							</div>
							<!-- <div id="go-to-signUp">
								<p>
									Not have an account?
									<router-link :to="{ name: 'SignUpForm' }">SIGN UP</router-link>
								</p>
							</div> -->
						</div>
					</div>
				</div>
			</div>
		</div>
		<PasswordMatchModal v-if="isPasswordMatch" />
	</div>
</template>

<style scoped>
#home {
	background-color: rgb(255, 255, 255, 0);
	color: black;
	display: flex;
	padding: 5% 13%;
	min-height: 575px;
}
#left {
	width: 50%;
	color: #495ab6;
	display: flex;
	flex-direction: column;
	justify-content: center;
}
#web-name {
	font-size: 65px;
	margin: 0;
}
img {
	width: 90%;
}
#right {
	width: 50%;
	display: flex;
	justify-content: center;
	align-items: center;
}

#logIn {
	/* background-color: #c7c7c780; */
	color: #495ab6;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 20px 0;
	width: 80%;
	border-radius: 10px;
	min-height: 480px;
	border: 1px solid lightgrey;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	font-size: 14px;
}
#signIn {
	width: 80%;
}
.input {
	margin-top: 20px;
}
input {
	width: 100%;
	padding: 7px 15px;
	box-sizing: border-box;
	/* border: 2px solid white; */
	border-radius: 4px;
	color: #636363;
	/* background-color: #ecececa1; */
	margin-top: 5px;
	border: 1px solid lightgray;
}

.btn-area {
	text-align: center;
	margin-top: 25px;
}
.btn-area button {
	width: 100%;
	margin-bottom: 10px;
}
.error {
	color: red;
	margin: 0;
	text-align: left;
}
#go-to-signUp a {
	background-color: rgb(255, 255, 255, 0);
}
#go-to-signUp a:hover {
	background-color: rgb(255, 255, 255, 0);
}
@media screen and (max-width: 768px) {
	#home {
		flex-direction: column;
		padding: 10%;
	}
	#left {
		width: 100%;
		align-items: center;
		margin: 0 0 -38px 0;
	}
	#web-name {
		text-align: center;
	}
	.description {
		text-align: center;
	}
	#right {
		width: 100%;
	}
}
</style>
