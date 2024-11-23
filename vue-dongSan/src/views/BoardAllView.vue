<template>
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
/* 네비게이션 바 스타일 */



/* 카드 컨테이너 */
.card-container {
  margin-top: -20px;
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
  background-color: rgb(79, 168, 27); /* 활성화 상태 배경색 */;
  color: white;
}

.pagination button:hover {
  background-color: #d6d6d6;
}
</style>
