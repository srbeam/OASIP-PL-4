<script setup></script>

<template>
	<div>
		<div id="container">
			<div id="left">
				<p class="welcome">Welcome , you are in</p>
				<p id="web-name">BOOKIING</p>

				<p>
					OASIP-PL4 Website for booking clinics <br />
					INT222 Integrated IT Project II
				</p>

				<img src="../assets/images/home2.png" />
			</div>
		</div>
	</div>
</template>

<style scoped>
#container {
	background-color: rgb(255, 255, 255, 0);
	color: black;
	display: flex;
	padding: 5% 13%;
	/* min-height: 575px; */
}
#left {
	width: 100%;
	color: #495ab6;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
#left p {
	text-align: center;
}
#web-name {
	font-size: 65px;
	margin: 0;
}
img {
	width: 50%;
}
.welcome {
	margin: 0;
}
</style>
