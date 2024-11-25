<template>
  <div v-if="property" class="modal">
    <h3>{{ property.name }}</h3>
    <p>가격: {{ property.price }}</p>
    <p>면적: {{ property.area }}m²</p>
    <p>카테고리: {{ property.category }}</p>
    <button @click="$emit('close')">닫기</button>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useMapSearchStore } from "@/stores/MapSearchStore";

const mapSearchStore = useMapSearchStore();
const property = computed(() => mapSearchStore.selectedProperty);
</script>

<style scoped>
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  z-index: 1000;
}
</style>
