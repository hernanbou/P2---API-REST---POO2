/**
 * @typedef {Object} ApiEnvelopeResponse
 * @property {*} dados Dados retornados pelo backend.
 * @property {'API_BNDES'|'MOCK_APRESENTACAO'|'ERRO_API'} origemDados Origem declarada pelo backend.
 * @property {'OK'|'FALHOU'} apiStatus Status da tentativa contra a API externa.
 * @property {string} mensagem Mensagem honesta para exibicao no painel.
 */

/**
 * @typedef {Object} PropostaLocal
 * @property {number} id Identificador local no H2.
 * @property {number} idPropostaBndes Identificador da proposta no BNDES, quando houver.
 * @property {'PARCEIRO_FINANCEIRO'|'PARCEIRO_INSTITUCIONAL'|'CADASTRO_LOCAL'} tipoOrigem Tipo de origem da proposta.
 * @property {'API_BNDES'|'MOCK_APRESENTACAO'|'CADASTRO_LOCAL'} origemDados Origem real dos dados.
 * @property {string} nomeProponente Nome ou razao social.
 * @property {string} cpfCnpj CPF/CNPJ do proponente.
 * @property {number} valorPretendido Valor solicitado.
 * @property {string} uf UF de investimento ou proponente.
 * @property {string} situacaoAtual Situacao atual registrada.
 */

/**
 * @typedef {Object} AtualizacaoPropostaRequest
 * @property {number} idProposta ID da proposta no BNDES.
 * @property {'PARCEIRO_FINANCEIRO'|'FINTECH'} tipoEnvio Define qual endpoint do backend sera usado.
 * @property {string} situacaoProposta Nova situacao.
 * @property {string} dataSituacaoProposta Data/hora da situacao em ISO local.
 * @property {string[]} opcaoGarantia Garantias informadas no payload.
 */

export const bndesTypesDocumented = true
