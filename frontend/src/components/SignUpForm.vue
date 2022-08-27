<script setup></script>

<template>
	<div>
		<div id="home">
			<div id="left">
				<p id="web-name">bookiing</p>

				<p>
					OASIP-PL4 Website for booking clinics <br />
					INT222 Integrated IT Project II
				</p>

				<img src="../assets/images/home2.png" />
			</div>
			<div id="right">
				<div id="register">
					<div class="title">
						<h3>Sign Up</h3>
					</div>

					<div id="signup">
						<div class="input">
							<label for="fanme">Name :</label> &nbsp;
							<input type="text" id="fname" name="username" placeholder="YOUR NAME" />
						</div>
						<div class="input">
							<label for="email">E-MAIL : </label>
							<input type="text" id="email" name="email" placeholder="YOUR E-MAIL" />
						</div>
						<div class="input">
							<label for="passwd">PASSWORD : </label>
							<input
								type="password"
								id="passwd"
								name="password"
								minlength="8"
								placeholder="YOUR PASSWORD"
							/>
						</div>
						<div class="input">
							<label for="fanme">CONFIRM-PASSWORD :</label>
							<input
								type="password"
								id="confirm-passwd"
								name="password"
								minlength="8"
								placeholder="COMFIRM YOUR PASSWORD"
							/>
						</div>
						<div class="input">
							<label for="role">ROLE : </label>
							<select>
								<option value="0" selected>Student</option>
								<option value="1">Lecturer</option>
								<option value="2">Admin</option>
							</select>
						</div>
						<div class="btn-area">
							<div>
								<router-link :to="{ name: 'Page', params: { page: 1 } }">
									<div>
										<button class="text-white btn btn-primary">Sign Up</button>
										<!-- <button class="text-white btn btn-primary" id="getstart">
                        Get Start !
                    </button> -->
									</div>
								</router-link>
							</div>
							<div id="go-to-signIn">
								<p>
									Already have an account?
									<router-link :to="{ name: 'SignInForm' }">SIGN IN</router-link>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped>
#home {
	background-color: rgb(255, 255, 255, 0);
	color: black;
	display: flex;
	padding: 0 13%;
}
#left {
	width: 50%;
	color: white;
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
	background-color: rgba(255, 255, 255, 0.2);
	color: white;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 20px 0;
	width: 90%;
	border-radius: 10px;
}
.input {
	margin-top: 10px;
}

.input select {
	color: black;
	padding: 7px 15px;
	border-radius: 4px;
	width: 100%;
}
input {
	width: 100%;
	padding: 7px 15px;
	box-sizing: border-box;
	border: 2px solid white;
	border-radius: 4px;
	color: #636363;
}
#signup {
	width: 80%;
}
.role {
	color: #636363;
	size: 220px;
}

.btn-area {
	text-align: center;
	margin-top: 25px;
}
.btn-area button {
	width: 100%;
	margin-bottom: 10px;
}
</style>
