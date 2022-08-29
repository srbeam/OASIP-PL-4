<script setup></script>

<template>
	<div>
		<div class="aboutas-container">
			<div class="grid grid-cols-3 gap-10 mt-5">
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
.aboutas-container {
	padding: 0 5%;
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
</style>
