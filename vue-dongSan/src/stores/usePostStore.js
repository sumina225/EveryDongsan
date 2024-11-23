// stores/usePostStore.js
import { defineStore } from "pinia";
import axios from "@/axios";

export const usePostStore = defineStore("post", {
    state: () => ({
        posts: [], // 게시물 리스트
        loading: false, // 로딩 상태
    }),

    actions: {
        // 게시물 등록
        async submitPost(postData) {
            this.loading = true;
            try {
                const token = localStorage.getItem("token"); // 저장된 JWT 토큰
                if (!token) {
                    throw new Error("로그인이 필요합니다.");
                }
                console.log("여기까지왔어요2")
                // API 요청
                const response = await axios.post("/board/posts", postData, {
                    headers: {
                        Authorization: `Bearer ${token}`, // 토큰 포함
                    },
                });

                // 성공적으로 등록된 게시물을 상태에 추가
                this.posts.push(response.data);
                return { success: true, message: "게시물이 성공적으로 등록되었습니다." };
            } catch (error) {
                return {
                    success: false,
                    message: error.response?.data?.message || "등록 중 문제가 발생했습니다.",
                };
            } finally {
                this.loading = false;
            }
        },
    },
});
