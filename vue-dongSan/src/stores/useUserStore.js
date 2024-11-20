import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    emailToIdMap: {
      "test@example.com": "testuser", // 하드코딩된 이메일-아이디 매핑
      "hello@example.com": "helloworld", // 다른 예시
      "ssafy@naver.com": "ssafy",
    },
    foundId: "",
    errorMsg: "", // 아이디 찾기 오류 메시지
    errorMsg2: "", // 비밀번호 찾기 오류 메시지
    foundEmail: "",
  }),
  actions: {
    // 이메일을 받아서 매칭되는 아이디를 찾는 함수
    findIdByEmail(email) {
      this.foundId = this.emailToIdMap[email] || ""; // 이메일에 해당하는 아이디 찾기
      this.errorMsg = this.foundId
        ? "" // 아이디가 존재하면 오류 메시지 비우기
        : "이메일에 매칭되는 아이디가 없습니다."; // 아이디가 없으면 오류 메시지
    },

    // 아이디를 받아서 해당 이메일을 찾는 함수
    findPwById(userId) {
      // 아이디에 해당하는 이메일을 찾기
      const email = Object.keys(this.emailToIdMap).find(
        (key) => this.emailToIdMap[key] === userId
      );

      if (email) {
        // 아이디에 해당하는 이메일이 존재하면 이메일을 찾음
        this.foundEmail = email;
        this.errorMsg2 = ""; // 오류 메시지 초기화
        console.log(`비밀번호를 ${this.foundEmail}로 전송했습니다.`);
      } else {
        // 아이디가 존재하지 않으면 오류 메시지
        this.foundEmail = "";
        this.errorMsg2 = "존재하는 아이디가 아닙니다.";
      }
    },
  },
});
