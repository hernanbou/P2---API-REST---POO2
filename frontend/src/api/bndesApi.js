import { httpClient } from './httpClient'

const cleanParams = (params) =>
  Object.fromEntries(Object.entries(params).filter(([, value]) => value !== '' && value !== null && value !== undefined))

export const bndesApi = {
  async listarFaixasFaturamento() {
    const { data } = await httpClient.get('/bndes/faixas-faturamento')
    return data
  },

  async listarSituacoes() {
    const { data } = await httpClient.get('/bndes/situacoes')
    return data
  },

  async consultarParceiroFinanceiro(filtros) {
    const { data } = await httpClient.get('/bndes/propostas/parceiro-financeiro', {
      params: cleanParams(filtros)
    })
    return data
  },

  async consultarParceiroInstitucional(filtros) {
    const { data } = await httpClient.get('/bndes/propostas/parceiro-institucional', {
      params: cleanParams({
        dataInicio: filtros.dataInicio,
        dataFim: filtros.dataFim
      })
    })
    return data
  },

  async atualizarParceiroFinanceiro(payload) {
    const { data } = await httpClient.post('/bndes/propostas/parceiro-financeiro/atualizar', payload)
    return data
  },

  async atualizarFintech(payload) {
    const { data } = await httpClient.post('/bndes/propostas/fintech/atualizar', payload)
    return data
  },

  async tokenStatus() {
    const { data } = await httpClient.get('/health/bndes-token')
    return data
  }
}
