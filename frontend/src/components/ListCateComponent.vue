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
	if (author != undefined || author != null) {
		getUserFromToken.value = VueJwtDecode.decode(author)
	}
}
onBeforeMount(async () => {
	await getUser()
})
</script>

<template>
	<div>
		<!-- The Modal -->
		<div
			class="fixed z-[2] left-0 top-0 w-full h-full overflow-auto flex justify-center items-center bg-black/50"
			v-if="showForm"
		>
			<div class="bg-[#fefefe] absolute rounded text-[#383838] p-5 mx-4 sm:mx-0">
				<div class="flex flex-row">
					<img src="../assets/images/category.png" width="50" />
					<h3 class="ml-5 mt-2">{{ currentEdit.eventCategoryName }}</h3>
				</div>
				<span class="text-sm text-red-500 mt-4" v-if="isBlank"
					>Please fill out the information completely. |
					กรุณากรอกข้อมูลให้ครบด้วยค่ะ</span
				>

				<span class="text-sm text-red-500 mt-4" v-if="isInvalidDuration">
					The event duration is out of range. (1-480 min) |
					เวลาของนัดอยู่นอกช่วงเวลาที่กำหนด (1-480 นาที)
				</span>
				<span class="text-sm text-red-500 mt-4" v-if="isDuplicateName">
					EventsName must not have duplicate names. | EventsName ต้องไม่มีชื่อซ้ำกัน
				</span>
				<div class="py-4">
					Clinic Name :
					<span class="text-sm text-red-500"> * </span>
					<span class="lenght"> 100 character | 100 ตัวอักษร </span><br />
					<input
						type="text"
						class="rounded-md w-full bg-gray-200/50 p-1 mb-3 text-sm"
						v-model="updateName"
						maxlength="100"
					/><br />
					Clinic Duration
					<span class="text-sm text-red-500"> * </span>
					<span class="lenght">between 1 and 480 | ช่วงเวลาระหว่าง 1 ถึง 480</span>
					<br />
					<input
						type="number"
						min="1"
						max="480"
						class="rounded-md text-center bg-gray-200/50 p-1 mb-3 text-sm"
						v-model="updateDuration"
					/>
					min.<br />
					Clinic Description :
					<span class="lenght"> 500 character | 500 ตัวอักษร</span> <br />
					<textarea
						type="text"
						class="rounded-md w-full bg-gray-200/50 p-1 text-sm"
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

		<!-- Categories -->
		<div class="p-5 grid gap-y-5 sm:gap-y-7 lg:mx-5 xl:mx-32">
			<h4 class="lg:hidden text-gray-500 text-center">Categories</h4>
			<div
				class="text-[#383838] p-4 rounded-lg border shadow lg:flex"
				v-for="category in categories"
			>
				<div class="hidden lg:flex lg:w-1/5 lg:justify-center">
					<img src="../assets/images/briefcase.png" class="lg:w-20 lg:h-20" />
				</div>
				<div class="lg:w-4/5 lg:flex">
					<div class="md:w-full lg:w-4/5 lg:pr-5 lg:pl-5">
						<div class="">
							<p class="text-[#495ab6] text-2xl">{{ category.eventCategoryName }}</p>

							<p>
								{{ category.eventCategoryDescription }}
							</p>

							<div
								class="w-full lg:w-44 bg-[#eee385] rounded-full p-1 px-3 text-center text-sm"
							>
								<span> Duration : {{ category.eventDuration }} min.</span>
							</div>
						</div>
					</div>

					<div class="md:w-full lg:w-2/5">
						<div
							class="text-center sm:grid sm:grid-cols-2 lg:flex lg:flex-col sm:gap-x-4 mt-2.5 sm:mt-4 lg:mt-0"
						>
							<div class="mb-2">
								<button
									class="w-full bg-blue-500 hover:bg-blue-400 p-2 px-3 rounded-md text-white"
									@click="editMode(category)"
									v-if="
										getUserFromToken != undefined &&
										getUserFromToken.Roles === 'ROLE_admin'
									"
								>
									Edit category
								</button>
							</div>

							<router-link
								:to="{ name: 'ListByCategory', params: { id: category.id } }"
								v-if="
									getUserFromToken != undefined &&
									(getUserFromToken.Roles === 'ROLE_student' ||
										getUserFromToken.Roles === 'ROLE_admin')
								"
							>
								<button
									class="w-full bg-orange-600 hover:bg-orange-400 p-2 px-3 rounded-md text-white"
								>
									Show events in category
								</button>
							</router-link>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
