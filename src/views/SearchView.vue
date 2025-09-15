<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { hotelsSearch } from '@/api/auth'
import SearchBar from '@/components/SearchBar.vue'

const route = useRoute()
const hotels = ref([])
const loading = ref(false)
const error = ref('')

const titleText = computed(() => {
  const r = (route.query.region || '').toString()
  const q = (route.query.q || '').toString()
  if (r) return `‘${r}’ 지역 호텔`
  if (q) return `‘${q}’ 검색 결과`
  return '전체 호텔'
})

function cover(h) { return h.coverImageUrl || `https://picsum.photos/seed/h${h.id}/320/200` }

async function fetchList () {
  loading.value = true
  error.value = ''
  hotels.value = []

  const region = (route.query.region || '').toString()
  const q = (route.query.q || '').toString()
  const regionExact = String(route.query.regionExact || '').toLowerCase() === 'true'

  const params = { page: 0, size: 200 }
  if (region) params.region = region
  if (q) params.q = q
  if (regionExact) params.regionExact = true

  try {
    const data = await hotelsSearch(params)
    hotels.value = data?.content ?? []
  } catch (e) {
    console.error('hotel search failed', e)
    error.value = e?.message || '요청 실패'
  } finally {
    loading.value = false
  }
}

onMounted(fetchList)
watch(() => route.query, fetchList)
</script>

<template>
  <div class="container">
    <section class="mt-6">
      <div style="font-weight:700; font-size:22px; margin-bottom:12px;">{{ titleText }}</div>
      <SearchBar />
    </section>

    <section class="mt-6" v-if="loading">
      <div class="card p-6 text-muted" style="text-align:center;">불러오는 중…</div>
    </section>

    <section class="mt-6" v-else-if="error">
      <div class="card p-6" style="text-align:center; color:#b91c1c;">{{ error }}</div>
    </section>

    <section class="mt-6" v-else-if="hotels.length">
      <!-- 리스트 렌더링은 기존 그대로 -->
      ...
    </section>

    <section v-else class="mt-6">
      <div class="card p-6 text-muted" style="text-align:center;">조건에 맞는 호텔이 없습니다.</div>
    </section>
  </div>
</template>
