<template>
  <div class="section">
    <div class="section-content">
      <h2>{{ title }}</h2>
      <slot></slot>
      <!-- 섹션 내용은 slot을 통해 전달 -->
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
});
</script>

<style scoped>
.section {
  margin: 20px 0;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.section-content {
  padding: 10px;
}

.section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}
</style>
