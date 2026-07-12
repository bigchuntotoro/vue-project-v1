import { createRouter, createWebHistory } from 'vue-router'
import BoardList from '../views/BoardList.vue'
import BoardWrite from '../views/BoardWrite.vue'
import BoardDetail from '../views/BoardDetail.vue'
import BoardEdit from '../views/BoardEdit.vue'

const routes = [
  { path: '/', name: 'BoardList', component: BoardList },
  { path: '/write', name: 'BoardWrite', component: BoardWrite },
  { path: '/board/:id', name: 'BoardDetail', component: BoardDetail, props: true },
  { path: '/board/:id/edit', name: 'BoardEdit', component: BoardEdit, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
