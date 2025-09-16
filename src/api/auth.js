import axios from 'axios'

const baseURL = import.meta.env.VITE_API_BASE || 'http://localhost:8888/api'

const api = axios.create({
  baseURL,
  withCredentials: true // refresh 쿠키
})

let accessToken = localStorage.getItem('accessToken') || null
let userProfile = JSON.parse(localStorage.getItem('userProfile') || 'null')

export function getUser(){ return userProfile }
export function isLoggedIn(){ return !!accessToken }
export function setAuth(at, user){
  accessToken = at
  userProfile = user || null
  if (at) localStorage.setItem('accessToken', at); else localStorage.removeItem('accessToken')
  if (user) localStorage.setItem('userProfile', JSON.stringify(user)); else localStorage.removeItem('userProfile')
}
export async function login(username, password){
  const { data } = await api.post('/auth/login', { username, password })
  setAuth(data.accessToken, data.user)
  return data
}
export async function logout(){
  try { await api.post('/auth/logout', {}) } catch {}
  setAuth(null, null)
}

api.interceptors.request.use((config)=>{
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`
  return config
})

// ---- 401 처리 & 동시 요청 큐 ----
let refreshing = false
let waiters = []
function waitRefresh(){ return new Promise(res => waiters.push(res)) }
function resume(){ waiters.forEach(r=>r()); waiters = [] }

api.interceptors.response.use(
  r => r,
  async (error) => {
    const original = error?.config
    const status = error?.response?.status
    if (!original || original._retry) return Promise.reject(error)

    if (status === 401) {
      if (refreshing) {
        await waitRefresh()
        return api(original)
      }
      refreshing = true
      original._retry = true
      try {
        const resp = await axios.post(`${baseURL}/auth/refresh`, {}, { withCredentials: true })
        const newToken = resp.data?.accessToken
        if (newToken) {
          setAuth(newToken, userProfile)
          return api(original)
        } else {
          setAuth(null, null)
          return Promise.reject(error)
        }
      } catch (e) {
        setAuth(null, null)
        return Promise.reject(e)
      } finally {
        refreshing = false
        resume()
      }
    }
    return Promise.reject(error)
  }
)

// ---- API helpers ----
export async function hotelsSearch(params){ return (await api.get('/hotels/search', { params })).data }
export async function hotelDetail(id){ return (await api.get(`/hotels/${id}`)).data }
export async function subscribeEmail(email){ return (await api.post('/marketing/subscribe', { email })).data }

// (옵션) 위시리스트
export async function getWishlist(){ return (await api.get('/my/wishlist')).data }
export async function addWishlist(hotelId){ return (await api.post('/my/wishlist', { hotelId })).data }
export async function removeWishlist(hotelId){ return (await api.delete(`/my/wishlist/${hotelId}`)).data }

// ✅ 지역 목록 (distinct) — 제주 제외 기본
export async function fetchRegions(excludeJeju = true){
  return (await api.get('/hotels/regions', { params: { excludeJeju } })).data
}

export default api
