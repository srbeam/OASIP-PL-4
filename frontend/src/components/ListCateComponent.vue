<script setup>
import { onBeforeMount, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import VueJwtDecode from 'vue-jwt-decode'

defineEmits(['deleteEvent'])
const props = defineProps({
	categories: {
		type: Array,
		require: true
	}
})

const showForm = ref(false)
const categories = computed(() => props.categories)

const currentEdit = ref()
const updateName = ref()
const updateDescription = ref()
const updateDuration = ref()

let isBlank = ref(false)
let isInvalidDuration = ref(false)
let isDuplicateName = ref(false)

const editMode = (category) => {
	showForm.value = true
	currentEdit.value = category
	updateName.value = currentEdit.value.eventCategoryName
	updateDescription.value = currentEdit.value.eventCategoryDescription
	updateDuration.value = currentEdit.value.eventDuration
}

const save = () => {
	validateNameDuplicate(updateName.value)
	console.log(updateDuration.value == '' && updateDuration.value != 0)
	console.log(validateDuration(updateDuration.value))
	if (
		updateName.value == '' ||
		updateDescription.value == '' ||
		(updateDuration.value == '' && updateDuration.value != 0)
	) {
		isBlank.value = true
		isInvalidDuration.value = false
		isDuplicateName.value = false
	} else if (!validateNameDuplicate(updateName.value)) {
		isDuplicateName.value = true
		isBlank.value = false
		isInvalidDuration.value = false
	} else if (!validateDuration(updateDuration.value)) {
		isInvalidDuration.value = true
		isBlank.value = false
		isDuplicateName.value = false
	} else {
		editCategory()
	}
}
const author = localStorage.getItem('token')
let refreshToken = localStorage.getItem('refreshToken')
const token = ref()
const editCategory = async () => {
	const res = await fetch(
		`${import.meta.env.VITE_BACK_URL}/categories/${currentEdit.value.id}`,
		{
			method: 'PUT',
			headers: {
				'content-type': 'application/json',
				Authorization: `Bearer ${author}`
			},
			body: JSON.stringify({
				eventCategoryName: updateName.value.trim(),
				eventCategoryDescription: updateDescription.value,
				eventDuration: updateDuration.value
			})
		}
	)
	if (res.status === 200) {
		console.log('edited successfully')
		// cancelEdit()
		goSuccess()
	} else if (res.status == 401) {
		getRefreshToken()
	} else console.log('error, cannot be added')
}
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
		is401.value = true
	} else {
		console.log('Error,cannot get refresh token from backend')
	}
}
const saveLocal = () => {
	localStorage.setItem('token', `${token.value.accessToken}`)
	localStorage.setItem('refreshToken', `${token.value.refreshToken}`)
}
const validateDuration = (duration) => {
	return duration <= 480 && duration >= 1
}

const validateNameDuplicate = (categoryName) => {
	const nameAllCategory = []
	categories.value.forEach((category) => {
		if (category.id != currentEdit.value.id) {
			nameAllCategory.push(category.eventCategoryName.toLowerCase())
		}
	})
	return !nameAllCategory.includes(categoryName.toLowerCase())
}

const cancelEdit = () => {
	showForm.value = false
	isBlank.value = false
	isInvalidDuration.value = false
	isDuplicateName.value = false
	updateName.value = ''
	updateDescription.value = ''
	updateDuration.value = ''
}

const appRouter = useRouter()
const goSuccess = () => appRouter.go(0)
const getUserFromToken = ref()
const getUser = () => {
	getUserFromToken.value = VueJwtDecode.decode(localStorage.getItem('token'))
}
onBeforeMount(async () => {
	await getUser()
})
</script>

<template>
	<div>
		<!-- <button id="myBtn">Open Modal</button> -->

		<!-- The Modal -->
		<div class="pop-up" v-if="showForm">
			<div class="modal-content p-5">
				<div class="flex flex-row">
					<img src="../assets/images/category.png" class="" width="50" />
					<h3 class="ml-5 mt-2">{{ currentEdit.eventCategoryName }}</h3>
				</div>
				<div class="error mt-4" v-if="isBlank">
					Please fill out the information completely. | กรุณากรอกข้อมูลให้ครบด้วยค่ะ
				</div>
				<div class="error mt-4" v-if="isInvalidDuration">
					The event duration is out of range. (1-480 min) |
					เวลาของนัดอยู่นอกช่วงเวลาที่กำหนด (1-480 นาที)
				</div>
				<div class="error mt-4" v-if="isDuplicateName">
					EventsName must not have duplicate names. | EventsName ต้องไม่มีชื่อซ้ำกัน
				</div>
				<div class="py-4">
					Clinic Name :
					<span class="error"> * </span>
					<span class="lenght"> 100 character | 100 ตัวอักษร </span><br />
					<input
						type="text"
						class="rounded-md w-96"
						v-model="updateName"
						maxlength="100"
					/><br />
					Clinic Duration
					<span class="error"> * </span>
					<span class="lenght">between 1 and 480 | ช่วงเวลาระหว่าง 1 ถึง 480</span>
					<br />
					<input
						type="number"
						min="1"
						max="480"
						class="rounded-md text-center"
						v-model="updateDuration"
					/>
					min.<br />
					Clinic Description :
					<span class="lenght"> 500 character | 500 ตัวอักษร</span> <br /><textarea
						type="text"
						class="rounded-md"
						v-model="updateDescription"
						maxlength="500"
					></textarea>
				</div>
				<div class="grid grid-cols-2 text-white">
					<button class="p-2 bg-green-500 hover:bg-green-600" @click="save">
						save
					</button>
					<button class="p-2 bg-red-500 hover:bg-red-600" @click="cancelEdit">
						cancel
					</button>
				</div>
			</div>
		</div>

		<div id="category-container">
			<div class="category-box" v-for="category in categories">
				<!-- <div
							class="category-box flex flex-row items-center rounded-xl"
							v-for="category in categories"
						> -->
				<div class="img-area">
					<img src="../assets/images/briefcase.png" />
				</div>
				<div class="content">
					<div>
						<p class="categoryName">{{ category.eventCategoryName }}</p>
						<p class="cateDesc">
							Description : {{ category.eventCategoryDescription }}
						</p>
						<span
							id="duration"
							class="rounded-full w-44 h-7 p-1 px-3 text-black text-sm"
							>Duration : {{ category.eventDuration }} min.</span
						>
					</div>
				</div>
				<div id="btn-area">
					<div class="Btn">
						<button
							class="edit bg-blue-500 hover:bg-blue-400 p-2 px-3 rounded-md text-white"
							@click="editMode(category)"
							v-if="getUserFromToken.Roles === 'ROLE_admin'"
						>
							Edit category
						</button>
						<router-link
							:to="{ name: 'ListByCategory', params: { id: category.id } }"
							v-if="
								getUserFromToken.Roles === 'ROLE_student' ||
								getUserFromToken.Roles === 'ROLE_admin'
							"
						>
							<button
								class="bg-orange-600 hover:bg-orange-400 p-2 px-3 rounded-md text-white"
							>
								Show events in category
							</button>
						</router-link>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
#category-container {
	padding: 3% 15%;
	display: grid;
	grid-row-gap: 30px;
}
.category-box {
	/* background-color: rgba(255, 255, 255, 0.5); */
	/* width: 150%; */
	/* height: 12em; */
	color: #383838;
	/* margin-left: 20%; */
	/* margin-top: 3%; */
	padding: 2.5%;
	display: flex;
	border-radius: 10px;
	border: 1px solid lightgrey;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
}
.img-area {
	/* background-color: red; */
	width: 12%;
	padding: 0 30px;
}
.content {
	width: 80%;
}

#duration {
	background-color: #eee385;
}

.edit {
	width: 79.5%;
	margin-bottom: 3%;
}

.Btn {
	text-align: center;
	color: white;
}

/* The Modal (background) */
.pop-up {
	position: fixed;
	z-index: 2;
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

.error {
	font-size: 0.9em;
}

/* Modal Content/Box */
.modal-content {
	background-color: #fefefe;
	position: absolute;
	margin: 6% auto; /* 15% from the top and centered */
	margin-left: 25%;
	padding: 20px;
	border: 1px solid #888;
	width: 50%; /* Could be more or less, depending on screen size */
	color: #383838;
}

input {
	background-color: rgba(220, 220, 220, 0.4);
	padding: 1.5%;
	margin-bottom: 2%;
	font-size: 0.9em;
}

textarea {
	width: 95%;
	height: 7em;
	resize: both;
	background-color: rgba(220, 220, 220, 0.4);
	padding: 1.5%;
	font-size: 0.9em;
}
.categoryName {
	color: #495ab6;
	font-size: 36px;
	margin: 0;
}

@media screen and (max-width: 768px) {
}
</style>
