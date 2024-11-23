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

          
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Swal from "sweetalert2";
import apiClient from "@/axios";

// 사용자 입력값을 관리하는 변수
const userId = ref("");
const email = ref("");
const activeTab = ref("id"); // 'id'와 'password' 탭을 관리하는 변수

// 아이디 찾기 요청 처리 함수
const handleSubmit = async () => {
  if (!email.value) {
    Swal.fire({
      icon: "warning",
      title: "입력 오류",
      text: "이메일을 입력해주세요.",
    });
    return;
  }

  try {
    const response = await apiClient.get("/member/findId", {
      params: { emailId: email.value.split("@")[0], emailDomain: email.value.split("@")[1] },
    });

    if (response.status === 200) {
      Swal.fire({
        icon: "success",
        title: "아이디 찾기 성공",
        text: `찾은 아이디: ${response.data}`,
      });
    } else {
      Swal.fire({
        icon: "error",
        title: "아이디 찾기 실패",
        text: "해당 이메일로 생성된 계정이 존재하지 않습니다.",
      });
    }
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "오류 발생",
      text: "아이디 찾기 중 문제가 발생했습니다. 다시 시도해주세요.",
    });
    console.error(error);
  }
};

// 비밀번호 찾기 요청 처리 함수
const handleSubmit2 = async () => {
  if (!userId.value) {
    Swal.fire({
      icon: "warning",
      title: "입력 오류",
      text: "아이디를 입력해주세요.",
    });
    return;
  }

  try {
    const response = await apiClient.post("/member/findPw", {
      email: userId.value, // 이메일 기반으로 찾는 로직
    });

    Swal.fire({
      icon: "success",
      title: "비밀번호 찾기 성공",
      text: "임시 비밀번호가 이메일로 발송되었습니다.",
    });
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "비밀번호 찾기 실패",
      text: "아이디가 존재하지 않거나 이메일 발송에 실패했습니다.",
    });
    console.error(error);
  }
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
