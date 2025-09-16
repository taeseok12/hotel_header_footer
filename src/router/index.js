import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'
import SearchView from '@/views/SearchView.vue'   // ✅ 추가/확인

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/',      name: 'home',   component: MainView },
    { path: '/search',name: 'search', component: SearchView } // ✅ 필요
  ],
})

export default router
