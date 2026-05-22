import { Inbox } from 'lucide-react'

export default function EmptyState({ title = 'Nenhum dado encontrado', description = 'Execute uma consulta ou cadastre uma proposta local.' }) {
  return (
    <div className="flex flex-col items-center justify-center rounded-lg border border-dashed border-purple-200 bg-purple-50/40 px-6 py-10 text-center">
      <Inbox className="h-9 w-9 text-nubank-500" />
      <h3 className="mt-3 text-sm font-semibold text-slate-900">{title}</h3>
      <p className="mt-1 max-w-md text-sm text-slate-500">{description}</p>
    </div>
  )
}
