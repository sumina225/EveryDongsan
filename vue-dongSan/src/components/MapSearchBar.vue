<template>
  <div class="search-container">
    <!-- 검색 입력창 -->
    <input
      v-model="searchQuery"
      :readonly="isSchoolSelected"
      @click="openModal"
      type="text"
      placeholder="학교명을 입력하세요"
      class="search-field"
    />
    <button class="search-button" @click="handleSearchSchools">검색</button>

    <!-- 모달 -->
    <div v-if="isModalOpen" class="modal-backdrop" @click.self="closeModal">
      <div class="modal">
        <h2>학교 검색</h2>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="학교명을 입력하세요"
          class="modal-search-input"
        />
        <ul>
          <li
            v-for="(school, index) in filteredSchools"
            :key="index"
            @click="selectSchool(school.name)"
          >
            {{ school.name }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import Swal from "sweetalert2";
import { useMapSearchStore } from "@/stores/MapSearchStore";
import { useSchoolStore } from "@/stores/schoolStore";

const mapSearchStore = useMapSearchStore();
const schoolStore = useSchoolStore();
const searchQuery = ref(""); // 검색어
const isSchoolSelected = ref(false); // 학교 선택 여부
const isModalOpen = ref(false); // 모달 열림 상태

// Pinia 스토어에서 학교 데이터 로드
onMounted(() => {
  schoolStore.loadSchools(); // 스토어의 loadSchools 메서드 호출
  console.log("학교 데이터:", schoolStore.schoolList); // 데이터 로드 확인
});

// 검색된 학교 목록 필터링
const filteredSchools = computed(() => {
  if (!searchQuery.value.trim()) return schoolStore.schoolList.slice(0, 5); // 기본 상위 5개 표시
  return schoolStore.schoolList.filter((school) =>
    school.name.includes(searchQuery.value)
  );
});

// 검색 처리
const handleSearchSchools = async () => {
  if (!isSchoolSelected.value) {
    Swal.fire({
      icon: "warning",
      title: "학교를 선택해주세요!",
      text: "먼저 학교를 선택한 후 검색하세요.",
    });
    return;
  }
  console.log(searchQuery.value);
  await mapSearchStore.searchBySchool(searchQuery.value); // Pinia 스토어를 통한 검색 요청
};

// 모달 열기
const openModal = () => {
  isModalOpen.value = true;
};

// 모달 닫기
const closeModal = () => {
  isModalOpen.value = false;
};

// 학교 선택
const selectSchool = (selectedSchool) => {
  searchQuery.value = selectedSchool; // 선택된 학교를 검색창에 반영
  isSchoolSelected.value = true; // 학교 선택 상태 변경
  isModalOpen.value = false; // 모달 닫기
  Swal.fire({
    icon: "info",
    title: "학교 선택 완료",
    text: `선택된 학교: ${selectedSchool}`,
  });
};
</script>

<style scoped>
div input,
button {
  font-size: 16px;
  font-weight: 600;
}

.search-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 5px;
}

.search-field {
  width: 300px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  color: #036618;
}

.search-button {
  padding: 10px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #45a049;
}

/* 모달 백그라운드 */
.modal-backdrop {
  position: fixed;
  top: 200px;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 가장 위에 표시 */
}

/* 모달 스타일 */
.modal {
  background: white;
  padding: 20px;
  border-radius: 12px;
  width: 400px;
  height: 400px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  position: relative;
  top: -50px; /* 살짝 위로 이동 */
}

.modal h2 {
  margin-bottom: 16px;
}

.modal input {
  width: 100%;
  padding: 10px;
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.modal ul {
  list-style: none;
  padding: 0;
}

.modal li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #ddd;
}

.modal li:hover {
  background-color: #f5f5f5;
}
</style>
