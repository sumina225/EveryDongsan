<template>
  <div class="header"></div>
  <div class="wrap">
    <div id="container">
      <div class="step step1">
        <h1 class="logo">
          <div @click="goHome">에브리동산</div>
        </h1>
        <div class="membership">
          <dl :class="{ ok: check === 1, error: check === 0 }">
            <dt>아이디</dt>
            <dd>
              <input
                v-model="uid"
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
                v-model="password1"
                type="password"
                maxlength="16"
                placeholder=""
                autocomplete="off"
                title="비밀번호"
              />
              <span></span>
            </dd>
            <p>{{ msgBox }}</p>
          </dl>
          <dl :class="{ ok: check2 === 1, error: check2 === 0 }">
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
            </dd>
            <span></span>
            <p>{{ msgBox }}</p>
          </dl>
          <dl>
            <dt>이름</dt>
            <dd>
              <input
                v-model="userName"
                type="text"
                maxlength="25"
                placeholder=""
                autocomplete="off"
                title="이름"
              />
            </dd>
          </dl>
          <dl class="birth">
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
            </dd>
          </dl>
          <dl class="radio_select role">
            <dt>역할</dt>
            <dd>
              <div>
                <input
                  type="radio"
                  id="Student"
                  name="role"
                  value="Student"
                  v-model="selectedRole"
                /><label
                  for="Student"
                  :class="{ on: selectedRole === 'Student' }"
                  ><span><em></em>학생</span></label
                >
              </div>
              <div>
                <input
                  type="radio"
                  id="Rental"
                  name="role"
                  value="Rental"
                  v-model="selectedRole"
                /><label for="Rental" :class="{ on: selectedRole === 'Rental' }"
                  ><span><em></em>부동산</span></label
                >
              </div>
            </dd>
            <p></p>
          </dl>
          <dl class="email">
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
            </dd>
          </dl>
          <dl class="phone">
            <dt>휴대폰</dt>
            <dd>
              <input
                v-model="mobile"
                type="text"
                name="mobile"
                id="phone"
                autocomplete="off"
                placeholder="‘-’ 없이 입력"
              />
            </dd>
          </dl>
        </div>
        <div class="btn_wrap">
          <a href class="btn gray previous">이전</a>
          <a href="#" @click="nextStep" class="btn blue">다음</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const selectedRole = ref("");
const check = ref(-1); // 체크상태 초기값 -1
const check2 = ref(-1);
const msgBox = ref(""); // 메세지 박스
const uid = ref("");

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

// 특수문자 체크 (영문+숫자 허용)
const _is_hangle = (value) => {
  const allowedChars =
    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
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

    if (
      (ch < "0" || ch > "9") &&
      (ch < "A" || ch > "Z") &&
      (code > 255 || code < 0)
    ) {
      strbyte += 2; // 한글인 경우 2byte로 계산
    } else {
      strbyte += 1; // 숫자, 문자는 1byte로 계산
    }
  }

  return strbyte < min || strbyte > max;
};

// id 체크하는 함수들 ----------------------------------------------------------------
watch(uid, (newVal) => {
  uidCheck();
});

// 아이디 체크
const uidCheck = () => {
  msgBox.value = ""; // 메세지 초기화
  check.value = -1; // 초기화 상태

  // 값이 비어있는지 체크
  if (uid.value.length <= 0) {
    msgBox.value = "아이디를 입력해주세요.";
    check.value = 0;
    return;
  }
  // 한글체크
  if (_is_hangle(uid.value)) {
    msgBox.value = "한글/특수문자는 사용하실 수 없습니다.";
    check.value = 0; // 에러상태
    return;
  }

  // 아이디 길이 체크
  if (_valid_length_check(6, 12, uid.value)) {
    msgBox.value = "아이디의 길이는 6-12자 입니다.";
    check.value = 0; // 에러상태
    return;
  }
  //바이트 체크
  if (_valid_byte_check(6, 12, uid.value)) {
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
const password1 = ref("");
const password2 = ref("");

//비밀번호 체크

watch(password1, (newVal) => {
  pwdCheck();
});

watch(password2, (newVal) => {
  pwdCheck();
});

const pwdCheck = () => {
  const pwd = password1.value; // 비밀번호 입력값
  const pwdConfirm = password2.value; // 비밀번호 재입력값
  msgBox.value = ""; // 메세지 초기화
  check2.value = -1; // 초기화 상태

  // 비밀번호가 비어있는지 체크
  if (pwd.length <= 0) {
    msgBox.value = "비밀번호를 입력해주세요.";
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

  // 비밀번호와 비밀번호 확인 일치 체크
  if (pwd !== pwdConfirm) {
    isOk = false;
    errorMessages.push("비밀번호가 일치하지 않습니다.");
  }

  // 비밀번호 조건이 하나라도 오류가 있으면 오류 메시지 표시
  if (!isOk) {
    msgBox.value = errorMessages.join(" ");
    check2.value = 0; // 에러 상태
    return;
  }

  // 모든 조건을 만족하면
  check2.value = 1; // 성공 상태
  msgBox.value = "사용할 수 있는 비밀번호입니다.";
};

//-------------------------------- 비밀번호 체크 end --------------------------//

const userName = ref("");
const year = ref("");
const month = ref("");
const day = ref("");
const email = ref("");
const mobile = ref("");

// 다음 단계로 이동하는 함수
const nextStep = () => {
  // 현재 단계에서 부모로 'next-step' 이벤트를 발생시킴
  // 이를 통해 부모 컴포넌트에서 처리할 수 있도록 함
  console.log("다음 단계로 이동");
};

const goHome = () => {
  router.push("/");
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
  background-color: #f5f5f5; /* 배경 색 설정 */
}

header {
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
  max-width: 500px; /* 스텝 크기를 제한 */
  padding: 20px;
  background: #fafafa; /* 배경 색을 추가하면 더 보기 좋음 */
  /* box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); 그림자 추가 */
  margin: auto;
}

/* 로고 스타일 */
.logo {
  width: 100%;
  height: 50px;
  text-align: center;
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
}

.step h1 {
  font-size: 2rem;
  color: #2e6afd;
}

/* ok 처리 -------------------------------------------------------- */
.membership input:checked + label em,
.membership label.on em {
  background-color: #3d82f7;
  border: 1px solid #3d82f7;
}

.membership dl.ok dd input {
  border-color: #3d82f7;
}

.membership dl.ok dd span {
  position: absolute;
  display: block;
  right: 10px;
  width: 12px;
  height: 12px;
  top: 20px;
  cursor: pointer;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-image: url(data:image/svg+xml;charset=utf-8;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCAxNSAxMSc+PHBhdGggIGZpbGw9JyMyZTZhZmQnIGZpbGwtcnVsZT0nZXZlbm9kZCcgZD0nTTUuODIyIDEwLjUwMmwtNC42MS01LjMxMUMuODU1IDQuNzguOSA0LjE1OCAxLjMxMSAzLjgwMWMuNDEyLS4zNTYgMS4wMzQtLjMxMiAxLjM5MS4wOTlsMy4yNTcgMy43NTJMMTMuMTE0Ljg0OGMuMzk0LS4zNzYgMS4wMTktLjM1OSAxLjM5NC4wMzQuMzc1LjM5NC4zNiAxLjAxOC0uMDM1IDEuMzkzbC04LjY1MSA4LjIyN3onLz48L3N2Zz4=);
}

.membership dl.ok p {
  clear: both;
  display: block;
  margin-top: 7px;
  font-size: 12px;
  color: #3d82f7;
  line-height: 1.4;
}

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
  height: 12px;
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
  color: #fc0000;
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
  border-color: #fc0000;
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
  background-color: #ff0000e3;
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
  background-color: #df5668;
}
</style>
