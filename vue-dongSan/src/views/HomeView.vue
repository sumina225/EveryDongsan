<template>
  <div class="home-container">
    <MainBanner />
    <div class="main-content">
      <MainSideBar />
      <div class="content">
        <SlidingBanner />
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue"; // Vue 3에서 정의하는 방식
import MainSideBar from "@/components/MainSideBar.vue";
import MainBanner from "../components/MainBanner.vue";
import PostList from "../components/PostList.vue";
import SlidingBanner from "@/components/SlidingBanner.vue";

export default defineComponent({
  name: "Home",
  components: {
    MainBanner,
    MainSideBar,
    PostList,
    SlidingBanner,
  },
});
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
}

.main-content {
  display: flex;
  flex-grow: 1;
}

.main-content .content {
  margin-left: 300px;
  display: flex;
  flex-direction: column;
  width: 100%;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
}

.main-content .content .post-list {
  margin: 20px 0;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  box-sizing: border-box;
}

.main-content .content .post-list h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 10px 0;
  position: relative;
  bottom: 0;
  width: 100%;
}

.MainSidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 250px;
  height: 100vh;
  background-color: #f4f4f4;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  z-index: 10;
}

/* @media (min-width: 768px) {
  .main-content .content {
    min-height: 500px;
  } */
</style>