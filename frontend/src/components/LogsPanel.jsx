import React from 'react'
import { formatCurrency, formatDateTime } from '../utils/formatters'
import Badge from './Badge'
import EmptyState from './EmptyState'

const tabs = [
  { id: 'consultas', label: 'Consultas API' },
  { id: 'atualizacoes', label: 'Atualizacoes' },
  { id: 'propostas', label: 'Propostas locais' }
]

export default function LogsPanel({ consultas, atualizacoes, propostas }) {
  const [active, setActive] = React.useState('consultas')

  return (
    <section className="card p-5 overflow-x-auto">
      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-950">Historico local</h2>
          <p className="mt-1 text-sm text-slate-500">Registros persistidos no H2 durante consultas, CRUD e atualizacoes.</p>
        </div>
        <div className="flex rounded-full bg-purple-50 p-1">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              className={`rounded-full px-3 py-1.5 text-sm font-semibold transition ${active === tab.id ? 'bg-white text-nubank-700 shadow-sm' : 'text-slate-500'}`}
              onClick={() => setActive(tab.id)}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      <div className="mt-5">
        {active === 'consultas' && <ConsultasTable consultas={consultas} />}
        {active === 'atualizacoes' && <AtualizacoesTable atualizacoes={atualizacoes} />}
        {active === 'propostas' && <PropostasResumo propostas={propostas} />}
      </div>
    </section>
  )
}

function ConsultasTable({ consultas }) {
  if (consultas.length === 0) return <EmptyState title="Sem consultas registradas" description="Carregue referencias ou consulte propostas." />

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-slate-100 text-left text-sm">
        <thead className="bg-slate-50 text-xs uppercase text-slate-500">
          <tr>
            <th className="px-4 py-3">ID</th>
            <th className="px-4 py-3">Tipo</th>
            <th className="px-4 py-3">Endpoint</th>
            <th className="px-4 py-3">Status</th>
            <th className="px-4 py-3">Origem</th>
            <th className="px-4 py-3">Data</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {consultas.slice().reverse().slice(0, 10).map((log) => (
            <tr key={log.id}>
              <td className="px-4 py-3 font-semibold">{log.id}</td>
              <td className="px-4 py-3">{log.tipoConsulta}</td>
              <td className="px-4 py-3 text-xs text-slate-500">{log.endpoint}</td>
              <td className="px-4 py-3">{log.statusHttp || '-'}</td>
              <td className="px-4 py-3">
                <Badge>{log.origemDados}</Badge>
              </td>
              <td className="px-4 py-3">{formatDateTime(log.dataHora)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

function AtualizacoesTable({ atualizacoes }) {
  if (atualizacoes.length === 0) return <EmptyState title="Sem atualizacoes registradas" description="Abra uma proposta e registre uma atualizacao." />

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-slate-100 text-left text-sm">
        <thead className="bg-slate-50 text-xs uppercase text-slate-500">
          <tr>
            <th className="px-4 py-3">ID</th>
            <th className="px-4 py-3">ID BNDES</th>
            <th className="px-4 py-3">Tipo</th>
            <th className="px-4 py-3">Modo</th>
            <th className="px-4 py-3">Situacao</th>
            <th className="px-4 py-3">Status</th>
            <th className="px-4 py-3">Data</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {atualizacoes.slice().reverse().slice(0, 10).map((item) => (
            <tr key={item.id}>
              <td className="px-4 py-3 font-semibold">{item.id}</td>
              <td className="px-4 py-3">{item.idPropostaBndes}</td>
              <td className="px-4 py-3">{item.tipoEnvio}</td>
              <td className="px-4 py-3">
                <Badge>{item.modoExecucao}</Badge>
              </td>
              <td className="px-4 py-3">
                <Badge>{item.situacaoProposta}</Badge>
              </td>
              <td className="px-4 py-3">{item.statusHttp}</td>
              <td className="px-4 py-3">{formatDateTime(item.dataHoraEnvio)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

function PropostasResumo({ propostas }) {
  if (propostas.length === 0) return <EmptyState title="Sem propostas locais" description="Consulte a API ou use o cadastro local." />

  return (
    <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
      {propostas.slice().reverse().slice(0, 9).map((proposal) => (
        <div key={proposal.id} className="rounded-lg border border-slate-100 bg-slate-50 p-4">
          <div className="flex items-start justify-between gap-3">
            <div>
              <h3 className="text-sm font-bold text-slate-900">{proposal.nomeProponente}</h3>
              <p className="mt-1 text-xs text-slate-500">{proposal.municipio || '-'} / {proposal.uf || '-'}</p>
            </div>
            <Badge>{proposal.origemDados}</Badge>
          </div>
          <div className="mt-3 flex items-center justify-between gap-3 text-sm">
            <span className="font-semibold">{formatCurrency(proposal.valorPretendido)}</span>
            <Badge>{proposal.situacaoAtual}</Badge>
          </div>
        </div>
      ))}
    </div>
  )
}
