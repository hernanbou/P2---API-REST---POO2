import { httpClient } from './httpClient'

const cleanParams = (params) =>
  Object.fromEntries(Object.entries(params).filter(([, value]) => value !== '' && value !== null && value !== undefined))

export const propostaLocalApi = {
  async listar() {
    const { data } = await httpClient.get('/propostas-locais')
    return data
  },

  async filtrar(params) {
    const { data } = await httpClient.get('/propostas-locais/filtro', {
      params: cleanParams(params)
    })
    return data
  },

  async criar(payload) {
    const { data } = await httpClient.post('/propostas-locais', payload)
    return data
  },

  async atualizar(id, payload) {
    const { data } = await httpClient.put(`/propostas-locais/${id}`, payload)
    return data
  },

  async remover(id) {
    await httpClient.delete(`/propostas-locais/${id}`)
  }
}
