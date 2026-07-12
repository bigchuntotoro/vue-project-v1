<template>
  <div v-if="board" class="board-detail">
    <h2 class="title">{{ board.title }}</h2>
    <div class="meta">
      <span>작성자: {{ board.writer }}</span>
      <span>작성일: {{ formatDate(board.createdDate) }}</span>
      <span>조회수: {{ board.viewCount }}</span>
    </div>
    <div class="content">{{ board.content }}</div>
    <div class="actions">
      <button class="btn" @click="$router.push('/')">목록</button>
      <button class="btn" @click="$router.push(`/board/${board.id}/edit`)">수정</button>
      <button class="btn btn-danger" @click="remove">삭제</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import boardApi from '../api/board'

const props = defineProps({ id: [String, Number] })
const route = useRoute()
const router = useRouter()
const board = ref(null)

const fetchDetail = async () => {
  const { data } = await boardApi.getDetail(route.params.id)
  board.value = data
}

const remove = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return
  await boardApi.remove(board.value.id)
  router.push('/')
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.substring(0, 16).replace('T', ' ')
}

onMounted(fetchDetail)
</script>

<style scoped>
.board-detail {
  background: #fff;
  padding: 24px;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.title {
  margin: 0 0 12px;
  border-bottom: 2px solid #2c3e50;
  padding-bottom: 12px;
}

.meta {
  display: flex;
  gap: 16px;
  color: #888;
  font-size: 13px;
  margin-bottom: 20px;
}

.content {
  min-height: 200px;
  padding: 12px 0;
  white-space: pre-wrap;
  line-height: 1.6;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn {
  padding: 10px 20px;
  border-radius: 4px;
  border: 1px solid #ddd;
  background: #fff;
}

.btn-danger {
  background: #e74c3c;
  color: #fff;
  border-color: #e74c3c;
}
</style>
