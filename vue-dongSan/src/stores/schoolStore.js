// src/stores/schoolStore.js
import { defineStore } from "pinia";
import { schools } from "../assets/SchoolList";

export const useSchoolStore = defineStore("schoolStore", {
  state: () => ({
    schoolList: [], // 학교 목록 저장
  }),
  actions: {
    // 학교 목록 로드
    loadSchools() {
      this.schoolList = schools; // 데이터 파일에서 불러오기
    },
  },
});
