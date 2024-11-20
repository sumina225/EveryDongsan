<template>
  <header class="header">
    <div class="header-logo">Everydongsan</div>
    <nav>
      <ul>
        <li><a href="#">홈</a></li>
        <li><a href="#">게시판</a></li>
        <li><a href="#">공지사항</a></li>
        <li><a href="#">로그인</a></li>
      </ul>
    </nav>
  </header>
</template>

<script setup>
// setup 구문 사용
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f5f5f5;
}
.header-logo {
  font-size: 24px;
  font-weight: bold;
}
nav ul {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}
nav ul li {
  margin-left: 20px;
}
nav ul li a {
  text-decoration: none;
  color: #333;
  transition: color 0.3s;
}
nav ul li a:hover {
  color: #f91f15; /* 링크 호버 시 색상 변경 */
}
</style>
