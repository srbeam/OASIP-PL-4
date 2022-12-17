<script setup>
import BaseNavBar from '../components/BaseNavBar.vue'
</script>

<template>
	<div>
		<BaseNavBar />
		<div class="py-14 flex justify-center items-center">
			<div
				class="grid grid-rows-3 gap-y-16 text-[#495ab6] sm:text-lg lg:flex xl:w-11/12 2xl:w-10/12 xl:mt-10"
			>
				<div
					class="flex flex-col items-center justify-center border-2 shadow-md rounded-xl mx-5 lg:h-[350px] xl:h-[420px]"
				>
					<img
						src="../assets/images/about-us/baimon.jpg"
						class="w-2/5 rounded-full sm:w-1/4 lg:w-5/12 2xl:w-1/2"
					/>

					<p class="text-center mt-3.5 mb-2.5">
						Nitchakarn Pasanee<br />63130500036
					</p>
					<a href="https://github.com/baimorn" target="_blank">
						<i
							class="fa fa-github text-2xl sm:text-3xl hover:text-[#5669d1]"
							aria-hidden="true"
						></i>
					</a>
				</div>
				<div
					class="flex flex-col items-center justify-center border-2 shadow-md rounded-xl py-4 mx-5"
				>
					<img
						src="../assets/images/about-us/beam.jpg"
						class="w-2/5 rounded-full sm:w-1/4 lg:w-5/12 2xl:w-1/2"
					/>
					<p class="text-center mt-3.5 mb-2.5">Sarisa Phulakorn<br />63130500116</p>
					<a href="https://github.com/srbeam" target="_blank">
						<i
							class="fa fa-github text-2xl sm:text-3xl hover:text-[#5669d1]"
							aria-hidden="true"
						></i>
					</a>
				</div>
				<div
					class="flex flex-col items-center justify-center border-2 shadow-md rounded-xl py-4 mx-5"
				>
					<img
						src="../assets/images/about-us/spy.jpg"
						class="w-2/5 rounded-full sm:w-1/4 lg:w-5/12 2xl:w-1/2"
					/>

					<p class="text-center mt-3.5 mb-2.5">
						Supichaya Muennuch<br />63130500123
					</p>
					<a href="https://github.com/spyy18" target="_blank">
						<i
							class="fa fa-github text-2xl sm:text-3xl hover:text-[#5669d1]"
							aria-hidden="true"
						></i>
					</a>
				</div>
			</div>
		</div>
	</div>
</template>
