<script setup>
import BaseNavBar from '../components/BaseNavBar.vue'
</script>

<template>
	<div id="root">
		<BaseNavBar />
		<div class="aboutus-container">
			<div class="aboutus-container-grid">
				<div class="girl">
					<img src="../assets/images/about-us/baimon.jpg" class="girl-img" />

					<p>Nitchakarn Pasanee<br />63130500036</p>
					<a href="https://github.com/baimorn" target="_blank">
						<!-- <img src="../assets/images/git.png" width="30" /> -->
						<i class="fa fa-github" aria-hidden="true"></i>
					</a>
				</div>
				<div class="girl">
					<img src="../assets/images/about-us/beam.jpg" class="girl-img" />
					<p>Sarisa Phulakorn<br />63130500116</p>
					<a href="https://github.com/srbeam" target="_blank">
						<!-- <img src="../assets/images/git.png" width="30" /> -->
						<i class="fa fa-github" aria-hidden="true"></i>
					</a>
				</div>
				<div class="girl">
					<img src="../assets/images/about-us/spy.jpg" class="girl-img" />

					<p>Supichaya Muennuch<br />63130500123</p>
					<a href="https://github.com/spyy18" target="_blank">
						<!-- <img src="../assets/images/git.png" width="30" /> -->
						<i class="fa fa-github" aria-hidden="true"></i>
					</a>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
#root {
	height: 100vh;
}
.aboutus-container {
	height: 85%;
	display: flex;
	justify-content: center;
	align-items: center;
}
.aboutus-container-grid {
	display: grid;
	grid-template-columns: repeat(3, auto);
	grid-column-gap: 80px;
	width: 75%;
	height: 65%;
}
.girl {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	border: 1px solid lightgrey;
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	border-radius: 10px;
}
.girl i {
	color: #495ab6;
	font-size: 25px;
}
.girl i:hover {
	color: #5669d1;
}

.girl-img {
	width: 50%;
	border-radius: 50%;
}
p {
	margin: 20px 0 10px 0;
	text-align: center;
	color: #495ab6;
}

@media screen and (max-width: 768px) {
	#root {
		height: 100%;
	}
	.aboutus-container {
		padding: 30px 0 60px 0;
	}
	.aboutus-container-grid {
		grid-template-columns: none;
		grid-template-rows: repeat(3, auto);
		grid-row-gap: 70px;
	}
	.girl {
		min-height: auto;
	}
	.girl-img {
		width: 30%;
	}
}
@media screen and (max-width: 640px) {
}
@media screen and (max-width: 480px) {
}
@media screen and (max-width: 375px) {
}
</style>
