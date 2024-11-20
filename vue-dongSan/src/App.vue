<template>
  <div>
    <!-- 현재 경로가 '/login'이 아닐 때만 헤더를 보여줌 -->
    <header v-if="currentRoute !== '/login'">
      <!-- 헤더 내용 추가 -->
    </header>

    <!-- 라우트에 맞는 컴포넌트 출력 -->
    <router-view />

    <!-- 푸터는 '/login'이 아닐 때, 스크롤이 맨 밑에 있을 때만 보이기 -->
    <AppFooter v-if="shouldShowFooter" />
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import AppFooter from "./components/AppFooter.vue";

// 현재 라우트 경로 가져오기
const route = useRoute();
const currentRoute = computed(() => route.path);

// 스크롤이 맨 밑에 있는지 여부를 체크하는 변수
const isAtBottom = ref(false);

// 푸터를 보여줄지 여부를 계산하는 변수
const shouldShowFooter = computed(() => {
  return currentRoute.value !== "/login" && isAtBottom.value;
});

// 스크롤 이벤트 리스너 추가
const checkScrollPosition = () => {
  const scrollPosition = window.scrollY + window.innerHeight;
  const documentHeight = document.documentElement.scrollHeight;

  // 스크롤이 맨 밑에 도달하면 isAtBottom을 true로 설정
  isAtBottom.value = scrollPosition >= documentHeight - 10;
};

// 마운트 시 스크롤 이벤트 리스너 등록
onMounted(() => {
  window.addEventListener("scroll", checkScrollPosition);
});

// 컴포넌트 언마운트 시 스크롤 이벤트 리스너 제거
watch(
  () => currentRoute.value,
  () => {
    window.removeEventListener("scroll", checkScrollPosition);
    isAtBottom.value = false; // 새 페이지로 이동 시 스크롤 상태 초기화
  }
);
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
</style>
