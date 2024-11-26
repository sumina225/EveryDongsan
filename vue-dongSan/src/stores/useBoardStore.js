import { defineStore } from "pinia";
import axios from "@/axios";
import Swal from "sweetalert2";

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

        if (role === "학생") {
          url = "/board/studentListAll";
        } else if (role === "부동산") {
          url = "/board/rentalListAll";
        } else if (role === "내목록") {
          url = "/board/myList";
        }

        const response = await axios.get(url, {
          params: {
            page: this.currentPage,
            size: this.postsPerPage,
          },
        });

        console.log("응답 데이터:", response.data);

        if (response.data.length === 0) {
          Swal.fire({
            title: "알림",
            text: "마지막 페이지입니다.",
            icon: "info",
            confirmButtonText: "확인",
          });
          if (this.currentPage > 1) {
            this.currentPage--;
          }
          return;
        }

        this.posts = response.data; // 게시물 리스트
        this.currentRole = role;
      } catch (error) {
        console.error("게시물 로드 실패:", error);
        Swal.fire({
          title: "오류",
          text: "게시물을 가져오는 중 오류가 발생했습니다.",
          icon: "error",
          confirmButtonText: "확인",
        });
      } finally {
        this.loading = false;
      }
    },

    // 역할 변경
    async changeRole(role) {
      this.currentPage = 1;
      this.posts = [];
      await this.fetchPosts(role);
    },

    // 페이지 변경
    async changePage(page) {
      if (page < 1) return;
      this.currentPage = page;
      await this.fetchPosts(this.currentRole);
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

        if (response.status === 200) {
          // 로컬 상태에서 게시물 제거
          this.posts = this.posts.filter((post) => post.articleNo !== articleNo);

          // 성공 알림
          Swal.fire({
            title: "삭제 완료",
            text: "게시물이 성공적으로 삭제되었습니다.",
            icon: "success",
            confirmButtonText: "확인",
          });
        } else {
          console.error("삭제 실패:", response.data.message);
          Swal.fire({
            title: "오류",
            text: "게시물 삭제에 실패했습니다. 다시 시도해주세요.",
            icon: "error",
            confirmButtonText: "확인",
          });
        }
      } catch (error) {
        console.error("게시물 삭제 중 오류 발생:", error);
        Swal.fire({
          title: "오류",
          text: "게시물 삭제 중 오류가 발생했습니다.",
          icon: "error",
          confirmButtonText: "확인",
        });
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
          params.content = query;
        } else if (type === "school") {
          url = "/board/searchBySchool";
          params.school = query;
        }

        const response = await axios.get(url, { params });
        console.log("검색 결과:", response.data);

        if (response.data.length === 0) {
          Swal.fire({
            title: "검색 결과 없음",
            text: "검색 결과가 없습니다.",
            icon: "info",
            confirmButtonText: "확인",
          });
          return false;
        }

        this.posts = response.data; // 검색 결과 업데이트
        return true;
      } catch (error) {
        console.error("검색 실패:", error);
        Swal.fire({
          title: "오류",
          text: "검색 중 오류가 발생했습니다.",
          icon: "error",
          confirmButtonText: "확인",
        });
        return false;
      }
    },
  },
});
