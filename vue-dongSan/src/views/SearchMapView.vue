<template>
  <searchBar></searchBar>
</template>

<script setup>
import { ref } from "vue";
import searchBar from "../components/MapSearchBar.vue"
</script>

<style scope>
#map {
  position: relative;
  font-size: 14px;
  line-height: 1.2;
  margin: 0;
  padding: 0;
}
</style>
