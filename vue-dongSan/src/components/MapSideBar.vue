<template>
  <div class="sidebar" @scroll="handleScroll">
    <div class="sidebar_btn">
      <!-- 메뉴 아이템 -->
      <div
        v-for="(menu, index) in menuItems"
        :key="index"
        class="menu-item"
        tabindex="0"
        @click="handleMenuClick(menu.label)"
      >
        <div class="menu-label">{{ menu.label }}</div>
      </div>
    </div>

    <div
      v-for="property in visibleProperties"
      :key="property.no"
      class="property-card"
      @click="handlePropertyClick(property.no)"
    >
      <div class="image-wrapper">
        <img src="../assets/apt.png" alt="매물 이미지" />
      </div>
      <div class="property-info">
        <div class="property-title">{{ property.school }} · {{ property.name }}</div>
        <div class="property-price">{{ property.price }} ({{ property.rentalType }})</div>
        <div class="property-size">{{ property.area }}m² ({{ property.category }})</div>
        <div class="property-description">매물 점수: {{ property.score }}</div>
      </div>
    </div>
    <div class="NoCard" v-show="visibleProperties.length <= 0">등록된 매물이 없습니다.</div>
  </div>

  <!-- 모달 -->
  <PropertyDetailModal
    v-if="isModalOpen"
    :property="selectedProperty"
    @close="isModalOpen = false"
  />
</template>

<script setup>
import { ref, computed } from "vue";
import { useMapSearchStore } from "@/stores/MapSearchStore";
import { usePropertyStore } from "../stores/PropertyStore";
import PropertyDetailModal from "./PropertyDetailModal.vue";

const mapSearchStore = useMapSearchStore();
const propertyStore = usePropertyStore();
const selectedProperty = ref(null); // 선택된 매물 데이터
const properties = computed(() => mapSearchStore.properties); // 전체 매물 데이터

// 인피니티 스크롤 관련 상태 관리
const visibleCount = ref(7); // 처음 표시할 매물 개수
const visibleProperties = computed(() => properties.value.slice(0, visibleCount.value)); // 화면에 보이는 매물만

// 스크롤 이벤트 핸들러
const handleScroll = (event) => {
  const element = event.target;
  if (element.scrollTop + element.clientHeight >= element.scrollHeight) {
    // 스크롤이 끝에 도달했을 때 추가 로드
    loadMoreProperties();
  }
};

// 추가 매물 로드
const loadMoreProperties = () => {
  if (visibleCount.value < properties.value.length) {
    visibleCount.value += 7; // 한 번에 7개씩 로드
  }
};

const isModalOpen = ref(false);

//상세정보보기
const handlePropertyClick = async (no) => {
  try {
    const propertyDetail = await propertyStore.fetchPropertyByNo(no);

    console.log("가져온 매물 상세 정보:", propertyDetail);
    selectedProperty.value = propertyDetail; // 선택된 매물 데이터 저장
    isModalOpen.value = true; // 모달 열기
  } catch (error) {
    console.error("매물 상세 정보를 가져오는 중 오류 발생:", error);
  }
};

// 메뉴 항목 관리
const menuItems = ref([{ label: "전세" }, { label: "월세" }, { label: "별점" }]);

// 메뉴 클릭 시 필터링 처리
const handleMenuClick = (menuLabel) => {
  console.log(`선택된 메뉴: ${menuLabel}`);
  if (menuLabel === "전세") {
    mapSearchStore.properties = mapSearchStore.properties.filter(
      (property) => property.rentalType === "전세"
    );
  } else if (menuLabel === "월세") {
    mapSearchStore.properties = mapSearchStore.properties.filter(
      (property) => property.rentalType === "월세"
    );
  } else if (menuLabel === "별점") {
    mapSearchStore.properties.sort((a, b) => b.score - a.score);
  }
};
</script>

<style scoped>
.sidebar {
  width: 340px;
  height: calc(100vh - 20px); /* 맵 높이에 맞춤 */
  background-color: #fff;
  overflow-y: auto;
  padding: 16px;
  border-left: 1px solid #e6e6e6;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  z-index: 100;
  position: relative;
}

.sidebar_btn {
  display: flex;
  justify-content: space-around; /* 버튼 간 간격 균등 배치 */
  align-items: center; /* 수직 중앙 정렬 */
  width: 100%; /* 전체 너비 */
  height: 60px; /* 높이 조정 */
  margin-top: -10px;
  margin-bottom: 10px;
  background-color: #ffffff; /* 배경색 */
  padding: 10px 0; /* 위아래 여백 추가 */
  /* border-top: 1px solid #e6e6e6; 상단 테두리 추가 */
  border-bottom: 1px solid #e6e6e6; /* 하단 테두리 추가 */
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.1); /* 약간의 그림자 */
}

.menu-item {
  width: 80px; /* 버튼 전체 너비 */
  height: 40px; /* 버튼 높이 */
  display: flex;
  align-items: center; /* 텍스트 수직 중앙 정렬 */
  justify-content: center; /* 텍스트 가로 중앙 정렬 */
  padding: 0 16px; /* 좌우 여백 */
  font-size: 15px; /* 텍스트 크기 */
  font-weight: bold; /* 텍스트 굵기 추가 */
  color: #333; /* 텍스트 색상 */
  background-color: white; /* 버튼 배경색 */
  cursor: pointer;
  user-select: none;
  border: 1px solid #dcdcdc; /* 테두리 추가 */
  border-radius: 8px; /* 버튼 모서리를 둥글게 */
  box-shadow: 0px 1px 3px rgba(0, 0, 0, 0.1); /* 약간의 그림자 추가 */
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.menu-item:hover {
  background-color: #e0f7e9; /* 호버 시 버튼 배경색 변경 */
  border-color: #039b47; /* 호버 시 테두리 색상 변경 */
  transform: scale(1.05); /* 호버 시 약간 확대 효과 */
}

.menu-item:active {
  background-color: #c8e6d8; /* 클릭 시 버튼 배경색 변경 */
  transform: scale(0.95); /* 클릭 시 약간 축소 효과 */
}

.property-card {
  display: flex;
  flex-direction: row;
  margin-bottom: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.3s ease;
}

.property-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.image-wrapper {
  width: 112px;
  height: 112px;
  overflow: hidden;
  position: relative;
  margin: auto;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.property-info {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.property-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.property-price {
  font-size: 14px;
  color: #f56b2a;
  margin-bottom: 4px;
}

.property-size {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.property-description {
  font-size: 12px;
  color: #888;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.NoCard {
  font-size: 20px;
  text-align: center;
  margin-top: 40px;
  font-weight: 700;
  font-family: "Pretendard", -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo",
    "Malgun Gothic", "맑은 고딕", helvetica, sans-serif;
}
</style>
