<template>
  <div class="board-view">
    <!-- AI 검색 버튼 -->
    <div class="aiIcon" @click="AIModalOpen">
      <img src="../assets/AI.png" alt="AI" />
    </div>

    <!-- AI 검색 모달 -->
    <AISearchModal :AIOpen="AIOpen" @closeAI="closeAI" />

    <!-- 게시물 리스트 -->
    <ul class="post-list">
      <BoardPostCard
        v-for="post in postStore.posts"
        :key="post.articleNo"
        :post="post"
        @click="postStore.selectPost(post)"
      />
    </ul>

    <!-- 페이지네이션 -->
    <div class="pagination">
      <button
        :disabled="postStore.currentPage === 1"
        @click="changePage(postStore.currentPage - 1)"
      >
        이전
      </button>
      <span>현재 페이지: {{ postStore.currentPage }}</span>
      <button
        :disabled="!postStore.posts.length"
        @click="changePage(postStore.currentPage + 1)"
      >
        다음
      </button>
    </div>

    <!-- 게시물 상세 모달 -->
    <BoardModalCard
      v-if="postStore.selectedPost"
      :post="postStore.selectedPost"
      @close="postStore.closeModal"
    />
  </div>
</template>
<script setup>
import { onMounted, watch, ref } from "vue";
import { useBoardStore } from "../stores/useBoardStore.js";
import { useTabStore } from "../stores/useTabStore.js"; // 탭 스토어 추가
import BoardPostCard from "../components/BoardPostCard.vue";
import BoardModalCard from "../components/BoardModalCard.vue";
import { useRouter } from "vue-router";
import AISearchModal from "../components/AISearchModal.vue";

const AIOpen = ref(false); // 모달 열기/닫기 상태
// AI 온오프
const AIModalOpen = () => {
  AIOpen.value = true;
};

const closeAI = () => {
  AIOpen.value = false;
};

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
const changePage = async (page) => {
  if (page < 1) return;
  await postStore.changePage(page);

  if (!postStore.posts.length) {
    alert("마지막 페이지입니다.");
    postStore.currentPage--;
  }
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

.pagination span {
  display: inline-block;
  margin: 0 10px;
  font-weight: bold;
  color: #555;
}

/* AI */
.aiIcon {
  position: fixed;
  top: 100px;
  left: 10px;
  width: 200px;
  height: 200px;
  border-radius: 50%; /* 둥근 형태 */
  overflow: hidden; /* 이미지 밖 잘림 */
  cursor: pointer;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
}

.aiIcon img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지가 둥근 형태에 맞게 잘림 */
}
</style>
