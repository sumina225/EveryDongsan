// stores/useImageStore.js
import { defineStore } from "pinia";

export const useImageStore = defineStore("imageStore", {
    state: () => ({
        images: [], // assets/room의 이미지 배열
        imageMap: {}, // articleNo 별 이미지 매핑
    }),
    actions: {
        // assets/room의 이미지를 초기화
        initializeImages() {
            const imageModules = import.meta.glob('@/assets/room/*.png', { eager: true });
            this.images = Object.keys(imageModules).map((key) => imageModules[key].default);
        },

        // 게시물에 이미지를 매핑
        assignImageToPost(post) {
            // 이미 매핑된 이미지가 있으면 반환
            if (this.imageMap[post.articleNo]) {
                return this.imageMap[post.articleNo];
            }

            // 매핑되지 않은 경우 새로운 이미지 할당
            const index = Object.keys(this.imageMap).length % this.images.length;
            this.imageMap[post.articleNo] = this.images[index];
            return this.imageMap[post.articleNo];
        },
    },
});
