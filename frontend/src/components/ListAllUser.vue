<script setup>
import { ref, onBeforeMount, computed } from 'vue'

import ConfirmDeleteUserModal from './ConfirmDeleteUserModal.vue'
import NoLoginModal from './NoLoginModal.vue'
import ForAdminModal from './ForAdminModal.vue'
import ListallComponent from './ListallComponent.vue'
import AddUserModal from './AddUserModal.vue'

import EditUserModal from './EditUserModal.vue'
import SuccessModal from './SuccessModal.vue'

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
		window.location.reload()
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

const showConfirmDelete = (user) => {
	isShowConfirm.value = true
	selectedUser.value = user
}
const isShowConfirm = ref(false)
const isChooseConfirm = ref(false)
const closeConfirmModal = () => {
	isShowConfirm.value = false
}

const deleteUsers = async () => {
	isChooseConfirm.value = true
	getUsers()
	if (isChooseConfirm.value) {
		const res = await fetch(
			`${import.meta.env.VITE_BACK_URL}/users/${selectedUser.value.id}`,
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
			typeOfModal.value = 'deleteUser'
			isDeleteSuccess.value = true
			setTimeout(toggleDeleteSuccess, 3000)
			getUsers()
		} else if (res.status === 401) {
			getRefreshToken()
			isChooseConfirm.value = true
			isShowConfirm.value = false
			isDeleteSuccess.value = false
			typeOfModal.value = ''
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

let editingUserValues = ref()
const updateUser = async (user) => {
	await updateUsersfetch(user)
	// window.location.reload()
	getUsers()
}
const updateUsersfetch = async (user) => {
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
				role: user.role
			})
		})
		if (res.status === 200) {
			console.log('edited successfully')
			typeOfModal.value = 'editUser'
			isUpdateSuccess.value = true
			getUsers()
			setTimeout(toggleUpdateSuccess, 3000)
			editingUserName.value = ''
			editingUserEmail.value = ''
			editingUserRole.value = ''
			editUserMode.value = false
		} else if (res.status === 401) {
			getRefreshToken()
			typeOfModal.value = ''
		} else {
			console.log('error, cannot be added')
		}
	}
}

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
	const d = new Date(date + ' UTC')
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
	getUsers()
	isShowAddModal.value = true
}
const isShowAddModal = ref()
const closeAddModal = () => {
	isShowAddModal.value = false
	getUsers()
	typeOfModal.value = 'addUser'
}

const isShowEditUserModal = ref()
const closeEditUserModal = () => {
	isShowEditUserModal.value = false
}
const typeOfModal = ref('none')
</script>

<template>
	<div>
		<AddUserModal v-if="isShowAddModal" @closeModal="closeAddModal" />
		<NoLoginModal v-if="is401" />
		<ForAdminModal v-if="is403" />
		<!-- <DeleteSuccessModal v-if="isDeleteSuccess" /> -->
		<ConfirmDeleteUserModal
			v-if="isShowConfirm"
			@closeModal="closeModal"
			@confirm="deleteUsers"
			:userSelected="selectedUser"
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
			:typeOfModal="typeOfModal"
		/>
		<SuccessModal v-if="isDeleteSuccess" :typeOfModal="typeOfModal" />

		<div class="py-10 px-4 relative">
			<div class="overflow-auto rounded-lg shadow hidden lg:block lg:mx-20">
				<div
					v-if="users.length == 0"
					class="flex flex-cols justify-center items-center text-[#5c5c5c]"
				>
					<h1>No Users</h1>
					<p>Let's Add new user</p>
				</div>
				<table class="w-full">
					<thead class="text-[#495ab6] bg-gray-100 border-b-2 border-gray-200">
						<tr>
							<th class="p-3 text-sm track-wide text-left">Name</th>
							<th class="p-3 text-sm track-wide text-left">Email</th>
							<th class="p-3 text-sm track-wide text-left">Role</th>
							<th class="p-3 text-sm track-wide text-left">Created On</th>
							<th class="p-3 text-sm track-wide text-left">Updated On</th>
							<th class="p-3 text-sm track-wide text-left">Action</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-gray-100">
						<tr class="bg-white" v-for="user in users" :key="user.id">
							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								{{ user.name }}
							</td>
							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								{{ user.email }}
							</td>
							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								{{ user.role }}
							</td>
							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								{{ extractDate(user.createdOn) }} {{ extractTime(user.createdOn) }}
							</td>
							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								{{ extractDate(user.updatedOn) }} {{ extractTime(user.updatedOn) }}
							</td>

							<td class="p-3 text-sm text-gray-700 whitespace-nowrap">
								<button
									type="button"
									class="bg-black text-white rounded-md py-1 px-2.5"
									@click="toEditUserMode(user)"
								>
									Edit</button
								>&nbsp;
								<button
									type="button"
									class="bg-[#d9534f] text-white rounded-md py-1 px-2.5"
									@click="showConfirmDelete(user)"
								>
									Delete
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="grid grid-cols-1 gap-4 lg:hidden sm:px-14">
				<h4 class="text-gray-500 m-0">List All Users</h4>
				<div
					class="bg-white p-4 rounded-lg shadow sm:flex"
					v-for="user in users"
					:key="user.id"
				>
					<div class="w-full">
						<div class="flex items-center space-x-2 text-sm text-[#495ab6]">
							Name : &nbsp;<span class="text-gray-500">{{ user.name }}</span>
							<span class="text-gray-500 py-1 px-3 rounded-full bg-gray-200 text-xs">
								{{ user.role.toUpperCase() }}
							</span>
						</div>
						<div class="flex items-center space-x-2 text-sm text-[#495ab6]">
							Email : &nbsp;<span class="text-gray-500">{{ user.email }}</span>
						</div>
						<div class="flex items-center space-x-2 text-sm text-[#495ab6]">
							Created On : &nbsp;<span class="text-gray-500">{{
								user.createdOn
							}}</span>
						</div>
						<div class="flex items-center space-x-2 text-sm text-[#495ab6]">
							Updated On : &nbsp;<span class="text-gray-500">{{
								user.updatedOn
							}}</span>
						</div>
					</div>
					<div class="w-full text-right mt-2 sm:mt-0 sm:w-1/2">
						<button
							type="button"
							class="bg-black text-white rounded-md py-1 px-2.5 mr-2"
							@click="toEditUserMode(user)"
						>
							Edit
						</button>
						<button
							type="button"
							class="bg-[#d9534f] text-white rounded-md py-1 px-2.5"
							@click="showConfirmDelete(user)"
						>
							Delete
						</button>
					</div>
				</div>
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
