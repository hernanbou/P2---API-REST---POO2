package br.com.hernan.bndes.service;

import br.com.hernan.bndes.client.BndesApiClient;
import br.com.hernan.bndes.dto.ApiEnvelopeResponse;
import br.com.hernan.bndes.dto.BndesApiResult;
import br.com.hernan.bndes.dto.PropostaFiltroRequest;
import br.com.hernan.bndes.dto.PropostaLocalRequest;
import br.com.hernan.bndes.dto.PropostaLocalResponse;
import br.com.hernan.bndes.exception.ApiBndesException;
import br.com.hernan.bndes.model.PropostaLocal;
import br.com.hernan.bndes.repository.PropostaLocalRepository;
import br.com.hernan.bndes.util.MockDataFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PropostaService {

    private final PropostaLocalRepository repository;
    private final BndesApiClient apiClient;
    private final ConsultaApiLogService logService;
    private final ObjectMapper objectMapper;
    private final boolean useMockOnFailure;

    public PropostaService(
            PropostaLocalRepository repository,
            BndesApiClient apiClient,
            ConsultaApiLogService logService,
            ObjectMapper objectMapper,
            @Value("${bndes.api.use-mock-on-failure:true}") boolean useMockOnFailure
    ) {
        this.repository = repository;
        this.apiClient = apiClient;
        this.logService = logService;
        this.objectMapper = objectMapper;
        this.useMockOnFailure = useMockOnFailure;
    }

    @Transactional
    public ApiEnvelopeResponse<List<PropostaLocalResponse>> consultarParceiroFinanceiro(PropostaFiltroRequest filtros) {
        BndesApiResult<List<Map<String, Object>>> resultado = apiClient.consultarPropostasParceiroFinanceiro(filtros);
        logService.registrarResultado("PROPOSTAS_PARCEIRO_FINANCEIRO", "GET", filtros, resultado);

        if (resultado.sucesso()) {
            List<PropostaLocalResponse> propostas = resultado.body().stream()
                    .map(raw -> fromFinanceiro(raw, "API_BNDES"))
                    .map(this::salvarOuAtualizarImportada)
                    .map(PropostaLocalResponse::from)
                    .toList();
            return new ApiEnvelopeResponse<>(propostas, "API_BNDES", "OK", "Propostas financeiras importadas da API BNDES e salvas no H2.");
        }

        if (useMockOnFailure) {
            List<PropostaLocalResponse> mock = MockDataFactory.propostasFinanceiras().stream()
                    .map(this::salvarOuAtualizarImportada)
                    .map(PropostaLocalResponse::from)
                    .toList();
            logService.registrarMock("PROPOSTAS_PARCEIRO_FINANCEIRO", "GET", resultado.endpoint(), filtros,
                    "Fallback honesto: propostas financeiras mockadas para apresentacao academica.", mock);
            return new ApiEnvelopeResponse<>(mock, "MOCK_APRESENTACAO", "FALHOU",
                    "A API BNDES nao respondeu ou nao autorizou a chamada. Exibindo dados mockados para demonstracao academica.");
        }

        throw new ApiBndesException(resultado.mensagem(), HttpStatus.BAD_GATEWAY);
    }

    @Transactional
    public ApiEnvelopeResponse<List<PropostaLocalResponse>> consultarParceiroInstitucional(LocalDate dataInicio, LocalDate dataFim) {
        BndesApiResult<List<Map<String, Object>>> resultado = apiClient.consultarPropostasParceiroInstitucional(dataInicio, dataFim);
        logService.registrarResultado("PROPOSTAS_PARCEIRO_INSTITUCIONAL", "GET", Map.of("dataInicio", dataInicio, "dataFim", dataFim), resultado);

        if (resultado.sucesso()) {
            List<PropostaLocalResponse> propostas = resultado.body().stream()
                    .map(raw -> fromInstitucional(raw, "API_BNDES"))
                    .map(this::salvarOuAtualizarImportada)
                    .map(PropostaLocalResponse::from)
                    .toList();
            return new ApiEnvelopeResponse<>(propostas, "API_BNDES", "OK", "Propostas institucionais importadas da API BNDES e salvas no H2.");
        }

        if (useMockOnFailure) {
            List<PropostaLocalResponse> mock = MockDataFactory.propostasInstitucionais().stream()
                    .map(this::salvarOuAtualizarImportada)
                    .map(PropostaLocalResponse::from)
                    .toList();
            logService.registrarMock("PROPOSTAS_PARCEIRO_INSTITUCIONAL", "GET", resultado.endpoint(), Map.of("dataInicio", dataInicio, "dataFim", dataFim),
                    "Fallback honesto: propostas institucionais mockadas para apresentacao academica.", mock);
            return new ApiEnvelopeResponse<>(mock, "MOCK_APRESENTACAO", "FALHOU",
                    "A API BNDES nao respondeu ou nao autorizou a chamada. Exibindo dados mockados para demonstracao academica.");
        }

        throw new ApiBndesException(resultado.mensagem(), HttpStatus.BAD_GATEWAY);
    }

    @Transactional(readOnly = true)
    public List<PropostaLocalResponse> listar() {
        return repository.findAll().stream().map(PropostaLocalResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Optional<PropostaLocalResponse> buscarPorId(Long id) {
        return repository.findById(id).map(PropostaLocalResponse::from);
    }

    @Transactional
    public PropostaLocalResponse criar(PropostaLocalRequest request) {
        PropostaLocal proposta = new PropostaLocal();
        aplicarRequest(proposta, request);
        if (isBlank(proposta.getTipoOrigem())) {
            proposta.setTipoOrigem("CADASTRO_LOCAL");
        }
        if (isBlank(proposta.getOrigemDados())) {
            proposta.setOrigemDados("CADASTRO_LOCAL");
        }
        if (isBlank(proposta.getSituacaoAtual())) {
            proposta.setSituacaoAtual("EM_ANALISE");
        }
        return PropostaLocalResponse.from(repository.save(proposta));
    }

    @Transactional
    public Optional<PropostaLocalResponse> atualizar(Long id, PropostaLocalRequest request) {
        return repository.findById(id).map(proposta -> {
            aplicarRequest(proposta, request);
            return PropostaLocalResponse.from(repository.save(proposta));
        });
    }

    @Transactional
    public Optional<PropostaLocalResponse> deletar(Long id) {
        return repository.findById(id).map(proposta -> {
            PropostaLocalResponse response = PropostaLocalResponse.from(proposta);
            repository.delete(proposta);
            return response;
        });
    }

    @Transactional(readOnly = true)
    public List<PropostaLocalResponse> filtrar(String nome, String uf, String situacao) {
        return repository.findAll().stream()
                .filter(p -> isBlank(nome) || containsIgnoreCase(p.getNomeProponente(), nome))
                .filter(p -> isBlank(uf) || equalsIgnoreCase(p.getUf(), uf))
                .filter(p -> isBlank(situacao) || equalsIgnoreCase(p.getSituacaoAtual(), situacao))
                .map(PropostaLocalResponse::from)
                .toList();
    }

    private PropostaLocal salvarOuAtualizarImportada(PropostaLocal importada) {
        PropostaLocal destino = Optional.ofNullable(importada.getIdPropostaBndes())
                .flatMap(repository::findByIdPropostaBndes)
                .orElseGet(PropostaLocal::new);
        copiar(importada, destino);
        destino.setDataImportacao(LocalDateTime.now());
        return repository.save(destino);
    }

    private void aplicarRequest(PropostaLocal proposta, PropostaLocalRequest request) {
        proposta.setIdPropostaBndes(request.getIdPropostaBndes());
        proposta.setTipoOrigem(request.getTipoOrigem());
        proposta.setOrigemDados(request.getOrigemDados());
        proposta.setNomeProponente(request.getNomeProponente());
        proposta.setCpfCnpj(request.getCpfCnpj());
        proposta.setEmailProponente(request.getEmailProponente());
        proposta.setTelefoneProponente(request.getTelefoneProponente());
        proposta.setValorPretendido(request.getValorPretendido());
        proposta.setFaixaFaturamento(request.getFaixaFaturamento());
        proposta.setFinalidade(request.getFinalidade());
        proposta.setMunicipio(request.getMunicipio());
        proposta.setUf(request.getUf());
        proposta.setSituacaoAtual(request.getSituacaoAtual());
        proposta.setDataSolicitacao(request.getDataSolicitacao());
        proposta.setDataSituacaoAtual(request.getDataSituacaoAtual());
        proposta.setDiasNaSituacaoAtual(request.getDiasNaSituacaoAtual());
        proposta.setOpcoesApoioJson(request.getOpcoesApoioJson());
        proposta.setHistoricoSituacaoJson(request.getHistoricoSituacaoJson());
        proposta.setObservacao(request.getObservacao());
    }

    private void copiar(PropostaLocal origem, PropostaLocal destino) {
        destino.setIdPropostaBndes(origem.getIdPropostaBndes());
        destino.setTipoOrigem(origem.getTipoOrigem());
        destino.setOrigemDados(origem.getOrigemDados());
        destino.setNomeProponente(origem.getNomeProponente());
        destino.setCpfCnpj(origem.getCpfCnpj());
        destino.setEmailProponente(origem.getEmailProponente());
        destino.setTelefoneProponente(origem.getTelefoneProponente());
        destino.setValorPretendido(origem.getValorPretendido());
        destino.setFaixaFaturamento(origem.getFaixaFaturamento());
        destino.setFinalidade(origem.getFinalidade());
        destino.setMunicipio(origem.getMunicipio());
        destino.setUf(origem.getUf());
        destino.setSituacaoAtual(origem.getSituacaoAtual());
        destino.setDataSolicitacao(origem.getDataSolicitacao());
        destino.setDataSituacaoAtual(origem.getDataSituacaoAtual());
        destino.setDiasNaSituacaoAtual(origem.getDiasNaSituacaoAtual());
        destino.setOpcoesApoioJson(origem.getOpcoesApoioJson());
        destino.setHistoricoSituacaoJson(origem.getHistoricoSituacaoJson());
        destino.setObservacao(origem.getObservacao());
    }

    private PropostaLocal fromFinanceiro(Map<String, Object> raw, String origemDados) {
        Map<String, Object> solicitacao = asMap(raw.get("solicitacao"));
        Map<String, Object> municipioInvestimento = asMap(first(solicitacao, "municipioInvestimento", "municipioProponente"));
        Map<String, Object> uf = asMap(first(municipioInvestimento, "uf"));

        PropostaLocal proposta = new PropostaLocal();
        proposta.setIdPropostaBndes(toLong(first(raw, "id", "idProposta", "idPropostaBndes", "idPropostaDesignada", "identificador")));
        proposta.setTipoOrigem("PARCEIRO_FINANCEIRO");
        proposta.setOrigemDados(origemDados);
        proposta.setNomeProponente(text(first(solicitacao, "nomeProponente", "nomeRazaoSocialProponente")));
        proposta.setCpfCnpj(firstText(solicitacao, "numeroCnpjProponente", "numeroCpfProponente", "cnpjOuCpf"));
        proposta.setEmailProponente(text(first(solicitacao, "emailProponente")));
        proposta.setTelefoneProponente(text(first(solicitacao, "telefoneProponente")));
        proposta.setValorPretendido(toBigDecimal(first(solicitacao, "valorPretendido", "valorFinanciamento")));
        proposta.setFaixaFaturamento(text(first(solicitacao, "faixaFaturamentoProponente", "faixaFaturamento")));
        proposta.setFinalidade(text(first(solicitacao, "finalidade", "finalidades")));
        proposta.setMunicipio(text(first(municipioInvestimento, "nome", "municipio")));
        proposta.setUf(text(first(uf, "sigla", "nome", "codigo")));
        proposta.setSituacaoAtual(text(first(raw, "situacaoAtual", "situacaoProposta")));
        proposta.setDataSolicitacao(toLocalDateTime(first(solicitacao, "dataSolicitacao", "dataEnvio")));
        proposta.setDataSituacaoAtual(toLocalDateTime(first(raw, "dataSituacaoAtual")));
        proposta.setDiasNaSituacaoAtual(toInteger(first(raw, "diasNaSituacaoAtual")));
        proposta.setOpcoesApoioJson(toJson(first(raw, "opcoesApoio", "tipoApoio", "opcaoGarantia")));
        proposta.setHistoricoSituacaoJson(toJson(first(raw, "historicoSituacao")));
        proposta.setObservacao("Importada da consulta de parceiro financeiro do Canal MPME.");
        return proposta;
    }

    private PropostaLocal fromInstitucional(Map<String, Object> raw, String origemDados) {
        PropostaLocal proposta = new PropostaLocal();
        proposta.setIdPropostaBndes(toLong(first(raw, "idProposta", "id", "idPropostaBndes")));
        proposta.setTipoOrigem("PARCEIRO_INSTITUCIONAL");
        proposta.setOrigemDados(origemDados);
        proposta.setNomeProponente(firstText(raw, "nomeRazaoSocialProponente", "nomeProponente"));
        proposta.setCpfCnpj(firstText(raw, "numeroCnpjProponente", "numeroCpfProponente", "cnpjOuCpf"));
        proposta.setEmailProponente(text(first(raw, "emailProponente")));
        proposta.setTelefoneProponente(text(first(raw, "telefoneProponente")));
        proposta.setValorPretendido(toBigDecimal(first(raw, "valorPretendido")));
        proposta.setFaixaFaturamento(text(first(raw, "faixaFaturamentoProponente")));
        proposta.setFinalidade(firstText(raw, "finalidade", "descricaoNecessidadeApoio", "linhaCreditoSugeridaBNDES"));
        proposta.setMunicipio(text(first(raw, "municipioProponente", "municipioInvestimento")));
        proposta.setUf(text(first(raw, "ufProponente", "ufInvestimento")));
        proposta.setSituacaoAtual(firstText(raw, "situacaoAtual", "situacaoProposta"));
        proposta.setDataSolicitacao(toLocalDateTime(first(raw, "dataSolicitacao")));
        proposta.setDataSituacaoAtual(toLocalDateTime(first(raw, "dataSituacaoAtual")));
        proposta.setDiasNaSituacaoAtual(toInteger(first(raw, "diasNaSituacaoAtual")));
        proposta.setOpcoesApoioJson(toJson(first(raw, "linhaCreditoSugeridaBNDES")));
        proposta.setHistoricoSituacaoJson(toJson(first(raw, "historicoSituacao")));
        proposta.setObservacao("Importada da consulta de parceiro institucional do Canal MPME.");
        return proposta;
    }

    private Map<String, Object> asMap(Object value) {
        if (!(value instanceof Map<?, ?>)) {
            return Map.of();
        }
        return objectMapper.convertValue(value, new TypeReference<>() {
        });
    }

    private Object first(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key) && map.get(key) != null) {
                return map.get(key);
            }
        }
        return null;
    }

    private String firstText(Map<String, Object> map, String... keys) {
        return text(first(map, keys));
    }

    private String text(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map<?, ?> map) {
            Object readable = firstMapValue(map, "nome", "descricao", "situacaoProposta", "sigla", "codigo");
            return readable == null ? toJson(value) : readable.toString();
        }
        if (value instanceof List<?>) {
            return toJson(value);
        }
        return value.toString();
    }

    private Object firstMapValue(Map<?, ?> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key) && map.get(key) != null) {
                return map.get(key);
            }
        }
        return null;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        if (value == null || value.toString().isBlank()) {
            return null;
        }
        return new BigDecimal(value.toString().replace(",", "."));
    }

    private Long toLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null || value.toString().isBlank()) {
            return null;
        }
        return Long.parseLong(value.toString());
    }

    private Integer toInteger(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value == null || value.toString().isBlank()) {
            return null;
        }
        return Integer.parseInt(value.toString());
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }
        if (value == null || value.toString().isBlank()) {
            return null;
        }
        String text = value.toString();
        try {
            if (text.length() == 10) {
                return LocalDate.parse(text).atStartOfDay();
            }
            if (text.endsWith("Z") || text.contains("+")) {
                return OffsetDateTime.parse(text).toLocalDateTime();
            }
            return LocalDateTime.parse(text);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return value.toString();
        }
    }

    private boolean containsIgnoreCase(String base, String search) {
        return base != null && search != null && base.toLowerCase().contains(search.toLowerCase());
    }

    private boolean equalsIgnoreCase(String base, String other) {
        return base != null && other != null && base.equalsIgnoreCase(other);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
