import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'node:path'

export default defineConfig({
  plugins: [vue()],
  server: { port: 5173 },
  resolve: {
    alias: {
      '@': path.resolve(process.cwd(), 'src')
    }
  }
})
