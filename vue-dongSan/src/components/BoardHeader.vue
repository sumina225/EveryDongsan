<template>
  <header class="navbar">
    <div class="navbar-inner">
      <!-- 로고 -->
      <div class="navbar-logo">
        <a href="/">
          <img src="..\assets\sublogo.png" alt="이미지" />
        </a>
      </div>

      <!-- 네비게이션 버튼 -->
      <div class="navbar-links">
        <a href="/board" class="navbar-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path fill="currentColor"
              d="M4 19v-2h2v-7c0-1.383.417-2.612 1.25-3.688.833-1.075 1.917-1.779 3.25-2.112v-.7c0-.417.146-.77.438-1.063A1.447 1.447 0 0 1 12 2c.417 0 .77.146 1.063.438.291.291.437.645.437 1.062v.7c1.333.333 2.417 1.037 3.25 2.112C17.583 7.388 18 8.617 18 10v7h2v2H4Zm8 3c-.55 0-1.02-.196-1.412-.587A1.926 1.926 0 0 1 10 20h4c0 .55-.196 1.02-.588 1.413A1.926 1.926 0 0 1 12 22Zm-4-5h8v-7c0-1.1-.392-2.042-1.175-2.825C14.042 6.392 13.1 6 12 6s-2.042.392-2.825 1.175C8.392 7.958 8 8.9 8 10v7Z">
            </path>
          </svg>
        </a>
        <a href="/searchMap" class="navbar-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path fill="currentColor"
              d="m19.326 20.712-5.809-5.808a5.693 5.693 0 0 1-1.765.98 6.394 6.394 0 0 1-2.133.354c-1.849 0-3.414-.64-4.697-1.923C3.641 13.033 3 11.468 3 9.619c0-1.849.637-3.414 1.91-4.697C6.183 3.641 7.744 3 9.593 3c1.866 0 3.44.64 4.723 1.922 1.282 1.283 1.922 2.848 1.922 4.697 0 .75-.117 1.46-.352 2.132a6.052 6.052 0 0 1-.982 1.793l5.834 5.808a.888.888 0 0 1 .262.654c0 .261-.096.497-.288.706a.944.944 0 0 1-.693.288.945.945 0 0 1-.693-.288Zm-9.707-6.436c1.29 0 2.39-.453 3.297-1.36s1.36-2.006 1.36-3.297c0-1.308-.453-2.411-1.36-3.31-.907-.898-2.006-1.347-3.297-1.347-1.308 0-2.411.45-3.309 1.347-.898.899-1.348 2.002-1.348 3.31 0 1.29.45 2.39 1.348 3.297.898.907 2.001 1.36 3.31 1.36Z">
            </path>
          </svg>
        </a>

        <!-- 로그인 상태에 따른 UI -->
        <div v-if="isLoggedIn" class="user-section">
          <span @click="toggleDropdown" class="user-name">{{ userName }}님</span>
          <div v-if="dropdownOpen" class="user-dropdown">
            <button @click="logout" class="logout-btn">로그아웃</button>
            <button @click="myPage" class="logout-btn">내 정보</button>
            <button @click="myList" class="logout-btn">등록 매물(MY)</button>
          </div>
        </div>
        <button v-else class="navbar-button" @click="goToLogin">로그인</button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from "vue";
import { useUserStore } from "@/stores/useUserStore"; // Pinia 스토어
import { useRouter } from "vue-router";

const router = useRouter();
const userStore = useUserStore();
const isLoggedIn = computed(() => userStore.isLoggedIn);
const userName = computed(() => userStore.user?.name || "Guest");
const dropdownOpen = ref(false);

const goToLogin = () => router.push("/login");
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
};
const logout = async () => {
  await userStore.logout(router);
  dropdownOpen.value = false; // 드롭다운 닫기
};

const myPage = () => {
  router.push("/mypage");
};
const myList = () => {
  router.push("/myList");
};
</script>

<style scoped>
.navbar {
  position: relative;
  top: 0;
  width: 100%;
  height: 60px;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(10px);
  z-index: 10000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 95%;
  max-width: 1200px;
}

.navbar-logo img {
  margin-top: 5px;
  margin-left: 5px;
  height: 60px;
}

.navbar-links {
  display: flex;
  align-items: center;
  gap: 20px;
}

.navbar-icon svg {
  width: 24px;
  height: 24px;
  cursor: pointer;
  transition: transform 0.2s;
}

.navbar-icon svg:hover {
  transform: scale(1.2);
}

.navbar-button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background-color: rgb(79, 168, 27);
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.navbar-button:hover {
  background-color: rgb(48, 103, 17);
}

/* 로그인 상태 */
.user-section {
  position: relative;
  cursor: pointer;
}

.user-name {
  font-weight: bold;
  color: rgb(79, 168, 27);
}

.user-dropdown {
  position: absolute;
  top: 30px;
  right: 0;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px;

}

.logout-btn {
  width: 100px;
  height: 30px;
  background: none;
  border: none;
  color: rgb(79, 168, 27);
  font-weight: bold;
  cursor: pointer;
}

.logout-btn:hover {
  color: rgb(48, 103, 17);
}
</style>
