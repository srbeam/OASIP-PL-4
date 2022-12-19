<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import SuccessModal from './SuccessModal.vue'
defineEmits(['closeModal'])

const users = ref([])
const author = localStorage.getItem('token')
const userName = ref('')
const userEmail = ref('')
const userPass = ref('')
const confirmUserPass = ref('')
const userRole = ref('student')
const userId = ref('')

let isInvalidEmail = ref(false)
let nameIsNull = ref(false)
let emailIsNull = ref(false)
let nameIsNullMsg = ref(' Name is require field')
let emailNullMsg = ref(' Email is require field')
let emailNotValidMsg = ref(' Email is not valid')
let validInput = ref(false)

const isDupplicateName = ref(false)
const isDupplicateEmail = ref(false)
const dupplicateNameMsg = ref(' This name is already use')
const dupplicateEmailMsg = ref(' This email is already use')

const passIsNull = ref(false)
const confirmPassIsNull = ref(false)
const addUser = () => {
	if (userName.value == '') {
		nameIsNull.value = true
		validInput.value = false
		isDupplicateName.value = false
		isNotUniqe.value = false
	} else {
		nameIsNull.value = false
		validInput.value = true
		isDupplicateName.value = false
		// checkDupplicateName(userName.value)
	}
	if (userPass.value == '') {
		passIsNull.value = true
		validInput.value = false
		isNotUniqe.value = false
	} else {
		passIsNull.value = false
		validInput.value = true
		checklengthPass()
	}
	if (confirmUserPass.value == '') {
		confirmPassIsNull.value = true
		validInput.value = false
		isNotUniqe.value = false
	} else {
		confirmPassIsNull.value = false
		validInput.value = true
		checklengthPass()
	}

	if (userEmail.value == '') {
		emailIsNull.value = true
		isInvalidEmail.value = false
		isDupplicateEmail.value = false
		validInput.value = false
		isNotUniqe.value = false
	} else if (!validateEmail(userEmail.value.trim())) {
		isInvalidEmail.value = true
		emailIsNull.value = false
		isDupplicateEmail.value = false
		validInput.value = false
		isNotUniqe.value = false
	} else {
		emailIsNull.value = false
		isInvalidEmail.value = false
		validInput.value = true
		isDupplicateEmail.value = false
	}
	// checkDupplicateEmail(userEmail.value)
	validateMatchPass()
	if (
		nameIsNull.value === false &&
		emailIsNull.value === false &&
		isInvalidEmail.value === false &&
		passIsNull.value === false &&
		confirmPassIsNull.value === false &&
		passLengthNotValid.value === false &&
		isPassNotMatch.value === false
	) {
		validInput.value === true
		const newUser = {
			name: userName.value.trim(),
			email: userEmail.value.trim(),
			password: userPass.value,
			role: userRole.value
		}
		fetchAddUser(newUser)
	}
}
const typeOfModal = ref()
const isNotUniqe = ref(false)
const isAddSuccess = ref(false)
const fetchAddUser = async (newUser) => {
	isNotUniqe.value = false
	if (validInput.value) {
		//ใช้ตัวแปร env แทนการเขียน path
		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users/signup`, {
			method: 'POST',
			headers: {
				'content-type': 'application/json',
				Authorization: `Bearer ${author}`
			},

			body: JSON.stringify(newUser)
		})
		console.log(res.status)
		if (res.status === 200) {
			console.log('added sucessfully')
			console.log(res)
			// getUsers()
			validInput.value = false
			typeOfModal.value = 'addUser'
			isAddSuccess.value = true
			setTimeout(toggleAddSuccess, 3000)
			userId.value = ''
			userName.value = ''
			userEmail.value = ''
			userPass.value = ''
			confirmUserPass.value = ''
			userRole.value = 'student'
			isNotUniqe.value = false
		} else if (res.status === 400) {
			isNotUniqe.value = true
			typeOfModal.value = ''
		} else console.log('error, cannot be added')
	}
}
const toggleAddSuccess = () => {
	if (isAddSuccess.value === true) {
		isAddSuccess.value = false
		// getUsers()
	} else {
		isAddSuccess.value = true
	}
}

const validateEmail = (email) => {
	const reg =
		/^(([^<>()[\]\\.,;:\s*$&!#?@"]+(\.[^<>()[\]\\.,;:\s*$&!#?@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	return reg.test(String(email).toLocaleLowerCase())
}
const passLengthNotValid = ref(false)
const passLengthNotValidMsg = ref(' Password must be at least 8')
const conPassLengthNotValid = ref(false)
const conPassLengthNotValidMsg = ref(' Password must be at least 8')
const checklengthPass = () => {
	if (userPass.value.length < 8) {
		passLengthNotValid.value = true
		validInput.value = false
	} else {
		passLengthNotValid.value = false
		validInput.value = true
	}
	if (confirmUserPass.value.length < 8) {
		conPassLengthNotValid.value = true
		validInput.value = false
	} else {
		conPassLengthNotValid.value = false
		validInput.value = true
	}
}
const isPassNotMatch = ref(false)
const isPassNotMatchMsg = ref(' The password DOES NOT match')
const validateMatchPass = () => {
	if (userPass.value != confirmUserPass.value) {
		isPassNotMatch.value = true
		validInput.value = false
	} else {
		isPassNotMatch.value = false
		validInput.value = true
	}
}
</script>

<template>
	<div>
		<SuccessModal v-if="isAddSuccess" :typeOfModal="typeOfModal" />

		<div class="modal">
			<div class="modal-container">
				<div class="close">
					<p @click="$emit('closeModal')">X</p>
				</div>

				<div class="modal-content">
					<h3>Add User</h3>
					<div id="signup">
						<div class="input">
							<div>
								<label for="fname">NAME<span class="error"> * </span></label>
								<span
									class="error"
									v-if="nameIsNull === true && isDupplicateName === false"
								>
									Name is require field</span
								>
								<span
									v-show="nameIsNull === false && isDupplicateName === true"
									class="error"
								>
									{{ dupplicateNameMsg }}
								</span>
							</div>

							<input
								type="text"
								id="fname"
								name="username"
								placeholder="YOUR NAME"
								v-model="userName"
								maxlength="100"
							/>
						</div>
						<div class="input">
							<label for="email">E-MAIL<span class="error"> * </span></label>
							<span v-show="emailIsNull === true" class="error">
								{{ emailNullMsg }}
							</span>
							<span
								v-show="isInvalidEmail === true && emailIsNull === false"
								class="error"
							>
								{{ emailNotValidMsg }}
							</span>
							<span v-show="isDupplicateEmail === true" class="error">
								{{ dupplicateEmailMsg }}
							</span>
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
							<label for="passwd">PASSWORD<span class="error"> * </span></label>
							<span class="error" v-if="passIsNull === true">
								Password is require field</span
							>
							<span
								class="error"
								v-if="passIsNull === false && passLengthNotValid === true"
							>
								{{ passLengthNotValidMsg }}
							</span>
							<input
								type="password"
								id="passwd"
								name="password"
								minlength="8"
								maxlength="14"
								placeholder="YOUR PASSWORD"
								v-model="userPass"
							/>
						</div>
						<div class="input">
							<label for="fanme">CONFIRM-PASSWORD<span class="error"> * </span></label>
							<span class="error" v-if="confirmPassIsNull === true">
								Please confirm password</span
							>
							<span
								class="error"
								v-if="confirmPassIsNull === false && isPassNotMatch === true"
							>
								{{ isPassNotMatchMsg }}</span
							>

							<input
								type="password"
								id="confirm-passwd"
								name="password"
								minlength="8"
								maxlength="14"
								placeholder="COMFIRM YOUR PASSWORD"
								v-model="confirmUserPass"
							/>
						</div>
						<div class="input">
							<label for="role">ROLE<span class="error"> * </span></label>
							<select v-model="userRole">
								<option value="student" selected>Student</option>
								<option value="lecturer">Lecturer</option>
								<option value="admin">Admin</option>
							</select>
						</div>
						<div class="btn-area">
							<p class="error" v-if="isNotUniqe && validInput">
								This name or email is already use
							</p>

							<button class="text-white btn btn-primary" @click="addUser">
								Add User
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.modal {
	top: 0;
	left: 0;
	background-color: rgba(0, 0, 0, 0.8);
	position: fixed;
	width: 100%;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 1;
}
.modal-container {
	border: 1px solid black;
	background-color: white;
	border-radius: 10px;
	min-width: 250px;
	width: 33%;
}
.close {
	display: flex;
	justify-content: right;
}
.close p {
	color: gray;
	padding: 5px 20px;
	margin: 0;
	cursor: pointer;
}
.close p:hover {
	color: rgb(173, 173, 173);
}
.modal-content {
	text-align: center;
	margin: 0 0 25px 0;
	padding: 25px;
	color: black;
}

.error {
	color: red;
	margin: 0;
	text-align: left;
}
#home {
	background-color: rgb(255, 255, 255, 0);
	color: black;
	display: flex;
	padding: 3% 13%;
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
#register {
	/* background-color: #c7c7c780; */
	color: #495ab6;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 20px 0;
	width: 90%;
	border-radius: 10px;
	border: 1px solid lightgrey;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	font-size: 14px;
}
.input {
	margin-top: 10px;
	text-align: left;
	width: 100%;
}
.error {
	color: red;
}
.input select {
	color: black;
	padding: 7px 15px;
	border-radius: 4px;
	width: 100%;
	/* background-color: #ecececa1; */
	border: 1px solid lightgray;
}
input {
	width: 100%;
	padding: 7px 15px;
	box-sizing: border-box;
	border: 2px solid white;
	border-radius: 4px;
	color: #636363;
	/* background-color: #ecececa1; */
	border: 1px solid lightgray;
}
#signup {
	width: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
.role {
	color: #636363;
	size: 220px;
}

.btn-area {
	text-align: center;
	margin-top: 25px;

	width: 100%;
}
.btn-area button {
	width: 100%;
	/* margin-bottom: 10px; */
}
#go-to-signIn a:hover {
	background-color: rgb(255, 255, 255, 0);
}
</style>
