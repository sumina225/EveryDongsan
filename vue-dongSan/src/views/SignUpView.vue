<template>
  <div class="header"></div>
  <div class="wrap">
    <div id="container">
      <div class="step step1">
        <h1 class="logo">
          <div @click="goHome">
            <img src="../assets/mainlog.png" alt="로고" />
          </div>
        </h1>
        <div class="membership">
          <dl :class="{ ok: check === 1, error: check === 0 }">
            <dt>아이디</dt>
            <dd>
              <input
                v-model="id"
                type="text"
                maxlength="12"
                placeholder=""
                autocomplete="off"
                title="아이디"
              />
              <span></span>
            </dd>
            <input type="hidden" name="check_id" value="0" />
            <p>{{ msgBox }}</p>
          </dl>
          <dl :class="{ ok: check2 === 1, error: check2 === 0 }">
            <dt>비밀번호</dt>
            <dd>
              <input
                v-model="password"
                type="password"
                maxlength="16"
                placeholder=""
                autocomplete="off"
                title="비밀번호"
              />
              <span></span>
            </dd>
            <p>{{ msgPw }}</p>
          </dl>
          <dl :class="{ ok: check3 === 1, error: check3 === 0 }">
            <dt>비밀번호 재확인</dt>
            <dd>
              <input
                v-model="password2"
                type="password"
                maxlength="15"
                placeholder=""
                autocomplete="off"
                title="비밀번호 재확인"
              />
              <span></span>
            </dd>
            <p>{{ msgPw2 }}</p>
          </dl>
          <dl :class="{ ok: checkName === 1, error: checkName === 0 }">
            <dt>이름</dt>
            <dd>
              <input
                v-model="name"
                type="text"
                maxlength="25"
                placeholder=""
                autocomplete="off"
                title="이름"
              />
              <span></span>
            </dd>
            <p>{{ msgName }}</p>
          </dl>
          <dl class="birth" :class="{ ok: checkBirth === 1, error: checkBirth === 0 }">
            <dt>생년월일</dt>
            <dd>
              <input
                v-model="year"
                type="text"
                maxlength="4"
                placeholder="년(4자)"
                autocomplete="off"
                title="년(4자)"
              />
              <div>
                <input
                  style="margin-left: 3.7%"
                  v-model="month"
                  type="text"
                  maxlength="2"
                  placeholder="월"
                  autocomplete="off"
                  title="월"
                />
              </div>
              <div>
                <input
                  style="margin-left: 3.7%"
                  v-model="day"
                  type="text"
                  maxlength="2"
                  placeholder="일"
                  autocomplete="off"
                  title="일"
                />
              </div>
              <span style="right: 18px"></span>
            </dd>
            <p>{{ birthMsg }}</p>
          </dl>
          <dl class="radio_select role">
            <dt>역할</dt>
            <dd>
              <div>
                <input
                  type="radio"
                  id="Student"
                  name="role"
                  value="학생"
                  v-model="selectedRole"
                /><label for="Student" :class="{ on: selectedRole === '학생' }"
                  ><span><em></em>학생</span></label
                >
              </div>
              <div>
                <input
                  type="radio"
                  id="Rental"
                  name="role"
                  value="부동산"
                  v-model="selectedRole"
                /><label for="Rental" :class="{ on: selectedRole === '부동산' }"
                  ><span><em></em>부동산</span></label
                >
              </div>
            </dd>
            <p></p>
          </dl>
          <dl class="school" :class="{ ok: checkSchool === 1, error: checkSchool === 0 }">
            <dt>학교</dt>
            <dd @click="openModal">
              <input
                v-model="school"
                type="text"
                name="school"
                id="school"
                autocomplete="off"
                title="학교"
                readonly
                :class="{ filled: school }"
              />
              <span></span>
            </dd>
            <p>{{ msgSchool }}</p>
          </dl>
          <dl class="email" :class="{ ok: checkEmail === 1, error: checkEmail === 0 }">
            <dt>이메일</dt>
            <dd>
              <input
                v-model="email"
                type="text"
                name="email"
                id="email"
                autocomplete="off"
                title="이메일"
              />
              <span></span>
            </dd>
            <p>{{ msgEmail }}</p>
          </dl>
          <dl class="phone" :class="{ ok: checkPhone === 1, error: checkPhone === 0 }">
            <dt>휴대폰</dt>
            <dd>
              <input
                v-model="tel"
                type="text"
                name="mobile"
                id="phone"
                autocomplete="off"
                placeholder="‘-’ 입력 해주세요."
              />
              <span></span>
            </dd>
            <p>{{ msgPhone }}</p>
          </dl>
        </div>
        <div class="btn_wrap">
          <a href class="btn gray previous">이전</a>
          <a href="#" @click="nextStep" class="btn blue">다음</a>
        </div>
      </div>
    </div>
  </div>

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
</template>

<script setup>
import { ref, watch, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useSchoolStore } from "@/stores/schoolStore";
import apiClient from "@/axios";
import Swal from "sweetalert2";

// -------- 학교 검색  -------//
const school = ref(""); // 선택된 학교 이름
const searchQuery = ref(""); // 검색어
const isModalOpen = ref(false); // 모달 열림 상태
const schoolStore = useSchoolStore();
const checkSchool = ref(-1); // 유효성 체크
const msgSchool = ref(""); // 에러 메시지

// Pinia 스토어에서 학교 데이터 로드
onMounted(() => {
  schoolStore.loadSchools(); // 스토어의 loadSchools 메서드 호출
});

// 검색된 학교 목록 필터링
const filteredSchools = computed(() => {
  if (!searchQuery.value.trim()) return schoolStore.schoolList.slice(0, 5);
  console.log(schoolStore.schoolList);
  return schoolStore.schoolList.filter((school) => school.name.includes(searchQuery.value));
});

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
  school.value = selectedSchool;
  checkSchool.value = 1; // 유효성 체크 완료
  msgSchool.value = "선택된 학교: " + selectedSchool;
  isModalOpen.value = false; // 모달 닫기
};

// ----------------------------  학교 end ----------------------  //

const router = useRouter();
const selectedRole = ref("");
const check = ref(-1); // 체크상태 초기값 -1
const check2 = ref(-1); // pwd 체크
const check3 = ref(-1); // pwd 재확인 체크
const checkName = ref(-1); // nameCheck 이름 체쿠
//------------------------------------------------------------
const msgBox = ref(""); // 메세지 박스
const msgPw = ref(""); // 비밀번호 체크
const msgPw2 = ref("");
const msgName = ref(""); // 이름체크 메세지 박스
//---------------------------------------------------------

// 특수문자 체크 (한글+영문+숫자 허용)
const _valid_value_check = (value) => {
  for (let i = 0; i < value.length; i++) {
    const code = value.charCodeAt(i);
    if (code >= 0 && code <= 47) {
      return true;
    }
  }
  return false;
};

//특수문자 체크(이름 확인용,영문+한글 허용)
const _valid_name_check = (value) => {
  const regex = /^[a-zA-Z가-힣]*$/;

  //정규식을 사용하여 값이 한글과 영문만 포함하는지 확인
  return regex.test(value);
};

// 특수문자 체크 (영문+숫자 허용)
const _is_hangle = (value) => {
  const allowedChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  for (let i = 0; i < value.length; i++) {
    if (!allowedChars.includes(value[i])) {
      return true;
    }
  }
  return false;
};

// 특수문자 체크 (숫자 허용)
const _is_num = (value) => {
  const allowedChars = "0123456789";
  for (let i = 0; i < value.length; i++) {
    if (!allowedChars.includes(value[i])) {
      return true;
    }
  }
  return false;
};

// 길이 체크 (공통)
const _valid_length_check = (min, max, value) => {
  value = value.trim();
  return value.length < min || value.length > max;
};

// 바이트수 체크
const _valid_byte_check = (min, max, value) => {
  let strbyte = 0;
  value = value.trim();

  for (let i = 0; i < value.length; i++) {
    const code = value.charCodeAt(i);
    const ch = value[i].toUpperCase();

    if ((ch < "0" || ch > "9") && (ch < "A" || ch > "Z") && (code > 255 || code < 0)) {
      strbyte += 2; // 한글인 경우 2byte로 계산
    } else {
      strbyte += 1; // 숫자, 문자는 1byte로 계산
    }
  }

  return strbyte < min || strbyte > max;
};

// id 체크하는 함수들 ----------------------------------------------------------------

const id = ref("");

watch(id, (newVal) => {
  idCheck();
});

// 아이디 체크
const idCheck = () => {
  msgBox.value = ""; // 메세지 초기화
  check.value = -1; // 초기화 상태

  // 값이 비어있는지 체크
  if (id.value.length <= 0) {
    msgBox.value = "아이디를 입력해주세요.";
    check.value = 0;
    return;
  }
  // 한글체크
  if (_is_hangle(id.value)) {
    msgBox.value = "한글/특수문자는 사용하실 수 없습니다.";
    check.value = 0; // 에러상태
    return;
  }

  // 아이디 길이 체크
  if (_valid_length_check(6, 12, id.value)) {
    msgBox.value = "아이디의 길이는 6-12자 입니다.";
    check.value = 0; // 에러상태
    return;
  }
  //바이트 체크
  if (_valid_byte_check(6, 12, id.value)) {
    msgBox.value = "아이디가 6자~12자 바이트 범위를 초과하였습니다.";
    check.value = 0;
    return;
  }
  console.log(check);
  // 모든 조건 통과
  check.value = 1; // 성공 상태
  msgBox.value = "사용할 수 있는 아이디 입니다.";
};
//---------------------------- id 체크하는 함수 end --------------------------- //
const password = ref("");
const password2 = ref("");

//비밀번호 체크

watch(password, (newVal) => {
  pwdCheck();
});

watch(password2, (newVal) => {
  pwdCheck2();
});

const pwdCheck = () => {
  const pwd = password.value; // 비밀번호 입력값
  msgPw.value = ""; // 메세지 초기화
  check2.value = -1; // 초기화 상태

  // 비밀번호가 비어있는지 체크
  if (pwd.length <= 0) {
    msgPw.value = "비밀번호를 입력해주세요.";
    check2.value = 0; // 에러 상태
    return;
  }

  let isOk = true; // 초기 상태는 모두 OK
  let errorMessages = []; // 오류 메시지들을 배열로 저장

  // 비밀번호 길이 체크 (6-12자)
  if (_valid_length_check(8, 16, pwd)) {
    isOk = false;
    errorMessages.push("비밀번호는 8-16자여야 합니다.");
  }

  // 한글 포함 체크
  if (_is_hangle(pwd)) {
    isOk = false;
    errorMessages.push("비밀번호에 한글은 사용할 수 없습니다.");
  }

  // 비밀번호 조건이 하나라도 오류가 있으면 오류 메시지 표시
  if (!isOk) {
    msgPw.value = errorMessages.join(" ");
    check2.value = 0; // 에러 상태
    return;
  }

  // 모든 조건을 만족하면
  check2.value = 1; // 성공 상태
};

const pwdCheck2 = () => {
  const pwd = password.value;
  const pwdConfirm = password2.value;
  check3.value = -1;
  msgPw2.value = "";

  if (pwd !== pwdConfirm || check2.value !== 1) {
    check3.value = 0; // 에러코드
    msgPw2.value = "비밀번호가 일치하지 않습니다.";
    return;
  }

  check3.value = 1;
};

//-------------------------------- 비밀번호 체크 end --------------------------//

//---------------- 이름 체크 start --------------------- //
const name = ref("");

watch(name, (newVal) => {
  nameCheck();
});

const nameCheck = () => {
  const username = name.value;

  checkName.value = -1; // 초기화
  msgName.value = "";

  if (username.length <= 0) {
    checkName.value = 0;
    msgName.value = "이름을 입력해주세요.";
    return;
  }

  if (!_valid_name_check(username)) {
    checkName.value = 0;
    msgName.value = "이름을 정확히 입력해주세요.(특수문자,숫자,공백 입력 불가)";
    return;
  }

  checkName.value = 1;
};

// ----------------------------- 생년월일 체크 ----------------------------- //

const year = ref("");
const month = ref("");
const day = ref("");
const checkBirth = ref(-1); // 초기화
const birthMsg = ref(""); // 생년월일 관련 메시지

// 생년월일 유효성 체크 함수
const birthCheck = () => {
  birthMsg.value = ""; // 초기화
  checkBirth.value = -1;

  const value = year.value + month.value + day.value;

  // 8자리 입력 확인
  if (value.length !== 8) {
    birthMsg.value = "생년월일을 8자리로 입력해주세요.";
    checkBirth.value = 0;
    return;
  }
  const nextStep = async () => {
    // 유효성 체크
    if (
      check.value !== 1 ||
      check2.value !== 1 ||
      check3.value !== 1 ||
      checkName.value !== 1 ||
      checkEmail.value !== 1 ||
      checkPhone.value !== 1 ||
      checkSchool.value !== 1
    ) {
      alert("모든 필드를 올바르게 입력해주세요.");
      return;
    }

    // 입력된 데이터를 객체로 수집
    const memberData = {
      id: id.value,
      password: password.value,
      name: name.value,
      emailId: email.value.split("@")[0],
      emailDomain: email.value.split("@")[1],
      tel: tel.value,
      role: selectedRole.value,
      school: school.value,
    };

    try {
      const response = await apiClient.post("/member/signup", memberData);

      if (response.status === 200) {
        alert("회원가입이 완료되었습니다!");
        router.push("/");
      } else {
        alert(`회원가입 실패: ${response.data.message || "오류가 발생했습니다."}`);
      }
    } catch (error) {
      console.error("회원가입 요청 중 오류 발생:", error.response?.data || error.message);
      alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  // 숫자만 입력 확인
  if (!/^\d{8}$/.test(value)) {
    birthMsg.value = "생년월일은 숫자로만 입력해주세요.";
    checkBirth.value = 0;
    return;
  }

  // 연, 월, 일 유효성 검사
  const currentYear = new Date().getFullYear();
  const inputYear = parseInt(year.value);
  const inputMonth = parseInt(month.value);
  const inputDay = parseInt(day.value);

  // 올바른 연도, 월, 일 범위 확인
  if (inputYear < 1900 || inputYear > currentYear || inputMonth > 12 || inputDay > 31) {
    birthMsg.value = "올바른 생년월일을 입력해주세요.";
    checkBirth.value = 0;
    return;
  }

  // 만 14세 미만 회원 가입 제한
  if (currentYear - inputYear < 14) {
    birthMsg.value = "만 14세 미만은 가입할 수 없습니다.";
    checkBirth.value = 0;
    return;
  }

  checkBirth.value = 1;
};

// 생년월일 입력값이 변경될 때마다 유효성 체크
watch([year, month, day], () => {
  birthCheck();
});

// ----------------------------------이메일 체크 ---------------------------------- //

const email = ref("");
const msgEmail = ref("");
const checkEmail = ref(-1);

watch(email, (newVal) => {
  EmailCheck();
});

const EmailCheck = () => {
  msgEmail.value = "";
  checkEmail.value = -1;

  if (email.value.length <= 0) {
    checkEmail.value = 0;
    msgEmail.value = "이메일을 입력해주세요.";
    return;
  }

  // 이메일 정규식 체크
  const emailRegex = /^[a-z0-9._\-]{3,}@[a-z0-9.-]+\.[a-z]{2,4}$/i;
  if (!emailRegex.test(email.value)) {
    checkEmail.value = 0;
    msgEmail.value = "올바른 이메일 주소를 입력해주세요.";
    return;
  }

  checkEmail.value = 1;
};

// ---------------------------------- 휴대폰 번호 체크 ---------------------- //
const tel = ref("");
const checkPhone = ref(-1);
const msgPhone = ref("");

watch(tel, (newVal) => {
  PhoneCheck();
});

// 휴대폰 번호 유효성 체크
const PhoneCheck = () => {
  msgPhone.value = ""; // 메세지 초기화
  checkPhone.value = -1; // 초기화 상태

  const number = tel.value.trim();

  // 번호가 비어있는지 체크
  if (number.length <= 0) {
    msgPhone.value = "번호를 입력해주세요.";
    checkPhone.value = 0;
    return;
  }

  // 010, 011, 019 등으로 시작하는지 확인
  const phoneRegex = /^01[0-9]{1}-[0-9]{3,4}-[0-9]{4}$/; // 한국의 휴대폰 번호 형식에 맞는 정규식
  if (!phoneRegex.test(number)) {
    msgPhone.value = "올바른 휴대폰 번호를 입력해주세요. ex(01x-1xxx-2xxx)";
    checkPhone.value = 0;
    return;
  }

  // 모든 조건을 만족하면
  checkPhone.value = 1; // 성공 상태
};

//-------------------------------- 입력부분 체크 END ------ //

// 다음 단계로 이동하는 함수
const nextStep = async () => {
  // 유효성 체크
  if (
    check.value !== 1 ||
    check2.value !== 1 ||
    check3.value !== 1 ||
    checkName.value !== 1 ||
    checkEmail.value !== 1 ||
    checkPhone.value !== 1 ||
    checkSchool.value !== 1
  ) {
    alert("모든 필드를 올바르게 입력해주세요.");
    return;
  }

  // 입력된 데이터를 객체로 수집
  const memberData = {
    id: id.value,
    password: password.value,
    name: name.value,
    emailId: email.value.split("@")[0],
    emailDomain: email.value.split("@")[1],
    tel: tel.value,
    role: selectedRole.value,
    school: school.value,
  };

  try {
    const response = await apiClient.post("/member/signup", memberData);

    if (response.status === 201) {
      Swal.fire({
        icon: "success",
        title: "회원가입 완료",
        text: response.data, // 서버에서 반환한 메시지 표시
        confirmButtonText: "확인",
      });
      router.push("/");
    }
  } catch (error) {
    if (error.response && error.response.data) {
      Swal.fire({
        icon: "error",
        title: "회원가입 실패",
        text: error.response.data, // 서버에서 반환한 오류 메시지
        confirmButtonText: "다시 시도",
      });
    } else {
      Swal.fire({
        icon: "error",
        title: "예상치 못한 오류",
        text: "회원가입 중 문제가 발생했습니다. 다시 시도해주세요.",
        confirmButtonText: "확인",
      });
    }
  }
};
</script>

<style scoped>
body,
html {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  font-size: 12px;
  text-decoration: none;
  background-color: #f5f5f5;
  /* 배경 색 설정 */
}

.header {
  height: 50px;
}

div {
  display: block;
}

.wrap {
  width: 100%;
  height: 100%;
}

#container {
  width: 920px;
  margin: auto;
}

.step1,
.step2,
.step3,
.step4 {
  width: 100%;
  max-width: 500px;
  /* 스텝 크기를 제한 */
  padding: 20px;
  background: #fafafa;
  /* 배경 색을 추가하면 더 보기 좋음 */
  margin: auto;
}

/* 로고 스타일 */
.logo {
  width: 100%;
  height: 160px;
  text-align: center;
  margin-top: -60px;
}

.logo img {
  width: 80%;
  height: 160px;
}

.logo a {
  overflow: hidden;
  display: block;
  height: 100%;
  background: url("/images/svg/logo.svg") 0 0 no-repeat;
  background-size: 100% 100%;
}

/* 공통 스타일 */
.step {
  padding: 20px;
  border-radius: 8px;
}

.step1 {
  background-color: #fffefe;
  margin-top: 10px;
}

.step h1 {
  font-size: 2rem;
  color: #2e6afd;
}

/* ok 처리 -------------------------------------------------------- */
.membership dl.ok dd input {
  border-color: #3d82f7;
}

.membership dl.ok dd span {
  position: absolute;
  display: block;
  right: 10px;
  width: 12px;
  height: 10px;
  top: 20px;
  cursor: pointer;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-image: url(data:image/svg+xml;charset=utf-8;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCAxNSAxMSc+PHBhdGggIGZpbGw9JyMyZTZhZmQnIGZpbGwtcnVsZT0nZXZlbm9kZCcgZD0nTTUuODIyIDEwLjUwMmwtNC42MS01LjMxMUMuODU1IDQuNzguOSA0LjE1OCAxLjMxMSAzLjgwMWMuNDEyLS4zNTYgMS4wMzQtLjMxMiAxLjM5MS4wOTlsMy4yNTcgMy43NTJMMTMuMTE0Ljg0OGMuMzk0LS4zNzYgMS4wMTktLjM1OSAxLjM5NC4wMzQuMzc1LjM5NC4zNiAxLjAxOC0uMDM1IDEuMzkzbC04LjY1MSA4LjIyN3onLz48L3N2Zz4=);
}

/* .membership dl.ok p {
  clear: both;
  display: block;
  margin-top: 7px;
  font-size: 12px;
  color: #3d82f7;
  line-height: 1.4;
} */

/* 아이디,비밀번호 에러 및 ajax 처리 이벤트 */
.membership dl p {
  clear: both;
  display: none;
  margin-top: 7px;
  font-size: 12px;
  color: #ff223c;
  line-height: 1.4;
}

.membership dl.error p {
  display: block;
}

.membership dl.error dd input {
  border-color: #ff223c;
}

.membership dl.error dd span {
  position: absolute;
  display: block;
  right: 10px;
  width: 12px;
  height: 10px;
  top: 20px;
  cursor: pointer;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-image: url(data:image/svg+xml;charset=utf-8;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCAzMCAzMCcgPjxwYXRoIGZpbGw9JyNmZjIyM2MnIGQ9J20xNy44NjMgMTQuOTg0IDExLjUxOCAxMS41MThjMC43OTUgMC43OTUgMC43OTUgMi4wODQgMCAyLjg3OXMtMi4wODQgMC43OTUtMi44NzkgMGwtMTEuNTE4LTExLjUxOC0xMS41MTggMTEuNTE4Yy0wLjc5NSAwLjc5NS0yLjA4NCAwLjc5NS0yLjg3OSAwcy0wLjc5NS0yLjA4NCAwLTIuODc5bDExLjUxOC0xMS41MTgtMTEuNTE4LTExLjUxOGMtMC43OTUtMC43OTUtMC43OTUtMi4wODQgMC0yLjg3OXMyLjA4NC0wLjc5NSAyLjg3OSAwbDExLjUxOCAxMS41MTggMTEuNTE4LTExLjUxOGMwLjc5NS0wLjc5NSAyLjA4NC0wLjc5NSAyLjg3OSAwczAuNzk1IDIuMDg0IDAgMi44NzlsLTExLjUxOCAxMS41MTh6Jy8+PC9zdmc+);
}

/* 생년월일 */
.membership dl.birth dd {
  height: 50px;
  border: none;
  vertical-align: top;
}

.membership dl.birth dd input {
  display: inline-block;
  width: 31%;
  float: left;
}

.membership dl.birth dd div {
  margin-left: 2.5%;
}

/* role 선택버튼 */
.membership dl.radio_select div label.on span {
  color: #068148;
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
  margin: 0px;
  padding: 0;
  display: block;
  border: 1px solid #bbb;
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
  margin: -12px 0 0 0;
  padding: 0;
}

.membership dl.radio_select label.on {
  border-color: #089e58;
  /* background-color: #ff0000e3; */
  color: #f8182b;
  z-index: 3;
}

.membership dl.radio_select dd {
  height: 50px;
}

.membership dl {
  position: relative;
  margin-top: 20px;
  clear: both;
}

.membership dl dt {
  margin-bottom: 8px;
  font-size: 14px;
  color: #3d3d3d;
  font-weight: bold;
}

.membership dl dd {
  position: relative;
  font-size: 0;
}

.membership dl dd input {
  width: 100%;
  padding: 15px 0;
  font-size: 14px;
  border: 1px solid #bbb;
  border-radius: 4px;
  display: block;
  box-sizing: border-box;
  outline: None;
  text-indent: 15px;
}

.btn_wrap {
  text-align: center;
  margin: 60px 0 0;
}

.btn_wrap a {
  display: inline-block;
  font-size: 16px;
  padding: 0;
  height: 50px;
  min-width: 49%;
  background-color: rgb(79, 168, 27);
  color: white;
  text-decoration: none;
  border-radius: 5px;
  font-weight: bold;
  line-height: 50px;
  margin-left: 5px;
}

.btn_wrap a.gray {
  background-color: #c8c8c8;
}

.btn_wrap a:first-child {
  margin: 0;
}

.btn_wrap a:hover {
  background-color: rgb(66, 161, 103);
}

/* 모달 디자인 / 학교  */

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
