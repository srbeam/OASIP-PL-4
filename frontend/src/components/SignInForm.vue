<script setup>
import { ref } from 'vue'
</script>

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
				<div id="logIn">
					<div class="title">
						<h3>Sign In</h3>
					</div>

					<div id="signIn">
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
						<div class="btn-area">
							<div>
								<router-link :to="{ name: 'Page', params: { page: 1 } }">
									<div>
										<button class="text-white btn btn-primary">Sign In</button>
										<!-- <button class="text-white btn btn-primary" id="getstart">
                        Get Start !
                    </button> -->
									</div>
								</router-link>
							</div>
							<div id="go-to-signUp">
								<p>
									Not have an account?
									<router-link :to="{ name: 'Home' }">SIGN UP</router-link>
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
	min-height: 575px;
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

#logIn {
	background-color: rgba(255, 255, 255, 0.2);
	color: white;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 20px 0;
	width: 90%;
	border-radius: 10px;
	min-height: 380px;
}
#signIn {
	width: 80%;
}
.input {
	margin-top: 10px;
}
input {
	width: 100%;
	padding: 7px 15px;
	box-sizing: border-box;
	border: 2px solid white;
	border-radius: 4px;
	color: #636363;
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
