<template>
  <div v-if="property" class="modal">
    <!-- 이미지 슬라이드 -->
    <div class="image-slider">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="slide"
        :class="{ active: index === currentSlide }"
      >
        <img :src="image" alt="매물 이미지" />
      </div>
      <button class="prev-button" @click="prevSlide">◀</button>
      <button class="next-button" @click="nextSlide">▶</button>
    </div>

    <!-- 매물 정보 -->
    <h3>{{ property.home.name }}</h3>
    <p>위치: {{ property.home.school }}</p>
    <p>가격: {{ property.home.price }}</p>
    <p>면적: {{ property.home.area }}m²</p>
    <p>카테고리: {{ property.home.category }}</p>
    <p>연락처: {{ property.tel }}</p>

    <!-- 별점과 리뷰 -->
    <div class="rating-section" @click="toggleReviewModal">
      <span class="stars">⭐⭐⭐⭐⭐</span>
      <span class="review-count">({{ reviewCount }} 리뷰)</span>
    </div>

    <button class="close-button" @click="$emit('close')">닫기</button>

    <!-- 리뷰 확장 모달 -->
    <div v-if="isReviewModalOpen" class="review-modal">
      <h4>리뷰 목록</h4>
      <ul>
        <li v-for="(review, index) in reviews" :key="index">
          <strong>{{ review.user }}</strong> - {{ review.score }}점: {{ review.content }}
        </li>
      </ul>

      <!-- 리뷰 작성 -->
      <div class="review-input">
        <h5>리뷰 작성</h5>
        <textarea v-model="newReview" placeholder="리뷰를 작성하세요..."></textarea>
        <div class="star-rating">
          <span
            v-for="starIndex in 5"
            :key="starIndex"
            class="star"
            :class="{ selected: starIndex <= selectedScore }"
            @click="selectScore(starIndex)"
          >
            ★
          </span>
        </div>
        <button @click="submitReview">리뷰 등록</button>
      </div>

      <button class="close-review-button" @click="toggleReviewModal">닫기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "@/axios.js";

defineProps({
  property: {
    type: Object,
    required: true,
  },
});

const images = [
  "https://landthumb-phinf.pstatic.net/20241118_171/land_naver_1731913908609qbX3g_JPEG/lg.jpg?type=m562",
  "https://landthumb-phinf.pstatic.net/20241118_56/land_naver_1731913908611IVCSF_JPEG/lg.jpg?type=m562",
  "https://landthumb-phinf.pstatic.net/20241118_163/land_naver_1731913908617cVae8_JPEG/lg.jpg?type=m562",
];

const isReviewModalOpen = ref(false);
const reviews = ref([]);
const newReview = ref("");
const selectedScore = ref(0);
const reviewCount = ref(0); // 리뷰 개수 상태 관리

// 리뷰 모달 토글
const toggleReviewModal = async () => {
  isReviewModalOpen.value = !isReviewModalOpen.value;
  if (isReviewModalOpen.value) {
    await fetchReviews();
  }
};

// 리뷰 개수 조회
const fetchReviewCount = async () => {
  try {
    const response = await axios.get(`/board/${property.articleNo}/reviews`);
    reviewCount.value = response.data.length; // 총 리뷰 개수 계산
  } catch (error) {
    console.error("리뷰 개수 조회 실패:", error);
    reviewCount.value = 0;
  }
};

// 리뷰 조회
const fetchReviews = async () => {
  try {
    const response = await axios.get(`/board/${property.articleNo}/reviews`);
    reviews.value = response.data;
    console.log(response);
  } catch (error) {
    console.error("리뷰 조회 실패:", error);
  }
};

// 리뷰 등록
const submitReview = async () => {
  if (!newReview.value.trim() || selectedScore.value === 0) {
    alert("리뷰 내용과 별점을 모두 입력해주세요.");
    return;
  }

  try {
    const newReviewData = {
      content: newReview.value,
      score: selectedScore.value,
    };

    await axios.post(`/board/${property.articleNo}/reviews`, newReviewData);
    alert("리뷰가 등록되었습니다.");
    newReview.value = "";
    selectedScore.value = 0;

    // 서버에서 갱신된 리뷰 목록 및 개수 가져오기
    await fetchReviews();
    await fetchReviewCount();
  } catch (error) {
    console.error("리뷰 등록 실패:", error);
    alert("리뷰 등록에 실패했습니다.");
  }
};

// 슬라이드 로직
const currentSlide = ref(0);

const prevSlide = () => {
  currentSlide.value = currentSlide.value === 0 ? images.length - 1 : currentSlide.value - 1;
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % images.length;
};

const selectScore = (score) => {
  selectedScore.value = score;
};

// 초기 리뷰 개수 로드
onMounted(fetchReviewCount);
</script>

<style scoped>
/* 메인 모달 스타일 */
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  width: 400px;
}

.image-slider {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  height: 200px;
  margin-bottom: 20px;
}

.image-slider .slide {
  position: absolute;
  opacity: 0;
  transition: opacity 0.5s ease;
}

.image-slider .slide.active {
  opacity: 1;
}

.image-slider img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.prev-button,
.next-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  padding: 10px;
  cursor: pointer;
}

.prev-button {
  left: 10px;
}

.next-button {
  right: 10px;
}

.rating-section {
  display: flex;
  align-items: center;
  margin: 10px 0;
  cursor: pointer;
}

.rating-section .stars {
  font-size: 20px;
  color: gold;
}

.rating-section .review-count {
  margin-left: 10px;
  font-size: 14px;
  color: gray;
}

/* 확장 모달 스타일 */
.review-modal {
  position: absolute;
  top: 0;
  right: -420px;
  width: 400px;
  height: 100%;
  background: #f9f9f9;
  border-left: 1px solid #ddd;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  animation: slide-in 0.3s forwards;
}

.review-modal ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.review-modal li {
  padding: 10px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 10px;
}

.review-input {
  margin-top: 20px;
}

.review-input textarea {
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.review-input button {
  width: 100%;
  padding: 10px;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

/* 별점 스타일 */
.star-rating {
  display: flex;
  justify-content: center;
  margin: 10px 0;
}

.star-rating .star {
  font-size: 25px;
  color: #ccc;
  cursor: pointer;
  transition: color 0.3s;
}

.star-rating .star.selected {
  color: gold;
}
</style>
