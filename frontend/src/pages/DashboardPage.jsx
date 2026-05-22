import React from 'react'
import { Activity, Clock3, Database, ShieldCheck } from 'lucide-react'
import { bndesApi } from '../api/bndesApi'
import Alert from '../components/Alert'
import Header from '../components/Header'
import LocalProposalForm from '../components/LocalProposalForm'
import LogsPanel from '../components/LogsPanel'
import ProposalDetailsModal from '../components/ProposalDetailsModal'
import ProposalSearchForm from '../components/ProposalSearchForm'
import ProposalTable from '../components/ProposalTable'
import ProposalUpdateModal from '../components/ProposalUpdateModal'
import ReferencePanel from '../components/ReferencePanel'
import StatCard from '../components/StatCard'
import { useLogs } from '../hooks/useLogs'
import { useProposals } from '../hooks/useProposals'
import { useReferences } from '../hooks/useReferences'

export default function DashboardPage() {
  const references = useReferences()
  const proposals = useProposals()
  const logs = useLogs()

  const [detailsProposal, setDetailsProposal] = React.useState(null)
  const [updateProposal, setUpdateProposal] = React.useState(null)
  const [editingProposal, setEditingProposal] = React.useState(null)
  const [tokenStatus, setTokenStatus] = React.useState(null)
  const [globalError, setGlobalError] = React.useState(null)

  const refreshToken = React.useCallback(async () => {
    try {
      const status = await bndesApi.tokenStatus()
      setTokenStatus(status)
    } catch (error) {
      setTokenStatus({ tokenEmMemoria: false, mensagem: error.message })
    }
  }, [])

  const refreshDashboard = React.useCallback(async () => {
    try {
      await Promise.all([logs.refresh(), refreshToken()])
    } catch (error) {
      setGlobalError(error.message)
    }
  }, [logs, refreshToken])

  React.useEffect(() => {
    proposals.loadLocal().catch((error) => setGlobalError(error.message))
    refreshDashboard()
  }, [])

  const handleSearch = async (tipoConsulta, filtros) => {
    setGlobalError(null)
    try {
      await proposals.searchExternal(tipoConsulta, filtros)
      await refreshDashboard()
    } catch (error) {
      setGlobalError(error.message)
    }
  }

  const handleLocalSubmit = async (payload, id) => {
    setGlobalError(null)
    try {
      if (id) {
        await proposals.updateLocal(id, payload)
        setEditingProposal(null)
      } else {
        await proposals.createLocal(payload)
      }
      await refreshDashboard()
    } catch (error) {
      setGlobalError(error.message)
    }
  }

  const handleDelete = async (id) => {
    setGlobalError(null)
    try {
      await proposals.deleteLocal(id)
      await refreshDashboard()
    } catch (error) {
      setGlobalError(error.message)
    }
  }

  const handleUpdateSubmit = async (payload) => {
    const response = await proposals.registerUpdate(payload)
    await refreshDashboard()
    return response
  }

  const tokenHelper = tokenStatus?.mensagem || 'Status ainda nao consultado.'

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-slate-100">
      <Header />

      <main className="mx-auto -mt-2 max-w-7xl space-y-6 px-4 pb-10 sm:px-6 lg:px-8">
        <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
          <StatCard icon={Database} label="Propostas locais" value={logs.health?.totalPropostasLocais ?? proposals.proposals.length} helper="Registros no H2" />
          <StatCard icon={Clock3} label="Consultas feitas" value={logs.health?.totalConsultas ?? logs.consultas.length} helper="Chamadas e fallbacks" />
          <StatCard icon={Activity} label="Atualizacoes" value={logs.health?.totalAtualizacoes ?? logs.atualizacoes.length} helper="REAL ou DEMONSTRACAO" />
          <StatCard icon={ShieldCheck} label="Token BNDES" value={tokenStatus?.tokenEmMemoria ? 'Ativo' : 'Indisponivel'} helper={tokenHelper} />
        </section>

        <Alert type="error">{globalError}</Alert>
        <Alert type={proposals.message?.type}>{proposals.message?.text}</Alert>

        <div className="grid gap-6 xl:grid-cols-[0.95fr_1.05fr]">
          <ReferencePanel
            faixas={references.faixas}
            situacoes={references.situacoes}
            loading={references.loading}
            message={references.message}
            onLoadFaixas={async () => {
              await references.loadFaixas()
              await refreshDashboard()
            }}
            onLoadSituacoes={async () => {
              await references.loadSituacoes()
              await refreshDashboard()
            }}
          />

          <ProposalSearchForm
            situacoes={references.situacoes}
            faixas={references.faixas}
            loading={proposals.loading}
            onSubmit={handleSearch}
          />
        </div>

        <ProposalTable
          proposals={proposals.proposals}
          loading={proposals.loading}
          onDetails={setDetailsProposal}
          onEdit={(proposal) => {
            setEditingProposal(proposal)
            window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })
          }}
          onDelete={handleDelete}
          onUpdate={setUpdateProposal}
        />

        <div className="grid gap-6 xl:grid-cols-[0.85fr_1.15fr]">
          <LocalProposalForm
            editingProposal={editingProposal}
            onSubmit={handleLocalSubmit}
            onCancel={() => setEditingProposal(null)}
          />

          <LogsPanel
            consultas={logs.consultas}
            atualizacoes={logs.atualizacoes}
            propostas={proposals.proposals}
          />
        </div>
      </main>

      <ProposalDetailsModal proposal={detailsProposal} onClose={() => setDetailsProposal(null)} />
      <ProposalUpdateModal
        proposal={updateProposal}
        situacoes={references.situacoes}
        onSubmit={handleUpdateSubmit}
        onClose={() => setUpdateProposal(null)}
      />
    </div>
  )
}
