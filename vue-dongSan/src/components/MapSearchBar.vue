<template>
    <div class="search-container">
        <!-- 검색 입력 -->
        <div class="search-input">
            <input v-model="searchQuery" :readonly="isSchoolSelected"
                @click="isSchoolSelected ? resetSelection() : null" type="text" placeholder="학교명을 입력하세요"
                class="search-field" />
            <button class="search-button" @click="handleSearchSchools">
                검색
            </button>
        </div>

        <!-- 지도 검색 버튼 -->
        <div v-if="isSchoolSelected" class="map-search-button">
            <button @click="handleSearchMap">지도 검색</button>
        </div>

        <!-- 모달 -->
        <div v-if="isModalOpen" class="modal-backdrop" @click.self="closeModal">
            <div class="modal">
                <h2>학교 검색 결과</h2>
                <ul>
                    <li v-for="(school, index) in filteredSchools" :key="index" @click="selectSchool(school.name)">
                        {{ school.name }}
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useSchoolStore } from "@/stores/SchoolStore";
import axios from "@/axios";

const schoolStore = useSchoolStore(); // Pinia 스토어
const searchQuery = ref(""); // 검색어
const isModalOpen = ref(false); // 모달 열림 상태
const isSchoolSelected = ref(false); // 학교 선택 여부
const schoolResults = ref([]); // 검색 결과

// Pinia 스토어에서 학교 데이터 로드
onMounted(() => {
    searchQuery.value = ""; // 검색어 초기화
    schoolStore.loadSchools();
});

// 검색된 학교 목록 필터링
const filteredSchools = computed(() => {
    if (!searchQuery.value.trim()) return schoolStore.schoolList.slice(0, 5);
    return schoolStore.schoolList.filter((school) =>
        school.name.includes(searchQuery.value)
    );
});

// 학교 검색 요청
const handleSearchSchools = () => {
    if (!searchQuery.value.trim()) {
        alert("학교명을 입력해주세요.");
        return;
    }

    // SchoolStore 데이터를 기반으로 검색
    schoolResults.value = schoolStore.schoolList.filter((school) =>
        school.name.includes(searchQuery.value)
    );

    // 검색 결과가 없으면 경고 메시지 표시
    if (schoolResults.value.length === 0) {
        alert("검색 결과가 없습니다.");
        return;
    }

    isModalOpen.value = true; // 모달 열기
};


// 지도 검색 요청
const handleSearchMap = async () => {
    if (!searchQuery.value) {
        alert("학교를 선택해주세요.");
        return;
    }

    try {
        const response = await axios.post("/api/search-map", {
            school: searchQuery.value, // 선택된 학교명 전송
        });
        console.log("지도 검색 결과:", response.data);
        // TODO: 지도 결과를 렌더링하거나 페이지 이동 처리
    } catch (error) {
        console.error("지도 검색 요청 실패:", error);
    }
};

// 모달 닫기
const closeModal = () => {
    isModalOpen.value = false;
    schoolResults.value = []; // 검색 결과 초기화
};

// 학교 선택
const selectSchool = (selectedSchool) => {
    searchQuery.value = selectedSchool; // 선택된 학교를 검색창에 반영
    isSchoolSelected.value = true; // 학교 선택 상태 변경
    isModalOpen.value = false; // 모달 닫기
};

// 선택 초기화
const resetSelection = () => {
    isSchoolSelected.value = false;
    searchQuery.value = "";
};
</script>

<style scoped>
/* 검색 컨테이너 중앙 정렬 */
.search-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 75px;
    /* 화면 전체 중앙 정렬 */
    background-color: #f9f9f9;
}

/* 검색 입력 및 버튼 */
.search-input {
    display: flex;
    align-items: center;
    gap: 10px;
    /* 입력 필드와 버튼 간격 */
}

.search-field {
    width: 350px;
    padding: 10px 14px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.search-button {
    padding: 10px 20px;
    background-color: #4caf50;
    color: white;
    font-weight: bold;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.search-button:hover {
    background-color: #45a049;
}

/* 모달 백그라운드 */
.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

/* 모달 스타일 */
.modal {
    background: white;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.modal h2 {
    margin-bottom: 16px;
}

.modal-search-input {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    margin-bottom: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.modal ul {
    list-style: none;
    padding: 0;
    margin: 0;
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