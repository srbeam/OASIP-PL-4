<script setup>
import { ref, onBeforeMount, computed } from 'vue'

import AddSuccessModal from './AddSuccessModal.vue'
import UpdateSuccessModal from './UpdateSuccessModal.vue'
import DeleteSuccessModal from './DeleteSuccessModal.vue'
import ConfirmDeleteUserModal from './ConfirmDeleteUserModal.vue'
const users = ref([])
const getUsers = async () => {
	const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users`)
	if (res.status === 200) {
		users.value = await res.json()
	} else {
		console.log('Error,cannot get events from backend')
	}
}
onBeforeMount(async () => {
	await getUsers()
})

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
				method: 'DELETE'
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
		} else {
			console.log('Error!! Can not delete this user.')
		}
	}
}

const editUserMode = ref(false)
const toEditUserMode = (user) => {
	editUserMode.value = true
	console.log(user)
	userId.value = user.id
	editingUserName.value = user.name.trim()
	editingUserEmail.value = user.email.trim()
	editingUserRole.value = user.role
	// userName.value = user.name
	// userEmail.value = user.email
	// userRole.value = user.role
	nameIsNull.value = false
	emailIsNull.value = false
	isInvalidEmail.value = false
	isDupplicateEmail.value = false
	isDupplicateName.value = false
}
const updateUser = async () => {
	await updateUsersfetch(userId.value)
	// window.location.reload()
	getUsers()
}
const updateUsersfetch = async (id) => {
	console.log(editUserMode.value)
	checkNullNameEdit()
	checkNullEmailEdit()

	if (validateEmail(editingUserEmail.value.trim()) === false) {
		isInvalidEmail.value = true
	} else {
		isInvalidEmail.value = false
	}

	checkDupInputEdit()
	if (
		isDupplicateName.value === false &&
		isDupplicateEmail.value === false &&
		nameIsNull.value === false &&
		emailIsNull.value === false &&
		isInvalidEmail.value === false
	) {
		const res = await fetch(`${import.meta.env.VITE_BACK_URL}/users/${id}`, {
			method: 'PUT',
			headers: {
				'content-type': 'application/json'
			},
			body: JSON.stringify({
				name: editingUserName.value.trim(),
				email: editingUserEmail.value.trim(),
				role: editingUserRole.value
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
		} else console.log('error, cannot be added')
	}
}
const isUpdateSuccess = ref(false)
const toggleUpdateSuccess = () => {
	console.log(isUpdateSuccess.value)
	if (isUpdateSuccess.value === true) {
		isUpdateSuccess.value = false
	} else {
		isUpdateSuccess.value = true
	}
}
const editingUserName = ref('')
const editingUserEmail = ref('')
const editingUserRole = ref('')
const checkNullNameEdit = () => {
	if (editingUserName.value.trim() == '') {
		nameIsNull.value = true
	} else {
		nameIsNull.value = false
	}
}
const checkNullEmailEdit = () => {
	if (editingUserEmail.value.trim() === '') {
		emailIsNull.value = true
		isInvalidEmail.value = false
	} else {
		emailIsNull.value = false
	}
}
const selectedUser = ref()
const checkDupInputEdit = () => {
	getUsers()
	selectedUser.value = users.value.filter((user) => {
		return user.id === userId.value
	})
	if (selectedUser.value[0].name === editingUserName.value.trim()) {
		isDupplicateName.value = false
	} else {
		isDupplicateName.value = users.value.some(
			(u) => u.name.toLowerCase() == editingUserName.value.toLowerCase()
		)
	}

	if (selectedUser.value[0].email === editingUserEmail.value.trim()) {
		isDupplicateEmail.value = false
	} else {
		isDupplicateEmail.value = users.value.some(
			(u) => u.email.toLowerCase() == editingUserEmail.value.toLowerCase()
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
</script>

<template>
	<div>
		<div class="all-user-container">
			<div id="insertBar">
				<div>
					<div class="flex mb-4">
						<div id="edit-user-mode" v-if="editUserMode" class="flex">
							<label>Name :</label>
							<div class="nameInput">
								<input
									type="text"
									placeholder="Type you name here..."
									class="rounded-md w-90 text-black"
									v-model="editingUserName"
									maxlength="100"
								/>
								<p
									v-show="nameIsNull === true && isDupplicateName === false"
									class="error"
								>
									{{ nameIsNullMsg }}
								</p>
								<p
									v-show="nameIsNull === false && isDupplicateName === true"
									class="error"
								>
									{{ dupplicateNameMsg }}
								</p>
							</div>
							<div class="emailInput">
								<label>Email :</label>
								<input
									type="text"
									placeholder="Type you email here..."
									class="rounded-md w-90 text-black"
									v-model="editingUserEmail"
									maxlength="50"
								/>
								<p v-show="emailIsNull === true" class="error">
									{{ emailNullMsg }}
								</p>
								<p
									v-show="isInvalidEmail === true && emailIsNull === false"
									class="error"
								>
									{{ emailNotValidMsg }}
								</p>
								<p v-show="isDupplicateEmail === true" class="error">
									{{ dupplicateEmailMsg }}
								</p>
							</div>
							<div class="roleInput">
								<label>Role :</label>
								<select
									class="border-2 border-gray-200 rounded-md p-1 text text-black w-56"
									v-model="editingUserRole"
								>
									<option value="student" selected>Student</option>
									<option value="admin">Admin</option>
									<option value="lecturer">Lecturer</option>
								</select>
							</div>
						</div>
						<!-- <div id="add-user-mode" v-else class="flex">
							<label>Name :</label>
							<div class="nameInput">
								<input
									type="text"
									placeholder="Type you name here..."
									class="rounded-md w-90 text-black"
									v-model="userName"
									maxlength="100"
								/>
								<p
									v-show="nameIsNull === true && isDupplicateName === false"
									class="error"
								>
									{{ nameIsNullMsg }}
								</p>
								<p
									v-show="nameIsNull === false && isDupplicateName === true"
									class="error"
								>
									{{ dupplicateNameMsg }}
								</p>
							</div>
							<div class="emailInput">
								<label>Email :</label>
								<input
									type="text"
									placeholder="Type you email here..."
									class="rounded-md w-90 text-black"
									v-model="userEmail"
									maxlength="50"
								/>
								<p v-show="emailIsNull === true" class="error">
									{{ emailNullMsg }}
								</p>
								<p v-show="isInvalidEmail === true" class="error">
									{{ emailNotValidMsg }}
								</p>
								<p v-show="isDupplicateEmail === true" class="error">
									{{ dupplicateEmailMsg }}
								</p>
							</div>
							<div class="roleInput">
								<label>Role :</label>
								<select
									class="border-2 border-gray-200 rounded-md p-1 text text-black w-56"
									v-model="userRole"
								>
									<option value="student" selected>Student</option>
									<option value="admin">Admin</option>
									<option value="lecturer">Lecturer</option>
								</select>
							</div>
						</div> -->

						<div id="buttonsearch" v-if="editUserMode">
							<button
								class="bg-green-600 hover:bg-green-700 p-2 px-3 rounded-md ml-5"
								@click="updateUser()"
							>
								Save
							</button>
							<button
								class="bg-gray-200 hover:bg-gray-100 p-2 px-3 rounded-md ml-5 text-black"
								@click="clearInput()"
							>
								Cancel
							</button>
							<!-- <button
								class="bg-green-600 hover:bg-green-700 p-2 px-3 rounded-md ml-5"
								@click="addUser"
								v-if="editUserMode === false"
							>
								Add
							</button> -->
						</div>
					</div>
				</div>
			</div>

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
							<td>{{ user.createdOn }}</td>
							<td>{{ user.updatedOn }}</td>

							<td>
								<button
									type="button"
									class="btn btn-dark"
									style="opacity: 1; color: white"
									@click="toEditUserMode(user)"
								>
									Edit</button
								>&nbsp;
								<button
									type="button"
									class="btn btn-danger"
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
			<AddSuccessModal v-if="isAddSuccess === true && editUserMode === false" />
			<UpdateSuccessModal v-if="isUpdateSuccess === true" />
			<ConfirmDeleteUserModal
				@confirm="deleteUsers"
				@closeModal="closeConfirmModal"
				v-if="isShowConfirm"
			/>
			<DeleteSuccessModal v-if="isDeleteSuccess === true" />
		</div>
	</div>
</template>

<style scoped>
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
}
#insertBar {
	width: 100%;
}
input[type='text'],
select {
	padding: 6px 20px;
}
.table {
	display: flex;
	justify-content: center;
	width: 100%;
}
table {
	width: 100%;
	text-align: center;
}
tr {
	height: 65px;
}
.datas {
	color: white;
}
label,
input {
	margin-right: 10px;
}
.error {
	text-align: right;
	margin-right: 10px;
	color: red;
}
/* label {
 align-self: center;
} */
</style>