import { defineStore } from "pinia";
import axios from "axios";
import Papa from "papaparse";

export const usePostStore = defineStore("postStore", {
  state: () => ({
    posts: [], // 게시물 데이터
    loading: false, // 로딩 상태
  }),
  actions: {
    async loadPosts() {
      this.loading = true;
      try {
        const response = await axios.get("../assets/dummy_posts.csv");
        const parsedData = Papa.parse(response.data, { header: true });
        this.posts = parsedData.data; // CSV 데이터를 JSON으로 변환 후 저장
      } catch (error) {
        console.error("CSV 파일 로드 오류:", error);
      } finally {
        this.loading = false;
      }
    },
  },
});
