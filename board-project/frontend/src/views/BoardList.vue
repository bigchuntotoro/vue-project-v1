<template>
  <div class="board-list">
    <div class="toolbar">
      <select v-model="searchType">
        <option value="">전체</option>
        <option value="title">제목</option>
        <option value="writer">작성자</option>
        <option value="content">내용</option>
      </select>
      <input
        v-model="keyword"
        type="text"
        placeholder="검색어를 입력하세요"
        @keyup.enter="search"
      />
      <button class="btn btn-search" @click="search">검색</button>
      <router-link to="/write" class="btn btn-primary">글쓰기</router-link>
    </div>

    <table class="board-table">
      <thead>
        <tr>
          <th style="width: 70px">번호</th>
          <th>제목</th>
          <th style="width: 120px">작성자</th>
          <th style="width: 160px">작성일</th>
          <th style="width: 70px">조회수</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="items.length === 0">
          <td colspan="5" class="empty">게시글이 없습니다.</td>
        </tr>
        <tr v-for="item in items" :key="item.id" @click="goDetail(item.id)">
          <td>{{ item.id }}</td>
          <td class="title-cell">{{ item.title }}</td>
          <td>{{ item.writer }}</td>
          <td>{{ formatDate(item.createdDate) }}</td>
          <td>{{ item.viewCount }}</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination" v-if="totalPages > 1">
      <button :disabled="page === 1" @click="changePage(page - 1)">이전</button>
      <button
        v-for="p in totalPages"
        :key="p"
        :class="{ active: p === page }"
        @click="changePage(p)"
      >{{ p }}</button>
      <button :disabled="page === totalPages" @click="changePage(page + 1)">다음</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import boardApi from '../api/board'

const router = useRouter()

const items = ref([])
const page = ref(1)
const size = ref(10)
const totalPages = ref(0)
const searchType = ref('')
const keyword = ref('')

const fetchList = async () => {
  const { data } = await boardApi.getList({
    page: page.value,
    size: size.value,
    searchType: searchType.value,
    keyword: keyword.value
  })
  items.value = data.content
  totalPages.value = data.totalPages
}

const search = () => {
  page.value = 1
  fetchList()
}

const changePage = (p) => {
  page.value = p
  fetchList()
}

const goDetail = (id) => {
  router.push(`/board/${id}`)
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.substring(0, 10)
}

onMounted(fetchList)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  align-items: center;
}

.toolbar input {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.toolbar select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  text-decoration: none;
  font-size: 14px;
  display: inline-block;
}

.btn-search {
  background: #eee;
}

.btn-primary {
  background: #2c3e50;
  color: #fff;
}

.board-table {
  background: #fff;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.board-table th {
  background: #f0f1f3;
  padding: 12px;
  text-align: left;
  font-size: 14px;
  border-bottom: 1px solid #e2e2e2;
}

.board-table td {
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.board-table tbody tr:hover {
  background: #f9fafb;
  cursor: pointer;
}

.title-cell {
  font-weight: 500;
}

.empty {
  text-align: center;
  color: #999;
  padding: 40px 0;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 6px;
  margin-top: 20px;
}

.pagination button {
  padding: 6px 12px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
}

.pagination button.active {
  background: #2c3e50;
  color: #fff;
  border-color: #2c3e50;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: default;
}
</style>
