import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const frontendEnv = loadEnv(mode, '..', 'FRONTEND_')

  return {
    plugins: [react()],
    define: {
      __FRONTEND_API_URL__: JSON.stringify(frontendEnv.FRONTEND_API_URL || 'http://localhost:8080/api')
    },
    server: {
      port: 5173
    }
  }
})
