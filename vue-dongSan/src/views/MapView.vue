<template>
  <div class="map-container">
    <div class="search-bar">
      <MapsearchBar />
    </div>
    <div class="map-wrap">
      <MapKaKao />
      <MapSideBar />
    </div>
  </div>
</template>

<script setup>
import MapKaKao from "@/components/MapKaKao.vue";
import MapsearchBar from "../components/MapSearchBar.vue";
import MapSideBar from "@/components/MapSideBar.vue";
import { useMapSearchStore } from "@/stores/MapSearchStore";
import { ref,onMounted } from "vue";
import { useRoute } from "vue-router";

// 선택된 학교 정보
const selectedSchool = ref(null);
const route = useRoute();
const mapSearchStore = useMapSearchStore();

onMounted(async () => {
  const schoolName = route.query.school;
  if (schoolName) {
    await mapSearchStore.searchBySchool(schoolName); // 매물 검색
  }
});

// 별점순 정렬 함수
const sortSchoolsByRating = () => {
  schools.value.sort((a, b) => b.rating - a.rating);
};

// 선택된 학교 정보 표시
const displaySchoolInfo = (school) => {
  selectedSchool.value = school;
};
</script>

<style scoped>
.map-container {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 100vh;
  position: relative;
}

.map-wrap {
  flex: 1;
  position: relative;
}

#map {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1; /* 맵의 z-index를 낮게 설정 */
}

.search-bar {
  position: absolute;
  top: 5px; /* 서치바의 위치를 화면 상단 10px 아래로 조정 */
  left: 50%;
  transform: translateX(-50%); /* 가로 중앙 정렬 */
  z-index: 10; /* 서치바의 z-index를 높게 설정 */
  width: 30%; /* 서치바 너비 */
  height: 60px;
  background: rgba(255, 255, 255, 0.9); /* 배경색과 투명도 조절 */
  border-radius: 100px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 5px;
}

/* #sidebar {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
  height: 100%;
  background-color: #fff;
  z-index: 10; /* 맵보다 위에 오도록 설정 */
</style>
