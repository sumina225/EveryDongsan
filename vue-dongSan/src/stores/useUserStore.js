import { defineStore } from "pinia";
import apiClient from "@/axios";
import Swal from "sweetalert2";

export const useUserStore = defineStore("userStore", {
  state: () => ({
    user: null, // 사용자 정보
    token: localStorage.getItem("token") || null, // 저장된 JWT 토큰
    isLoggedIn: !!localStorage.getItem("token"), // 로그인 상태
  }),
  actions: {
    // 로그인
    async login(credentials) {
      try {
        console.log("로그인 요청 데이터:", credentials);

        const response = await apiClient.post("/member/login", credentials);
        console.log("서버 응답:", response);

        const token = response.data;
        if (!token) {
          throw new Error("서버에서 토큰을 받지 못했습니다.");
        }

        this.token = token;
        this.isLoggedIn = true;
        localStorage.setItem("token", token);

        // 사용자 정보 가져오기
        await this.fetchUserProfile();

        console.log("로그인 성공");
        return true;
      } catch (error) {
        console.error(
          "로그인 요청 중 오류 발생:",
          error.response?.data || error.message
        );
        this.isLoggedIn = false;
        return false;
      }
    },

    // 사용자 프로필 가져오기
    async fetchUserProfile() {
      if (!this.token) {
        console.error("토큰이 없습니다.");
        return;
      }

      try {
        const response = await apiClient.get("/member/profile", {
          headers: { Authorization: `Bearer ${this.token}` },
        });

        this.user = response.data;
        localStorage.setItem("user", JSON.stringify(this.user));
        console.log("사용자 정보 가져오기 성공:", this.user);
      } catch (error) {
        console.error(
          "사용자 정보 가져오기 실패:",
          error.response?.data || error.message
        );
        this.logout(); // 실패하면 로그아웃 처리
      }
    },

    // 로그아웃
    async logout(router) {
      // 토큰이 없으면 로그아웃 요청을 하지 않음
      if (!this.token) {
        console.warn("로그아웃 요청 전에 토큰이 없습니다.");
        return;
      }

      try {
        const response = await apiClient.post(
          "/member/logout",
          {},
          {
            headers: { Authorization: `Bearer ${this.token}` },
          }
        );

        // 백엔드 응답 메시지 출력
        console.log("로그아웃 응답:", response.data);

        // Swal로 변경
        Swal.fire({
          icon: "info", // 적절한 아이콘 선택 (정보 표시용)
          title: "로그아웃",
          text: response.data, // 백엔드에서 받은 메시지 출력
          confirmButtonText: "확인",
        }).then(() => {
          // 추가적인 동작이 필요한 경우 여기에 작성
          // 예: 로그아웃 후 페이지 리다이렉트
          window.location.href = "/"; // 메인 페이지로 이동
        });
      } catch (error) {
        console.error(
          "백엔드 로그아웃 실패:",
          error.response?.data || error.message
        );
        alert("로그아웃 처리 중 문제가 발생했습니다.");
      } finally {
        // 상태 초기화 및 페이지 이동
        this.user = null;
        this.token = null;
        this.isLoggedIn = false;
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        if (router) {
          router.push("/"); // 메인 페이지로 이동
        }
        console.log("로그아웃 완료 및 메인 페이지 이동");
      }
    },

    // 토큰 유효성 검사 및 상태 복원
    async checkToken() {
      const token = localStorage.getItem("token");
      if (token) {
        this.token = token;
        this.isLoggedIn = true;
        await this.fetchUserProfile(); // 토큰 유효 시 프로필 가져오기
      } else {
        this.logout();
      }
    },
  },
});
