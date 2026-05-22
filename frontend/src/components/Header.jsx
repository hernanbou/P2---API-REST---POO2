import { Landmark } from 'lucide-react'
import Badge from './Badge'

export default function Header() {
  return (
    <header className="overflow-hidden bg-gradient-to-br from-nubank-900 via-nubank-700 to-nubank-500 text-white">
      <div className="mx-auto flex max-w-7xl flex-col gap-6 px-4 py-8 sm:px-6 lg:px-8">
        <div className="flex flex-wrap items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-white/15">
              <Landmark className="h-6 w-6" />
            </div>
            <div>
              <h1 className="text-2xl font-bold sm:text-3xl">Painel de Propostas BNDES - Canal MPME</h1>
              <p className="mt-1 max-w-3xl text-sm text-purple-100 sm:text-base">
                Integracao academica com a API REST do BNDES para consulta e atualizacao de propostas.
              </p>
            </div>
          </div>
          <div className="flex flex-wrap gap-2">
            <Badge>Canal MPME</Badge>
          </div>
        </div>
      </div>
    </header>
  )
}
