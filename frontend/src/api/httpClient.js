import axios from 'axios'

const baseURL =
  typeof __FRONTEND_API_URL__ !== 'undefined'
    ? __FRONTEND_API_URL__
    : 'http://localhost:8080/api'

export const httpClient = axios.create({
  baseURL,
  timeout: 25000,
  headers: {
    Accept: 'application/json'
  }
})

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.mensagem ||
      error.response?.data?.erro ||
      error.message ||
      'Falha inesperada ao conversar com o backend.'
    return Promise.reject(new Error(message))
  }
)
