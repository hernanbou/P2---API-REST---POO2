package br.com.hernan.bndes.client;

import br.com.hernan.bndes.dto.AtualizacaoPropostaRequest;
import br.com.hernan.bndes.dto.BndesApiResult;
import br.com.hernan.bndes.dto.PropostaFiltroRequest;
import br.com.hernan.bndes.service.BndesTokenService;
import br.com.hernan.bndes.util.AtualizacaoPayloadMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BndesApiClient {

    private final RestClient restClient;
    private final BndesTokenService tokenService;
    private final ObjectMapper objectMapper;

    public BndesApiClient(
            RestClient.Builder restClientBuilder,
            BndesTokenService tokenService,
            ObjectMapper objectMapper,
            @Value("${bndes.api.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    public BndesApiResult<List<Map<String, Object>>> listarFaixasFaturamento() {
        return executeListGet("/seguro/faixaFaturamentoBNDES", new LinkedMultiValueMap<>(), false);
    }

    public BndesApiResult<List<Map<String, Object>>> listarSituacoes() {
        return executeListGet("/seguro/situacao", new LinkedMultiValueMap<>(), false);
    }

    public BndesApiResult<List<Map<String, Object>>> consultarPropostasParceiroFinanceiro(PropostaFiltroRequest filtros) {
        String endpoint = "/seguro/proposta/de/" + filtros.getDataInicio() + "/ate/" + filtros.getDataFim();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        add(params, "cnpjOuCpf", filtros.getCnpjOuCpf());
        add(params, "situacao", filtros.getSituacao());
        add(params, "valorFinanciamentoMinimo", filtros.getValorFinanciamentoMinimo());
        add(params, "valorFinanciamentoMaximo", filtros.getValorFinanciamentoMaximo());
        add(params, "ufInvestimento", filtros.getUfInvestimento());
        add(params, "municipioInvestimento", filtros.getMunicipioInvestimento());
        add(params, "faixaFaturamento", filtros.getFaixaFaturamento());
        add(params, "origemProposta", filtros.getOrigemProposta());
        add(params, "codFinalidadeProposta", filtros.getCodFinalidadeProposta());
        return executeListGet(endpoint, params, true);
    }

    public BndesApiResult<List<Map<String, Object>>> consultarPropostasParceiroInstitucional(LocalDate dataInicio, LocalDate dataFim) {
        String endpoint = "/parceiro/proposta/de/" + dataInicio + "/ate/" + dataFim;
        return executeListGet(endpoint, new LinkedMultiValueMap<>(), true);
    }

    public BndesApiResult<String> atualizarPropostaParceiroFinanceiro(AtualizacaoPropostaRequest request) {
        return executePost("/seguro/parceiro/proposta", AtualizacaoPayloadMapper.toBndesPayload(request), true);
    }

    public BndesApiResult<String> atualizarPropostaFintech(AtualizacaoPropostaRequest request) {
        return executePost("/fintech/proposta", AtualizacaoPayloadMapper.toBndesPayload(request), true);
    }

    private BndesApiResult<List<Map<String, Object>>> executeListGet(
            String endpoint,
            MultiValueMap<String, String> params,
            boolean authenticated
    ) {
        Optional<String> token = tokenIfNeeded(authenticated, endpoint);
        if (authenticated && token.isEmpty()) {
            return BndesApiResult.falha(503, endpoint, "Token BNDES indisponivel para chamada autenticada.", "Token ausente no backend.");
        }

        try {
            RestClient.RequestHeadersSpec<?> request = restClient.get()
                    .uri(uriBuilder -> buildUri(uriBuilder.path(endpoint), params))
                    .accept(MediaType.APPLICATION_JSON);

            if (authenticated) {
                request = request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get());
            }

            ResponseEntity<String> response = request.retrieve().toEntity(String.class);
            String body = response.getBody() == null ? "" : response.getBody();
            return BndesApiResult.sucesso(parseList(body), response.getStatusCode().value(), endpoint, resumo(body));
        } catch (RestClientResponseException ex) {
            return BndesApiResult.falha(
                    ex.getStatusCode().value(),
                    endpoint,
                    mensagemHttp(ex.getStatusCode().value()),
                    resumo(ex.getResponseBodyAsString())
            );
        } catch (RestClientException | JsonProcessingException ex) {
            return BndesApiResult.falha(503, endpoint, "Falha de rede, SSL, timeout ou parsing ao chamar API BNDES.", resumo(ex.getMessage()));
        }
    }

    private BndesApiResult<String> executePost(String endpoint, Map<String, Object> payload, boolean authenticated) {
        Optional<String> token = tokenIfNeeded(authenticated, endpoint);
        if (authenticated && token.isEmpty()) {
            return BndesApiResult.falha(503, endpoint, "Token BNDES indisponivel para chamada autenticada.", "Token ausente no backend.");
        }

        try {
            RestClient.RequestBodySpec request = restClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            if (authenticated) {
                request = request.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get());
            }

            ResponseEntity<String> response = request.body(payload).retrieve().toEntity(String.class);
            String body = response.getBody() == null ? "" : response.getBody();
            return BndesApiResult.sucesso(body, response.getStatusCode().value(), endpoint, resumo(body));
        } catch (RestClientResponseException ex) {
            return BndesApiResult.falha(
                    ex.getStatusCode().value(),
                    endpoint,
                    mensagemHttp(ex.getStatusCode().value()),
                    resumo(ex.getResponseBodyAsString())
            );
        } catch (RestClientException ex) {
            return BndesApiResult.falha(503, endpoint, "Falha de rede, SSL ou timeout ao chamar API BNDES.", resumo(ex.getMessage()));
        }
    }

    private Optional<String> tokenIfNeeded(boolean authenticated, String endpoint) {
        if (!authenticated) {
            return Optional.empty();
        }
        // O cliente sempre pede o token ao service. Se expirar ou nao existir, o service tenta renovar antes da chamada.
        return tokenService.getAccessToken();
    }

    private URI buildUri(org.springframework.web.util.UriBuilder uriBuilder, MultiValueMap<String, String> params) {
        params.forEach((key, values) -> values.forEach(value -> uriBuilder.queryParam(key, value)));
        return uriBuilder.build();
    }

    private void add(MultiValueMap<String, String> params, String key, Object value) {
        if (value instanceof String text && StringUtils.hasText(text)) {
            params.add(key, text);
        } else if (value instanceof BigDecimal decimal) {
            params.add(key, decimal.toPlainString());
        } else if (value != null) {
            params.add(key, value.toString());
        }
    }

    private List<Map<String, Object>> parseList(String json) throws JsonProcessingException {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        Object raw = objectMapper.readValue(json, Object.class);
        if (raw instanceof List<?> list) {
            return list.stream()
                    .map(item -> objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {
                    }))
                    .toList();
        }

        if (raw instanceof Map<?, ?> map) {
            Object dados = firstPresent(map, "dados", "data", "items", "content", "propostas", "resultado");
            if (dados instanceof List<?> list) {
                return list.stream()
                        .map(item -> objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {
                        }))
                        .toList();
            }
            return List.of(objectMapper.convertValue(map, new TypeReference<Map<String, Object>>() {
            }));
        }

        return List.of();
    }

    private Object firstPresent(Map<?, ?> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    private String mensagemHttp(int status) {
        if (status == 401 || status == 403) {
            return "A API BNDES nao autorizou a chamada. Verifique API_KEY, API_SECRET e escopos.";
        }
        if (status >= 500) {
            return "A API BNDES retornou erro interno ou indisponibilidade.";
        }
        return "A API BNDES recusou a requisicao. Verifique filtros e payload.";
    }

    private String resumo(String value) {
        if (value == null) {
            return null;
        }
        return value.length() > 2000 ? value.substring(0, 2000) : value;
    }
}
