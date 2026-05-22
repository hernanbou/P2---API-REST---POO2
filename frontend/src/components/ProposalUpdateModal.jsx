import React from 'react'
import { Send, X } from 'lucide-react'
import { GARANTIAS } from '../constants/garantias'
import { SITUACOES_PADRAO } from '../constants/situacoes'
import { TIPOS_APOIO } from '../constants/tipoApoio'
import Alert from './Alert'
import Badge from './Badge'
import LoadingSpinner from './LoadingSpinner'

const nowLocal = () => new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16)

export default function ProposalUpdateModal({ proposal, situacoes, onSubmit, onClose }) {
  const [form, setForm] = React.useState(emptyForm())
  const [loading, setLoading] = React.useState(false)
  const [result, setResult] = React.useState(null)
  const [error, setError] = React.useState(null)

  React.useEffect(() => {
    if (proposal) {
      setForm({
        ...emptyForm(),
        idProposta: proposal.idPropostaBndes || '',
        situacaoProposta: proposal.situacaoAtual || 'EM_ANALISE'
      })
      setResult(null)
      setError(null)
    }
  }, [proposal])

  if (!proposal) return null

  const situacaoOptions = situacoes.length > 0 ? situacoes.map((s) => s.situacaoProposta) : SITUACOES_PADRAO

  const update = (field, value) => setForm((current) => ({ ...current, [field]: value }))

  const toggleGarantia = (garantia) => {
    setForm((current) => {
      const exists = current.opcaoGarantia.includes(garantia)
      return {
        ...current,
        opcaoGarantia: exists
          ? current.opcaoGarantia.filter((item) => item !== garantia)
          : [...current.opcaoGarantia, garantia]
      }
    })
  }

  const submit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError(null)
    try {
      // O backend decide entre modo REAL e DEMONSTRACAO usando BNDES_ENABLE_REAL_POST.
      const payload = normalizePayload(form)
      const response = await onSubmit(payload)
      setResult(response)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-slate-950/55 p-4">
      <div className="max-h-[92vh] w-full max-w-4xl overflow-y-auto rounded-lg bg-white shadow-2xl">
        <div className="sticky top-0 z-10 flex items-center justify-between gap-3 border-b border-slate-100 bg-white px-5 py-4">
          <div>
            <h2 className="text-lg font-bold text-slate-950">Atualizar situacao da proposta</h2>
            <p className="text-sm text-slate-500">{proposal.nomeProponente}</p>
          </div>
          <button className="icon-btn" title="Fechar" onClick={onClose}>
            <X className="h-4 w-4" />
          </button>
        </div>

        <form className="space-y-5 p-5" onSubmit={submit}>
          <Alert type="info">
            Por padrao, o backend registra a atualizacao em modo demonstracao e nao envia POST real ao BNDES.
          </Alert>
          <Alert type="error">{error}</Alert>

          <div className="grid gap-4 md:grid-cols-3">
            <label>
              <span className="label">Tipo de envio</span>
              <select className="field mt-1" value={form.tipoEnvio} onChange={(event) => update('tipoEnvio', event.target.value)}>
                <option value="PARCEIRO_FINANCEIRO">PARCEIRO_FINANCEIRO</option>
                <option value="FINTECH">FINTECH</option>
              </select>
            </label>
            <label>
              <span className="label">ID proposta BNDES</span>
              <input className="field mt-1" type="number" value={form.idProposta} onChange={(event) => update('idProposta', event.target.value)} required />
            </label>
            <label>
              <span className="label">Situacao</span>
              <select className="field mt-1" value={form.situacaoProposta} onChange={(event) => update('situacaoProposta', event.target.value)} required>
                {situacaoOptions.filter(Boolean).map((situacao) => (
                  <option key={situacao} value={situacao}>
                    {situacao}
                  </option>
                ))}
              </select>
            </label>
            <label>
              <span className="label">Data da situacao</span>
              <input className="field mt-1" type="datetime-local" value={form.dataSituacaoProposta} onChange={(event) => update('dataSituacaoProposta', event.target.value)} required />
            </label>
            <label>
              <span className="label">Tipo de apoio</span>
              <select className="field mt-1" value={form.tipoApoio} onChange={(event) => update('tipoApoio', event.target.value)}>
                <option value="">Nao informado</option>
                {TIPOS_APOIO.map((tipo) => (
                  <option key={tipo} value={tipo}>
                    {tipo}
                  </option>
                ))}
              </select>
            </label>
            <label>
              <span className="label">Valor contratado</span>
              <input className="field mt-1" type="number" min="0" value={form.valorContratado} onChange={(event) => update('valorContratado', event.target.value)} />
            </label>
            <label>
              <span className="label">Taxa de juros</span>
              <input className="field mt-1" type="number" min="0" step="0.01" value={form.taxaJuros} onChange={(event) => update('taxaJuros', event.target.value)} />
            </label>
            <label>
              <span className="label">Taxa de desconto</span>
              <input className="field mt-1" type="number" min="0" step="0.01" value={form.taxaDesconto} onChange={(event) => update('taxaDesconto', event.target.value)} />
            </label>
            <label>
              <span className="label">Prazo operacao</span>
              <input className="field mt-1" type="number" min="1" value={form.prazoOperacao} onChange={(event) => update('prazoOperacao', event.target.value)} />
            </label>
            <label>
              <span className="label">Prazo antecipacao</span>
              <input className="field mt-1" type="number" min="0" value={form.prazoAntecipacao} onChange={(event) => update('prazoAntecipacao', event.target.value)} />
            </label>
            <label>
              <span className="label">Data contratacao</span>
              <input className="field mt-1" type="datetime-local" value={form.dataContratacao} onChange={(event) => update('dataContratacao', event.target.value)} />
            </label>
            <label className="md:col-span-3">
              <span className="label">Motivo da situacao</span>
              <textarea className="field mt-1 min-h-24" value={form.motivoSituacaoProposta} onChange={(event) => update('motivoSituacaoProposta', event.target.value)} placeholder="Justificativa opcional para registro local" />
            </label>
          </div>

          <div>
            <span className="label">Opcoes de garantia</span>
            <div className="mt-2 flex flex-wrap gap-2">
              {GARANTIAS.map((garantia) => (
                <label key={garantia} className="inline-flex cursor-pointer items-center gap-2 rounded-full border border-slate-200 px-3 py-2 text-sm">
                  <input type="checkbox" checked={form.opcaoGarantia.includes(garantia)} onChange={() => toggleGarantia(garantia)} />
                  {garantia}
                </label>
              ))}
            </div>
          </div>

          <div className="flex flex-wrap items-center justify-end gap-3">
            {loading && <LoadingSpinner label="Registrando" />}
            <button className="btn-secondary" type="button" onClick={onClose}>
              Cancelar
            </button>
            <button className="btn-primary" type="submit" disabled={loading}>
              <Send className="h-4 w-4" />
              Registrar atualizacao
            </button>
          </div>
        </form>

        {result && (
          <div className="border-t border-slate-100 p-5">
            <div className="flex flex-wrap items-center gap-2">
              <Badge>{result.modoExecucao}</Badge>
              <Badge>{result.sucesso ? 'SUCESSO' : 'ERRO'}</Badge>
              <span className="text-sm text-slate-600">{result.endpointUsado}</span>
            </div>
            <p className="mt-3 text-sm font-medium text-slate-800">{result.mensagem}</p>
            <div className="mt-4 grid gap-4 lg:grid-cols-2">
              <JsonResult title="JSON enviado" value={result.requestJson} />
              <JsonResult title="Resposta" value={result.responseJson} />
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

function emptyForm() {
  return {
    tipoEnvio: 'PARCEIRO_FINANCEIRO',
    idProposta: '',
    situacaoProposta: 'EM_ANALISE',
    dataSituacaoProposta: nowLocal(),
    tipoApoio: '',
    valorContratado: '',
    taxaJuros: '',
    taxaDesconto: '',
    prazoOperacao: '',
    prazoAntecipacao: '',
    dataContratacao: '',
    motivoSituacaoProposta: '',
    opcaoGarantia: []
  }
}

function normalizePayload(form) {
  const numericOrNull = (value) => (value === '' || value === null || value === undefined ? null : Number(value))

  return {
    ...form,
    idProposta: Number(form.idProposta),
    valorContratado: numericOrNull(form.valorContratado),
    taxaJuros: numericOrNull(form.taxaJuros),
    taxaDesconto: numericOrNull(form.taxaDesconto),
    prazoOperacao: numericOrNull(form.prazoOperacao),
    prazoAntecipacao: numericOrNull(form.prazoAntecipacao),
    dataContratacao: form.dataContratacao || null,
    tipoApoio: form.tipoApoio || null,
    motivoSituacaoProposta: form.motivoSituacaoProposta || null
  }
}

function JsonResult({ title, value }) {
  return (
    <div className="rounded-lg bg-slate-950 p-4 text-white">
      <h3 className="text-sm font-semibold">{title}</h3>
      <pre className="mt-3 max-h-64 overflow-auto whitespace-pre-wrap text-xs text-purple-50">
        {formatJson(value)}
      </pre>
    </div>
  )
}

function formatJson(value) {
  if (!value) return '-'
  try {
    return JSON.stringify(JSON.parse(value), null, 2)
  } catch {
    return value
  }
}
