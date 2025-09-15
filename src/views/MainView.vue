<template>
  <div class="container">
    <!-- 검색 -->
    <section class="mt-6">
      <div style="font-weight:700; font-size:22px; margin-bottom:12px;">어디로 떠나시나요?</div>
      <SearchBar />
    </section>

    <!-- 추천 지역 & See all -->
    <RegionCards />

    <!-- 결과 -->
    <section class="mt-6" v-if="hotels.length">
      <h3 class="mb-4" style="font-weight:600; font-size:18px;">검색 결과</h3>
      <ul class="grid" style="gap:12px;">
        <li v-for="h in hotels" :key="h.id" class="card p-4" style="display:flex; gap:16px;">
          <img :src="cover(h)" :alt="`${h.name} 이미지`"
               style="width:160px; height:110px; object-fit:cover; border-radius:12px;" />
          <div style="flex:1;">
            <div style="display:flex; justify-content:space-between;">
              <div>
                <div style="font-weight:600; font-size:16px;">{{ h.name }}</div>
                <div class="text-muted" style="font-size:14px;">
                  {{ h.region }} <span v-if="h.address">· {{ h.address }}</span>
                </div>
              </div>
              <div style="text-align:right;">
                <div class="text-muted" style="font-size:13px;">
                  평점 {{ (h.rating ?? 0).toFixed(1) }}
                  <span v-if="h.gradeLevel"> / 성급 {{ h.gradeLevel }}</span>
                </div>
                <div v-if="h.avgPricePerNight && h.avgPricePerNight > 0" style="font-weight:700;">
                  ₩{{ n(h.avgPricePerNight) }} / 박
                </div>
              </div>
            </div>

            <div v-if="h.amenities && h.amenities.length" class="text-muted"
                 style="font-size:12px; margin-top:6px;">
              어메니티: {{ h.amenities.join(', ') }}
            </div>

            <div style="margin-top:8px; display:flex; gap:8px;">
              <a :href="h.homepageUrl || '#'" target="_blank" class="btn ghost">홈페이지</a>
              <button class="btn ghost">♡ 찜하기</button>
            </div>
          </div>
        </li>
      </ul>
    </section>

    <section v-else class="mt-6">
      <div class="card p-6 text-muted" style="text-align:center;">조건에 맞는 호텔이 없습니다.</div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, watch, ref } from 'vue'
import { useRoute } from 'vue-router'
import SearchBar from '@/components/SearchBar.vue'
import RegionCards from '@/components/RegionCards.vue'
import { hotelsSearch } from '@/api/auth'

const route = useRoute()
const hotels = ref([])

function n(x) { return Number(x).toLocaleString() }
function cover(h) { return h.coverImageUrl || `https://picsum.photos/seed/h${h.id}/320/200` }

async function fetchList() {
  const region = (route.query.region || '').toString()
  const q = (route.query.q || '').toString()

  // 쿼리를 비우면 전체 목록
  const params = { page: 0, size: 24 }
  if (region) params.region = region
  if (q) params.q = q

  try {
    const data = await hotelsSearch(params)
    hotels.value = data?.content ?? []
  } catch (e) {
    console.error('hotel search failed', e)
    hotels.value = []
  }
}

onMounted(fetchList)
watch(() => route.query, fetchList)
</script>
