<template>
    <div class="modal-backdrop" @click.self="closeModal">
      <div class="modal-container">
        <!-- 사진 슬라이드 -->
        <div class="modal-slider">
          <div class="slider-container">
            <img
              v-for="(image, index) in images"
              :key="index"
              :src="image"
              alt="매물 이미지"
              :class="{ active: index === currentSlide }"
            />
            <button class="prev-btn" @click="prevSlide">‹</button>
            <button class="next-btn" @click="nextSlide">›</button>
          </div>
        </div>
  
        <!-- 상세 정보 -->
        <div class="modal-details">
          <!-- 제목 및 내용 -->
          <div class="detail-section">
            <h2>{{ post.title }}</h2>
            <hr />
            <p>{{ post.content }}</p>
          </div>
  
          <!-- 추가 정보 -->
          <div class="info-section">
            <div class="info-item">
              <i class="icon-category"></i>
              <span>{{ post.home.category }}</span>
            </div>
            <div class="info-item">
              <i class="icon-rental"></i>
              <span>{{ post.home.rentalType }}: {{ post.home.price }}</span>
            </div>
            <div class="info-item">
              <i class="icon-area"></i>
              <span>면적: {{ post.home.area }}m²</span>
            </div>
            <div class="info-item">
              <i class="icon-school"></i>
              <span>학교: {{ post.home.school }}</span>
            </div>
            <div class="info-item">
              <i class="icon-score"></i>
              <span>평점: {{ post.home.score }}</span>
            </div>
          </div>
        </div>
  
        <button class="close-btn" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  
  // Props
  defineProps({
    post: Object, // 부모로부터 post 객체를 받음
  });
  
  // 슬라이드 로직
  const images = ref([
    "https://via.placeholder.com/600x400",
    "https://via.placeholder.com/600x400?text=Image+2",
    "https://via.placeholder.com/600x400?text=Image+3",
  ]);
  
  const currentSlide = ref(0);
  
  const nextSlide = () => {
    currentSlide.value = (currentSlide.value + 1) % images.value.length;
  };
  
  const prevSlide = () => {
    currentSlide.value =
      (currentSlide.value - 1 + images.value.length) % images.value.length;
  };
  </script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  width: 80%;
  max-width: 800px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  animation: fadeIn 0.3s ease-in-out;
}

.modal-slider {
  position: relative;
  width: 100%;
  overflow: hidden;
}

.slider-container {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.slider-container img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  display: none;
}

.slider-container img.active {
  display: block;
}

.prev-btn,
.next-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  font-size: 24px;
  padding: 10px;
  cursor: pointer;
  z-index: 1;
}

.prev-btn {
  left: 10px;
}

.next-btn {
  right: 10px;
}

.modal-details {
  padding: 20px;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h2 {
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: bold;
}

.detail-section hr {
  border: none;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
}

.detail-section p {
  font-size: 16px;
  line-height: 1.5;
  color: #555;
}

.info-section {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-item i {
  font-size: 20px;
  color: rgb(79, 168, 27);
}

.close-btn {
  display: block;
  width: 100%;
  padding: 10px 0;
  background-color: rgb(79, 168, 27);
  color: white;
  text-align: center;
  font-weight: bold;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background-color: rgb(65, 145, 25);
}
</style>
