<template>
  <header class="navbar">
    <div class="navbar-container">
      <h1 class="navbar-logo" @click="goToHome">에브리동산</h1>
      <nav>
        <ul class="navbar-menu">
          <li><a href="#" @click="goToHome">홈</a></li>
          <li><a href="#" @click="goToBoard">게시판</a></li>
          <li><a href="#" @click="goToSearch">부동산</a></li>
          <!-- 로그인 상태에 따라 버튼을 동적으로 변경 -->
          <li v-if="isLoggedIn">
            <a href="#" @click="handleLogout">로그아웃</a>
          </li>
          <li v-else>
            <a href="#" @click="goToLogin">로그인</a>
          </li>
        </ul>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/useUserStore"; // Pinia 상태 관리 예시

const router = useRouter();
const userStore = useUserStore(); // Pinia 스토어 사용

// 로그인 상태 확인 (토큰이 있는지 여부)
const isLoggedIn = computed(() => !!userStore.token);

// 네비게이션 이동 함수
const goToHome = () => {
  router.push("/");
};

const goToLogin = () => {
  router.push("/login");
};

const goToBoard = () => {
  router.push("/board");
};

const goToSearch = () => {
  router.push("/searchMap");
};

// 로그아웃 처리
const handleLogout = () => {
  userStore.logout(); // Pinia에서 로그아웃 함수 호출
  router.push("/"); // 로그아웃 후 홈으로 이동
};
</script>

<style scoped>
/* 네비게이션 바 스타일 */
.navbar {
  background-color: #333333; /* 메인 색상 */
  color: white;
  padding: 10px 20px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 auto;
  padding: 5px;
}

.navbar-logo {
  font-size: 1.5rem;
  font-weight: bold;
  cursor: pointer;
}

.navbar-menu {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
}

.navbar-menu li {
  margin: 0 15px;
}

.navbar-menu a {
  text-decoration: none;
  color: rgb(255, 254, 254);
  font-size: 1rem;
  font-weight: bold;
  transition: color 0.3s;
}

.navbar-menu a:hover {
  color: #f73b3b; /* 호버 시 강조 색상 */
}
</style>
