import { ListChecks, RefreshCcw } from 'lucide-react'
import Alert from './Alert'
import LoadingSpinner from './LoadingSpinner'

export default function ReferencePanel({ faixas, situacoes, loading, message, onLoadFaixas, onLoadSituacoes }) {
  return (
    <section className="card p-5">
      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-950">Referencias da API</h2>
          <p className="mt-1 text-sm text-slate-500">Faixas de faturamento e situacoes usadas nos filtros e no modal.</p>
        </div>
        {loading && <LoadingSpinner />}
      </div>

      <div className="mt-4 flex flex-wrap gap-3">
        <button className="btn-secondary" onClick={onLoadFaixas} disabled={loading}>
          <RefreshCcw className="h-4 w-4" />
          Carregar faixas
        </button>
        <button className="btn-primary" onClick={onLoadSituacoes} disabled={loading}>
          <ListChecks className="h-4 w-4" />
          Carregar situacoes
        </button>
      </div>

      <div className="mt-4">
        <Alert type={message?.type}>{message?.text}</Alert>
      </div>

      <div className="mt-5 grid gap-4 lg:grid-cols-2">
        <div className="rounded-lg border border-slate-100 bg-slate-50/70 p-4">
          <h3 className="text-sm font-semibold text-slate-800">Faixas de faturamento</h3>
          <div className="mt-3 space-y-2">
            {faixas.length === 0 ? (
              <p className="text-sm text-slate-500">Nenhuma faixa carregada.</p>
            ) : (
              faixas.map((faixa) => (
                <div key={faixa.codigo} className="flex items-center gap-3 rounded-lg bg-white px-3 py-2 text-sm">
                  <span className="flex h-7 w-7 items-center justify-center rounded-full bg-purple-50 font-bold text-nubank-700">{faixa.codigo}</span>
                  <span>{faixa.nome}</span>
                </div>
              ))
            )}
          </div>
        </div>

        <div className="rounded-lg border border-slate-100 bg-slate-50/70 p-4">
          <h3 className="text-sm font-semibold text-slate-800">Situacoes</h3>
          <div className="mt-3 grid gap-2 sm:grid-cols-2">
            {situacoes.length === 0 ? (
              <p className="text-sm text-slate-500">Nenhuma situacao carregada.</p>
            ) : (
              situacoes.map((situacao) => (
                <span key={situacao.situacaoProposta || situacao.codigo} className="rounded-lg bg-white px-3 py-2 text-xs font-semibold text-slate-700">
                  {situacao.situacaoProposta || situacao.nome}
                </span>
              ))
            )}
          </div>
        </div>
      </div>
    </section>
  )
}
