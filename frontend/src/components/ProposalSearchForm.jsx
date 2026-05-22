import React from 'react'
import { Search } from 'lucide-react'
import { SITUACOES_PADRAO } from '../constants/situacoes'
import Alert from './Alert'

const today = new Date()
const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)

const initialFilters = {
  tipoConsulta: 'PARCEIRO_FINANCEIRO',
  dataInicio: thirtyDaysAgo.toISOString().slice(0, 10),
  dataFim: today.toISOString().slice(0, 10),
  cnpjOuCpf: '',
  situacao: '',
  valorFinanciamentoMinimo: '',
  valorFinanciamentoMaximo: '',
  ufInvestimento: '',
  municipioInvestimento: '',
  faixaFaturamento: '',
  origemProposta: '',
  codFinalidadeProposta: ''
}

export default function ProposalSearchForm({ situacoes, faixas, loading, onSubmit }) {
  const [filters, setFilters] = React.useState(initialFilters)

  const update = (field, value) => {
    setFilters((current) => ({ ...current, [field]: value }))
  }

  const submit = (event) => {
    event.preventDefault()
    // O aviso fica visivel porque a propria documentacao informa possivel mudanca ENVIADA -> RECEBIDA.
    onSubmit(filters.tipoConsulta, filters)
  }

  const situacaoOptions = situacoes.length > 0 ? situacoes.map((s) => s.situacaoProposta) : SITUACOES_PADRAO
  const financeiro = filters.tipoConsulta === 'PARCEIRO_FINANCEIRO'

  return (
    <section className="card p-5">
      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-950">Consultar propostas</h2>
          <p className="mt-1 text-sm text-slate-500">Busca propostas atribuidas ao parceiro e salva o retorno no H2.</p>
        </div>
      </div>

      <form className="mt-5 space-y-4" onSubmit={submit}>
        <div className="grid gap-4 md:grid-cols-3">
          <label>
            <span className="label">Tipo de consulta</span>
            <select className="field mt-1" value={filters.tipoConsulta} onChange={(event) => update('tipoConsulta', event.target.value)}>
              <option value="PARCEIRO_FINANCEIRO">Parceiro financeiro</option>
              <option value="PARCEIRO_INSTITUCIONAL">Parceiro institucional</option>
            </select>
          </label>
          <label>
            <span className="label">Data inicio</span>
            <input className="field mt-1" type="date" value={filters.dataInicio} onChange={(event) => update('dataInicio', event.target.value)} required />
          </label>
          <label>
            <span className="label">Data fim</span>
            <input className="field mt-1" type="date" value={filters.dataFim} onChange={(event) => update('dataFim', event.target.value)} required />
          </label>
        </div>

        {financeiro && (
          <>
            <Alert type="warning">
              A consulta de propostas financeiras pode alterar propostas ENVIADA para RECEBIDA conforme documentacao da API. Use credenciais de teste ou modo demonstracao.
            </Alert>

            <div className="grid gap-4 md:grid-cols-3">
              <label>
                <span className="label">CPF/CNPJ</span>
                <input className="field mt-1" value={filters.cnpjOuCpf} onChange={(event) => update('cnpjOuCpf', event.target.value)} placeholder="Opcional" />
              </label>
              <label>
                <span className="label">Situacao</span>
                <select className="field mt-1" value={filters.situacao} onChange={(event) => update('situacao', event.target.value)}>
                  <option value="">Todas</option>
                  {situacaoOptions.filter(Boolean).map((situacao) => (
                    <option key={situacao} value={situacao}>
                      {situacao}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                <span className="label">Faixa de faturamento</span>
                <select className="field mt-1" value={filters.faixaFaturamento} onChange={(event) => update('faixaFaturamento', event.target.value)}>
                  <option value="">Todas</option>
                  {faixas.map((faixa) => (
                    <option key={faixa.codigo} value={faixa.codigo}>
                      {faixa.nome}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                <span className="label">Valor minimo</span>
                <input className="field mt-1" type="number" min="0" value={filters.valorFinanciamentoMinimo} onChange={(event) => update('valorFinanciamentoMinimo', event.target.value)} />
              </label>
              <label>
                <span className="label">Valor maximo</span>
                <input className="field mt-1" type="number" min="0" value={filters.valorFinanciamentoMaximo} onChange={(event) => update('valorFinanciamentoMaximo', event.target.value)} />
              </label>
              <label>
                <span className="label">UF</span>
                <input className="field mt-1 uppercase" maxLength="2" value={filters.ufInvestimento} onChange={(event) => update('ufInvestimento', event.target.value.toUpperCase())} placeholder="SP" />
              </label>
              <label>
                <span className="label">Municipio</span>
                <input className="field mt-1" value={filters.municipioInvestimento} onChange={(event) => update('municipioInvestimento', event.target.value)} />
              </label>
              <label>
                <span className="label">Origem da proposta</span>
                <input className="field mt-1" value={filters.origemProposta} onChange={(event) => update('origemProposta', event.target.value)} />
              </label>
              <label>
                <span className="label">Codigo finalidade</span>
                <input className="field mt-1" value={filters.codFinalidadeProposta} onChange={(event) => update('codFinalidadeProposta', event.target.value)} />
              </label>
            </div>
          </>
        )}

        <div className="flex justify-end">
          <button className="btn-primary" type="submit" disabled={loading}>
            <Search className="h-4 w-4" />
            Consultar API BNDES
          </button>
        </div>
      </form>
    </section>
  )
}
