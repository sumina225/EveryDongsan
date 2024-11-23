<template>
  <div class="sidebar">
    <!-- 로고 -->
    <div class="top-section">
      <div class="logo">
        <img src="../assets/mainlog.png" alt="새 로고" class="logo-image" />
      </div>

      <!-- 로그인/로그아웃 상태에 따라 변경 -->
      <div class="wrap">
        <!-- 로그인 상태 메시지 -->
        <div v-if="isLoggedIn" class="welcome-message">
          <p>{{ userName }}</p> 님, 환영합니다.
        </div>
        <div class="action-button" @click="handlePrimaryAction">
          {{ primaryActionText }}
        </div>
        <div class="action-button secondary" @click="handleSecondaryAction">
          {{ secondaryActionText }}
        </div>
        <div class="find-password" v-if="!isLoggedIn" @click="goToFind">
          아이디/비밀번호 찾기
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
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/useUserStore";

const router = useRouter();
const userStore = useUserStore();

onMounted(() => {
  userStore.checkToken(); // 토큰 체크
});

const isLoggedIn = computed(() => userStore.isLoggedIn);
const userName = computed(() => userStore.user?.name || "");

// 동적으로 텍스트와 동작 변경
const primaryActionText = computed(() =>
  isLoggedIn.value ? "로그아웃" : "로그인"
);
const secondaryActionText = computed(() =>
  isLoggedIn.value ? "내 정보" : "회원가입"
);

// 버튼 동작
const handlePrimaryAction = async () => {
  if (isLoggedIn.value) {
    await userStore.logout(router); // 로그아웃
  } else {
    router.push("/login"); // 로그인 페이지로 이동
  }
};

const handleSecondaryAction = () => {
  if (isLoggedIn.value) {
    router.push("/myPage"); // 내 정보 페이지로 이동
  } else {
    router.push("/sign"); // 회원가입 페이지로 이동
  }
};

// 검색 관련 로직
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

const goToFind = () => router.push("/find");
const searchCampus = () => {};


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

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.logo-image {
  width: 230px;
  height: auto;
  object-fit: contain;
}

/* 공통 버튼 스타일 */
.wrap {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.welcome-message {
  margin-top: 5px;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
  display: flex;
  float: left;
}

.welcome-message p{
  color: rgb(79, 168, 27);
  font-weight: bold;
}

.action-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 85%;
  height: 40px;
  margin-bottom: 12px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
}

.action-button {
  background-color: rgb(79, 168, 27);
  color: white;
}

.action-button.secondary {
  border: 1px solid #d2cece;
  background-color: #fff;
  color: #737373;
}

.find-password {
  width: 85%;
  font-size: 13px;
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
  margin-left: 10px;
}

input {
  width: 95%;
  height: 44px;
  padding: 0 12px;
  border-radius: 12px;
  font-size: 14px;
  background-color: #f2f2f2;
  margin-left:5px;
  margin-top:5px;
}


</style>