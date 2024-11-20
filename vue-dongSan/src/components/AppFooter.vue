<template>
  <div>
    <footer :class="{ 'fixed-footer': isAtBottom }" class="footer" ref="footer">
      <p>&copy; 2024 Everydongsan. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";

const isAtBottom = ref(false);
const footer = ref(null);

onMounted(() => {
  const observer = new IntersectionObserver(
    ([entry]) => {
      isAtBottom.value = entry.isIntersecting;
    },
    {
      threshold: 0.1, // 10%가 보일 때 고정 시작
    }
  );

  if (footer.value) {
    observer.observe(footer.value);
  }

  onBeforeUnmount(() => {
    if (footer.value) {
      observer.disconnect();
    }
  });
});
</script>

<style scoped>
.content {
  min-height: 100vh;
  padding-bottom: 60px; /* footer 높이만큼 패딩 추가 */
}

.footer {
  position: relative;
  text-align: center;
  padding: 30px;
  background-color: #333;
  color: #fff;
  transition: transform 0.3s;
}

.fixed-footer {
  position: relative;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 10;
}
</style>
