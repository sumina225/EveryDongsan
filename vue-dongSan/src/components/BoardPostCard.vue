<template>
  <div v-if="post" class="post-card" @click="openPost">
    <div class="image-wrapper">
      <img src="https://via.placeholder.com/150" alt="게시물 이미지" />
    </div>
    <div class="content-wrapper">
      <h4 class="title">{{ post.title }}</h4>
      <p class="description">{{ post.content }}</p>
      <div class="info">
        <span>{{ post.home.category }}</span>
        <span>{{ post.home.rentalType }}: {{ post.home.price }}만원</span>
        <span>{{ post.home.school }}</span>
        <span>⭐ {{ post.home.score }}</span>
      </div>
    </div>
  </div>
  <div v-else>데이터를 불러오는 중입니다...</div>
</template>

<script setup>
import { useBoardStore } from "@/stores/useBoardStore";

const props = defineProps({
  post: {
    type: Object,
    required: true, // 반드시 필요
  },
});

const postStore = useBoardStore();
const openPost = () => {
  console.log("post selected:", props.post);
};
</script>

<style scoped>
.post-card {
  display: flex;
  flex-direction: column;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease;
}
.post-card:hover {
  transform: scale(1.03);
}
.image-wrapper img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
.content-wrapper {
  padding: 12px;
}
.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}
.description {
  font-size: 14px;
  color: #555;
  margin-bottom: 12px;
}
.info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #777;
}
</style>
