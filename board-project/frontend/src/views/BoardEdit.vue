<template>
  <div class="board-form" v-if="form">
    <h2>수정</h2>
    <div class="form-group">
      <label>제목</label>
      <input v-model="form.title" type="text" />
    </div>
    <div class="form-group">
      <label>작성자</label>
      <input v-model="form.writer" type="text" />
    </div>
    <div class="form-group">
      <label>내용</label>
      <textarea v-model="form.content" rows="12"></textarea>
    </div>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <div class="actions">
      <button class="btn" @click="$router.push(`/board/${form.id}`)">취소</button>
      <button class="btn btn-primary" @click="submit">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import boardApi from '../api/board'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const errorMessage = ref('')

const fetchDetail = async () => {
  const { data } = await boardApi.getDetail(route.params.id)
  form.value = data
}

const submit = async () => {
  if (!form.value.title || !form.value.writer || !form.value.content) {
    errorMessage.value = '모든 항목을 입력해주세요.'
    return
  }
  try {
    await boardApi.update(form.value.id, form.value)
    router.push(`/board/${form.value.id}`)
  } catch (e) {
    errorMessage.value = e.response?.data?.message || '수정 중 오류가 발생했습니다.'
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
.board-form {
  background: #fff;
  padding: 24px;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
}

.error {
  color: #e74c3c;
  font-size: 13px;
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

.btn-primary {
  background: #2c3e50;
  color: #fff;
  border-color: #2c3e50;
}
</style>
