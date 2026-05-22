const variants = {
  ENVIADA: 'bg-blue-50 text-blue-700 border-blue-200',
  RECEBIDA: 'bg-purple-50 text-purple-700 border-purple-200',
  EM_ANALISE: 'bg-amber-50 text-amber-700 border-amber-200',
  EM_NEGOCIACAO: 'bg-cyan-50 text-cyan-700 border-cyan-200',
  CONTRATADA: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  RECUSADA: 'bg-rose-50 text-rose-700 border-rose-200',
  CANCELADA: 'bg-slate-100 text-slate-700 border-slate-200',
  EXPIRADA: 'bg-orange-50 text-orange-700 border-orange-200',
  API_BNDES: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  MOCK_APRESENTACAO: 'bg-amber-50 text-amber-700 border-amber-200',
  CADASTRO_LOCAL: 'bg-slate-100 text-slate-700 border-slate-200',
  DEMONSTRACAO: 'bg-purple-50 text-purple-700 border-purple-200',
  REAL: 'bg-emerald-50 text-emerald-700 border-emerald-200'
}

export default function Badge({ children, tone }) {
  const key = tone || children
  return (
    <span className={`inline-flex items-center rounded-full border px-2.5 py-1 text-xs font-semibold ${variants[key] || 'bg-slate-50 text-slate-700 border-slate-200'}`}>
      {children || '-'}
    </span>
  )
}
