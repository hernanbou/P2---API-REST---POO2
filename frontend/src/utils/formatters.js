export const formatCurrency = (value) => {
  if (value === null || value === undefined || value === '') return '-'
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(Number(value))
}

export const formatDateTime = (value) => {
  if (!value) return '-'
  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short',
    timeStyle: 'short'
  }).format(new Date(value))
}

export const formatDate = (date) => new Date(date).toISOString().slice(0, 10)

export const parseJsonText = (value) => {
  if (!value) return []
  try {
    return JSON.parse(value)
  } catch {
    return [value]
  }
}
