<template>
  <div class="sidebar">
    <!-- 로고 -->
    <div class="top-section">
      <div class="logo">
        <img src="../assets/mainlog.png" alt="새 로고" class="logo-image" />
      </div>

      <!-- 로그인 상태에 따라 UI 변경 -->
      <div class="wrap" v-if="!isLoggedIn">
        <div class="login-button" @click="goToLogin">로그인</div>
        <div class="signup-button" @click="goToSignup">회원가입</div>
        <div class="find-password" @click="goToFind">아이디/비밀번호 찾기</div>
      </div>
      <div class="wrap" v-else>
        <div class="user-info">
          <div class="user-name">
            <span style="color: red; font-weight: bold">{{ userName }}</span>님
            안녕하세요.
          </div>
          <div class="user-dropdown">
            <div class="myPage" @click="viewProfile">내 정보</div>
            <div class="logout_btn" @click="logout">로그아웃</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 검색 섹션 -->
    <div class="search-section">
      <div class="search-title">우리학교 부동산 둘러보기</div>
      <input
        type="text"
        v-model="searchQuery"
        placeholder="찾으시는 매물을 검색하세요."
        @input="searchCampus"
      />
    </div>

    <!-- 검색 결과 -->
    <div class="search-results" v-if="filteredResults.length">
      <ul>
        <li v-for="(campus, index) in filteredResults" :key="index">
          <a :href="campus.link" target="_blank">
            {{ campus.name }} - {{ campus.members }}명
          </a>
        </li>
      </ul>
    </div>
    <div v-else-if="searchQuery.length > 0" class="no-results">
      검색 결과가 없습니다.
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/useUserStore"; // Pinia 스토어 예시

const router = useRouter();
const userStore = useUserStore();

onMounted(() => {
  userStore.checkToken();
});

const isLoggedIn = computed(() => userStore.isLoggedIn);
const userName = computed(() => userStore.user?.name || "Guest");

const searchQuery = ref("");
const campusList = ref([
  { name: "경희대", members: 15000, link: "https://everytime.kr/campus/1" },
  { name: "중앙대", members: 12000, link: "https://everytime.kr/campus/2" },
  { name: "연세대 신촌캠", members: 18000, link: "https://everytime.kr/campus/3" },
]);

const filteredResults = computed(() => {
  if (!searchQuery.value.trim()) return [];
  return campusList.value.filter((campus) =>
    campus.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const goToLogin = () => router.push("/login");
const goToSignup = () => router.push("/sign");
const goToFind = () => router.push("/find");
const viewProfile = () => router.push("/myPage");
const logout = async () => {
  await userStore.logout(router);
};
</script>

<style scoped>
/* 사이드바 스타일 */
.sidebar {
  position: fixed;
  top: 0;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #f9f9f9;
  width: 300px;
  height: 100%;
  background-color: #fff;
}

/* 로고 섹션 */
.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.logo-image {
  width: 230px; /* 원하는 크기로 설정 */
  height: auto;
  object-fit: contain;
}

/* 로그인 상태 */
.wrap {
  width: 100%;
  display: flex;
  flex-direction: column; /* 세로 정렬 */
  align-items: center; /* 가로 중앙 정렬 */
}

.login-button,
.signup-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 90%;
  height: 40px;
  margin-bottom: 12px;
  
  border-radius: 12px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

.login-button {
  border: 1px solid #d2cece;
  background-color: #fff;
  color: #737373;
}

.signup-button {
  background-color: rgb(79, 168, 27); /* 활성화 상태 배경색 */
  color: white;
}

.find-password {
  width: 90%;
  font-size: 12px;
  color: #737373;
  text-align: center;
  cursor: pointer;
}

/* 검색 섹션 */
.search-section {
  padding: 24px 12px 12px 12px;
}

.search-title {
  font-size: 16px;
  font-weight: bold;
  margin-left:10px;
}

input {
  width: 100%;
  height: 44px;
  padding: 0 12px;
  border-radius: 12px;
  font-size: 14px;
  background-color: #f2f2f2;
}

/* 검색 결과 */
.search-results ul {
  list-style: none;
  padding: 0;
}

.search-results a {
  text-decoration: none;
  color: black;
}
</style>
