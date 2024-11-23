<template>
  <div class="wrap">
    <div class="mypage-container">
      <!-- 프로필 카드 -->
      <div class="profile-card">
        <div class="profile-image">
          <img
            v-if="userInfo.role === '부동산'"
            src="../assets/apt.png"
            alt="부동산 이미지"
          />
          <img
            v-else-if="userInfo.role === '학생'"
            src="../assets/apt.png"
            alt="학생 이미지"
          />
        </div>
        <div class="profile-info">
          <div class="info-box">
            <p class="info-title">이름</p>
            <p class="info-content">{{ userInfo.name }}</p>
          </div>
          <div class="info-box">
            <p class="info-title">아이디</p>
            <p class="info-content">{{ userInfo.id }}</p>
          </div>
          <div class="info-box">
            <p class="info-title">역할</p>
            <p class="info-content">{{ userInfo.role }}</p>
          </div>
        </div>
      </div>

      <!-- 수정 카드 -->
      <div class="edit-card">
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            v-model="editableInfo.password"
            type="password"
            placeholder="비밀번호 입력"
            :class="{
              valid: validationState.password.isValid,
              invalid: !validationState.password.isValid,
            }"
            @blur="validatePassword"
          />
          <p v-if="!validationState.password.isValid" class="error-message">
            {{ validationState.password.message }}
          </p>
        </div>

        <div class="form-group">
          <label for="password2">비밀번호 재확인</label>
          <input
            id="password2"
            v-model="editableInfo.password2"
            type="password"
            placeholder="비밀번호 재입력"
            :class="{
              valid:
                validationState.password2.isValid &&
                validationState.password.isValid,
              invalid: !validationState.password2.isValid,
            }"
            @blur="validatePassword2"
          />
          <p v-if="!validationState.password2.isValid" class="error-message">
            {{ validationState.password2.message }}
          </p>
        </div>

        <div class="form-group">
          <label for="email">이메일</label>
          <input
            id="email"
            v-model="emailInput"
            type="email"
            placeholder="이메일 입력"
            :class="{
              valid: validationState.email.isValid,
              invalid: !validationState.email.isValid,
            }"
            @blur="validateEmail"
          />
          <p v-if="!validationState.email.isValid" class="error-message">
            {{ validationState.email.message }}
          </p>
        </div>

        <div class="form-group">
          <label for="school">학교</label>
          <input
            id="school"
            v-model="editableInfo.school"
            type="text"
            placeholder="학교 검색"
            readonly
            @click="openModal"
          />
        </div>

        <div class="form-group">
          <label for="tel">휴대폰 번호</label>
          <input
            id="phone"
            v-model="editableInfo.tel"
            type="text"
            autocomplete="off"
            placeholder="'-' 없이 입력"
            :class="{
              valid: validationState.tel.isValid,
              invalid: !validationState.tel.isValid,
            }"
            @blur="validateTel"
          />
          <p v-if="!validationState.tel.isValid" class="error-message">
            {{ validationState.tel.message }}
          </p>
        </div>

        <div class="button-group">
          <button @click="updateMember" class="update-btn">회원 수정</button>
          <button @click="confirmDelete" class="delete-btn">회원 삭제</button>
        </div>
      </div>
    </div>

    <!-- 학교 검색 모달 -->
    <div v-if="isModalOpen" class="modal-backdrop" @click.self="closeModal">
      <div class="modal">
        <h2>학교 검색</h2>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="학교명을 입력하세요"
          @input="searchSchools"
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
import { ref, onMounted, computed, watch } from "vue";
import apiClient from "@/axios";
import Swal from "sweetalert2";
import { useSchoolStore } from "@/stores/schoolStore";

const userInfo = ref({
  name: "",
  id: "",
  role: "",
});

const editableInfo = ref({
  password: "",
  password2: "",
  emailId: "",
  emailDomain: "",
  tel: "",
  school: "",
});

const emailInput = ref(""); // 이메일 입력을 관리
const isModalOpen = ref(false); // 모달 열림 상태
const searchQuery = ref(""); // 학교 검색어
const schoolStore = useSchoolStore(); // Pinia에서 학교 데이터 사용

const filteredSchools = computed(() => {
  if (!searchQuery.value.trim()) return schoolStore.schoolList.slice(0, 5);
  return schoolStore.schoolList.filter((school) =>
    school.name.includes(searchQuery.value)
  );
});

// 유효성 상태 및 메시지
const validationState = ref({
  password: { isValid: true, message: "" },
  password2: { isValid: true, message: "" },
  email: { isValid: true, message: "" },
  tel: { isValid: true, message: "" },
});

// 모달 열기 및 닫기
const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
};

// 학교 선택
const selectSchool = (selectedSchool) => {
  editableInfo.value.school = selectedSchool;
  closeModal();
};

// 유효성 검사 함수
const validatePassword = () => {
  const password = editableInfo.value.password;
  if (password.length < 8 || password.length > 16) {
    validationState.value.password = {
      isValid: false,
      message: "비밀번호는 8~16자 사이여야 합니다.",
    };
    return false;
  }
  validationState.value.password = { isValid: true, message: "" };
  return true;
};

const validatePassword2 = () => {
  // 비밀번호가 유효한 상태에서만 비밀번호 재확인을 체크
  if (!validationState.value.password.isValid) {
    validationState.value.password2 = {
      isValid: false,
      message: "비밀번호 입력을 먼저 완료해주세요.",
    };
    return false;
  }

  if (editableInfo.value.password !== editableInfo.value.password2) {
    validationState.value.password2 = {
      isValid: false,
      message: "비밀번호가 일치하지 않습니다.",
    };
    return false;
  }
  validationState.value.password2 = { isValid: true, message: "" };
  return true;
};

const validateEmail = () => {
  const emailRegex = /^[a-z0-9._\-]+@[a-z0-9.-]+\.[a-z]{2,4}$/i;
  if (!emailRegex.test(emailInput.value)) {
    validationState.value.email = {
      isValid: false,
      message: "유효한 이메일 주소를 입력해주세요.",
    };
    return false;
  }
  validationState.value.email = { isValid: true, message: "" };
  return true;
};

const validateTel = () => {
  const telRegex = /^01[0-9]{1}-[0-9]{3,4}-[0-9]{4}$/;
  if (!telRegex.test(editableInfo.value.tel)) {
    validationState.value.tel = {
      isValid: false,
      message: "올바른 휴대폰 번호를 입력해주세요.  (예: 010-1234-5678)",
    };
    return false;
  }
  validationState.value.tel = { isValid: true, message: "" };
  return true;
};

// 기존 회원 정보 불러오기
onMounted(async () => {
  try {
    const response = await apiClient.get("/member/profile", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    const data = response.data;
    userInfo.value = {
      name: data.name,
      id: data.id,
      role: data.role,
    };
    editableInfo.value.emailId = data.emailId;
    editableInfo.value.emailDomain = data.emailDomain;
    emailInput.value = `${data.emailId}@${data.emailDomain}`;
    editableInfo.value.school = data.school;
    editableInfo.value.tel = data.tel;
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "오류",
      text: "회원 정보를 불러오는 중 문제가 발생했습니다.",
    });
  }
});

// 이메일 입력값 변경 감지
watch(emailInput, () => {
  const [emailId, emailDomain] = emailInput.value.split("@");
  editableInfo.value.emailId = emailId || "";
  editableInfo.value.emailDomain = emailDomain || "";
});

// 회원 정보 수정
const updateMember = async () => {
  if (
    !validatePassword() ||
    !validatePassword2() ||
    !validateEmail() ||
    !validateTel()
  ) {
    Swal.fire({
      icon: "error",
      title: "비밀번호 불일치",
      text: "비밀번호와 재확인 비밀번호가 일치하지 않습니다.",
    });
    return;
  }
  console.log({
    emailId: editableInfo.value.emailId,
    emailDomain: editableInfo.value.emailDomain,
    password: editableInfo.value.password,
    tel: editableInfo.value.tel,
    school: editableInfo.value.school,
  });
  try {
    await apiClient.put(
      "/member/update",
      null, // 데이터는 params로 보냄
      {
        params: {
          emailId: editableInfo.value.emailId,
          emailDomain: editableInfo.value.emailDomain,
          pw: editableInfo.value.password,
          tel: editableInfo.value.tel,
          school: editableInfo.value.school,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );

    Swal.fire({
      icon: "success",
      title: "수정 완료",
      text: "회원 정보가 성공적으로 수정되었습니다.",
    }).then(() => {
      //확인 버튼을 누르면 메인 페이지로 이동
      window.location.href = "/";
    });
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "수정 실패",
      text: "회원 정보 수정 중 문제가 발생했습니다.",
    });
  }
};

// 회원 삭제
const confirmDelete = () => {
  Swal.fire({
    title: "회원 탈퇴",
    text: "정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "탈퇴",
    cancelButtonText: "취소",
  }).then((result) => {
    if (result.isConfirmed) {
      deleteAccount();
    }
  });
};

const deleteAccount = async () => {
  try {
    await apiClient.delete("/member/delete", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    Swal.fire({
      icon: "success",
      title: "탈퇴 완료",
      text: "회원 탈퇴가 완료되었습니다.",
    }).then(() => {
      localStorage.removeItem("token");
      location.reload();
    });
  } catch (error) {
    Swal.fire({
      icon: "error",
      title: "탈퇴 실패",
      text: "회원 탈퇴 중 문제가 발생했습니다.",
    });
  }
};
</script>

<style scoped>
.wrap {
  display: flex;
  justify-content: center; /* 좌우 중앙 정렬 */
  align-items: center; /* 상하 중앙 정렬 */
  width: 100%;
  height: 80vh; /* 뷰포트 전체 높이 */
  background-color: #f5f5f5; /* 배경색 추가 (선택 사항) */
}

.mypage-container {
  display: flex;
  gap: 15px;
  margin: 20px auto;
  max-width: 1200px;
}

.profile-card {
  width: 350px;
  height: 500px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: center;
  padding: 20px;
}

.profile-image img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  margin-bottom: 20px;
}

.profile-info {
  text-align: left;
}

.profile-info .info-item {
  margin: 10px 0;
  font-size: 1.2rem;
}

.info-box {
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 5px;
  margin-bottom: 15px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.info-box:hover {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  transform: translateY(-2px);
}

.info-title {
  font-weight: bold;
  font-size: 0.9rem;
  margin-left: 5px;
  color: #333;
  margin-bottom: 5px;
}

.info-content {
  margin-left: 8px;
  font-size: 1.1rem;
  color: #555;
}

.edit-card {
  width: 500px;
  height: 500px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.form-group {
  margin-top: 5px;
  margin-bottom: 10px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 13px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-group {
  display: flex;
  justify-content: center; /* 버튼 간격 조정 */
  margin-top: 12px;
}
.update-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.update-btn:hover {
  background-color: #0056b3;
}

.delete-btn {
  padding: 10px 20px;
  background-color: #d33;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background-color: #b71c1c;
}

/* 모달 css */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
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

/* 유효성 검사 통과 */
input.valid {
  border-color: #007bff;
}

/* 유효성 검사 실패 */
input.invalid {
  border-color: #d33;
}

/* 에러 메시지 스타일 */
.error-message {
  color: #d33;
  font-size: 0.9rem;
  margin-top: 5px;
}
</style>
