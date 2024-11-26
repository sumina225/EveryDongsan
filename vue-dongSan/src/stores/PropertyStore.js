import { defineStore } from "pinia";
import axios from "@/axios";
import Swal from "sweetalert2";

export const usePropertyStore = defineStore("propertyStore", {
  state: () => ({
    propertyDetail: null, // 최종 상세 데이터 저장
  }),
  actions: {
    async fetchPropertyByNo(no) {
      try {
        // Step 1: searchMap/{no} 호출
        const searchResponse = await axios.get(`/map/searchBoard/${no}`);
        const articleNum = searchResponse.data; // articleNo 추출
        const num = articleNum[0].articleNo;

        if (!articleNum) {
          Swal.fire({
            icon: "error",
            title: "오류 발생",
            text: "해당 매물의 정보를 찾을 수 없습니다.",
          });
          return null;
        }

        // Step 2: board/{articleNo} 호출
        const detailResponse = await axios.get(`/board/${num}`);
        this.propertyDetail = detailResponse.data; // 상세 정보 저장

        return this.propertyDetail; // 최종 반환
      } catch (error) {
        console.error("매물 상세 정보 가져오기 실패:", error);
        Swal.fire({
          icon: "error",
          title: "오류 발생",
          text: "매물 상세 데이터를 불러오는 중 문제가 발생했습니다.",
        });
        return null; // 실패 시 null 반환
      }
    },
  },
});
