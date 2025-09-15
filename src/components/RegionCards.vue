<template>
  <section class="mt-6">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
      <h3 style="font-weight:700; font-size:18px;">여행에 빠지다</h3>
      <button class="btn text" @click="seeAll" aria-label="모든 호텔 보기">See all</button>
    </div>

    <div class="grid" style="grid-template-columns: repeat(4, minmax(0, 1fr)); gap:16px;">
      <div v-for="c in cards" :key="c.name" class="card p-0" style="overflow:hidden;">
        <img :alt="`${c.name} 대표 이미지`" :src="c.img" style="width:100%; height:160px; object-fit:cover;">
        <div class="p-4">
          <div style="font-weight:600; margin-bottom:6px;">{{ c.name }}</div>
          <div class="text-muted" style="font-size:12px; margin-bottom:10px;">평균가 {{ c.avg }}</div>
          <button class="btn primary" @click="goRegionExact(c.regionExact)">Book a Hotel</button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

/**
 * DB의 region 컬럼 값에 정확히 맞춰 주세요.
 * - 서울: 서울특별시
 * - 부산: 부산광역시
 * - 강원: 강원도
 * - 제주: 제주특별자치도
 */
const cards = [
  { name: '서울', regionExact: '서울특별시', img: 'https://picsum.photos/seed/seoulCard/640/360',  avg: '₩120,000' },
  { name: '부산', regionExact: '부산광역시', img: 'https://picsum.photos/seed/busanCard/640/360',  avg: '₩100,000' },
  { name: '강원', regionExact: '강원도',   img: 'https://picsum.photos/seed/gangwonCard/640/360',avg: '₩95,000' },
  { name: '제주', regionExact: '제주특별자치도', img: 'https://picsum.photos/seed/jejuCard/640/360',  avg: '₩130,000' },
]

function goRegionExact(region) {
  router.push({ path: '/search', query: { region, regionExact: 'true' } }) // ✅ 정확 일치
}
function seeAll() {
  router.push({ path: '/search' }) // 전체 목록
}
</script>

<style scoped>
.btn.text { background: transparent; border: 0; padding: 6px 8px; color: #6b7280; cursor: pointer; }
.btn.text:hover { text-decoration: underline; }
</style>
