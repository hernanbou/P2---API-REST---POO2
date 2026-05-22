# Painel de Propostas BNDES - Canal MPME

**Integrante:** Hernan Bou - RA: 2040482523032

## 1. Capa e Introducao

O Painel de Propostas BNDES - Canal MPME e um projeto academico full stack criado para demonstrar integracao com a API REST do BNDES, persistencia local em H2 e uma interface React para operacao por agentes financeiros, parceiros institucionais ou fintechs.

O problema resolvido e a centralizacao das operacoes basicas de consulta, acompanhamento e atualizacao de propostas atribuidas no Canal MPME, mantendo logs locais para auditoria academica e demonstracao.

O sistema nao simula aprovacao publica de credito. Ele representa um painel interno de integracao.

## 2. Documentacao da API Backend

### 2.1 Entidades

#### ConsultaApiLog

Registra chamadas feitas para a API BNDES.

Principais atributos:

- `id`: identificador local.
- `tipoConsulta`: FAIXAS_FATURAMENTO, SITUACOES, PROPOSTAS_PARCEIRO_FINANCEIRO ou PROPOSTAS_PARCEIRO_INSTITUCIONAL.
- `metodoHttp`: GET ou POST.
- `endpoint`: endpoint chamado.
- `parametros`: JSON dos parametros enviados.
- `statusHttp`: status recebido.
- `sucesso`: indica sucesso da chamada local/externa.
- `origemDados`: API_BNDES, MOCK_APRESENTACAO ou ERRO_API.
- `mensagem`: mensagem amigavel.
- `erroResumo`: resumo do erro quando houver.
- `respostaResumo`: resumo da resposta.
- `dataHora`: data/hora do registro.

#### PropostaLocal

Representa proposta importada da API, mock ou cadastro local.

Principais atributos:

- `id`: identificador local no H2.
- `idPropostaBndes`: identificador da proposta no BNDES.
- `tipoOrigem`: PARCEIRO_FINANCEIRO, PARCEIRO_INSTITUCIONAL ou CADASTRO_LOCAL.
- `origemDados`: API_BNDES, MOCK_APRESENTACAO ou CADASTRO_LOCAL.
- `nomeProponente`, `cpfCnpj`, `emailProponente`, `telefoneProponente`.
- `valorPretendido`, `faixaFaturamento`, `finalidade`, `municipio`, `uf`.
- `situacaoAtual`, `dataSolicitacao`, `dataSituacaoAtual`, `diasNaSituacaoAtual`.
- `opcoesApoioJson`, `historicoSituacaoJson`, `observacao`.

#### AtualizacaoPropostaLog

Representa uma tentativa de atualizacao de proposta.

Principais atributos:

- `id`: identificador local.
- `idPropostaBndes`: proposta atualizada.
- `tipoEnvio`: PARCEIRO_FINANCEIRO ou FINTECH.
- `endpointUsado`: `/seguro/parceiro/proposta` ou `/fintech/proposta`.
- `modoExecucao`: REAL ou DEMONSTRACAO.
- `situacaoProposta`, `dataSituacaoProposta`, `tipoApoio`.
- `valorContratado`, `taxaJuros`, `taxaDesconto`, `prazoOperacao`.
- `requestJson`, `responseJson`, `statusHttp`, `sucesso`, `mensagem`.

### 2.2 Endpoints

| Metodo | Rota | Descricao | Status esperado |
|---|---|---|---|
| GET | `/api/health` | Status do backend e contadores | 200 |
| GET | `/api/health/bndes-token` | Status do token sem expor valor | 200 |
| GET | `/api/bndes/faixas-faturamento` | Lista faixas aceitas | 200 |
| GET | `/api/bndes/situacoes` | Lista situacoes possiveis | 200 |
| GET | `/api/bndes/propostas/parceiro-financeiro` | Consulta propostas financeiras | 200 |
| GET | `/api/bndes/propostas/parceiro-institucional` | Consulta propostas institucionais | 200 |
| POST | `/api/bndes/propostas/parceiro-financeiro/atualizar` | Atualiza/simula proposta financeira | 200 |
| POST | `/api/bndes/propostas/fintech/atualizar` | Atualiza/simula proposta fintech | 200 |
| GET | `/api/propostas-locais` | Lista propostas locais | 200 |
| GET | `/api/propostas-locais/{id}` | Busca por ID | 200 ou 404 |
| POST | `/api/propostas-locais` | Cria proposta local | 201 |
| PUT | `/api/propostas-locais/{id}` | Atualiza proposta local | 200 ou 404 |
| DELETE | `/api/propostas-locais/{id}` | Exclui proposta local | 204 ou 404 |
| GET | `/api/propostas-locais/filtro` | Filtra propostas locais | 200 |
| GET | `/api/logs` | Lista logs de consultas | 200 |
| GET | `/api/logs/{id}` | Busca log por ID | 200 ou 404 |
| GET | `/api/logs/filtro` | Filtra logs | 200 |
| GET | `/api/atualizacoes` | Lista atualizacoes | 200 |
| GET | `/api/atualizacoes/{id}` | Busca atualizacao por ID | 200 ou 404 |
| GET | `/api/atualizacoes/proposta/{idPropostaBndes}` | Lista atualizacoes por proposta | 200 |

### 2.3 Exemplos JSON

#### POST /api/propostas-locais

```json
{
  "nomeProponente": "Empresa Demo Ltda",
  "cpfCnpj": "12345678000190",
  "valorPretendido": 250000.00,
  "uf": "SP",
  "municipio": "Sao Paulo",
  "finalidade": "Capital de giro",
  "situacaoAtual": "EM_ANALISE"
}
```

#### POST /api/bndes/propostas/parceiro-financeiro/atualizar

```json
{
  "idProposta": 910001,
  "tipoEnvio": "PARCEIRO_FINANCEIRO",
  "situacaoProposta": "EM_ANALISE",
  "dataSituacaoProposta": "2026-05-22T10:30:00",
  "tipoApoio": "FINANCIAMENTO",
  "valorContratado": 150000,
  "taxaJuros": 1.45,
  "prazoOperacao": 36,
  "motivoSituacaoProposta": "Registro academico",
  "opcaoGarantia": ["FGI"]
}
```

#### POST /api/bndes/propostas/fintech/atualizar

```json
{
  "idProposta": 910001,
  "tipoEnvio": "FINTECH",
  "situacaoProposta": "RECEBIDA",
  "dataSituacaoProposta": "2026-05-22T10:30:00",
  "motivoSituacaoProposta": "Atualizacao academica no fluxo fintech",
  "opcaoGarantia": []
}
```

#### Erro 404

```json
{
  "status": 404,
  "erro": "Recurso nao encontrado",
  "mensagem": "Proposta local nao encontrada",
  "timestamp": "2026-05-22T10:30:00"
}
```

## 3. Diagrama de Arquitetura

```text
Browser React/Tailwind
↕ HTTP/JSON
Controllers Spring Boot
↕
Services
↔ BndesApiClient ↔ API BNDES
↕
Repositories
↕
H2 Database
```

## 4. Guia de Execucao

### Pre-requisitos

- Java 17 ou 21.
- Maven.
- Node.js e npm.
- Credenciais BNDES apenas se for testar chamadas autenticadas reais.

### Configurar `.env`

Arquivo na raiz:

```env
API_KEY=chave-secreta
API_SECRET=consumer-secret
BNDES_API_BASE_URL=https://apis-gateway.bndes.gov.br/pme/1.4
BNDES_TOKEN_URL=https://apis-gateway.bndes.gov.br/token
BNDES_ENABLE_REAL_POST=false
BNDES_USE_MOCK_ON_FAILURE=true
BNDES_TOKEN_REFRESH_RATE_MS=3600000
FRONTEND_API_URL=http://localhost:8080/api
```

### Rodar backend

```bash
cd backend
mvn spring-boot:run
```

### Rodar frontend

```bash
cd frontend
npm install
npm run dev
```

### Acessar H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:bndesdb`
- User: `sa`
- Password: em branco

### Testar via navegador

1. Abrir http://localhost:5173.
2. Carregar faixas.
3. Carregar situacoes.
4. Consultar propostas.
5. Abrir detalhes.
6. Registrar atualizacao em modo demonstracao.
7. Cadastrar, editar e excluir proposta local.

### Testar via Postman/Insomnia

Exemplos:

- `GET http://localhost:8080/api/health`
- `GET http://localhost:8080/api/bndes/faixas-faturamento`
- `GET http://localhost:8080/api/propostas-locais`
- `GET http://localhost:8080/api/propostas-locais/999999`
- `POST http://localhost:8080/api/propostas-locais`

## 5. Roteiro de Apresentacao

### Explicacao - 4 minutos

- Explicar que o projeto e um painel academico de integracao.
- Explicar que a API BNDES Canal MPME e para agentes/parceiros.
- Mostrar que o frontend nao tem credenciais.
- Explicar fluxo:

```text
React -> Controller -> Service -> Repository -> H2
Service -> BndesApiClient -> API BNDES
```

### Show me the code - 4 minutos

- `PropostaLocal`: `@Entity`, `@Id`, campos principais.
- `PropostaLocalRepository`: `JpaRepository`.
- `PropostaService`: busca por ID com `Optional`.
- Controllers: retorno com `ResponseEntity`.
- `BndesTokenService`: token no startup com `@PostConstruct` e renovacao com `@Scheduled`.
- `DotenvLoader`: leitura de `.env` e `../.env`.
- `.gitignore`: `.env` ignorado.

### Demonstracao pratica - 7 minutos

- Abrir o frontend.
- Carregar faixas de faturamento.
- Carregar situacoes.
- Consultar propostas.
- Mostrar tabela.
- Abrir modal de detalhes.
- Registrar atualizacao em modo demonstracao.
- Cadastrar proposta local.
- Editar proposta local.
- Excluir proposta local.
- Abrir H2 Console.
- Mostrar tabelas `CONSULTA_API_LOG`, `PROPOSTA_LOCAL`, `ATUALIZACAO_PROPOSTA_LOG`.
- Buscar ID inexistente e mostrar erro 404 amigavel.

## 6. Geracao do PDF

O ambiente usado para gerar este projeto nao tinha `pandoc` instalado. Para converter:

```bash
pandoc docs/Documentacao-Painel-Propostas-BNDES.md -o docs/Documentacao-Painel-Propostas-BNDES.pdf
```

## 7. Observacoes Finais

- Mock nunca e apresentado como dado real.
- POST demonstracao nunca e apresentado como envio real.
- Credenciais e token nao aparecem no frontend.
- O backend e o unico responsavel por chamar a API BNDES.
- O projeto foi mantido simples para apresentacao de 10 a 15 minutos.
