import { useCallback, useState } from 'react'
import { bndesApi } from '../api/bndesApi'
import { propostaLocalApi } from '../api/propostaLocalApi'

export function useProposals() {
  const [proposals, setProposals] = useState([])
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  const loadLocal = useCallback(async () => {
    setLoading(true)
    try {
      const data = await propostaLocalApi.listar()
      setProposals(data)
      return data
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
      throw error
    } finally {
      setLoading(false)
    }
  }, [])

  const searchExternal = async (tipoConsulta, filtros) => {
    setLoading(true)
    try {
      // Todas as chamadas externas passam pelo backend; o frontend nunca recebe token nem credenciais.
      const envelope =
        tipoConsulta === 'PARCEIRO_INSTITUCIONAL'
          ? await bndesApi.consultarParceiroInstitucional(filtros)
          : await bndesApi.consultarParceiroFinanceiro(filtros)
      setProposals(envelope.dados || [])
      setMessage({ type: envelope.apiStatus === 'FALHOU' ? 'warning' : 'success', text: envelope.mensagem })
      return envelope
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
      throw error
    } finally {
      setLoading(false)
    }
  }

  const createLocal = async (payload) => {
    const created = await propostaLocalApi.criar(payload)
    await loadLocal()
    setMessage({ type: 'success', text: 'Proposta local cadastrada no H2.' })
    return created
  }

  const updateLocal = async (id, payload) => {
    const updated = await propostaLocalApi.atualizar(id, payload)
    await loadLocal()
    setMessage({ type: 'success', text: 'Proposta local atualizada.' })
    return updated
  }

  const deleteLocal = async (id) => {
    await propostaLocalApi.remover(id)
    await loadLocal()
    setMessage({ type: 'success', text: 'Proposta local excluida.' })
  }

  const registerUpdate = async (payload) => {
    // O tipo de envio define se o backend simula/chama parceiro financeiro ou fintech.
    const response =
      payload.tipoEnvio === 'FINTECH'
        ? await bndesApi.atualizarFintech(payload)
        : await bndesApi.atualizarParceiroFinanceiro(payload)
    await loadLocal()
    setMessage({ type: response.sucesso ? 'success' : 'warning', text: response.mensagem })
    return response
  }

  return {
    proposals,
    loading,
    message,
    setMessage,
    loadLocal,
    searchExternal,
    createLocal,
    updateLocal,
    deleteLocal,
    registerUpdate
  }
}
