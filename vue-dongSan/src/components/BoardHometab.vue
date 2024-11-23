<template>
  <div class="home-tab-wrapper">
    <nav class="home-tab-left">
      <div class="home-tab">
        <!-- 전체 -->
        <div
          class="home-tab-item"
          :class="{ active: activeTab === 'all' }"
          @click="setActiveTab('all')"
        >
          <span>전체</span>
        </div>

        <!-- 부동산 -->
        <div
          class="home-tab-item"
          :class="{ active: activeTab === 'rental' }"
          @click="setActiveTab('rental')"
        >
          <span>부동산</span>
        </div>

        <!-- 학생 -->
        <div
          class="home-tab-item"
          :class="{ active: activeTab === 'student' }"
          @click="setActiveTab('student')"
        >
          <span>학생</span>
        </div>
      </div>
    </nav>

    <!-- 매물등록 버튼 (로그인 상태에서만 보임) -->
    <div class="home-tab-right" v-if="isLoggedIn">
      <button class="register-button" @click="goToRegister">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="currentColor"
          viewBox="0 0 24 24"
          height="16px"
          width="16px"
          class="icon"
        >
          <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"></path>
        </svg>
        매물등록
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useUserStore } from "@/stores/useUserStore"; // Pinia 스토어 사용

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 로그인 여부 확인
const isLoggedIn = computed(() => userStore.isLoggedIn);

const activeTab = ref("all");

// 라우터 이동 함수
const setActiveTab = (tab) => {
  activeTab.value = tab;

  if (tab === "all") {
    router.push("/board");
  } else if (tab === "rental") {
    router.push("/board/rental");
  } else if (tab === "student") {
    router.push("/board/student");
  }
};

// 매물등록 페이지로 이동
const goToRegister = () => {
  router.push("/board/register");
};

// 라우터 경로 변화에 따라 activeTab 동기화
watch(
  () => route.path,
  (newPath) => {
    if (newPath === "/board") {
      activeTab.value = "all";
    } else if (newPath === "/board/rental") {
      activeTab.value = "rental";
    } else if (newPath === "/board/student") {
      activeTab.value = "student";
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.home-tab-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 73%;
  padding: 12px 0;
  margin: 0 auto;
  margin-top: 60px;
  z-index: 999;
}

.home-tab-left {
  display: flex;
  align-items: center;
  height: 50px;
  user-select: none;
}

.home-tab {
  display: flex;
  align-items: center;
  position: relative;
}

.home-tab-item {
  cursor: pointer;
  padding: 10px 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
  text-align: center;
  transition: background-color 0.3s ease;
}

.home-tab-item.active {
  background-color: rgb(79, 168, 27); /* 활성화 상태 배경색 */
  color: white;
  font-weight: bold;
}

/* 매물등록 버튼 */
.home-tab-right {
  display: flex;
  align-items: center;
}

.register-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 16px;
  background-color: rgb(79, 168, 27);
  border: none;
  border-radius: 4px;
  color: white;
  font-weight: bold;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.register-button:hover {
  background-color: rgb(65, 145, 25);
}

.register-button .icon {
  margin-right: 8px;
}
</style>
