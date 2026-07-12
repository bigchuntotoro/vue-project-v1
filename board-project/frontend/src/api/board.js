import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
})

export default {
  // 목록 조회 (page, size, searchType, keyword)
  getList(params) {
    return api.get('/boards', { params })
  },
  // 상세 조회
  getDetail(id) {
    return api.get(`/boards/${id}`)
  },
  // 등록
  create(data) {
    return api.post('/boards', data)
  },
  // 수정
  update(id, data) {
    return api.put(`/boards/${id}`, data)
  },
  // 삭제
  remove(id) {
    return api.delete(`/boards/${id}`)
  }
}
