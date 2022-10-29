<script setup>
import { computed } from '@vue/reactivity'
import SuccessModal from './SuccessModal.vue'
defineEmits(['save', 'closeEditUserModal'])
const props = defineProps({
	editingUserValues: {
		type: Object,
		default: {}
	},
	isUpdateSuccess: {
		type: Boolean,
		default: false
	},
	isNameNull: {
		type: Boolean,
		default: false
	},
	duplicateName: {
		type: Boolean,
		default: false
	},
	isEmailNull: {
		type: Boolean,
		default: false
	},
	invalidEmail: {
		type: Boolean,
		default: false
	},
	duplicateEmail: {
		type: Boolean,
		default: false
	},
	typeOfModal: {
		type: String,
		default: ''
	}
})
const newValues = computed(() => props.editingUserValues)
const updateSuccess = computed(() => props.isUpdateSuccess)
const nameIsNull = computed(() => props.isNameNull)
const isDupplicateName = computed(() => props.duplicateName)
const emailIsNull = computed(() => props.isEmailNull)
const isInvalidEmail = computed(() => props.invalidEmail)
const isDupplicateEmail = computed(() => props.duplicateEmail)
</script>

<template>
	<div>
		<div class="modal">
			<div class="modal-container">
				<div class="close">
					<p @click="$emit('closeModal')">X</p>
				</div>

				<div class="modal-content">
					<h3>Edit User</h3>
					<div id="signup">
						<div class="input">
							<div>
								<label>Name :</label>
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
									This name is already use
								</span>
							</div>

							<input
								type="text"
								id="fname"
								name="username"
								placeholder="YOUR NAME"
								v-model="newValues.name"
								maxlength="100"
							/>
						</div>
						<div class="input">
							<div class="lable-ane-alert">
								<label>Email :</label>
								<p v-show="emailIsNull === true" class="error">
									Email is require field
								</p>
								<p
									v-show="isInvalidEmail === true && emailIsNull === false"
									class="error"
								>
									Email is not valid
								</p>
								<p v-show="isDupplicateEmail === true" class="error">
									This email is already use
								</p>
							</div>

							<input
								type="text"
								placeholder="Type you email here..."
								class="rounded-md text-black"
								v-model="newValues.email"
								maxlength="50"
							/>
						</div>
						<div class="input">
							<label>Role :</label>
							<select
								class="border-2 border-gray-200 rounded-md p-1 text text-black"
								v-model="newValues.role"
							>
								<option value="student" selected>Student</option>
								<option value="admin">Admin</option>
								<option value="lecturer">Lecturer</option>
							</select>
						</div>
					</div>
				</div>
				<div class="btn-area">
					<button class="save" @click="$emit('save', newValues)">Save</button>
					<button class="cancel" @click="$emit('closeEditUserModal')">Cancel</button>
				</div>
			</div>
		</div>

		<SuccessModal :typeOfModal="typeOfModal" v-if="updateSuccess" />
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
}
.modal-container {
	border: 1px solid black;
	background-color: white;
	border-radius: 10px;
	min-width: 400px;
	/* width: 35%; */
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
	/* margin: 0 0 25px 0; */
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
	margin-left: 5px;
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
	color: black;
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
	/* margin-top: 25px; */

	width: 100%;
	display: flex;
}
.btn-area button {
	width: 100%;
	/* margin-bottom: 10px; */
	padding: 10px 0;
	cursor: pointer;
}
#go-to-signIn a:hover {
	background-color: rgb(255, 255, 255, 0);
}
.save {
	background-color: #316cf4;
	border-bottom-left-radius: 10px;
}
.save:hover {
	background-color: #4f83fd;
}
.cancel {
	background-color: grey;
	border-bottom-right-radius: 10px;
}
.cancel:hover {
	background-color: rgb(152, 152, 152);
}
.lable-ane-alert {
	display: flex;
}
</style>
