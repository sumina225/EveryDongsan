<template>
  <div class="board-view">
    <!-- 게시물 리스트 -->
    <ul class="post-list">
      <BoardPostCard v-for="post in postStore.posts" :key="post.articleNo" :post="post"
        @click="postStore.selectPost(post)" />
    </ul>

    <!-- 페이지네이션 -->
    <div class="pagination">
      <button :disabled="postStore.currentPage === 1" @click="changePage(postStore.currentPage - 1)">
        이전
      </button>
      <button v-for="page in postStore.totalPages" :key="page" :class="{ active: page === postStore.currentPage }"
        @click="changePage(page)">
        {{ page }}
      </button>
      <button :disabled="postStore.currentPage === postStore.totalPages" @click="changePage(postStore.currentPage + 1)">
        다음
      </button>
    </div>

    <!-- 게시물 상세 모달 -->
    <BoardModalCard v-if="postStore.selectedPost" :post="postStore.selectedPost" @close="postStore.closeModal" />
  </div>
</template>

<script setup>
import { onMounted, watch } from "vue";
import { useBoardStore } from "../stores/useBoardStore.js";
import { useTabStore } from "../stores/useTabStore.js"; // 탭 스토어 추가
import BoardPostCard from "../components/BoardPostCard.vue";
import BoardModalCard from "../components/BoardModalCard.vue";
import { useRouter } from "vue-router";

const router = useRouter();
const postStore = useBoardStore();
const tabStore = useTabStore(); // 탭 스토어 사용

// 초기 데이터 로드 (선택된 탭에 따라 게시물 가져오기)
onMounted(async () => {
  await postStore.fetchPosts(tabStore.selectedTab);
});

// // 라우터 경로 또는 탭 변경 시 데이터 재요청
// watch(
//   () => [router.currentRoute.value.path, tabStore.selectedTab], // 라우터와 탭 상태를 감시
//   async ([newPath, newTab]) => {
//     if (!postStore.posts.length || newTab) {
//       await postStore.fetchPosts(newTab); // 선택된 탭에 따라 게시물 요청
//     }
//   }
// );

// 페이지 변경 로직
const changePage = (page) => {
  postStore.changePage(page);
};
</script>


<style scoped>
.board-view {
  width: 80%;
  /* 화면의 80%만 사용 */
  margin: 0 auto;
  /* 가운데 정렬 */
  padding: 20px;
}

.post-list {
  display: grid;
  /* 그리드 레이아웃 */
  grid-template-columns: repeat(4, 1fr);
  /* 한 줄에 4개 */
  gap: 16px;
  /* 카드 간격 */
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 8px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
  cursor: pointer;
}

.pagination button.active {
  background-color: #007bff;
  color: white;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
