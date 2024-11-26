<template>
  <div v-if="property" class="modal">
    <!-- 이미지 슬라이드 -->
    <div class="image-slider">
      <div
        class="slides-container"
        :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
      >
        <div v-for="(image, index) in images" :key="index" class="slide">
          <img :src="image" alt="매물 이미지" />
        </div>
      </div>
      <button class="prev-button" @click="prevSlide">◀</button>
      <button class="next-button" @click="nextSlide">▶</button>
    </div>

    <!-- 매물 정보 -->
    <div class="property-info">
      <h3 class="modal-title">{{ property.home.name }}</h3>
      <hr />
      <p><strong>위치:</strong> {{ property.home.school }}</p>
      <hr />
      <p><strong>가격:</strong> {{ property.home.price }}</p>
      <hr />
      <p><strong>면적:</strong> {{ property.home.area }}m²</p>
      <hr />
      <p><strong>카테고리:</strong> {{ property.home.category }}</p>
      <hr />
      <p><strong>연락처:</strong> {{ property.tel }}</p>
    </div>

    <!-- 별점과 리뷰 -->
    <div class="rating-section" @click="toggleReviewModal">
      <div class="stars">
        <span
          v-for="starIndex in 5"
          :key="starIndex"
          :class="{ filled: starIndex <= property.home.score }"
        >
          ★
        </span>
      </div>
      <span class="review-count">({{ reviewCount }} 리뷰)</span>
    </div>

    <button class="close-button" @click="$emit('close')">닫기</button>

    <!-- 리뷰 확장 모달 -->
    <div v-if="isReviewModalOpen" class="review-modal">
      <h4>리뷰 목록</h4>
      <ul class="review-list">
        <li
          v-for="(review, index) in reviews"
          :key="index"
          class="review-item"
          :class="{ 'last-review': index === reviews.length - 1 }"
        >
          <strong>{{ review.user }}</strong>
          <div class="stars">
            <span
              v-for="starIndex in 5"
              :key="starIndex"
              :class="{ filled: starIndex <= review.reviewScore }"
            >
              ★
            </span>
          </div>
          <p>{{ review.content }}</p>
        </li>
      </ul>
      <button class="review-button" @click="toggleReviewInput">
        리뷰 작성
      </button>
      <button class="close-review-button" @click="toggleReviewModal">
        닫기
      </button>
      <!-- 리뷰 작성 -->
      <div v-if="isReviewInputVisible" class="review-input">
        <textarea
          v-model="newReview"
          placeholder="리뷰를 작성하세요..."
        ></textarea>
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, defineEmits } from "vue";
import axios from "@/axios.js";

const props = defineProps({
  property: {
    type: Object,
    required: true,
  },
});

// Emits 정의
const emit = defineEmits(["close"]); // 'close' 이벤트 정의

const images = [
  "https://landthumb-phinf.pstatic.net/20241118_171/land_naver_1731913908609qbX3g_JPEG/lg.jpg?type=m562",
  "https://landthumb-phinf.pstatic.net/20241118_56/land_naver_1731913908611IVCSF_JPEG/lg.jpg?type=m562",
  "https://landthumb-phinf.pstatic.net/20241118_163/land_naver_1731913908617cVae8_JPEG/lg.jpg?type=m562",
];

const isReviewModalOpen = ref(false);
const isReviewInputVisible = ref(false);
const reviews = ref([]);
const newReview = ref("");
const selectedScore = ref(0);
const reviewCount = ref(0); // 리뷰 개수 상태 관리
const averageScore = ref(0); // 평균 별점

const toggleReviewInput = () => {
  isReviewInputVisible.value = !isReviewInputVisible.value;
};

const toggleReviewModal = () => {
  isReviewModalOpen.value = !isReviewModalOpen.value;
};

const fetchReviewCount = async () => {
  try {
    const response = await axios.get(
      `/board/${props.property.articleNo}/reviews`
    );
    reviews.value = response.data;
    reviewCount.value = reviews.value.length;
    const totalScore = reviews.value.reduce(
      (sum, review) => sum + review.score,
      0
    );
    averageScore.value =
      reviewCount.value > 0 ? totalScore / reviewCount.value : 0;
  } catch (error) {
    console.error("리뷰 데이터 로드 실패:", error);
  }
};

const submitReview = async () => {
  if (!newReview.value.trim() || selectedScore.value === 0) {
    alert("리뷰 내용과 별점을 모두 입력해주세요.");
    return;
  }

  try {
    const newReviewData = {
      content: newReview.value,
      reviewScore: selectedScore.value,
    };

    await axios.post(
      `/board/${props.property.articleNo}/reviews`,
      newReviewData
    );
    alert("리뷰가 등록되었습니다.");
    newReview.value = "";
    selectedScore.value = 0;
    await fetchReviewCount();
  } catch (error) {
    console.error("리뷰 등록 실패:", error);
    alert("리뷰 등록에 실패했습니다.");
  }
};

const currentSlide = ref(0);

const prevSlide = () => {
  currentSlide.value =
    currentSlide.value === 0 ? images.length - 1 : currentSlide.value - 1;
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % images.length;
};

const selectScore = (score) => {
  selectedScore.value = score;
};

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
  border-radius: 12px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  width: 500px;
  font-family: "Pretendard", sans-serif;
  animation: fade-in 0.3s ease-in-out;
}

@keyframes fade-in {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-title {
  text-align: center;
  margin-bottom: 16px;
  font-size: 22px;
  font-weight: bold;
  color: #3bb120; /* 초록색 테마 */
}

.property-info p {
  margin: 8px 0;
  font-size: 16px;
  line-height: 1.6;
}

.property-info hr {
  border: none;
  border-top: 1px solid #ddd;
  margin: 10px 0;
}

/* 슬라이드 스타일 */
.image-slider {
  position: relative;
  width: 100%;
  height: 250px;
  overflow: hidden;
  margin-bottom: 20px;
}

.slides-container {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.slide {
  min-width: 100%;
  height: 250px;
}

.slide img {
  width: 100%;
  height: 250px;
  object-fit: cover;
  border-radius: 8px;
}

.prev-button,
.next-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.prev-button:hover,
.next-button:hover {
  background: #3bb120;
}

.prev-button {
  left: 10px;
}

.next-button {
  right: 10px;
}

/* 별점 및 리뷰 */
.rating-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  cursor: pointer;
  transition: transform 0.3s ease;
  font-size: 20px;
}

.rating-section:hover {
  transform: scale(1.05);
}

.rating-section .stars {
  display: flex;
}

.rating-section .stars .filled {
  color: gold;
}

.stars .filled {
  color: gold;
}

.rating-section .review-count {
  margin-left: 10px;
  font-size: 14px;
  color: gray;
}

/* 리뷰 확장 모달 */
.review-modal {
  position: fixed;
  top: 0;
  left: 0; /* 오른쪽에서 슬라이드 */
  width: 400px;
  height: 100%;
  background: #f9f9f9;
  border-left: 1px solid #ddd;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  z-index: 1001;
  animation: slide-in 0.3s forwards;
}

@keyframes slide-in {
  from {
    left: 0;
  }
  to {
    left: 490px;
  }
}

.review-modal h4 {
  margin-bottom: 10px;
  font-size: 25px;
  font-weight: 600;
  color: #3bb120;
}

.review-modal ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.review-button {
  background-color: #3eaf24;
  margin-right: 10px;
  border-radius: 5px;
  padding: 7px;
  border: 1px solid #03a044;
  color: white;
}

.review-button:hover {
  background-color: #2a7718;
}

.close-review-button {
  margin-right: 10px;
  border-radius: 5px;
  padding: 7px;
  background-color: #fcfff8;
  border: 1px solid #53af79;
  color: rgb(85, 84, 84);
}

.close-review-button:hover {
  background-color: #9fa09f;
}

.review-modal li {
  padding: 10px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-bottom: 10px;
}

.review-input textarea {
  width: 100%;
  height: 80px;
  margin-top: 10px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.review-input button {
  width: 100%;
  padding: 10px;
  background: #3bb120;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  margin-top: 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.review-input button:hover {
  background: #2e8f18;
}

/* 닫기 버튼 스타일 */
.close-button {
  width: 100%;
  padding: 10px;
  background: #888;
  margin-top: 30px;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background: #555;
}

/* 버튼 스타일 */
button {
  font-family: "Pretendard", sans-serif;
  font-weight: bold;
}

/* 리뷰 목록 스타일 */
/* 리뷰 리스트 스타일 */
.review-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.review-item {
  padding: 10px;
  background: white;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
}

/* .review-item.last-review {
  border-bottom: none; /* 마지막 리뷰의 하단 선 제거 */

.review-item strong {
  display: block;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 5px;
}

.review-item .stars {
  margin-bottom: 5px;
}

.review-item p {
  font-size: 13px;
  color: #555;
}

/* 리뷰 등록 별 */
.star-rating {
  display: flex;
  justify-content: center;
  margin: 10px 0;
  cursor: pointer;
}

.star-rating .star {
  font-size: 30px;
  color: #ccc; /* 기본 색상 */
  margin: 0 5px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.star-rating .star.selected {
  color: gold; /* 선택된 별 색상 */
}

.star-rating .star:hover {
  color: #ffcc00; /* 마우스 오버 시 색상 */
}
</style>
