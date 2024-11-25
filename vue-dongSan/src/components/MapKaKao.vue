<template>
  <div id="map"></div>
</template>


<script setup>
import { onMounted, watch } from "vue";
import { useMapSearchStore } from "@/stores/MapSearchStore";

const mapSearchStore = useMapSearchStore();
let map,
  markers = [];

// 지도 초기화
onMounted(() => {
  const mapContainer = document.getElementById("map");

  if (!mapContainer) {
    console.error("#map 요소가 렌더링되지 않았습니다.");
    return;
  }

  map = new kakao.maps.Map(mapContainer, {
    center: new kakao.maps.LatLng(37.5665, 126.978), // 초기 중심 좌표
    level: 3, // 확대 수준
  });

  console.log("지도 초기화 완료");
});

// 마커 업데이트
watch(
  () => mapSearchStore.properties,
  (newProperties) => {
    // 기존 마커 제거
    markers.forEach((marker) => marker.setMap(null));
    markers = [];

    // 새로운 마커 추가
    newProperties.forEach((property) => {
      const marker = new kakao.maps.Marker({
        map,
        position: new kakao.maps.LatLng(property.latitude, property.longitude),
      });
      kakao.maps.event.addListener(marker, "click", () => {
        mapSearchStore.selectProperty(property); // 선택된 매물 설정
      });
      markers.push(marker);
    });

    // 지도 중심 이동
    if (newProperties.length > 0) {
      map.setCenter(
        new kakao.maps.LatLng(
          newProperties[0].latitude,
          newProperties[0].longitude
        )
      );
    }
  },
  { deep: true }
);

// 지도 중심 이동 감시
watch(
  () => mapSearchStore.mapCenter,
  (newCenter) => {
    if (newCenter && newCenter.latitude && newCenter.longitude) {
      map.setCenter(
        new kakao.maps.LatLng(newCenter.latitude, newCenter.longitude)
      );
    }
  }
);
</script>

<style scoped>
#map {
  width: 100%;
  height: 100%;
}
</style>
