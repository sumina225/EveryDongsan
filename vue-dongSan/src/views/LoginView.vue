<template>
  <div class="container">
    <!-- 로고 -->
    <div class="logo" @click="goToHome"></div>

    <!-- 로그인 폼 -->
    <form @submit.prevent="handleLogin">
      <div class="input-group">
        <input v-model="username" type="text" placeholder="아이디" required />
      </div>
      <div class="input-group">
        <input
          v-model="password"
          type="password"
          placeholder="비밀번호"
          required
        />
      </div>
      <div class="input-group">
        <button type="submit" class="login-btn">로그인</button>
      </div>

      <!-- 로그인 유지 체크박스 -->
      <div class="login-options">
        <label class="keep-login">
          <input type="checkbox" v-model="isChecked" class="checkbox-input" />
          <span class="checkbox"></span> 로그인 유지
        </label>
        <router-link to="/find" class="forgot-password"
          >아이디/비밀번호 찾기</router-link
        >
      </div>

      <!-- 회원가입 링크 -->
      <div class="signup">
        <p>
          <router-link to="/sign">회원가입</router-link>
        </p>
      </div>
    </form>

    <!-- 하단 링크 -->
    <footer>
      <a href="https://everytime.kr/page/serviceagreement">이용약관</a>
      <a href="https://everytime.kr/page/privacy">개인정보처리방침</a>
      <a href="https://everytime.kr/page/faq">문의하기</a>
      <a href="https://everytime.kr" class="copyright">© 에브리동산</a>
    </footer>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

const username = ref("");
const password = ref("");
const isChecked = ref(false);
const router = useRouter();

const handleLogin = () => {
  if (username.value && password.value) {
    alert(`아이디: ${username.value}\n비밀번호: ${password.value}`);
  } else {
    alert("아이디와 비밀번호를 입력해주세요.");
  }
};

const goToHome = () => {
  router.push("/");
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  border: 0;
  -webkit-touch-callout: none;
}

body,
div,
nav,
aside,
article,
h1,
h2,
h3,
h4,
ol,
ul,
li,
dl,
dt,
dd,
p,
span,
form,
th,
td,
input,
textarea,
select,
pre,
address {
  font-family: "Apple SD Gothic Neo", "맑은 고딕", "돋움", sans-serif;
}

img {
  display: block;
}

a {
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0.05);
  color: inherit;
  text-decoration: none;
}

@media (prefers-color-scheme: dark) {
  a {
    -webkit-tap-highlight-color: rgba(255, 255, 255, 0.05);
  }
}

address,
h1,
h2,
h3,
h4,
h5,
h6 {
  font-weight: normal;
}

input,
textarea,
select {
  outline: none;
}

input,
textarea {
  caret-color: #f91f15;
}

@media (prefers-color-scheme: dark) {
  input,
  textarea {
    caret-color: #ee3c35;
  }
}

input::-moz-placeholder,
textarea::-moz-placeholder {
  color: #a6a6a6;
}

@media (prefers-color-scheme: dark) {
  input::-moz-placeholder,
  textarea::-moz-placeholder {
    color: #6e6e6e;
  }
}

input::placeholder,
textarea::placeholder {
  color: #a6a6a6;
}

@media (prefers-color-scheme: dark) {
  input::placeholder,
  textarea::placeholder {
    color: #6e6e6e;
  }
}

body {
  color: #292929;
  background-color: #ffffff;
  font-size: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.container {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 360px;
  transform: translate(-50%, -50%);
}

.logo {
  margin: 0 auto 40px;
  width: 210px;
  height: 56px;
  background-image: url("https://account.everytime.kr/images/pages/login/logo_light.png");
  background-size: cover;
}

.input-group {
  margin-top: 20px;
}

input {
  display: block;
  box-sizing: border-box;
  padding: 10px;
  border-radius: 12px;
  width: 100%;
  font-size: 16px;
  background-color: #f2f2f2;
}

button {
  display: block;
  box-sizing: border-box;
  padding: 10px;
  border-radius: 12px;
  width: 100%;
  font-size: 16px;
  font-weight: bold;
  background-color: #f91f15;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #e31b14;
}

.login-options {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.keep-login {
  display: inline-flex;
  align-items: center;
  color: #a6a6a6;
  font-size: 14px;
  cursor: pointer;
  gap: 8px;
}

.checkbox-input {
  display: none;
}

.checkbox {
  width: 14px;
  height: 14px;
  border: 1px solid #a6a6a6;
  border-radius: 3px;
  background-color: white;
  position: relative;
  cursor: pointer;
}

.checkbox-input:checked + .checkbox {
  background-color: #f91f15;
  border-color: #f91f15;
}

.checkbox:after {
  content: "✔";
  position: absolute;
  top: 0;
  left: 2px;
  font-size: 12px;
  color: white;
  opacity: 0;
  transition: opacity 0.2s;
}

.checkbox-input:checked + .checkbox:after {
  opacity: 1;
}

.forgot-password {
  color: #737373;
  font-size: 14px;
  text-decoration: underline;
}

.signup {
  text-align: center;
  margin-top: 40px;
}

.signup a {
  color: #737373;
  font-size: 14px;
  text-decoration: underline;
}

footer {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 96px;
}

footer a {
  color: #737373;
  font-size: 14px;
}

footer a.copyright {
  font-weight: bold;
}
</style>
