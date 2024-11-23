<template>
  <div class="card">
    <img :src="post.image" :alt="post.title" />
    <h4>{{ post.title }}</h4>
    <p>{{ post.description }}</p>
  </div>
</template>

<script setup>
defineProps({
  post: {
    type: Object,
    required: true,
  },
});
</script>

<style scoped>
.card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s;
}

.card img {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.card h4 {
  margin-top: 8px;
  font-size: 1.1rem;
  color: #333;
}

.card p {
  margin-top: 4px;
  font-size: 0.9rem;
  color: #666;
}

.card:hover {
  transform: scale(1.05);
}
</style>
