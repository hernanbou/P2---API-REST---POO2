import { useState } from 'react'
import { bndesApi } from '../api/bndesApi'

export function useReferences() {
  const [faixas, setFaixas] = useState([])
  const [situacoes, setSituacoes] = useState([])
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  const loadFaixas = async () => {
    setLoading(true)
    try {
      const envelope = await bndesApi.listarFaixasFaturamento()
      setFaixas(envelope.dados || [])
      setMessage({ type: envelope.apiStatus === 'FALHOU' ? 'warning' : 'success', text: envelope.mensagem })
      return envelope
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
      throw error
    } finally {
      setLoading(false)
    }
  }

  const loadSituacoes = async () => {
    setLoading(true)
    try {
      const envelope = await bndesApi.listarSituacoes()
      setSituacoes(envelope.dados || [])
      setMessage({ type: envelope.apiStatus === 'FALHOU' ? 'warning' : 'success', text: envelope.mensagem })
      return envelope
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
      throw error
    } finally {
      setLoading(false)
    }
  }

  return {
    faixas,
    situacoes,
    loading,
    message,
    loadFaixas,
    loadSituacoes
  }
}
