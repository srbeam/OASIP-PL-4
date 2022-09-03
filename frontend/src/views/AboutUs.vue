<script setup></script>

<template>
	<div>
		<div class="aboutus-container">
			<div class="aboutus-container-grid">
				<div class="girl">
					<img src="../assets/images/036.png" class="girl-img" />
					<p>Nitchakarn Pasanee<br />63130500036</p>
					<a href="https://github.com/baimorn" target="_blank">
						<img src="../assets/images/git.png" width="30" />
					</a>
				</div>
				<div class="girl">
					<img src="../assets/images/134.png" class="girl-img" />
					<p>Sarisa Phulakorn<br />63130500116</p>
					<a href="https://github.com/srbeam" target="_blank">
						<img src="../assets/images/git.png" width="30" />
					</a>
				</div>
				<div class="girl">
					<img src="../assets/images/016.png" class="girl-img" />

					<p>Supichaya Muennuch<br />63130500123</p>
					<a href="https://github.com/spyy18" target="_blank">
						<img src="../assets/images/git.png" width="30" />
					</a>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
.aboutus-container {
	padding: 0 5%;
}
.aboutus-container-grid {
	display: grid;
	grid-template-columns: repeat(3, auto);
	grid-column-gap: 40px;
	margin-top: 20px;
}
.girl {
	/* text-align: center; */
	/* border: 1px solid black; */
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	min-height: 500px;
}
.girl-img {
	width: 60%;
}
p {
	margin: 20px 0 30px 0;
	text-align: center;
}
.git {
	/* margin-left: 45%; */
}
@media screen and (max-width: 768px) {
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
