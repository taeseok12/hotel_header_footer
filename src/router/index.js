import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'

import SearchView from '@/views/SearchView.vue'  

const routes = [
  { path: '/', component: MainView },
  { path: '/search', component: SearchView },     
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
