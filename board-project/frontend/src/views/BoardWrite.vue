<template>
  <div class="board-form">
    <h2>글쓰기</h2>
    <div class="form-group">
      <label>제목</label>
      <input v-model="form.title" type="text" placeholder="제목을 입력하세요" />
    </div>
    <div class="form-group">
      <label>작성자</label>
      <input v-model="form.writer" type="text" placeholder="작성자명을 입력하세요" />
    </div>
    <div class="form-group">
      <label>내용</label>
      <textarea v-model="form.content" rows="12" placeholder="내용을 입력하세요"></textarea>
    </div>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    <div class="actions">
      <button class="btn" @click="$router.push('/')">취소</button>
      <button class="btn btn-primary" @click="submit">등록</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import boardApi from '../api/board'

const router = useRouter()
const form = ref({ title: '', writer: '', content: '' })
const errorMessage = ref('')

const submit = async () => {
  if (!form.value.title || !form.value.writer || !form.value.content) {
    errorMessage.value = '모든 항목을 입력해주세요.'
    return
  }
  try {
    const { data: id } = await boardApi.create(form.value)
    router.push(`/board/${id}`)
  } catch (e) {
    errorMessage.value = e.response?.data?.message || '등록 중 오류가 발생했습니다.'
  }
}
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
