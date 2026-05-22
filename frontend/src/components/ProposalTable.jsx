import { Eye, Pencil, Send, Trash2 } from 'lucide-react'
import { formatCurrency } from '../utils/formatters'
import Badge from './Badge'
import EmptyState from './EmptyState'

export default function ProposalTable({ proposals, loading, onDetails, onEdit, onDelete, onUpdate }) {
  if (!loading && proposals.length === 0) {
    return <EmptyState title="Nenhuma proposta na tabela" description="Consulte a API BNDES ou cadastre uma proposta local." />
  }

  return (
    <section className="card overflow-hidden">
      <div className="flex items-center justify-between gap-3 border-b border-slate-100 px-5 py-4">
        <div>
          <h2 className="text-lg font-bold text-slate-950">Tabela de propostas</h2>
          <p className="text-sm text-slate-500">{proposals.length} registro(s) exibido(s)</p>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-slate-100 text-left text-sm">
          <thead className="bg-slate-50 text-xs uppercase text-slate-500">
            <tr>
              <th className="px-5 py-3">ID local</th>
              <th className="px-5 py-3">ID BNDES</th>
              <th className="px-5 py-3">Proponente</th>
              <th className="px-5 py-3">CPF/CNPJ</th>
              <th className="px-5 py-3">Valor pretendido</th>
              <th className="px-5 py-3">UF</th>
              <th className="px-5 py-3">Situacao</th>
              <th className="px-5 py-3">Origem</th>
              <th className="px-5 py-3 text-right">Acoes</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 bg-white">
            {proposals.map((proposal) => (
              <tr key={proposal.id} className="hover:bg-purple-50/40">
                <td className="px-5 py-4 font-semibold text-slate-800">{proposal.id}</td>
                <td className="px-5 py-4">{proposal.idPropostaBndes || '-'}</td>
                <td className="px-5 py-4">
                  <div className="max-w-[220px] truncate font-semibold text-slate-900" title={proposal.nomeProponente}>
                    {proposal.nomeProponente || '-'}
                  </div>
                  <div className="max-w-[220px] truncate text-xs text-slate-500">{proposal.emailProponente || ''}</div>
                </td>
                <td className="px-5 py-4">{proposal.cpfCnpj || '-'}</td>
                <td className="px-5 py-4 font-semibold">{formatCurrency(proposal.valorPretendido)}</td>
                <td className="px-5 py-4">{proposal.uf || '-'}</td>
                <td className="px-5 py-4">
                  <Badge>{proposal.situacaoAtual || '-'}</Badge>
                </td>
                <td className="px-5 py-4">
                  <Badge>{proposal.origemDados || '-'}</Badge>
                </td>
                <td className="px-5 py-4">
                  <div className="flex justify-end gap-2">
                    <button className="icon-btn" title="Ver detalhes" onClick={() => onDetails(proposal)}>
                      <Eye className="h-4 w-4" />
                    </button>
                    <button className="icon-btn" title="Editar local" onClick={() => onEdit(proposal)}>
                      <Pencil className="h-4 w-4" />
                    </button>
                    <button className="icon-btn" title="Atualizar situacao" onClick={() => onUpdate(proposal)}>
                      <Send className="h-4 w-4" />
                    </button>
                    <button
                      className="icon-btn hover:border-rose-300 hover:text-rose-700"
                      title="Excluir local"
                      onClick={() => {
                        if (window.confirm('Excluir esta proposta local do H2?')) onDelete(proposal.id)
                      }}
                    >
                      <Trash2 className="h-4 w-4" />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  )
}
