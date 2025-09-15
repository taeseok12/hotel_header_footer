<template>
  <div class="card p-4" style="position:relative;">
    <form @submit.prevent="onSubmit" class="grid" :style="gridStyle">
      <!-- 목적지(자유 검색) -->
      <div>
        <label class="text-muted" style="font-size:13px;">목적지</label>
        <input class="input" v-model="q" placeholder="도시/지역/호텔명" aria-label="목적지 검색" />
        <p class="text-muted" style="font-size:12px; margin-top:6px;">※ 오늘 이전은 선택 불가, 1~14박만 가능합니다.</p>
      </div>

      <!-- 체크인/아웃 (선택) -->
      <div>
        <label class="text-muted" style="font-size:13px;">체크인</label>
        <input class="input" type="date" v-model="checkIn" :min="today" />
      </div>
      <div>
        <label class="text-muted" style="font-size:13px;">체크아웃</label>
        <input class="input" type="date" v-model="checkOut" :min="minCheckout" />
      </div>

      <!-- 성인/소아/객실 (UI 유지용) -->
      <div>
        <label class="text-muted" style="font-size:13px;">성인 & 유아 & 객실</label>
        <div style="display:flex; gap:8px;">
          <input class="input" type="number" min="1" v-model.number="adults" />
          <input class="input" type="number" min="0" v-model.number="kids" />
          <input class="input" type="number" min="1" v-model.number="rooms" />
        </div>
      </div>

      <div style="align-self:end;">
        <button class="btn primary">검색</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const q = ref(String(route.query.q || ''))
const checkIn = ref(String(route.query.checkIn || ''))
const checkOut = ref(String(route.query.checkOut || ''))
const adults = ref(Number(route.query.adults || 2))
const kids = ref(Number(route.query.kids || 0))
const rooms = ref(Number(route.query.rooms || 1))

const today = new Date().toISOString().slice(0,10)
const minCheckout = computed(() => checkIn.value || today)

function onSubmit () {
  const params = {
    q: q.value || undefined,
    checkIn: checkIn.value || undefined,
    checkOut: checkOut.value || undefined,
    adults: adults.value || undefined,
    kids: kids.value || undefined,
    rooms: rooms.value || undefined,
  }
  // ✅ /search로 이동하여 결과 리스트 페이지에서 표시
  router.push({ path: '/search', query: params })
}

const gridStyle = 'grid-template-columns: 1.4fr 1fr 1fr 1fr auto; gap:12px;'
</script>
