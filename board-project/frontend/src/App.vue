<template>
  <div class="app">
    <header class="app-header">
      <router-link to="/" class="logo">📋 게시판</router-link>
    </header>
    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
</script>

<style>
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Malgun Gothic', sans-serif;
  background-color: #f5f6f8;
  color: #222;
}

.app-header {
  background-color: #2c3e50;
  padding: 16px 24px;
}

.logo {
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  text-decoration: none;
}

.app-main {
  max-width: 900px;
  margin: 32px auto;
  padding: 0 16px;
}

button {
  cursor: pointer;
  font-family: inherit;
}

table {
  border-collapse: collapse;
  width: 100%;
}

input, textarea {
  font-family: inherit;
}
</style>
