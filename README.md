# Painel de Propostas BNDES - Canal MPME

## Integrante

Hernan Bou  
RA: 2040482523032

## Objetivo

Sistema academico full stack para simular um painel interno de agente financeiro, parceiro institucional ou fintech que consulta e atualiza propostas atribuidas no Canal MPME usando a API REST do BNDES.

O sistema nao e um simulador publico de financiamento e nao aprova credito. Ele demonstra integracao backend com API externa, persistencia local em H2 e interface React para operacao e apresentacao.

## Contexto da API

A API BNDES Canal MPME e voltada a agentes financeiros e parceiros autorizados. Por isso, as credenciais ficam somente no backend, o frontend conversa apenas com rotas locais e os POSTs reais ficam desabilitados por padrao.

## Tecnologias usadas

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA / Hibernate
- H2 Database
- Bean Validation
- RestClient
- Maven
- React com Vite
- JavaScript
- Tailwind CSS
- Axios

## Arquitetura

```text
Browser React/Tailwind
  <-> HTTP/JSON
Controllers Spring Boot
  <->
Services
  <-> BndesApiClient <-> API BNDES
  <->
Repositories
  <->
H2 Database
```

## Funcionalidades

- Listar faixas de faturamento da API BNDES.
- Listar situacoes possiveis de proposta.
- Consultar propostas de parceiro financeiro por periodo e filtros.
- Consultar propostas de parceiro institucional por periodo.
- Atualizar proposta como parceiro financeiro.
- Atualizar proposta como fintech.
- Controlar POST real por `BNDES_ENABLE_REAL_POST`.
- Usar fallback mock honesto por `BNDES_USE_MOCK_ON_FAILURE`.
- Salvar consultas, propostas importadas e atualizacoes no H2.
- CRUD local completo de propostas.
- Historico de consultas e atualizacoes.
- H2 Console habilitado.

## Seguranca

- Credenciais ficam no `.env` da raiz.
- `.env`, `backend/.env` e `frontend/.env` estao no `.gitignore`.
- `API_KEY` representa a Consumer Key.
- `API_SECRET` representa a Consumer Secret.
- O backend monta `Basic base64(API_KEY:API_SECRET)` para gerar token.
- O token e gerado no backend no startup e renovado com `@Scheduled`.
- O frontend nunca recebe token, Basic Auth, API Key ou Consumer Secret.
- POST real fica desabilitado por padrao.

## Configurar `.env`

Crie/edite o arquivo `.env` na raiz do projeto:

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

O arquivo `.env.example` tem os mesmos nomes de variaveis sem credenciais reais.

## Rodar backend

```bash
cd backend
mvn spring-boot:run
```

## Rodar frontend

```bash
cd frontend
npm install
npm run dev
```

## Acessos

- Frontend: http://localhost:5173
- Backend: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

## Dados do H2

- JDBC URL: `jdbc:h2:mem:bndesdb`
- User: `sa`
- Password: em branco

## Endpoints do backend

| Metodo | Rota | Descricao | Status esperado |
|---|---|---|---|
| GET | `/api/health` | Status simples do backend | 200 |
| GET | `/api/health/bndes-token` | Status do token sem expor valor | 200 |
| GET | `/api/bndes/faixas-faturamento` | Consulta faixas de faturamento | 200 |
| GET | `/api/bndes/situacoes` | Consulta situacoes | 200 |
| GET | `/api/bndes/propostas/parceiro-financeiro` | Consulta propostas financeiras | 200 |
| GET | `/api/bndes/propostas/parceiro-institucional` | Consulta propostas institucionais | 200 |
| POST | `/api/bndes/propostas/parceiro-financeiro/atualizar` | Atualiza ou simula atualizacao financeira | 200 |
| POST | `/api/bndes/propostas/fintech/atualizar` | Atualiza ou simula atualizacao fintech | 200 |
| GET | `/api/propostas-locais` | Lista propostas locais | 200 |
| GET | `/api/propostas-locais/{id}` | Busca proposta local por ID | 200 ou 404 |
| POST | `/api/propostas-locais` | Cria proposta local | 201 |
| PUT | `/api/propostas-locais/{id}` | Atualiza proposta local | 200 ou 404 |
| DELETE | `/api/propostas-locais/{id}` | Exclui proposta local | 204 ou 404 |
| GET | `/api/propostas-locais/filtro` | Filtra por nome, UF e situacao | 200 |
| GET | `/api/logs` | Lista logs de chamadas API | 200 |
| GET | `/api/logs/{id}` | Busca log por ID | 200 ou 404 |
| GET | `/api/logs/filtro` | Filtra logs por tipo/sucesso | 200 |
| GET | `/api/atualizacoes` | Lista logs de atualizacao | 200 |
| GET | `/api/atualizacoes/{id}` | Busca atualizacao por ID | 200 ou 404 |
| GET | `/api/atualizacoes/proposta/{idPropostaBndes}` | Lista atualizacoes por proposta | 200 |

## Exemplos JSON

### Atualizacao de proposta

```json
{
  "idProposta": 910001,
  "tipoEnvio": "PARCEIRO_FINANCEIRO",
  "situacaoProposta": "EM_ANALISE",
  "dataSituacaoProposta": "2026-05-22T10:30:00",
  "tipoApoio": "FINANCIAMENTO",
  "valorContratado": 150000.00,
  "taxaJuros": 1.45,
  "taxaDesconto": 0,
  "prazoOperacao": 36,
  "prazoAntecipacao": 0,
  "dataContratacao": "2026-05-22T10:30:00",
  "motivoSituacaoProposta": "Registro academico em modo demonstracao",
  "opcaoGarantia": ["FGI"]
}
```

### Cadastro local

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

### Erro 404

```json
{
  "status": 404,
  "erro": "Recurso nao encontrado",
  "mensagem": "Proposta local nao encontrada",
  "timestamp": "2026-05-22T10:30:00"
}
```

## Entidades

- `ConsultaApiLog`: registra chamadas feitas a API BNDES, status HTTP, origem dos dados, parametros e resumo da resposta ou erro.
- `PropostaLocal`: representa proposta importada da API, mock de apresentacao ou cadastro local manual.
- `AtualizacaoPropostaLog`: registra tentativas reais ou simuladas de atualizacao de propostas.

## Uso de Optional

Os services usam `Optional` nas buscas por ID e em operacoes de atualizar/deletar para tratar ausencia de registro sem retornar `null`. Os controllers convertem ausencia em 404 amigavel.

## Uso de ResponseEntity

Todos os controllers retornam `ResponseEntity`, permitindo controlar 200, 201, 204, 400, 404 e erros tratados pelo `GlobalExceptionHandler`.

## Modo demonstracao dos POSTs

`BNDES_ENABLE_REAL_POST=false` e o padrao seguro. Nesse modo:

- O backend nao chama o POST real do BNDES.
- O JSON que seria enviado e salvo no H2.
- O log recebe `modoExecucao=DEMONSTRACAO`.
- O frontend mostra endpoint, JSON enviado e mensagem local.

Para envio real, altere somente com credenciais e autorizacao validas:

```env
BNDES_ENABLE_REAL_POST=true
```

## Fallback mock honesto

Com `BNDES_USE_MOCK_ON_FAILURE=true`, o backend tenta a API real primeiro. Se houver 401, 403, 500, timeout, SSL ou erro de rede, salva o erro e retorna dados marcados como:

```text
origemDados=MOCK_APRESENTACAO
apiStatus=FALHOU
```

O frontend informa que os dados sao mockados para demonstracao academica.

## Como demonstrar em 15 minutos

1. Explicacao - 4 minutos
   - O projeto e um painel academico para parceiros do Canal MPME.
   - A API e voltada a agentes/parceiros, nao ao usuario final comum.
   - Fluxo principal: React -> Controller -> Service -> Repository -> H2.
   - Integracao externa: Service -> BndesApiClient -> API BNDES.

2. Show me the code - 4 minutos
   - Mostrar `PropostaLocal` com `@Entity` e `@Id`.
   - Mostrar `PropostaLocalRepository extends JpaRepository`.
   - Mostrar `PropostaService` usando `Optional`.
   - Mostrar controllers com `ResponseEntity`.
   - Mostrar `BndesTokenService` com `@PostConstruct` e `@Scheduled`.
   - Mostrar `DotenvLoader` lendo `.env` e `../.env`.
   - Mostrar `.gitignore` ignorando `.env`.

3. Demonstracao pratica - 7 minutos
   - Abrir frontend.
   - Carregar faixas de faturamento.
   - Carregar situacoes.
   - Consultar propostas.
   - Abrir detalhes de uma proposta.
   - Registrar atualizacao em modo demonstracao.
   - Cadastrar proposta local.
   - Editar proposta local.
   - Excluir proposta local.
   - Abrir H2 Console e mostrar `CONSULTA_API_LOG`, `PROPOSTA_LOCAL`, `ATUALIZACAO_PROPOSTA_LOG`.
   - Buscar ID inexistente e mostrar 404 amigavel.

## Converter documentacao em PDF

O ambiente atual nao possui `pandoc`. Para gerar o PDF depois:

```bash
pandoc docs/Documentacao-Painel-Propostas-BNDES.md -o docs/Documentacao-Painel-Propostas-BNDES.pdf
```

## Limitacoes honestas

- Endpoints reais dependem de credenciais, autorizacao e escopos do BNDES.
- Algumas consultas podem nao retornar dados se a credencial nao tiver permissao.
- Mock existe apenas para apresentacao academica quando a API falha.
- POST real fica desabilitado por seguranca.
- Nao ha login/autenticacao de usuario porque isso foge do escopo academico solicitado.
