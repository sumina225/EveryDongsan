<script setup>
    import { useRouter } from "vue-router";
    import "swiper/swiper-bundle.css";
    import { Swiper, SwiperSlide } from "swiper/vue";
    import "swiper/css/navigation"; // Navigation 스타일
    import { Navigation } from "swiper/modules"; // Navigation 모듈

    const router = useRouter();

    const goToPage = (path) => {
    router.push(path);
    };
</script>

<template>
    <div class="sliding-banner">
    <swiper :modules="[Navigation]"
    navigation :autoplay="{ delay: 3000 }" loop>
      <swiper-slide @click="goToPage('/searchMap')">
        <img src="../assets/mapimg.png" alt="지도 페이지로 이동" />
        <div class="banner-text">
          <h3>지도 페이지</h3>
          <p>여기를 클릭하면 지도 페이지로 이동합니다.</p>
        </div>
      </swiper-slide>
      <swiper-slide @click="goToPage('/board')">
        <img src="../assets/boardimg.png" alt="게시판 페이지로 이동" />
        <div class="banner-text">
          <h3>게시판 페이지</h3>
          <p>여기를 클릭하면 게시판 페이지로 이동합니다.</p>
        </div>
      </swiper-slide>
    </swiper>
  </div>
</template>

<style scoped>
.sliding-banner {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

.sliding-banner img {
  width: 100%;
  height: 90vh;
  /* margin:20px; */
  padding: 20px;
  object-fit: cover;
  cursor: pointer;
}

.banner-text {
  position: absolute;
  bottom: 20px;
  left: 20px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 10px 20px;
  border-radius: 8px;
}

.banner-text h3 {
  margin: 0;
  font-size: 20px;
}

.banner-text p {
  margin: 5px 0 0;
  font-size: 14px;
}

/* 화살표 버튼 공통 스타일 */
.swiper-button-next,
.swiper-button-prev {
  color: white; /* 화살표 색상 */
  background-color: rgba(0, 0, 0, 0.5); /* 버튼 배경 */
  border-radius: 50%; /* 원형 버튼 */
  width: 50px; /* 버튼 크기 */
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  transition: all 0.3s ease; /* 부드러운 애니메이션 */
}

.swiper-button-next:hover,
.swiper-button-prev:hover {
  background-color: rgba(255, 255, 255, 0.8); /* 호버 시 배경색 변경 */
  color: black; /* 화살표 색상 변경 */
  transform: scale(1.1); /* 호버 시 크기 확대 */
}

/* 화살표 아이콘 크기 조정 */
.swiper-button-next::after,
.swiper-button-prev::after {
  font-size: 20px;
  font-weight: bold;
}

/* 버튼 위치 조정 */
.swiper-button-prev {
  left: 10px; /* 왼쪽 버튼 위치 */
}

.swiper-button-next {
  right: 10px; /* 오른쪽 버튼 위치 */
}
</style>