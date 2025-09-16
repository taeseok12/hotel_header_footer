<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 이미지: 없으면 fallback
const fallback = new URL('@/assets/regions/fallback.jpg', import.meta.url).href
function rimg(name) {
  try { return new URL(`@/assets/regions/${name}`, import.meta.url).href }
  catch { return fallback }
}

// ✅ 제주 제외: DB에 실제 존재하는 region 값과 동일하게 작성
const REGIONS = [
  { title: '서울',     query: '서울특별시', image: rimg('seoul.jpg'),     avg: 120000 },
  { title: '인천',     query: '인천광역시', image: rimg('incheon.jpg'),   avg: 110000 },
  { title: '울산',     query: '울산광역시', image: rimg('ulsan.jpg'),     avg: 105000 },
  { title: '부산',     query: '부산광역시', image: rimg('busan.jpg'),     avg: 100000 },
  { title: '경상남도', query: '경상남도',   image: rimg('gyeongnam.jpg'), avg:  95000 },
  { title: '경상북도', query: '경상북도',   image: rimg('gyeongbuk.jpg'), avg:  90000 },
  { title: '전라남도', query: '전라남도',   image: rimg('jeonnam.jpg'),   avg:  90000 },
  { title: '전라북도', query: '전라북도',   image: rimg('jeonbuk.jpg'),   avg:  90000 },
  { title: '강원도',   query: '강원도',     image: rimg('gangwon.jpg'),   avg:  95000 },
]

const PAGE_SIZE = 4
const page   = ref(0)
const pages  = Math.ceil(REGIONS.length / PAGE_SIZE)
const slice  = computed(() => {
  const start = page.value * PAGE_SIZE
  return REGIONS.slice(start, start + PAGE_SIZE)
})

function prev(){ if (page.value > 0) page.value-- }
function next(){ if (page.value < pages - 1) page.value++ }

function goAll(){ router.push({ path: '/search' }) }
function goRegion(r){
  router.push({ path: '/search', query: { region: r.query, regionExact: 'true' } })
}
</script>

<template>
  <section class="wrap">
    <div class="head">
      <h2>여행에 빠지다</h2>
      <div class="right">
        <div class="pager">
          <button class="nav" :disabled="page===0" @click="prev">‹</button>
          <span class="pi">{{ page+1 }} / {{ pages }}</span>
          <button class="nav" :disabled="page===pages-1" @click="next">›</button>
        </div>
        <button class="seeall" @click="goAll">See all</button>
      </div>
    </div>

    <div class="grid">
      <article v-for="r in slice" :key="r.query" class="card">
        <div class="img">
          <img :src="r.image || fallback" :alt="r.title" loading="lazy" />
        </div>
        <div class="body">
          <h3>{{ r.title }}</h3>
          <p class="meta">평균가 ₩{{ r.avg.toLocaleString() }}</p>
          <button class="cta" @click="goRegion(r)">Book a Hotel</button>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.wrap { margin: 32px 0 8px; }
.head { display:flex; align-items:center; justify-content:space-between; margin-bottom: 12px; }
.head h2 { margin:0; }
.right { display:flex; align-items:center; gap:12px; }

.pager { display:flex; align-items:center; gap:8px; }
.nav {
  width:32px; height:32px; border:1px solid #e6e6e6; background:#fff;
  border-radius:8px; font-size:18px; line-height:30px; cursor:pointer;
}
.nav:disabled { opacity:.4; cursor:not-allowed; }
.pi { color:#666; font-size:14px; min-width:48px; text-align:center; }
.seeall {
  height:32px; padding:0 12px; border:1px solid #111; background:#111; color:#fff;
  border-radius:8px; cursor:pointer; font-size:14px;
}

.grid {
  display:grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}
.card { background:#fff; border:1px solid #eee; border-radius:16px; overflow:hidden; display:flex; flex-direction:column; }
.img { height:180px; background:#f3f3f3; }
.img img { width:100%; height:100%; object-fit:cover; display:block; }
.body { padding:12px 14px 16px; }
h3 { margin:0 0 6px; }
.meta { margin:0 0 10px; color:#666; font-size:14px; }
.cta {
  display:inline-block; padding:8px 12px; border-radius:10px;
  background:#111; color:#fff; border:0; cursor:pointer;
}
@media (max-width: 1000px){ .grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px){ .grid { grid-template-columns: 1fr; } }
</style>
