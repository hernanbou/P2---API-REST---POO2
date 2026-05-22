import { useCallback, useState } from 'react'
import { logsApi } from '../api/logsApi'

export function useLogs() {
  const [consultas, setConsultas] = useState([])
  const [atualizacoes, setAtualizacoes] = useState([])
  const [health, setHealth] = useState(null)
  const [loading, setLoading] = useState(false)

  const refresh = useCallback(async () => {
    setLoading(true)
    try {
      const [consultasData, atualizacoesData, healthData] = await Promise.all([
        logsApi.listarConsultas(),
        logsApi.listarAtualizacoes(),
        logsApi.health()
      ])
      setConsultas(consultasData)
      setAtualizacoes(atualizacoesData)
      setHealth(healthData)
    } finally {
      setLoading(false)
    }
  }, [])

  return {
    consultas,
    atualizacoes,
    health,
    loading,
    refresh
  }
}
