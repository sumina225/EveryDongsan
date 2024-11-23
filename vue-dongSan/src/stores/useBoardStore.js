import { defineStore } from "pinia";
import axios from "@/axios";

export const useBoardStore = defineStore("post", {
    state: () => ({
        posts: [], // 게시물 리스트
        currentPage: 1, // 현재 페이지
        postsPerPage: 20, // 한 페이지에 보여줄 게시물 수
        totalPosts: 0, // 전체 게시물 수
        selectedPost: null, // 선택된 게시물 (모달용)
        loading: false, // 로딩 상태
    }),
    getters: {
        totalPages(state) {
            // 전체 페이지 수 계산
            return Math.ceil(state.totalPosts / state.postsPerPage);
        },
        isFirstPage(state) {
            return state.currentPage === 1; // 첫 페이지인지 확인
        },
        isLastPage(state) {
            return state.currentPage === state.totalPages; // 마지막 페이지인지 확인
        },
    },
    actions: {
        // 게시물 가져오기
        async fetchPosts() {
            this.loading = true;
            try {
                const response = await axios.get(`/board/listAll`, {
                    params: {
                        page: this.currentPage,
                        size: this.postsPerPage,
                    },
                });

                console.log("응답 데이터:", response.data); // 데이터 확인

                this.posts = response.data; // 응답 데이터를 바로 사용
            } catch (error) {
                console.error("게시물 로드 실패:", error);
                this.posts = [];
            } finally {
                this.loading = false;
            }


        },
        // 페이지 변경
        async changePage(page) {
            if (page < 1 || page > this.totalPages) return; // 유효하지 않은 페이지는 무시
            this.currentPage = page; // 현재 페이지 설정
            await this.fetchPosts(); // 새 페이지 게시물 가져오기
        },
        // 모달용 게시물 선택
        selectPost(post) {
            this.selectedPost = post;
        },
        // 모달 닫기
        closeModal() {
            this.selectedPost = null;
        },
    },
});
