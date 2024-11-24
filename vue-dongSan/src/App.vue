<template>
  <div class="root">
    <!-- 현재 경로가 '/login'이 아닐 때만 헤더를 보여줌 -->
    <BoardHeader v-if="!hearderHides.includes(currentRoute)" />

    <!-- 'boardtap' 경로에 포함된 경우에만 홈탭을 렌더링 -->
    <BoardHometab v-if="boardtap.includes(currentRoute)" />

    <!-- 라우트에 맞는 컴포넌트 출력 -->
    <router-view />

    <!-- 푸터는 '/login'이 아닐 때만 표시 -->
    <AppFooter v-if="!hiddenRoutes.includes(currentRoute)" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import AppFooter from "./components/AppFooter.vue";
import BoardHeader from "./components/BoardHeader.vue";
import BoardHometab from "./components/BoardHometab.vue";
import { useUserStore } from "./stores/useUserStore";

// UserStore를 통해 사용자 정보 확인
const userStore = useUserStore();
console.log(userStore); // 디버깅용 로그

// 라우트 관련 상태 관리
const route = useRoute();
const currentRoute = ref(route.path); // 초기 경로 설정
const hiddenRoutes = ["/login", "/searchMap"];
const hearderHides = ["/login", "/"];
const boardtap = ["/board", "/board/student", "/board/listAll", "/board/rental"];

// 경로 변경 감지
watch(
  () => route.path,
  (newPath) => {
    currentRoute.value = newPath;
    console.log("현재 경로 업데이트:", newPath); // 디버깅용
  }
);

// 스크롤 이벤트 상태
const isAtBottom = ref(false);

// 스크롤 이벤트 핸들러
const checkScrollPosition = () => {
  const scrollPosition = window.scrollY + window.innerHeight;
  const documentHeight = document.documentElement.scrollHeight;
  isAtBottom.value = scrollPosition >= documentHeight - 10; // 스크롤 맨 아래 여부
};

// 컴포넌트 마운트 시 이벤트 리스너 등록
onMounted(() => {
  window.addEventListener("scroll", checkScrollPosition);
});

onMounted(() => {
  userStore.checkToken(); // Pinia 스토어의 상태를 확인하고 로그인 여부 판단
});


</script>

<style>
/* 전체 페이지 기본 여백 제거 */
html,
body,
#app {
  margin: 0;
  padding: 0;
  box-sizing: border-box;

  font-family: "Apple SD Gothic Neo", "맑은 고딕", "돋움", sans-serif;
}

.root {
  display: flex;
  flex-direction: column;
}
</style>
