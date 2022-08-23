<script setup></script>

<template>
	<div id="gameWindow">
		<div>
			<nav class="navbar navbar-expand-lg">
				<div id="logo">
					<img src="./assets/images/background/logo.png" width="50%" />
				</div>

				<div class="collapse navbar-collapse" id="navbarNavAltMarkup">
					<div class="navbar-nav"></div>
				</div>
			</nav>
		</div>
		<div id="box"></div>
		<div>
			<div id="searchbar">
				<div>
					Search Name Event :
					<input class="form-control" type="text" id="searchname" />
				</div>
				<div class="selectcategory">
					<select name="Category" id="Category">
						<option value="Project Management">Search By Clinic Category</option>
						<option value="Dev-Ops Infra">Dev-Ops Infra</option>
						<option value="Database">Database</option>
						<option value="Front-End">Front-End</option>
						<option value="Back-End">Back-End</option>
					</select>
				</div>
				<div></div>
			</div>
		</div>

		<div>
			<div id="nexthome">
				<button type="button" class="btn btn-success" style="opacity: 1">
					Confirm
				</button>
			</div>
			<div id="buttonhome">
				<button type="button" class="btn btn-danger" style="opacity: 1">
					Cancle
				</button>
			</div>
			<div id="addhome">
				<button type="button" class="btn btn-info" style="opacity: 1; color: white">
					Add Event
				</button>
			</div>
			<div id="option">
				<a>You can create your event here</a><br />
				<a>คุณสามารถสร้างอีเวนต์ที่นี้ได้</a><br />
				<br />
				<br />
				<a>List by Category</a>
			</div>
		</div>
		<div>
			<div class="banner">
				<router-link :to="{ name: 'Schedule' }">Schedule</router-link>
			</div>
			<div>
				<router-view> </router-view>
			</div>
		</div>
	</div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Mitr&display=swap');

#gameWindow {
	font-family: 'Mitr';
	font-size: 16px;
	color: white;
	overflow: hidden;
	background-repeat: no-repeat;
	background-position: bottom;
	background-attachment: fixed;
	/* cursor: url(./assets/image/click/click1.png) 24 50, auto; */
}

html,
body {
	background: url(./assets/images/background/background.jpg);
}
#logo {
	margin-top: -10px;
}
#box {
	width: 350px;
	height: 550px;
	padding: 60px;
	background-color: white;
	opacity: 0.5;
	margin-left: 20px;
	margin-top: 35px;
}
#searchbar {
	position: fixed;
	width: 1115px;
	height: 550px;
	padding: 60px;
	background-color: white;
	opacity: 0.5;
	margin-left: 400px;
	margin-top: -550px;
	color: black;
}
#search {
	margin-left: 45em;
	position: absolute;
	width: 500px;
	margin-top: 0.25em;
}
.navbar-nav {
	margin-left: -10em;
	margin-top: 150em;
	margin-top: -1em;
	padding: 30px;
	color: black;
}
.navbar {
	background-color: #383838;
}
.selectcategory {
	margin-top: 25px;
}
#buttonhome {
	margin-top: -80px;
	margin-left: 550px;
	position: fixed;
}
#nexthome {
	margin-top: -80px;
	margin-left: 460px;
	position: fixed;
}
#addhome {
	margin-top: -520px;
	margin-left: 50px;
	position: fixed;
}
#option {
	margin-top: -478px;
	margin-left: 50px;
	position: fixed;
	color: #383838;
	font-size: 12px;
}
.banner {
	margin-left: 300px;
	margin-top: -639px;
	position: fixed;
}
</style>
