export default function StatCard({ icon: Icon, label, value, helper }) {
  return (
    <article className="card p-5">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-slate-500">{label}</p>
          <p className="mt-2 text-3xl font-bold text-slate-950">{value ?? '-'}</p>
          {helper && <p className="mt-1 text-xs text-slate-500">{helper}</p>}
        </div>
        {Icon && (
          <div className="flex h-11 w-11 items-center justify-center rounded-full bg-purple-50 text-nubank-700">
            <Icon className="h-5 w-5" />
          </div>
        )}
      </div>
    </article>
  )
}
