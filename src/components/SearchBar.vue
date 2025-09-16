<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 기존 로직 그대로
const destination = ref(route.query.region || route.query.q || '')
const checkIn     = ref(route.query.checkIn  || '')
const checkOut    = ref(route.query.checkOut || '')
const adults      = ref(Number(route.query.adults   ?? 2))
const children    = ref(Number(route.query.children ?? 0))
const rooms       = ref(Number(route.query.rooms    ?? 1))

const REGION_MAP = new Map([
  ['서울','서울특별시'],['서울시','서울특별시'],['서울특별시','서울특별시'],
  ['부산','부산광역시'],['부산시','부산광역시'],['부산광역시','부산광역시'],
  ['인천','인천광역시'],['인천시','인천광역시'],['인천광역시','인천광역시'],
  ['울산','울산광역시'],['울산시','울산광역시'],['울산광역시','울산광역시'],
  ['광주','광주광역시'],['광주시','광주광역시'],['광주광역시','광주광역시'],
  ['대구','대구광역시'],['대구시','대구광역시'],['대구광역시','대구광역시'],
  ['대전','대전광역시'],['대전시','대전광역시'],['대전광역시','대전광역시'],
  ['강원','강원도'],['강원도','강원도'],
  ['경남','경상남도'],['경상남도','경상남도'],
  ['경북','경상북도'],['경상북도','경상북도'],
  ['전남','전라남도'],['전라남도','전라남도'],
  ['전북','전라북도'],['전라북도','전라북도'],
  ['충남','충청남도'],['충청남도','충청남도'],
  ['충북','충청북도'],['충청북도','충청북도'],
])

function normalizeRegion(term){
  if(!term) return null
  const t = term.replace(/\s+/g,'')
  if (REGION_MAP.has(t)) return REGION_MAP.get(t)
  for (const [k,v] of REGION_MAP.entries()){
    if (t.startsWith(k)) return v
  }
  return null
}

function onSearch(){
  const term = (destination.value || '').trim()
  const query = {
    checkIn:  checkIn.value  || undefined,
    checkOut: checkOut.value || undefined,
    adults:   adults.value,
    children: children.value,
    rooms:    rooms.value,
    page: 0
  }
  if (term){
    const reg = normalizeRegion(term)
    if (reg){
      query.region = reg
      query.regionExact = true
      query.q = term
    } else {
      query.q = term
      query.region = undefined
      query.regionExact = undefined
    }
  } else {
    query.q = undefined
    query.region = undefined
    query.regionExact = undefined
  }
  router.push({ path:'/search', query })
}

watch(()=>route.query, (q)=>{
  destination.value = q.region || q.q || ''
  checkIn.value     = q.checkIn  || ''
  checkOut.value    = q.checkOut || ''
  adults.value      = Number(q.adults   ?? adults.value)
  children.value    = Number(q.children ?? children.value)
  rooms.value       = Number(q.rooms    ?? rooms.value)
}, { deep:true })
</script>

<template>
  <!-- 검색 바 박스 -->
  <div class="w-full rounded-3xl border border-gray-200 bg-white px-5 py-4 shadow-sm">
    <div class="flex items-center gap-3">
      <!-- 목적지 -->
      <div class="flex-1">
        <input
          v-model="destination"
          placeholder="도시/지역/호텔명"
          @keyup.enter="onSearch"
          class="h-12 w-full rounded-full border border-gray-300 px-5 text-[15px]
                 placeholder:text-gray-400 focus:border-black/70 focus:outline-none
                 focus:ring-1 focus:ring-black/70"
        />
        <p class="mt-2 px-1 text-xs text-gray-400">
          ※ 오늘 이전은 선택 불가, 1~14박만 가능합니다.
        </p>
      </div>

      <!-- 체크인 -->
      <input
        v-model="checkIn"
        type="date"
        class="h-12 w-[220px] rounded-full border border-gray-300 bg-white px-4 text-[15px]
               focus:border-black/70 focus:outline-none focus:ring-1 focus:ring-black/70"
      />

      <!-- 체크아웃 -->
      <input
        v-model="checkOut"
        type="date"
        class="h-12 w-[220px] rounded-full border border-gray-300 bg-white px-4 text-[15px]
               focus:border-black/70 focus:outline-none focus:ring-1 focus:ring-black/70"
      />

      <!-- 성인 / 유아 / 객실 -->
      <input
        v-model.number="adults"
        type="number" min="1"
        class="h-12 w-14 rounded-full border border-gray-300 bg-white text-center
               focus:border-black/70 focus:outline-none focus:ring-1 focus:ring-black/70"
      />
      <input
        v-model.number="children"
        type="number" min="0"
        class="h-12 w-14 rounded-full border border-gray-300 bg-white text-center
               focus:border-black/70 focus:outline-none focus:ring-1 focus:ring-black/70"
      />
      <input
        v-model.number="rooms"
        type="number" min="1"
        class="h-12 w-14 rounded-full border border-gray-300 bg-white text-center
               focus:border-black/70 focus:outline-none focus:ring-1 focus:ring-black/70"
      />

      <!-- 검색 버튼 -->
      <button
        class="h-12 rounded-full bg-black px-5 text-white hover:bg-black/90"
        @click="onSearch"
      >
        검색
      </button>
    </div>
  </div>
</template>


