<template>
  <header :class="['header', solid && 'solid']" role="banner">
    <div class="container" style="height: var(--header-h); display:flex; align-items:center;">
      <div style="flex:1">
        <router-link to="/" aria-label="홈으로 이동" style="font-weight:700; font-size:20px; letter-spacing:.2px;">
          Hotel
        </router-link>
      </div>
      <div style="flex:1; text-align:center; color:#4b5563; font-size:14px;">
        {{ centerTitle }}
      </div>
      <div style="flex:1; display:flex; justify-content:flex-end; gap:8px;">
        <button class="btn ghost" @click="onWishlistClick" aria-label="위시리스트">♡ <span class="hidden-sm">찜하기</span></button>
        <template v-if="user">
          <div style="position:relative;">
            <button class="btn ghost" @click="open = !open" :aria-expanded="open" aria-haspopup="menu">
              {{ user.nickname }} ▼
            </button>
            <div v-if="open" role="menu"
                 style="position:absolute; right:0; margin-top:6px; width:200px;"
                 class="card p-4">
              <button class="btn ghost" style="width:100%; margin-bottom:6px" @click="open=false">계정</button>
              <button class="btn ghost" style="width:100%; margin-bottom:6px" @click="open=false">결제내역</button>
              <button class="btn ghost" style="width:100%; margin-bottom:6px" @click="open=false">설정</button>
              <button class="btn ghost" style="width:100%; margin-bottom:6px" @click="open=false">고객지원</button>
              <hr style="margin:8px 0;">
              <button class="btn primary" style="width:100%;" @click="doLogout">로그아웃</button>
            </div>
          </div>
        </template>
        <template v-else>
          <router-link class="btn primary" to="/login">로그인 / 회원가입</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted, onUnmounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUser, logout } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const user = ref(getUser())
const open = ref(false)
const solid = ref(false)

const onScroll = () => { solid.value = window.scrollY > 4 }
onMounted(() => { onScroll(); window.addEventListener('scroll', onScroll) })
onUnmounted(() => window.removeEventListener('scroll', onScroll))

const centerTitle = computed(() => {
  const p = route.path
  if (p.startsWith('/login')) return '로그인'
  if (p.startsWith('/signup')) return '회원가입'
  if (p.startsWith('/find-password')) return '비밀번호 찾기'
  return ''
})

function onWishlistClick(){
  if (!user.value) router.push('/login')
  else alert('위시리스트는 추후 화면에서 제공됩니다. (API는 준비됨)')
}
async function doLogout(){
  await logout()
  open.value = false
  router.push('/')
  user.value = null
}
</script>
