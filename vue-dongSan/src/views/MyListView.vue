<template>
    <div class="my-list-view">
        <div class="title">
            <div class="logo">
                <!-- 나뭇잎 로고 (SVG) -->
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    class="feather feather-leaf">
                    <path d="M6 2v6a9 9 0 0 0 9 9h6"></path>
                    <path d="M9.14 9.14a5 5 0 1 0 5.72 5.72L6 2"></path>
                </svg>
            </div>
            <h3>My List</h3>
        </div>
        <!-- 본인 작성 게시물 리스트 -->
        <ul class="post-list">
            <BoardPostCard v-for="post in boardStore.posts" :key="post.articleNo" :post="post"
                @click="boardStore.selectPost(post)" />
        </ul>

        <!-- 페이지네이션 -->
        <div class="pagination">
            <button :disabled="boardStore.currentPage === 1" @click="changePage(boardStore.currentPage - 1)">
                이전
            </button>
            <button v-for="page in boardStore.totalPages" :key="page"
                :class="{ active: page === boardStore.currentPage }" @click="changePage(page)">
                {{ page }}
            </button>
            <button :disabled="boardStore.currentPage === boardStore.totalPages"
                @click="changePage(boardStore.currentPage + 1)">
                다음
            </button>
        </div>

        <!-- 게시물 상세 모달 -->
        <BoardModalCard v-if="boardStore.selectedPost" :post="boardStore.selectedPost" @close="boardStore.closeModal">
            <!-- 모달 내부 수정/삭제 버튼 -->
            <template #actions>
                <button class="edit-btn" @click="editPost(boardStore.selectedPost.articleNo)">수정</button>
                <button class="delete-btn" @click="deletePost(boardStore.selectedPost.articleNo)">삭제</button>
            </template>
        </BoardModalCard>
    </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useBoardStore } from "../stores/useBoardStore";
import { useImageStore } from "../stores/useImageStore";
import BoardPostCard from "@/components/BoardPostCard.vue";
import BoardModalCard from "@/components/BoardModalCard.vue";
import Swal from "sweetalert2";

const boardStore = useBoardStore();
const imageStore = useImageStore();

onMounted(async () => {
  // 이미지 초기화
  imageStore.initializeImages();

  // 게시물 데이터 로드 및 이미지 할당
  await boardStore.fetchPosts("내목록");
  boardStore.posts.forEach((post) => {
    post.image = imageStore.assignImageToPost(post);
  });
});

const changePage = async (page) => {
  await boardStore.changePage(page);
  boardStore.posts.forEach((post) => {
    post.image = imageStore.assignImageToPost(post);
  });
};

const deletePost = async (articleNo) => {
  const result = await Swal.fire({
    title: "삭제 확인",
    text: "정말로 삭제하시겠습니까?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "삭제",
    cancelButtonText: "취소",
  });

  
  if (result.isConfirmed) {
    await boardStore.deletePost(articleNo);
    boardStore.closeModal(); // 모달 닫기
  }
};
</script>

<style scoped>
.title {
    display: flex;
    align-items: center;
    gap: 15px;
    font-family: 'Arial', sans-serif;
    padding: 10px;
    border-bottom: 2px solid #e0e0e0;
    margin-bottom: 30px;
}

.logo {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    margin-left: 10px;
    background-color: #e8f5e9;
    /* 연한 초록색 배경 */
    border-radius: 50%;
    /* 동그라미 모양 */
}

.logo svg {
    width: 24px;
    height: 24px;
    color: #4caf50;
    /* 초록색 */
}

.title h3 {
    font-size: 25px;
    color: #525952;
    margin: 0;
}

.my-list-view {
    width: 80%;
    margin: 0 auto;
    padding: 20px;
}

.post-list {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
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

.edit-btn,
.delete-btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
}

.edit-btn {
    background-color: #007bff;
    color: white;
}

.edit-btn:hover {
    background-color: #0056b3;
}

.delete-btn {
    background-color: #d33;
    color: white;
}

.delete-btn:hover {
    background-color: #b71c1c;
}
</style>
