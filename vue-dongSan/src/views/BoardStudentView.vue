<template>
  <div class="app">
    <!-- 네비게이션 바 -->

    <!-- 게시물 카드 리스트 -->
    <div class="card-container">
      <div v-for="post in currentPosts" :key="post.id" class="sub-wrap">
        <Card :post="post" />
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagination">
      <button
        v-for="page in totalPages"
        :key="page"
        :class="{ active: page === currentPage }"
        @click="changePage(page)"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import Card from "../components/BoardPostCard.vue";

const posts = ref(
  Array.from({ length: 100 }, (_, i) => ({
    id: i + 1,
    title: `게시물 제목 ${i + 1}`,
    description: `이것은 게시물 ${i + 1}의 내용입니다.`,
    image: "https://via.placeholder.com/150",
  }))
);

const currentPage = ref(1);
const postsPerPage = 20;

const totalPages = computed(() => Math.ceil(posts.value.length / postsPerPage));

const currentPosts = computed(() => {
  const start = (currentPage.value - 1) * postsPerPage;
  const end = start + postsPerPage;
  return posts.value.slice(start, end);
});

const changePage = (page) => {
  currentPage.value = page;
};

const navigateTo = (section) => {
  console.log(`${section}로 이동합니다.`); // 실제 네비게이션 로직 구현 가능
};
</script>

<style scoped>
/* 전체 앱 스타일 */
.app {
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
}

/* 네비게이션 바 스타일 */
.navbar {
  position: sticky;
  top: 0;
  display: flex;
  justify-content: center;
  gap: 20px;
  background-color: rgba(255, 255, 255, 0.8); /* 투명한 배경 */
  backdrop-filter: blur(10px); /* 블러 효과 */
  padding: 15px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar button {
  background-color: transparent;
  border: none;
  font-size: 1rem;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 rgba(0, 0, 0, 0); /* 초기 상태에서 그림자 없음 */
}

.navbar button:hover {
  color: #007bff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2); /* 호버 시 그림자 추가 */
}

.navbar button:focus {
  outline: none;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
}

/* 카드 컨테이너 */
.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  padding: 20px 10%;
}

/* 각 카드 */
.sub-wrap {
  width: 22%;
  height: 25%;
}

/* 페이지네이션 스타일 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  margin-bottom: 30px;
  gap: 10px;
}

.pagination button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #f2f2f2;
  transition: background-color 0.3s;
}

.pagination button.active {
  background-color: #fc5454;
  color: white;
}

.pagination button:hover {
  background-color: #d6d6d6;
}
</style>
