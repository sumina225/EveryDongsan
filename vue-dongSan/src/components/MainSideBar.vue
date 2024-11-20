<template>
  <div class="sidebar">
    <!-- 로고/로그인/회원가입/아이디,비번찾기 div -->
    <div class="top-section">
      <!-- 로고 -->
      <div class="logo">
        <div class="logo-symbol"></div>
        <div class="logo-text"></div>
      </div>

      <!-- 로그인 -->
      <div class="login-button" @click="goToLogin">로그인</div>

      <!-- 회원가입 -->
      <div class="signup-button" @click="goToSignup">회원가입</div>

      <!-- 아이디/비밀번호 찾기 -->
      <div class="find-password">아이디/비밀번호 찾기</div>
    </div>

    <!-- 우리학교 커뮤니티 둘러보기 -->
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
          <a :href="campus.link" target="_blank"
            >{{ campus.name }} - {{ campus.members }}명</a
          >
        </li>
      </ul>
    </div>
    <div v-else-if="searchQuery.length > 0" class="no-results">
      검색 결과가 없습니다.
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const searchQuery = ref("");
const campusList = ref([
  { name: "경희대", members: 15000, link: "https://everytime.kr/campus/1" },
  { name: "중앙대", members: 12000, link: "https://everytime.kr/campus/2" },
  {
    name: "연세대 신촌캠",
    members: 18000,
    link: "https://everytime.kr/campus/3",
  },
]);

const filteredResults = computed(() => {
  if (searchQuery.value.trim() === "") return [];
  return campusList.value.filter((campus) =>
    campus.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const goToLogin = () => {
  router.push("/login");
};

const goToSignup = () => {
  router.push("/sign");
};

const searchCampus = () => {
  // 검색어 변경 시 자동으로 filteredResults가 업데이트됨
};
</script>

<style scoped>
/* 사이드바 스타일 */
.sidebar {
  position: fixed;
  top: 0;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  border-right: 1px solid #f9f9f9;
  width: 300px;
  height: 100%;
  overflow: auto;
  background-color: #fff;
}

/* 로고/로그인/회원가입/아이디,비번찾기 div */
.top-section {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 60px 24px 40px 24px;
  border-bottom: 1px solid #f2f2f2;
}

/* 로고 */
.logo {
  display: flex;
  align-items: center;
  margin-bottom: 28px;
  gap: 8px;
}

.logo-symbol {
  width: 38px;
  height: 38px;
  background-image: url("https://everytime.kr/images/about/symbol.svg");
}

.logo-text {
  width: 146px;
  height: 30px;
  background-image: url("https://everytime.kr/images/about/logo.svg");
}

/* 로그인 */
.login-button {
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  margin-bottom: 8px;
  border: 1px solid #ededed;
  border-radius: 12px;
  width: 100%;
  height: 40px;
  color: #737373;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

/* 회원가입 */
.signup-button {
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  margin-bottom: 12px;
  border-radius: 12px;
  width: 100%;
  height: 40px;
  color: #f9f9f9;
  font-size: 14px;
  font-weight: bold;
  background-color: #f91f15;
  cursor: pointer;
}

/* 아이디/비밀번호 찾기 */
.find-password {
  line-height: 18px;
  color: #737373;
  font-size: 12px;
  text-align: center;
  cursor: pointer;
}

/* 우리학교 커뮤니티 둘러보기 */
.search-section {
  display: flex;
  flex-direction: column;
  padding: 24px 12px 12px 12px;
  gap: 8px;
}

.search-title {
  display: flex;
  padding-left: 12px;
  line-height: 24px;
  color: #292929;
  font-size: 16px;
  font-weight: bold;
}

/* 검색 input */
input {
  position: relative;
  display: flex;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 0 48px 0 12px;
  border-radius: 12px;
  width: 100%;
  height: 44px;
  line-height: 20px;
  color: #292929;
  font-size: 14px;
  background-color: #f2f2f2;
  background-image: url("https://everytime.kr/images/about/icon_search.svg");
  background-repeat: no-repeat;
  background-position: center right 12px;
  background-size: 24px 24px;
}

/* 검색 결과 */
.search-results {
  margin-top: 20px;
}

.search-results ul {
  list-style: none;
  padding: 0;
}

.search-results li {
  margin-bottom: 10px;
}

.search-results a {
  text-decoration: none;
  color: #fff;
}

.no-results {
  color: #888;
  margin-top: 20px;
  margin-left: 20px;
}
</style>
