<template>
  <div class="wrap">
    <div class="container">
      <div class="account_area">
        <ul class="myinfo_tab">
          <li :class="{ on: activeTab === 'id' }" @click="activeTab = 'id'">
            <a href="#">아이디 찾기</a>
          </li>
          <li
            :class="{ on: activeTab === 'password' }"
            @click="activeTab = 'password'"
          >
            <a href="#">비밀번호 찾기</a>
          </li>
        </ul>
        <!-- 아이디 찾기 화면 -->
        <div v-if="activeTab === 'id'" class="find-password">
          <h3 class="pass_h3">아이디 찾기</h3>
          <p class="text_info">
            찾고자 하는 <span>아이디</span>의 <span>이메일</span>을 입력하세요.
          </p>
          <form @submit.prevent="handleSubmit">
            <input
              type="email"
              v-model="email"
              placeholder="이메일을 입력하세요"
              required
            />
            <button type="submit">아이디 찾기</button>
          </form>

          <div v-if="foundId">
            <p>찾은 아이디: {{ foundId }}</p>
          </div>
          <div v-if="errorMsg">
            <p style="color: red">{{ errorMsg }}</p>
          </div>
        </div>

        <!-- 비밀번호 찾기 화면 -->
        <div v-if="activeTab === 'password'" class="find-password">
          <h3 class="pass_h3">비밀번호 찾기</h3>
          <p class="text_info">
            <span>비밀번호</span>를 찾고자하는 <span>아이디</span>를
            입력해주세요.
          </p>
          <form @submit.prevent="handleSubmit2">
            <input
              type="text"
              v-model="userId"
              placeholder="아이디를 입력하세요"
              required
            />
            <button type="submit">비밀번호 찾기</button>
          </form>

          <div v-if="foundEmail">
            <p>비밀번호를 {{ foundEmail }}에 발송했습니다.</p>
          </div>
          <div v-if="errorMsg2">
            <p style="color: red">{{ errorMsg2 }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/useUserStore";

// 사용자 입력값을 관리하는 변수
const userId = ref("");
const email = ref("");
const activeTab = ref("id"); // 'id'와 'password' 탭을 관리하는 변수
const userStore = useUserStore();
const { foundId, errorMsg, errorMsg2, foundEmail, findIdByEmail, findPwById } =
  userStore;

// 아이디 찾기 요청 처리 함수
const handleSubmit = () => {
  if (!email.value) {
    userStore.errorMsg = "이메일을 입력해주세요.";
    return;
  }

  findIdByEmail(email.value); // 이메일로 아이디 찾기
};

// 비밀번호 찾기 요청 처리 함수
const handleSubmit2 = () => {
  if (!userId.value) {
    userStore.errorMsg2 = "존재하지 않는 아이디입니다.";
    return;
  }

  userStorefindPwById(userId.value); // 아이디로 이메일 찾기
};
</script>

<style scoped>
html,
body {
  width: 100%;
  height: 100%;
}

ol,
ul {
  list-style: none;
}

.icon-image {
  display: block;
  margin: 20px auto 10px; /* 가운데 정렬과 간격 */
  width: 50px; /* 아이콘 크기 */
  height: auto;
}

.wrap {
  width: 1000px;
  height: calc(100vh - 93px);
  padding: 0;
  margin: 9px auto 0 auto;
  background-color: #ffffff;
  flex-grow: 1;
}

.container {
  padding-top: 40px;
  padding-bottom: 40px;
  text-align: center;
  min-height: 500px;
}

.account_area {
  position: relative;
  padding-bottom: 40px;
}

.account_area h3 {
  display: block;
  height: 20px;
  margin: 44px 0 16px;
  font-size: 18px;
  letter-spacing: -1px;
  color: #666;
  font-weight: normal;
}

.account_area .myinfo_tab {
  overflow: hidden;
  width: 100%;
  height: 36px;
  margin-top: 24px;
  text-align: center;
  background: url(https://res.sooplive.co.kr/images/account/bg_tab.gif) repeat-x;
}

.account_area .myinfo_tab li {
  display: inline-block;
  width: 200px;
  font-size: 14px;
  list-style: none;
  border: 1px solid #eee;
  border-radius: 4px;
}

.account_area .myinfo_tab li a {
  display: block;
  padding: 10px 0 10px;
  line-height: 100%;
  text-align: center;
  color: #6c6c6c;
  text-decoration: none;
  letter-spacing: -1px;
}

.account_area .myinfo_tab li.on {
  border: 1px solid #888;
  border-bottom: none;
  background-color: #fff;
}

.account_area .myinfo_tab li.on a {
  padding: 13px 0 10px;
  font-weight: bold;
  text-decoration: underline;
  color: rgb(0, 0, 0);
}

.find-password {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.find-password h3.pass_h3 {
  display: block;
  width: 239px;
  height: 38px;
  margin: 50px auto 0;
  text-indent: -9999px;
}

.find-password .text_info {
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 15px;
  color: #888;
  text-align: center;
  line-height: 150%;
  letter-spacing: -1px;
}

.find-password .text_info span {
  color: #2e6afd;
  font-weight: bold;
}

.find-password input {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
}
.find-password button {
  padding: 10px 20px;
  background-color: #fd3e3e;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
</style>
