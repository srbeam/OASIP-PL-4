<script setup>
import { ref, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'
import VueJwtDecode from 'vue-jwt-decode'
import SuccessModal from './SuccessModal.vue'
defineEmits(['isHasToken'])
const userEmail = ref('')
const userPass = ref('')
const validateMsg = ref('')
const isPasswordMatch = ref(false)
const isInvalidEmail = ref(false)
const token = ref()
const getUserFromToken = ref()

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

			userEmail.value = ''
			userPass.value = ''
			validateMsg.value = ''
			token.value = await res.json()
			saveLocal()
			getUserFromToken.value = VueJwtDecode.decode(token.value.accessToken)
			setTimeout(togglePassMatch, 3000)
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
		if (getUserFromToken.value.Roles != 'ROLE_lecturer') {
			appRouter.push({ name: 'AddEvent' })
		} else {
			appRouter.push({ name: 'ListAll' })
		}
	} else {
		isPasswordMatch.value = true
	}
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
		<div class="flex flex-col py-5 md:mt-10 lg:flex-row lg:px-20">
			<div
				class="text-[#495ab6] flex flex-col justify-center= items-center lg:w-full lg:justify-center"
			>
				<h1 class="text-6xl">BOOKIING</h1>

				<p class="text-center text-sm m-0 xl:text-base">
					OASIP-PL4 Website for booking clinics <br />
					INT222 Integrated IT Project II
				</p>

				<img
					class="w-11/12 sm:w-4/6 lg:w-11/12 2xl:w-8/12 mt-2 mb-[-35px]"
					src="../assets/images/home2.png"
				/>
			</div>
			<div class="w-full flex justify-center items-center bg-white">
				<div
					class="w-4/5 sm:w-2/3 md:w-4/5 2xl:w-8/12 xl:h-[425px] border border-1 rounded-lg shadow-md text-[#495ab6] flex flex-col justify-center items-center py-5 text-sm"
				>
					<h3 class="m-0">Sign In</h3>

					<div class="w-4/5">
						<div class="mt-4">
							<label for="email">E-MAIL : </label>
							<input
								type="text"
								id="email"
								name="email"
								placeholder="YOUR E-MAIL"
								v-model="userEmail"
								maxlength="50"
								class="w-full rounded text-[#636363] mt-1 py-1.5 px-3.5 border-1 border-gray-300"
							/>
						</div>
						<div class="mt-4">
							<label for="passwd">PASSWORD : </label>
							<input
								type="password"
								id="passwd"
								name="password"
								placeholder="YOUR PASSWORD"
								v-model="userPass"
								maxlength="14"
								class="w-full rounded text-[#636363] mt-1 py-1.5 px-3.5 border-1 border-gray-300"
							/>
						</div>

						<div class="text-center mt-7">
							<p class="text-red-500 text-start">{{ validateMsg }}</p>
							<div>
								<div>
									<button
										class="text-white btn btn-primary w-full mb-2.5"
										@click="fetchMatchPass"
									>
										Sign In
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<SuccessModal v-if="isPasswordMatch" typeOfModal="login" />
	</div>
</template>

<style scoped></style>
