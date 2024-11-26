<template>
  <div class="home-tab-wrapper">
    <nav class="home-tab-left">
      <div class="home-tab">
        <!-- 전체 -->
        <div class="home-tab-item" :class="{ active: activeTab === 'all' }" @click="setActiveTab('all')">
          <span>전체</span>
        </div>

        <!-- 부동산 -->
        <div class="home-tab-item" :class="{ active: activeTab === 'rental' }" @click="setActiveTab('rental')">
          <span>부동산</span>
        </div>

        <!-- 학생 -->
        <div class="home-tab-item" :class="{ active: activeTab === 'student' }" @click="setActiveTab('student')">
          <span>학생</span>
        </div>
      </div>
    </nav>

    <!-- 검색 입력 -->
    <div class="search-bar">
      <select v-model="searchType">
        <option value="title">제목</option>
        <option value="school">학교</option>
      </select>
      <input v-if="value === title" v-model="searchQuery" type="text"
        :placeholder="searchType === 'title' ? '제목을 입력해주세요' : '학교 이름을 정확히 입력해주세요'" />
      <button class="search-button" @click="searchPosts">검색</button>
    </div>

    <!-- 매물등록 버튼과 검색 -->
    <div class="home-tab-right">
      <!-- 매물등록 버튼 (로그인 상태에서만 보임) -->
      <button class="register-button" v-if="isLoggedIn" @click="goToRegister">
        <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24" height="16px" width="16px"
          class="icon">
          <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"></path>
        </svg>
        매물등록
      </button>
    </div>
  </div>




</template>


<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/useUserStore.js";
import { useTabStore } from "../stores/useTabStore.js";
import { useBoardStore } from "../stores/useBoardStore.js"; // 게시물 스토어
import Swal from "sweetalert2";

const router = useRouter();
const userStore = useUserStore();
const tabStore = useTabStore();
const boardStore = useBoardStore();

// 로그인 여부 확인
const isLoggedIn = computed(() => userStore.isLoggedIn);

// 현재 선택된 탭
const activeTab = computed(() => tabStore.selectedTab);

// 검색 상태
const searchQuery = ref(""); // 검색어
const searchType = ref("title"); // 검색 타입 (title 또는 school)

// 탭 설정
const setActiveTab = async (tab) => {
  activeTab.value = tab;
  await tabStore.setTab(tab); // 선택된 탭에 따라 게시물 요청
};

// 매물등록 페이지로 이동
const goToRegister = () => {
  router.push("/board/register");
};

// 검색 기능
const searchPosts = async () => {
  if (!searchQuery.value) {
    alert("검색어를 입력해주세요.");
    return;
  }

  // 검색 요청
  const hasRes = await boardStore.searchPosts(searchQuery.value, searchType.value);
  console.log(hasRes);
  if (!hasRes) {
    await Swal.fire({
      icon: "info",
      title: "검색 결과 없음",
      text: "일치하는 매물이 없습니다.",
      confirmButtonText: "확인",
    });
    await tabStore.setTab("all"); // "전체" 탭으로 이동
  }
};

</script>


<style scoped>
.home-tab-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 73%;
  padding: 12px 0;
  margin: 0 auto;
  margin-top: 40px;
  margin-bottom: 20px;
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
  background-color: rgb(79, 168, 27);
  /* 활성화 상태 배경색 */
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

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-bar input {
  padding: 10px;
  width: 250px;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex: 1;
}

.search-bar select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-button {
  padding: 8px 16px;
  background-color: rgb(79, 168, 27);
  border: none;
  border-radius: 4px;
  color: white;
  cursor: pointer;
  font-weight: bold;
}

.search-button:hover {
  background-color: rgb(65, 145, 25);
}
</style>
