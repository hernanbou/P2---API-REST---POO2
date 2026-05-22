import React from 'react'
import { Save, X } from 'lucide-react'

const empty = {
  idPropostaBndes: '',
  tipoOrigem: 'CADASTRO_LOCAL',
  origemDados: 'CADASTRO_LOCAL',
  nomeProponente: '',
  cpfCnpj: '',
  emailProponente: '',
  telefoneProponente: '',
  valorPretendido: '',
  faixaFaturamento: '',
  finalidade: '',
  municipio: '',
  uf: '',
  situacaoAtual: 'EM_ANALISE',
  observacao: ''
}

export default function LocalProposalForm({ editingProposal, onSubmit, onCancel }) {
  const [form, setForm] = React.useState(empty)
  const isEditing = Boolean(editingProposal)

  React.useEffect(() => {
    setForm(editingProposal ? toForm(editingProposal) : empty)
  }, [editingProposal])

  const update = (field, value) => setForm((current) => ({ ...current, [field]: value }))

  const submit = async (event) => {
    event.preventDefault()
    const payload = {
      ...form,
      idPropostaBndes: form.idPropostaBndes ? Number(form.idPropostaBndes) : null,
      valorPretendido: form.valorPretendido ? Number(form.valorPretendido) : null,
      uf: form.uf.toUpperCase()
    }
    await onSubmit(payload, editingProposal?.id)
    if (!isEditing) setForm(empty)
  }

  return (
    <section className="card p-5">
      <div className="flex flex-wrap items-start justify-between gap-3">
        <div>
          <h2 className="text-lg font-bold text-slate-950">{isEditing ? 'Editar proposta local' : 'Cadastro local rapido'}</h2>
          <p className="mt-1 text-sm text-slate-500">CRUD local para demonstrar JPA, H2 e tratamento de 404.</p>
        </div>
        {isEditing && (
          <button className="btn-secondary" onClick={onCancel} type="button">
            <X className="h-4 w-4" />
            Cancelar edicao
          </button>
        )}
      </div>

      <form className="mt-5 grid gap-4 md:grid-cols-3" onSubmit={submit}>
        <label className="md:col-span-2">
          <span className="label">Nome proponente</span>
          <input className="field mt-1" value={form.nomeProponente} onChange={(event) => update('nomeProponente', event.target.value)} required />
        </label>
        <label>
          <span className="label">ID BNDES</span>
          <input className="field mt-1" type="number" value={form.idPropostaBndes} onChange={(event) => update('idPropostaBndes', event.target.value)} />
        </label>
        <label>
          <span className="label">CPF/CNPJ</span>
          <input className="field mt-1" value={form.cpfCnpj} onChange={(event) => update('cpfCnpj', event.target.value)} />
        </label>
        <label>
          <span className="label">Valor pretendido</span>
          <input className="field mt-1" type="number" min="0" value={form.valorPretendido} onChange={(event) => update('valorPretendido', event.target.value)} />
        </label>
        <label>
          <span className="label">Situacao</span>
          <input className="field mt-1" value={form.situacaoAtual} onChange={(event) => update('situacaoAtual', event.target.value)} />
        </label>
        <label>
          <span className="label">UF</span>
          <input className="field mt-1 uppercase" maxLength="2" value={form.uf} onChange={(event) => update('uf', event.target.value.toUpperCase())} />
        </label>
        <label>
          <span className="label">Municipio</span>
          <input className="field mt-1" value={form.municipio} onChange={(event) => update('municipio', event.target.value)} />
        </label>
        <label>
          <span className="label">Finalidade</span>
          <input className="field mt-1" value={form.finalidade} onChange={(event) => update('finalidade', event.target.value)} />
        </label>
        <label>
          <span className="label">Email</span>
          <input className="field mt-1" type="email" value={form.emailProponente} onChange={(event) => update('emailProponente', event.target.value)} />
        </label>
        <label>
          <span className="label">Telefone</span>
          <input className="field mt-1" value={form.telefoneProponente} onChange={(event) => update('telefoneProponente', event.target.value)} />
        </label>
        <label>
          <span className="label">Faixa faturamento</span>
          <input className="field mt-1" value={form.faixaFaturamento} onChange={(event) => update('faixaFaturamento', event.target.value)} />
        </label>
        <label className="md:col-span-3">
          <span className="label">Observacao</span>
          <textarea className="field mt-1 min-h-20" value={form.observacao} onChange={(event) => update('observacao', event.target.value)} />
        </label>
        <div className="md:col-span-3 flex justify-end">
          <button className="btn-primary" type="submit">
            <Save className="h-4 w-4" />
            {isEditing ? 'Salvar edicao' : 'Cadastrar proposta local'}
          </button>
        </div>
      </form>
    </section>
  )
}

function toForm(proposal) {
  return {
    idPropostaBndes: proposal.idPropostaBndes || '',
    tipoOrigem: proposal.tipoOrigem || 'CADASTRO_LOCAL',
    origemDados: proposal.origemDados || 'CADASTRO_LOCAL',
    nomeProponente: proposal.nomeProponente || '',
    cpfCnpj: proposal.cpfCnpj || '',
    emailProponente: proposal.emailProponente || '',
    telefoneProponente: proposal.telefoneProponente || '',
    valorPretendido: proposal.valorPretendido || '',
    faixaFaturamento: proposal.faixaFaturamento || '',
    finalidade: proposal.finalidade || '',
    municipio: proposal.municipio || '',
    uf: proposal.uf || '',
    situacaoAtual: proposal.situacaoAtual || 'EM_ANALISE',
    observacao: proposal.observacao || ''
  }
}
