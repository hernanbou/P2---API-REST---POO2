import { httpClient } from './httpClient'

export const logsApi = {
  async listarConsultas() {
    const { data } = await httpClient.get('/logs')
    return data
  },

  async listarAtualizacoes() {
    const { data } = await httpClient.get('/atualizacoes')
    return data
  },

  async health() {
    const { data } = await httpClient.get('/health')
    return data
  }
}
