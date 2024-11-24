import { defineStore } from "pinia";
import { useBoardStore } from "@/stores/useBoardStore";

export const useTabStore = defineStore("tab", {
    state: () => ({
        selectedTab: "all", // 현재 선택된 탭 (전체, 부동산, 학생)
    }),
    actions: {
        async setTab(tab) {
            this.selectedTab = tab; // 상태 업데이트
            const boardStore = useBoardStore(); // 게시물 관리 스토어 사용

            // 탭에 따라 게시물 요청
            if (tab === "all") {
                await boardStore.changeRole("all"); // 전체 게시물 요청
            } else if (tab === "rental") {
                await boardStore.changeRole("부동산"); // 부동산 게시물 요청
            } else if (tab === "student") {
                await boardStore.changeRole("학생"); // 학생 게시물 요청
            }
        },
    },
});
