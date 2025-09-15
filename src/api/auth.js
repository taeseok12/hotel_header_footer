import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
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

let refreshing = false
let waiters = []
function resume(){ waiters.forEach(r=>r()); waiters = [] }

api.interceptors.response.use(
  r => r,
  async (error) => {
    const original = error.config
    if (error?.response?.status === 401 && !original._retry) {
      if (refreshing) {
        await new Promise(res => waiters.push(res))
        return api(original)
      }
      original._retry = true
      refreshing = true
      try {
        const resp = await axios.post(`${import.meta.env.VITE_API_BASE}/auth/refresh`, {}, { withCredentials: true })
        const newToken = resp.data?.accessToken
        if (newToken) {
          setAuth(newToken, userProfile)
          resume()
          return api(original)
        }
      } catch (e) {
        setAuth(null, null)
      } finally { refreshing = false }
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

export default api
