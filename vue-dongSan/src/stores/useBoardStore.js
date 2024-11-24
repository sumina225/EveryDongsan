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
        currentRole: "all", // 현재 역할 ("all", "학생", "부동산")
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
                    url = "/board/myList"
                }

                // API 요청
                const response = await axios.get(url, {
                    params: {
                        page: this.currentPage,
                        size: this.postsPerPage,
                    },
                });

                console.log("응답 데이터:", response.data);

                // 상태 업데이트
                this.posts = response.data || [];
                this.totalPosts = response.data.totalElements || 0;
                this.currentRole = role;
            } catch (error) {
                console.error("게시물 로드 실패:", error);
                this.posts = [];
                this.totalPosts = 0;
            } finally {
                this.loading = false;
            }
        },

        // 역할 변경
        async changeRole(role) {
            this.currentPage = 1; // 역할 변경 시 페이지를 초기화
            await this.fetchPosts(role); // 새로운 역할에 맞는 게시물 요청
        },

        // 페이지 변경
        async changePage(page) {
            if (page < 1 || page > this.totalPages) return; // 유효하지 않은 페이지는 무시
            this.currentPage = page; // 현재 페이지 설정
            await this.fetchPosts(this.currentRole); // 현재 역할에 따라 게시물 가져오기
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
                // 삭제 API 요청
                const response = await axios.delete(`/board/${articleNo}/delete`);
                console.log("삭제 응답:", response.data);

                if (response.status === 200 && response.data) {
                    // 게시물 삭제 성공
                    // 현재 페이지의 게시물 목록 갱신
                    await this.fetchPosts("내목록");
                    return true;
                } else {
                    console.error("삭제 실패:", response.data.message);
                    return false;
                }
            } catch (error) {
                console.error("게시물 삭제 중 오류 발생:", error);
                return false;
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

                // 검색 타입에 따라 API 경로와 파라미터 설정
                if (type === "title") {
                    url = "/board/search";
                    params.content = query; // 제목 검색
                } else if (type === "school") {
                    url = "/board/searchBySchool";
                    params.school = query; // 학교 검색
                }

                // API 호출
                const response = await axios.get(url, { params });
                console.log("검색 결과:", response.data);

                // 상태 업데이트
                this.posts = response.data || [];
                this.totalPosts = response.data.totalElements || 0;

                // 검색 결과가 없을 경우 false 반환
                if (this.posts.length === 0) {
                    return false; // 검색 결과 없음
                }

                return true; // 검색 결과 있음
            } catch (error) {
                console.error("검색 실패:", error);
                this.posts = [];
                this.totalPosts = 0;
                return false;
            }
        },
    },
});
