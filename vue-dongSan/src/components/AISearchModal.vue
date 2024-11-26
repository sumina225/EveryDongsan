<template>
  <div v-if="AIOpen" class="ai-modal">
    <!-- 닫기 버튼 -->
    <button class="close-icon" @click="$emit('closeAI')">×</button>

    <!-- 검색 입력 -->
    <div class="search-section">
      <input
        type="number"
        v-model="price"
        placeholder="가격 입력 (숫자)"
        class="search-input"
      />
      <input
        type="text"
        v-model="school"
        placeholder="학교 이름 입력"
        class="search-input"
      />
      <button @click="handleSearchClick" class="search-button">검색</button>
    </div>

    <!-- 검색 결과 -->
    <div class="results-section">
      <div v-if="isLoading" class="loading-spinner"></div>
      <p v-else-if="results.cheep.length === 0 && results.total.length === 0">
        알맞는 매물이 없습니다.
      </p>
      <div v-else>
        <!-- Cheep 결과 -->
        <h4 class="impact_title">저렴한 매물 추천</h4>
        <ul>
          <li v-for="(item, index) in results.cheep" :key="index">
            <strong>{{ item.name }}</strong>
            <hr />
            <p>{{ item.whyRecommend }}</p>
          </li>
        </ul>

        <!-- Total 결과 -->
        <h4 class="impact_title">전체 매물 추천</h4>
        <ul>
          <li v-for="(item, index) in results.total" :key="index">
            <strong>{{ item.name }}</strong>
            <hr />
            <p>{{ item.whyRecommend }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps } from "vue";
import axios from "../axios";
import Swal from "sweetalert2";

const price = ref(""); // 가격 입력
const school = ref(""); // 학교 이름 입력
const results = ref({ cheep: [], total: [] }); // 검색 결과
const isLoading = ref(false); // 로딩 상태

defineProps({
  AIOpen: {
    type: Boolean,
    required: true,
  },
});

// 데이터 형식 통일 함수
const normalizeResponse = (response) => {
  if (response && typeof response === "object") {
    return {
      cheep: response.cheep || [],
      total: response.total || [],
    };
  }
  return { cheep: [], total: [] };
};

// 검색 함수
const searchAttractions = async () => {
  isLoading.value = true;
  try {
    const adjustedPrice = parseInt(price.value) * 10000; // 입력값을 만원 단위로 변환
    const response = await axios.get("/recommend", {
      params: { price: adjustedPrice, school: school.value },
    });
    console.log(response.data);
    const normalized = normalizeResponse(response.data);
    results.value = { ...normalized };
    Swal.fire({
      icon: "success",
      title: "검색 완료",
      text: "추천 매물을 확인하세요.",
    });
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "검색 실패",
      text: "검색 중 문제가 발생했습니다. 다시 시도해주세요.",
    });
    console.error("검색 실패:", error);
  } finally {
    isLoading.value = false;
  }
};

// 버튼 클릭 처리
const handleSearchClick = () => {
  if (!price.value || !school.value) {
    Swal.fire({
      icon: "warning",
      title: "입력 오류",
      text: "가격과 학교를 모두 입력해주세요.",
    });
    return;
  }

  searchAttractions();

  // 버튼 포커스 초기화
  document.activeElement.blur();
};
</script>

<style scoped>
.ai-modal {
  position: fixed;
  top: 100px;
  left: 10px;
  width: 400px;
  height: auto;
  max-height: 500px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  padding: 20px;
  overflow-y: auto;
}

/* 닫기 버튼 (X 아이콘) */
.close-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  font-weight: bold;
  color: #999;
  cursor: pointer;
  transition: color 0.3s;
}

.close-icon:hover {
  color: #333;
}

.impact_title{
  font-size: 15px;
  color : #40cc20;
  font-weight: 700;
}

/* 검색 입력 스타일 */
.search-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.search-input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  box-shadow: inset 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: border-color 0.3s, box-shadow 0.3s;
}

.search-input:focus {
  border-color: #3bb120;
  box-shadow: 0 0 5px rgba(59, 177, 32, 0.5);
  outline: none;
}

.search-button {
  padding: 12px;
  background-color: #3bb120;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #2a8b15;
}

/* 검색 결과 스타일 */
.results-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.results-section li {
  margin-bottom: 20px;
}

.results-section strong {
  display: block;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.results-section hr {
  border: none;
  border-top: 1px solid #ddd;
  margin: 8px 0;
}

.results-section p {
  font-size: 14px;
  color: #555;
}

/* 로딩 스피너 */
.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3bb120;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 20px auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
