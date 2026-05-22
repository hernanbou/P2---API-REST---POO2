import { X } from 'lucide-react'
import { formatCurrency, formatDateTime, parseJsonText } from '../utils/formatters'
import Badge from './Badge'

export default function ProposalDetailsModal({ proposal, onClose }) {
  if (!proposal) return null

  const opcoes = parseJsonText(proposal.opcoesApoioJson)
  const historico = parseJsonText(proposal.historicoSituacaoJson)

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-slate-950/55 p-4">
      <div className="max-h-[90vh] w-full max-w-3xl overflow-y-auto rounded-lg bg-white shadow-2xl">
        <div className="sticky top-0 flex items-center justify-between gap-3 border-b border-slate-100 bg-white px-5 py-4">
          <div>
            <h2 className="text-lg font-bold text-slate-950">Detalhes da proposta</h2>
            <p className="text-sm text-slate-500">ID local {proposal.id} | ID BNDES {proposal.idPropostaBndes || '-'}</p>
          </div>
          <button className="icon-btn" title="Fechar" onClick={onClose}>
            <X className="h-4 w-4" />
          </button>
        </div>

        <div className="grid gap-4 p-5 md:grid-cols-2">
          <Detail label="Proponente" value={proposal.nomeProponente} />
          <Detail label="CPF/CNPJ" value={proposal.cpfCnpj} />
          <Detail label="Email" value={proposal.emailProponente} />
          <Detail label="Telefone" value={proposal.telefoneProponente} />
          <Detail label="Valor pretendido" value={formatCurrency(proposal.valorPretendido)} />
          <Detail label="Municipio/UF" value={`${proposal.municipio || '-'} / ${proposal.uf || '-'}`} />
          <Detail label="Finalidade" value={proposal.finalidade} />
          <Detail label="Faixa de faturamento" value={proposal.faixaFaturamento} />
          <Detail label="Data solicitacao" value={formatDateTime(proposal.dataSolicitacao)} />
          <Detail label="Data situacao atual" value={formatDateTime(proposal.dataSituacaoAtual)} />
          <Detail label="Dias na situacao" value={proposal.diasNaSituacaoAtual ?? '-'} />
          <div>
            <span className="label">Situacao atual</span>
            <div className="mt-2">
              <Badge>{proposal.situacaoAtual}</Badge>
            </div>
          </div>
          <div>
            <span className="label">Origem dos dados</span>
            <div className="mt-2">
              <Badge>{proposal.origemDados}</Badge>
            </div>
          </div>
        </div>

        <div className="space-y-4 px-5 pb-5">
          <JsonBlock title="Opcoes de apoio" data={opcoes} />
          <JsonBlock title="Historico de situacao" data={historico} />
          {proposal.observacao && <Detail label="Observacao" value={proposal.observacao} />}
        </div>
      </div>
    </div>
  )
}

function Detail({ label, value }) {
  return (
    <div className="rounded-lg border border-slate-100 bg-slate-50/60 p-3">
      <span className="label">{label}</span>
      <p className="mt-1 break-words text-sm font-medium text-slate-800">{value || '-'}</p>
    </div>
  )
}

function JsonBlock({ title, data }) {
  return (
    <div className="rounded-lg border border-slate-100 bg-slate-950 p-4 text-white">
      <h3 className="text-sm font-semibold">{title}</h3>
      <pre className="mt-3 max-h-56 overflow-auto whitespace-pre-wrap text-xs text-purple-50">
        {JSON.stringify(data, null, 2)}
      </pre>
    </div>
  )
}
