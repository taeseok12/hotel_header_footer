<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute } from 'vue-router'
import { hotelsSearch } from '@/api/auth'

const route = useRoute()
const loading = ref(false)
const page = ref({ content: [], totalElements: 0, number: 0, size: 20 })

const items = computed(() => page.value.content ?? [])
const hasNoResult = computed(() => !loading.value && items.value.length === 0)

async function load () {
  loading.value = true
  try {
    // 라우터 쿼리 그대로 백엔드로 전달 (q / region / regionExact / 날짜/인원 등 모두)
    const data = await hotelsSearch({ ...route.query })
    page.value = data
  } finally {
    loading.value = false
  }
}

// 최초 / 쿼리 변경 시마다 재조회
watch(() => route.fullPath, load, { immediate: true })
</script>

<template>
  <div class="mt-6">
    <h2 class="text-2xl font-bold mb-4">
      <!-- 타이틀: 지역 or 키워드 or 전체 -->
      <template v-if="route.query.region && route.query.regionExact === 'true'">
        ‘{{ route.query.region }}’ 지역 호텔
      </template>
      <template v-else-if="route.query.q">
        ‘{{ route.query.q }}’ 검색 결과
      </template>
      <template v-else>
        전체 호텔
      </template>
    </h2>

    <div v-if="loading" class="py-10 text-gray-500">로딩 중…</div>
    <div v-else-if="hasNoResult" class="py-10 text-gray-500">조건에 맞는 호텔이 없습니다.</div>

    <div v-else class="space-y-5">
      <div
        v-for="h in items"
        :key="h.id"
        class="flex gap-4 p-4 border rounded-lg bg-white"
      >
        <img
          v-if="h.coverImageUrl"
          :src="h.coverImageUrl"
          alt="cover"
          class="w-56 h-36 object-cover rounded-md shrink-0"
        />
        <div class="flex-1">
          <div class="text-lg font-semibold">{{ h.name }}</div>
          <div class="text-gray-600 mt-1">
            {{ h.region }} · {{ h.address }}
          </div>
          <div class="text-gray-500 text-sm mt-1">
            평점 {{ h.rating ?? '-' }} / 성급 {{ h.gradeLevel ?? '-' }}
          </div>
          <div class="text-sm text-blue-600 mt-2" v-if="h.homepageUrl">
            <a :href="h.homepageUrl" target="_blank" rel="noreferrer">홈페이지</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
