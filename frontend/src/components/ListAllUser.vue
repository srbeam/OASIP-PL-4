<script setup>
import { ref, onBeforeMount, computed } from 'vue'

import AddSuccessModal from './AddSuccessModal.vue'
import UpdateSuccessModal from './UpdateSuccessModal.vue'
import DeleteSuccessModal from './DeleteSuccessModal.vue'
import ConfirmDeleteUserModal from './ConfirmDeleteUserModal.vue'
import NoLoginModal from './NoLoginModal.vue'
import ForAdminModal from './ForAdminModal.vue'
import ListallComponent from './ListallComponent.vue'
import AddUserModal from './AddUserModal.vue'

import EditUserModal from './EditUserModal.vue'

const users = ref([])
let author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const getUsers = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${author}`
		}
	})
	if (res.status === 200) {
		users.value = await res.json()
	} else if (res.status == 401) {
		getRefreshToken()
	} else if (res.status === 403) {
		is403.value = true
	} else {
		console.log('Error,cannot get events from backend')
	}
}
onBeforeMount(async () => {
	await getUsers()
})

const getRefreshToken = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/refresh`, {
		method: 'GET',
		headers: {
			Authorization: `Bearer ${refreshToken}`
		}
	})
	if (res.status === 200) {
		is401.value = false
		token.value = await res.json()
		saveLocal()
	} else if (res.status === 401) {
		// window.location.reload()
		is401.value = true
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}
const userName = ref('')
const userEmail = ref('')
const userRole = ref('student')
const userId = ref('')

let isInvalidEmail = ref(false)
let nameIsNull = ref(false)
let emailIsNull = ref(false)
let nameIsNullMsg = ref('Name is require field')
let emailNullMsg = ref('Email is require field')
let emailNotValidMsg = ref('Email is not valid')
let validInput = ref(false)

const isDupplicateName = ref(false)
const isDupplicateEmail = ref(false)
const dupplicateNameMsg = ref('This name is already use')
const dupplicateEmailMsg = ref('This email is already use')

const checkDupplicateName = (name) => {
	if (users.value.length <= 0) {
		isDupplicateName.value = false
	} else {
		isDupplicateName.value = users.value.some(
			(u) => u.name.toLowerCase() == name.toLowerCase()
		)
	}
}
const checkDupplicateEmail = (email) => {
	if (users.value.length <= 0) {
		isDupplicateEmail.value = false
	} else {
		isDupplicateEmail.value = users.value.some(
			(u) => u.email.toLowerCase() == email.toLowerCase()
		)
	}
}

const addUser = () => {
	if (userName.value == '') {
		nameIsNull.value = true
		validInput.value = false
		isDupplicateName.value = false
	} else {
		nameIsNull.value = false
		validInput.value = true
		isDupplicateName.value = false
		checkDupplicateName(userName.value)
	}

	if (userEmail.value == '') {
		emailIsNull.value = true
		isInvalidEmail.value = false
		isDupplicateEmail.value = false
		validInput.value = false
	} else if (!validateEmail(userEmail.value.trim())) {
		isInvalidEmail.value = true
		emailIsNull.value = false
		isDupplicateEmail.value = false
		validInput.value = false
	} else {
		emailIsNull.value = false
		isInvalidEmail.value = false
		validInput.value = true
		isDupplicateEmail.value = false
	}
	checkDupplicateEmail(userEmail.value)

	if (
		nameIsNull.value === false &&
		isDupplicateName.value === false &&
		emailIsNull.value === false &&
		isInvalidEmail.value === false &&
		isDupplicateEmail.value === false
	) {
		validInput.value === true
		const newUser = {
			name: userName.value.trim(),
			email: userEmail.value.trim(),
			role: userRole.value
		}
		fetchAddUser(newUser)
	}
}
const isAddSuccess = ref(false)
const fetchAddUser = async (newUser) => {
	if (validInput.value) {
		//ใช้ตัวแปร env แทนการเขียน path
		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users`, {
			method: 'POST',
			headers: {
				'content-type': 'application/json'
			},

			body: JSON.stringify(newUser)
		})
		console.log(res.status)
		if (res.status === 200) {
			console.log('added sucessfully')
			console.log(res)
			getUsers()
			validInput.value = false
			isAddSuccess.value = true
			setTimeout(toggleAddSuccess, 3000)
			userId.value = ''
			userName.value = ''
			userEmail.value = ''
			userRole.value = 'student'
		} else console.log('error, cannot be added')
	}
}
const toggleAddSuccess = () => {
	if (isAddSuccess.value === true) {
		isAddSuccess.value = false
		getUsers()
	} else {
		isAddSuccess.value = true
	}
}

const validateEmail = (email) => {
	const reg =
		/^(([^<>()[\]\\.,;:\s*$&!#?@"]+(\.[^<>()[\]\\.,;:\s*$&!#?@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	return reg.test(String(email).toLocaleLowerCase())
}
const isDeleteSuccess = ref(false)
const toggleDeleteSuccess = () => {
	console.log(isDeleteSuccess.value)
	if (isDeleteSuccess.value === true) {
		isDeleteSuccess.value = false
	} else {
		isDeleteSuccess.value = true
	}
}

const showConfirmDelete = (id) => {
	isShowConfirm.value = true
	selectedUser.value = id
}
const isShowConfirm = ref(false)
const isChooseConfirm = ref(false)
const closeConfirmModal = () => {
	isShowConfirm.value = false
}

const deleteUsers = async () => {
	isChooseConfirm.value = true
	if (isChooseConfirm.value) {
		const res = await fetch(
			`${import.meta.env.VITE_BACK_URL}/users/${selectedUser.value}`,
			{
				method: 'DELETE',
				headers: {
					Authorization: `Bearer ${author}`
				}
			}
		)
		if (res.status == 200) {
			users.value = users.value.filter((user) => {
				return user.id != selectedUser.value
			})

			isChooseConfirm.value = false
			isShowConfirm.value = false
			isDeleteSuccess.value = true
			setTimeout(toggleDeleteSuccess, 3000)
			getUsers()
		} else if (res.status === 401) {
			getRefreshToken()
			isChooseConfirm.value = true
			isShowConfirm.value = false
			isDeleteSuccess.value = false
		} else {
			console.log('Error!! Can not delete this user.')
		}
	}
}

const editUserMode = ref(false)
const toEditUserMode = (user) => {
	isShowEditUserModal.value = true
	editUserMode.value = true
	console.log(user)
	userId.value = user.id
	editingUserName.value = user.name.trim()
	editingUserEmail.value = user.email.trim()
	editingUserRole.value = user.role
	nameIsNull.value = false
	emailIsNull.value = false
	isInvalidEmail.value = false
	isDupplicateEmail.value = false
	isDupplicateName.value = false
	editingUserValues.value = {
		id: userId.value,
		name: editingUserName.value,
		email: editingUserEmail.value,
		role: editingUserRole.value
	}
}
// const toEditUserMode = (user) => {
// 	isShowEditUserModal.value = true
// 	editUserMode.value = true
// 	console.log(user)
// 	userId.value = user.id
// 	editingUserName.value = user.name.trim()
// 	editingUserEmail.value = user.email.trim()
// 	editingUserRole.value = user.role
// 	nameIsNull.value = false
// 	emailIsNull.value = false
// 	isInvalidEmail.value = false
// 	isDupplicateEmail.value = false
// 	isDupplicateName.value = false
// 	editingUserValues.value = user
// }
let editingUserValues = ref()
const updateUser = async (user) => {
	await updateUsersfetch(user)
	// window.location.reload()
	getUsers()
}
const alertInEditForm = ref()
const updateUsersfetch = async (user) => {
	// console.log(editUserMode.value)
	checkNullNameEdit(user.name.trim())
	checkNullEmailEdit(user.email.trim())

	if (validateEmail(user.email.trim()) === false) {
		isInvalidEmail.value = true
	} else {
		isInvalidEmail.value = false
	}

	checkDupInputEdit(user)

	if (
		isDupplicateName.value === false &&
		isDupplicateEmail.value === false &&
		nameIsNull.value === false &&
		emailIsNull.value === false &&
		isInvalidEmail.value === false
	) {
		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users/${user.id}`, {
			method: 'PUT',
			headers: {
				'content-type': 'application/json',
				Authorization: `Bearer ${author}`
			},
			body: JSON.stringify({
				name: user.name.trim(),
				email: user.email.trim(),
				role: user.role.value
			})
		})
		if (res.status === 200) {
			console.log('edited successfully')
			isUpdateSuccess.value = true
			getUsers()
			setTimeout(toggleUpdateSuccess, 3000)
			editingUserName.value = ''
			editingUserEmail.value = ''
			editingUserRole.value = ''
			editUserMode.value = false
		} else if (res.status === 401) {
			getRefreshToken()
		} else {
			console.log('error, cannot be added')
		}
	}
}
// const updateUsersfetch = async (user) => {
// 	// console.log(editUserMode.value)
// 	checkNullNameEdit()
// 	checkNullEmailEdit()

// 	if (validateEmail(editingUserEmail.value.trim()) === false) {
// 		isInvalidEmail.value = true
// 	} else {
// 		isInvalidEmail.value = false
// 	}

// 	checkDupInputEdit()
// 	if (
// 		isDupplicateName.value === false &&
// 		isDupplicateEmail.value === false &&
// 		nameIsNull.value === false &&
// 		emailIsNull.value === false &&
// 		isInvalidEmail.value === false
// 	) {
// 		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users/${id}`, {
// 			method: 'PUT',
// 			headers: {
// 				'content-type': 'application/json',
// 				Authorization: `Bearer ${author}`
// 			},
// 			body: JSON.stringify({
// 				name: editingUserName.value.trim(),
// 				email: editingUserEmail.value.trim(),
// 				role: editingUserRole.value
// 			})
// 		})
// 		if (res.status === 200) {
// 			console.log('edited successfully')
// 			isUpdateSuccess.value = true
// 			getUsers()
// 			setTimeout(toggleUpdateSuccess, 3000)
// 			editingUserName.value = ''
// 			editingUserEmail.value = ''
// 			editingUserRole.value = ''
// 			editUserMode.value = false
// 		} else if (res.status === 401) {
// 			getRefreshToken()
// 		} else {
// 			console.log('error, cannot be added')
// 		}
// 	}
// }
const isUpdateSuccess = ref(false)
const toggleUpdateSuccess = () => {
	console.log(isUpdateSuccess.value)
	if (isUpdateSuccess.value === true) {
		isUpdateSuccess.value = false
		isShowEditUserModal.value = false
	} else {
		isUpdateSuccess.value = true
	}
}
const editingUserName = ref('')
const editingUserEmail = ref('')
const editingUserRole = ref('')
const checkNullNameEdit = (name) => {
	if (name == '') {
		nameIsNull.value = true
	} else {
		nameIsNull.value = false
	}
}
const checkNullEmailEdit = (email) => {
	if (email === '') {
		emailIsNull.value = true
		isInvalidEmail.value = false
	} else {
		emailIsNull.value = false
	}
}
const selectedUser = ref()
const checkDupInputEdit = (newValues) => {
	getUsers()
	selectedUser.value = users.value.filter((user) => {
		return user.id === newValues.id
	})

	if (selectedUser.value[0].name === newValues.name.trim()) {
		isDupplicateName.value = false
	} else {
		isDupplicateName.value = users.value.some(
			(u) => u.name.toLowerCase() == newValues.name.trim().toLowerCase()
		)
	}

	if (selectedUser.value[0].email === newValues.email.trim()) {
		isDupplicateEmail.value = false
	} else {
		isDupplicateEmail.value = users.value.some(
			(u) => u.email.toLowerCase() == newValues.email.trim().toLowerCase()
		)
	}
}
const clearInput = () => {
	editingUserName.value = ''
	editingUserEmail.value = ''
	editingUserRole.value = 'student'
	userId.value = ''
	editUserMode.value = false
}

let monthNames = [
	'Jan',
	'Feb',
	'Mar',
	'Apr',
	'May',
	'Jun',
	'Jul',
	'Aug',
	'Sep',
	'Oct',
	'Nov',
	'Dec'
]
const extractDate = (date) => {
	//  console.log(date);
	const d = new Date(date + ' UTC')
	//  console.log(d);
	return `${d.getDate()} ${monthNames[d.getMonth()]} ${d.getFullYear()}`
}
const extractTime = (time) => {
	const t = new Date(time + ' UTC')
	const minute = computed(() => {
		if (t.getMinutes() < 10) return '0' + t.getMinutes()
		else return t.getMinutes()
	})
	return `${t.getHours()}:${minute.value} น.`
}
const is401 = ref()
const is403 = ref()

const closeModal = () => {
	isShowConfirm.value = false
}
const showAddModal = () => {
	isShowAddModal.value = true
	getUsers()
}
const isShowAddModal = ref()
const closeAddModal = () => {
	isShowAddModal.value = false
	getUsers()
}
const isShowEditUserModal = ref()
const closeEditUserModal = () => {
	isShowEditUserModal.value = false
}
</script>

<template>
	<div id="root">
		<AddUserModal v-if="isShowAddModal" @closeModal="closeAddModal" />
		<NoLoginModal v-if="is401" />
		<ForAdminModal v-if="is403" />
		<DeleteSuccessModal v-if="isDeleteSuccess" />
		<ConfirmDeleteUserModal
			v-if="isShowConfirm"
			@closeModal="closeModal"
			@confirm="deleteUsers"
		/>
		<EditUserModal
			v-if="isShowEditUserModal"
			@closeModal="closeEditUserModal"
			:editingUserValues="editingUserValues"
			@closeEditUserModal="closeEditUserModal"
			@save="updateUser"
			:isUpdateSuccess="isUpdateSuccess"
			:isNameNull="nameIsNull"
			:duplicateName="isDupplicateName"
			:isEmailNull="emailIsNull"
			:invalidEmail="isInvalidEmail"
			:duplicateEmail="isDupplicateEmail"
		/>
		<div class="all-user-container">
			<div class="table">
				<div v-if="users.length == 0" class="no-user">
					<h1>No Users</h1>
					<p>Let's Add new user</p>
				</div>
				<table v-else>
					<thead>
						<tr class="head">
							<th>Name</th>
							<th>Email</th>
							<th>Role</th>
							<th>Created On</th>
							<th>Updated On</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<tr class="datas" tr-hover v-for="user in users" :key="user.id">
							<td>{{ user.name }}</td>
							<td>{{ user.email }}</td>
							<td>{{ user.role }}</td>
							<td>
								{{ extractDate(user.createdOn) }} {{ extractTime(user.createdOn) }}
							</td>
							<td>
								{{ extractDate(user.updatedOn) }} {{ extractTime(user.updatedOn) }}
							</td>

							<td>
								<button
									type="button"
									class="edit-btn"
									style="opacity: 1; color: white"
									@click="toEditUserMode(user)"
								>
									Edit</button
								>&nbsp;
								<button
									type="button"
									class="delete-btn"
									style="opacity: 1; color: white"
									@click="showConfirmDelete(user.id)"
								>
									Delete
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<i
				class="fa fa-user-plus"
				aria-hidden="true"
				@click="showAddModal"
				v-if="!is401"
			></i>
		</div>
	</div>
</template>

<style scoped>
#root {
	position: relative;
	min-height: 88vh;
}
.no-user {
	display: flex;
	flex-direction: column;
	height: 400px;
	align-items: center;
	justify-content: center;
	color: #5c5c5c;
}

.all-user-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 0 5%;
	/* overflow: auto; */
}
#insertBar {
	width: 95%;
	margin-top: 30px;

	display: flex;
	justify-content: right;
}
input[type='text'],
select {
	padding: 6px 20px;
}
.table {
	display: flex;
	justify-content: center;
	width: 100%;
	margin: 30px 0;
	/* background-color: red; */
}
table {
	width: 100%;
	text-align: center;
}
th {
	color: #495ab6;
}
tr {
	height: 60px;
}
.datas {
	color: black;
}
label,
input {
	margin-right: 10px;
	margin-top: 0;
}
.error {
	text-align: right;
	margin-right: 10px;
	color: red;
}
.signOut-btn:hover {
	padding: 0;
}
.logout {
	margin-top: -200px;
}
button {
	color: white;
	padding: 5px 10px;
	border-radius: 5px;
}
.edit-btn {
	background-color: black;
}
.delete-btn {
	background-color: #d9534f;
}
label {
	color: #495ab6;
}
input {
	border: 2px solid lightgray;
}

i {
	padding: 15px;
	background-color: orange;
	border-radius: 50%;
	color: black;
	position: absolute;
	bottom: 2rem;
	right: 2rem;
	cursor: pointer;
}
i:hover {
	background-color: rgb(255, 176, 31);
}
</style>
