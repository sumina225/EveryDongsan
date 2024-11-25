import { defineStore } from "pinia";
import axios from "@/axios";
import Swal from "sweetalert2"; // swal 라이브러리 임포트

export const useMapSearchStore = defineStore("MapSearchStore", {
  state: () => ({
    schoolList: [], // 모든 학교 데이터
    properties: [], // 현재 지도에 표시할 매물 데이터
    selectedProperty: null, // 선택된 매물
    mapCenter: { latitude: 37.5665, longitude: 126.978 }, // 지도 중심 좌표 초기값
  }),
  actions: {
    loadSchools() {
      try {
        this.schoolList = schools; // 외부 데이터에서 학교 목록 불러오기
        console.log("학교 데이터 로드 완료:", this.schoolList);
      } catch (error) {
        console.error("학교 데이터를 불러오는 중 오류 발생:", error);
      }
    },
    async searchBySchool(schoolName) {
      try {
        const response = await axios.get("/map/searchBySchool", {
          params: { school: schoolName },
        });
        this.properties = response.data; // 매물 데이터 저장

        if (this.properties.length > 0) {
          // 매물이 있으면 지도 중심 좌표 업데이트
          const firstProperty = this.properties[0];
          this.mapCenter = {
            latitude: firstProperty.latitude,
            longitude: firstProperty.longitude,
          };
        } else {
          Swal.fire({
            icon: "info",
            title: "등록된 매물이 없습니다",
            text: `${schoolName} 근처에 등록된 매물이 없습니다.`,
          });
        }
      } catch (error) {
        console.error("학교 매물 검색 실패:", error);
        Swal.fire({
          icon: "error",
          title: "오류 발생",
          text: "학교 매물 데이터를 불러오는 중 문제가 발생했습니다.",
        });
      }
    },
  },
});
