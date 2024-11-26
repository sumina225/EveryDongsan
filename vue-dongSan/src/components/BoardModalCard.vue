<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-container">
      <div class="modal-slider">
        <img :src="post.image" alt="매물 이미지" />
      </div>
      <div class="modal-details">
        <div class="detail-section">
          <h2>{{ post.title }}</h2>
          <hr />
          <p>{{ post.content }}</p>
        </div>
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

          <!-- 별점 표시 -->
          <div class="info-item">
            <i class="icon-star"></i>
            <span class="stars">
              <i
                v-for="n in Math.floor(post.home.score)"
                :key="n"
                class="star full"
              ></i>
              <i v-if="post.home.score % 1 !== 0" class="star half"></i>
            </span>
          </div>
          <div class="info-item">
            <i class="icon-school"></i>
            <span>연락처: {{ post.tel }}</span>
          </div>
        </div>

        <!-- 버튼 섹션 -->
    <div class="actions">
      <!-- 슬롯 렌더링 -->
      <slot name="actions"></slot>
    </div>


        <button class="close-btn" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useImageStore } from "@/stores/useImageStore";

const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});

const imageStore = useImageStore();
props.post.image = imageStore.assignImageToPost(props.post);
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
  height: 100%;
  overflow: hidden;
}

.modal-slider img{
  width: 100%;
  height: 500px;
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
  margin-top : 20px;
}

.close-btn:hover {
  background-color: rgb(65, 145, 25);
}

.info-item .stars {
  display: flex;
  align-items: center;
  gap: 4px;
}

.info-item .stars .star {
  width: 16px;
  height: 16px;
  display: inline-block;
  background-color: gold;
  clip-path: polygon(
    50% 0%,
    61% 35%,
    98% 35%,
    68% 57%,
    79% 91%,
    50% 70%,
    21% 91%,
    32% 57%,
    2% 35%,
    39% 35%
  );
  /* 별 모양 */
}

.info-item .stars .half {
  background: linear-gradient(to right, gold 50%, #ddd 50%);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 10px 20px;
  border-top: 1px solid #ddd;
 
}

.action-btn {

  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.action-btn.edit {
  background-color: rgb(79, 168, 27);
  /* 초록색 */
  color: white;
}

.action-btn.edit:hover {
  background-color: rgb(65, 145, 25);
}

.action-btn.delete {
  background-color: rgb(213, 56, 56);
  /* 붉은색 */
  color: white;
}

.action-btn.delete:hover {
  background-color: rgb(179, 45, 45);
}
</style>
