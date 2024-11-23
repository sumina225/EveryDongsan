<template>
    <h1 class="logo">
        <img src="../assets/mainlog.png" alt="로고"/>
    </h1>
    <div class="wrap">
        <div id="container">
            <div class="step step1">
                <div class="membership">
                    <!-- 제목 -->
                    <dl>
                        <dt>제목</dt>
                        <dd>
                            <input
                                v-model="form.title"
                                type="text"
                                placeholder="게시물 이름을 입력해주세요.(눈에 띄는)"
                                autocomplete="off"/>
                        </dd>
                    </dl>

                    <!-- 내용 -->
                    <dl>
                        <dt>매물 설명 및 내용</dt>
                        <dd>
                            <textarea v-model="form.content" placeholder="매물 설명 및 내용을 간단하게 입력하세요."></textarea>
                        </dd>
                    </dl>

                    <!-- 학교 -->
                    <dl>
                        <dt>학교</dt>
                        <dd @click="openModal">
                            <input
                                v-model="form.home.school"
                                type="text"
                                readonly="readonly"
                                placeholder="학교를 선택하세요."
                                :class="{ filled: form.home.school }"/>
                        </dd>
                    </dl>

                    <!-- 매물 정보 -->
                    <dl>
                        <dt>매물 이름</dt>
                        <dd>
                            <input
                                v-model="form.home.name"
                                type="text"
                                placeholder="집 이름을 입력하세요."
                                autocomplete="off"/>
                        </dd>
                    </dl>
                    <dl>
                        <dt>위도</dt>
                        <dd>
                            <input
                                v-model="form.home.latitude"
                                type="text"
                                placeholder="위도를 입력하세요."
                                autocomplete="off"/>
                        </dd>
                    </dl>
                    <dl>
                        <dt>경도</dt>
                        <dd>
                            <input
                                v-model="form.home.longitude"
                                type="text"
                                placeholder="경도를 입력하세요."
                                autocomplete="off"/>
                        </dd>
                    </dl>
                    <dl>
                        <dt>카테고리</dt>
                        <dd>
                            <select v-model="form.home.category">
                                <option value="아파트">아파트</option>
                                <option value="원룸/투룸">원룸/투룸</option>
                                <option value="오피스텔">오피스텔</option>
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt>면적</dt>
                        <dd>
                            <input
                                v-model="form.home.area"
                                type="text"
                                placeholder="면적을 입력하세요.(m²)"
                                autocomplete="off"/>
                        </dd>
                    </dl>

                    <!-- 전세/월세 -->
                    <dl class="radio_select">
                        <dt>거래 유형</dt>
                        <dd>
                            <div>
                                <input
                                    type="radio"
                                    id="rentalType_jeonse"
                                    name="rentalType"
                                    value="전세"
                                    v-model="rentalType"/>
                                <label for="rentalType_jeonse" :class="{ on: rentalType === '전세' }">
                                    <span>
                                        <em></em>전세</span>
                                </label>
                            </div>
                            <div>
                                <input
                                    type="radio"
                                    id="rentalType_monthly"
                                    name="rentalType"
                                    value="월세"
                                    v-model="rentalType"/>
                                <label for="rentalType_monthly" :class="{ on: rentalType === '월세' }">
                                    <span>
                                        <em></em>월세</span>
                                </label>
                            </div>
                        </dd>
                    </dl>

                    <!-- 전세일 때 -->
                    <dl v-if="rentalType === '전세'">
                        <dt>보증금</dt>
                        <dd>
                            <input v-model="jeonseDeposit" type="text" placeholder="보증금을 입력하세요 (만원)"/>
                        </dd>
                    </dl>

                    <!-- 월세일 때 -->
                    <dl v-else-if="rentalType === '월세'">
                        <dt>보증금 / 월세</dt>
                        <dd>
                            <input v-model="monthlyDeposit" type="text" placeholder="보증금을 입력하세요 (만원)"/>
                            <input v-model="monthlyRent" type="text" placeholder="월세를 입력하세요 (만원)"/>
                        </dd>
                    </dl>
                </div>

                <!-- 버튼 -->
                <div class="btn_wrap">
                    <button class="btn gray" @click.prevent="resetForm">초기화</button>
                    <button class="btn blue" @click.prevent="submitPost">등록하기</button>
                </div>
            </div>
        </div>
        <!-- 학교 모달 -->
        <div v-if="isModalOpen" class="modal-backdrop" @click.self="closeModal">
            <div class="modal">
                <h2>학교 검색</h2>
                <input
                    v-model="searchQuery"
                    type="text"
                    placeholder="학교명을 입력하세요"
                    @input="searchSchools"/>
                <ul>
                    <li
                        v-for="(school, index) in filteredSchools"
                        :key="index"
                        @click="selectSchool(school.name)">
                        {{ school.name }}
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup="setup">
    import {ref, computed} from "vue";
    import Swal from "sweetalert2";
    import {useSchoolStore} from "@/stores/schoolStore";
    import {useUserStore} from "@/stores/useUserStore"; // Pinia 사용자 스토어
    import {usePostStore} from "@/stores/usePostStore"
    import apiClient from "axios";
    import {useRouter} from "vue-router";

    const router = useRouter();

    // 매물 등록 폼 데이터 초기화
    const form = ref({
        title: "",
        content: "",

        home: {
            school: "",
            name: "",
            latitude: "",
            longitude: "",
            area: "",
            category: "아파트",
            rentalType: "전세"
        }
    });

    // 거래 유형 및 금액 관련 상태 관리
    const rentalType = ref("전세");
    const jeonseDeposit = ref("");
    const monthlyDeposit = ref("");
    const monthlyRent = ref("");

    // 학교 데이터 및 검색 관련 상태 관리
    const schoolStore = useSchoolStore();
    const isModalOpen = ref(false);
    const searchQuery = ref("");

    // 사용자 역할 관리
    const userStore = useUserStore();
    const userRole = computed(
        () => userStore.user
            ?.role || ""
    );

    // 검색된 학교 목록 계산
    const filteredSchools = computed(() => {
        if (!searchQuery.value.trim()) {
            return schoolStore
                .schoolList
                .slice(0, 5);
        }
        return schoolStore
            .schoolList
            .filter((school) => school.name.includes(searchQuery.value));
    });

    // 학교 선택 모달 열기
    const openModal = () => {
        schoolStore.loadSchools();
        isModalOpen.value = true;
    };

    // 학교 선택 모달 닫기
    const closeModal = () => {
        isModalOpen.value = false;
        searchQuery.value = "";
    };

    // 학교 선택 처리
    const selectSchool = (schoolName) => {
        form.value.home.school = schoolName;
        closeModal();
    };

    // 폼 초기화 함수
    const resetForm = () => {
        form.value = {
            title: "",
            content: "",

            home: {
                school: "",
                name: "",
                latitude: "",
                longitude: "",
                area: "",
                category: "아파트",
                rentalType: "전세"
            }
        };
        jeonseDeposit.value = "";
        monthlyDeposit.value = "";
        monthlyRent.value = "";
    };

    // 폼 제출 처리
    const submitPost = async () => {
        const postStore = usePostStore(); // Pinia 스토어 연결

        // 거래 유형에 따른 데이터 구성
        const postData = {
            title: form.value.title,
            content: form.value.content,

            home: {
                name: form.value.home.name,
                latitude: form.value.home.latitude,
                longitude: form.value.home.longitude,
                area: form.value.home.area,
                school: form.value.home.school,
                category: form.value.home.category,
                rentalType: rentalType.value,
                price: rentalType.value === "전세"
                    ? `${jeonseDeposit.value}`
                    : `${monthlyDeposit.value}/${monthlyRent.value}`
            }
        };

        // 유효성 검사
        if (!postData.title.trim()) {
            Swal.fire("오류", "제목을 입력해주세요.", "error");
            return;
        }
        if (!postData.content.trim()) {
            Swal.fire("오류", "내용을 입력해주세요.", "error");
            return;
        }
        // if (!postData.school.trim()) {
        //     Swal.fire("오류", "학교를 선택해주세요.", "error");
        //     return;
        // }
        if (!postData.home.name.trim()) {
            Swal.fire("오류", "매물 이름을 입력해주세요.", "error");
            return;
        }
        if (!postData.home.latitude.trim() || !postData.home.longitude.trim()) {
            Swal.fire("오류", "위도와 경도를 입력해주세요.", "error");
            return;
        }
        if (!postData.home.area.trim()) {
            Swal.fire("오류", "면적을 입력해주세요.", "error");
            return;
        }
        if (rentalType.value === "전세" && !jeonseDeposit.value.trim()) {
            Swal.fire("오류", "전세 보증금을 입력해주세요.", "error");
            return;
        }
        if (rentalType.value === "월세" && (!monthlyDeposit.value.trim() || !monthlyRent.value.trim())) {
            Swal.fire("오류", "월세 보증금과 월세를 입력해주세요.", "error");
            return;
        }

        // Pinia 스토어를 통해 서버 요청
        const result = await postStore.submitPost(postData);

        if (result.success) {
            Swal.fire("등록 완료", result.message, "success");
            resetForm(); // 폼 초기화
            router.push("/board"); // 게시판으로 이동
        } else {
            Swal.fire("오류", result.message, "error");
        }
    };
</script>

<style scoped="scoped">
    body {
        font-family: "Apple SD Gothic Neo", "맑은 고딕", "돋움", sans-serif;
        background-color: #f9f9f9;
        margin: 0;
        padding: 0;
    }

    .logo {
        width: 100%;
        height: 150px;
        text-align: center;
        margin-top: 80px;
    }

    .logo img {
        width: 300px;
        height: 150px;
    }

    .wrap {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: flex-start;
        margin: 60px auto;
    }

    #container {
        width: 60%;
        background-color: white;
        padding: 40px;
        border-radius: 5px;
        border: 1px solid #ddd;
        /* 선 추가 */
    }

    .membership dl {
        margin-bottom: 20px;
    }

    .membership dt {
        font-weight: bold;
        margin-bottom: 10px;
    }

    .membership dd input,
    .membership dd select,
    .membership dd textarea {
        width: 100%;
        padding: 12px;
        font-size: 14px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .membership dd textarea {
        height: 100px;
    }

    .rental-type {
        display: flex;
        gap: 20px;
    }

    .rental-type button {
        padding: 10px 20px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: rgb(79, 168, 27);
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    .rental-type button.active {
        background-color: rgb(79, 168, 27);
        color: white;
        border-color: rgb(79, 168, 27);
    }

    .btn_wrap {
        margin-top: 40px;
        display: flex;
        justify-content: space-between;
    }

    .btn {
        width: 48%;
        text-align: center;
        padding: 12px 0;
        font-weight: bold;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
        text-decoration: none;
    }

    .btn.gray {
        background-color: #bbb;
        border: none;
        color: white;
    }

    .btn.blue {
        background-color: rgb(79, 168, 27);
        border: none;
        color: white;
    }

    /* 전월세버튼 */
    .membership dl.radio_select div label.on span {
        color: white;

    }

    .membership dl.radio_select div {
        position: relative;
        width: 50%;

        float: left;
        display: inline-block;
        text-align: center;
    }

    .membership dl.radio_select label {

        border-radius: 4px;
        line-height: 50px;
        height: 50px;
        cursor: pointer;
        margin: 0;
        padding: 0;
        display: block;
        border: 1px solid #201f1f;
        text-align: center;
        background-color: #fff;
        transition: background-color 0.3s ease, border-color 0.3s ease;
    }

    .membership dl.radio_select label.on {
        border: none;
        background-color: rgb(79, 168, 27);
        color: rgb(79, 168, 27);
    }

    .membership dl.radio_select label span {
        font-size: 14px;
        font-weight: bold;
    }

    .radio_select input[type="radio"] {
        margin-right: 5px;
        width: 0;
        position: absolute;
        top: 50%;
        left: 70px;
        margin: -12px 0 0;
        padding: 0;
    }

    .membership dl.radio_select dd {
        height: 50px;
    }

    /* 모달디자인 */
    .membership .school dd input {
        border: 1px solid #bbb;
        border-radius: 4px;
        box-sizing: border-box;
        display: block;
        outline: None;
        text-indent: 15px;
        padding: 15px 0;
        width: 100%;
        font-size: 14px;
        background-color: white;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .membership dl.school dd input.filled {
        background-color: #f2f2f2;
        /* 회색 배경 */
        cursor: not-allowed;
    }

    .modal {
        background-color: white;
        padding: 20px;
        border-radius: 8px;
        width: 400px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
        animation: fadeIn 0.3s ease-in-out;
        /* 부드러운 등장 애니메이션 */
    }

    .modal-backdrop {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-color: rgba(0, 0, 0, 0.5);
        /* 반투명 배경 */
        display: flex;
        justify-content: center;
        /* 가로 정중앙 */
        align-items: center;
        /* 세로 정중앙 */
        z-index: 1000;
        /* 모달 위에 표시 */
    }

    .modal h2 {
        margin-bottom: 16px;
    }

    .modal input {
        width: 100%;
        padding: 10px;
        margin-bottom: 16px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .modal ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .modal li {
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        margin-bottom: 8px;
        cursor: pointer;
        transition: background-color 0.2s ease;
    }

    .modal li:hover {
        background-color: #f2f2f2;
    }
</style>