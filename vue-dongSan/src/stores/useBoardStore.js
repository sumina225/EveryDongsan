import { defineStore } from "pinia";
import axios from "@/axios";

export const useBoardStore = defineStore("post", {
  state: () => ({
    posts: [], // 게시물 리스트
    currentPage: 1, // 현재 페이지
    postsPerPage: 20, // 한 페이지에 보여줄 게시물 수
    selectedPost: null, // 선택된 게시물 (모달용)
    loading: false, // 로딩 상태
    currentRole: "all", // 현재 역할 ("all", "학생", "부동산")
  }),
  getters: {
    isFirstPage(state) {
      return state.currentPage === 1; // 첫 페이지인지 확인
    },
  },
  actions: {
    // 게시물 가져오기
    async fetchPosts(role = "all") {
      this.loading = true;

      try {
        let url = "/board/listAll";

        // 역할에 따라 URL 변경
        if (role === "학생") {
          url = "/board/studentListAll";
        } else if (role === "부동산") {
          url = "/board/rentalListAll";
        } else if (role === "내목록") {
          url = "/board/myList";
        }

        // API 요청
        const response = await axios.get(url, {
          params: {
            page: this.currentPage,
            size: this.postsPerPage,
          },
        });

        console.log("응답 데이터:", response.data);

        // 데이터가 없으면 마지막 페이지임을 알림
        if (response.data.length === 0) {
          alert("마지막 페이지입니다.");
          if (this.currentPage > 1) {
            this.currentPage--; // 페이지 복구
          }
          return;
        }

        // 상태 업데이트
        this.posts = response.data; // 게시물 리스트
        this.currentRole = role;
      } catch (error) {
        console.error("게시물 로드 실패:", error);
        alert("게시물을 가져오는 중 오류가 발생했습니다.");
      } finally {
        this.loading = false;
      }
    },

    // 역할 변경
    async changeRole(role) {
      this.currentPage = 1; // 역할 변경 시 페이지를 초기화
      this.posts = []; // 게시물 초기화
      await this.fetchPosts(role); // 새로운 역할에 맞는 게시물 요청
    },

    // 페이지 변경
    async changePage(page) {
      if (page < 1) return; // 유효하지 않은 페이지는 무시
      this.currentPage = page; // 현재 페이지 설정
      await this.fetchPosts(this.currentRole); // 현재 역할에 따라 게시물 가져오기
    },

    // 다음 페이지 이동
    async nextPage() {
      this.currentPage++;
      await this.fetchPosts(this.currentRole);
    },

    // 이전 페이지 이동
    async prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        await this.fetchPosts(this.currentRole);
      }
    },

    // 모달용 게시물 선택
    selectPost(post) {
      this.selectedPost = post;
    },

    // 모달 닫기
    closeModal() {
      this.selectedPost = null;
    },

    // 게시물 삭제
    async deletePost(articleNo) {
      try {
        const response = await axios.delete(`/board/${articleNo}/delete`);
        console.log("삭제 응답:", response.data);

        if (response.status === 200 && response.data) {
          alert("게시물이 삭제되었습니다.");
          await this.fetchPosts(this.currentRole); // 현재 페이지 게시물 갱신
        } else {
          console.error("삭제 실패:", response.data.message);
        }
      } catch (error) {
        console.error("게시물 삭제 중 오류 발생:", error);
        alert("게시물 삭제 중 오류가 발생했습니다.");
      }
    },

    // 게시물 검색
    async searchPosts(query, type) {
      try {
        let url = "";
        const params = {
          page: this.currentPage,
          size: this.postsPerPage,
        };

        if (type === "title") {
          url = "/board/search";
          params.content = query; // 제목 검색
        } else if (type === "school") {
          url = "/board/searchBySchool";
          params.school = query; // 학교 검색
        }

        const response = await axios.get(url, { params });
        console.log("검색 결과:", response.data);

        if (response.data.length === 0) {
          alert("검색 결과가 없습니다.");
          return false;
        }

        this.posts = response.data; // 검색 결과 업데이트
        return true;
      } catch (error) {
        console.error("검색 실패:", error);
        alert("검색 중 오류가 발생했습니다.");
        return false;
      }
    },
  },
});
