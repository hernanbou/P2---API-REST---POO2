package br.com.hernan.bndes.service;

import br.com.hernan.bndes.client.BndesApiClient;
import br.com.hernan.bndes.dto.ApiEnvelopeResponse;
import br.com.hernan.bndes.dto.BndesApiResult;
import br.com.hernan.bndes.dto.FaixaFaturamentoResponse;
import br.com.hernan.bndes.dto.SituacaoPropostaResponse;
import br.com.hernan.bndes.exception.ApiBndesException;
import br.com.hernan.bndes.util.MockDataFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BndesReferenciaService {

    private final BndesApiClient apiClient;
    private final ConsultaApiLogService logService;
    private final boolean useMockOnFailure;

    public BndesReferenciaService(
            BndesApiClient apiClient,
            ConsultaApiLogService logService,
            @Value("${bndes.api.use-mock-on-failure:true}") boolean useMockOnFailure
    ) {
        this.apiClient = apiClient;
        this.logService = logService;
        this.useMockOnFailure = useMockOnFailure;
    }

    public ApiEnvelopeResponse<List<FaixaFaturamentoResponse>> listarFaixasFaturamento() {
        BndesApiResult<List<Map<String, Object>>> resultado = apiClient.listarFaixasFaturamento();
        logService.registrarResultado("FAIXAS_FATURAMENTO", "GET", Map.of(), resultado);

        if (resultado.sucesso()) {
            List<FaixaFaturamentoResponse> faixas = resultado.body().stream().map(this::toFaixa).toList();
            return new ApiEnvelopeResponse<>(faixas, "API_BNDES", "OK", "Faixas carregadas da API BNDES.");
        }

        if (useMockOnFailure) {
            List<FaixaFaturamentoResponse> mock = MockDataFactory.faixasFaturamento();
            logService.registrarMock("FAIXAS_FATURAMENTO", "GET", "/seguro/faixaFaturamentoBNDES", Map.of(),
                    "Fallback honesto: faixas mockadas para apresentacao academica.", mock);
            return new ApiEnvelopeResponse<>(mock, "MOCK_APRESENTACAO", "FALHOU",
                    "A API BNDES nao respondeu ou nao autorizou a chamada. Exibindo dados mockados para demonstracao academica.");
        }

        throw new ApiBndesException(resultado.mensagem(), HttpStatus.BAD_GATEWAY);
    }

    public ApiEnvelopeResponse<List<SituacaoPropostaResponse>> listarSituacoes() {
        BndesApiResult<List<Map<String, Object>>> resultado = apiClient.listarSituacoes();
        logService.registrarResultado("SITUACOES", "GET", Map.of(), resultado);

        if (resultado.sucesso()) {
            List<SituacaoPropostaResponse> situacoes = resultado.body().stream().map(this::toSituacao).toList();
            return new ApiEnvelopeResponse<>(situacoes, "API_BNDES", "OK", "Situacoes carregadas da API BNDES.");
        }

        if (useMockOnFailure) {
            List<SituacaoPropostaResponse> mock = MockDataFactory.situacoes();
            logService.registrarMock("SITUACOES", "GET", "/seguro/situacao", Map.of(),
                    "Fallback honesto: situacoes mockadas para apresentacao academica.", mock);
            return new ApiEnvelopeResponse<>(mock, "MOCK_APRESENTACAO", "FALHOU",
                    "A API BNDES nao respondeu ou nao autorizou a chamada. Exibindo dados mockados para demonstracao academica.");
        }

        throw new ApiBndesException(resultado.mensagem(), HttpStatus.BAD_GATEWAY);
    }

    private FaixaFaturamentoResponse toFaixa(Map<String, Object> item) {
        return new FaixaFaturamentoResponse(toInteger(item.get("codigo")), toText(item.get("nome")));
    }

    private SituacaoPropostaResponse toSituacao(Map<String, Object> item) {
        return new SituacaoPropostaResponse(
                toText(item.get("nome")),
                toInteger(item.get("codigo")),
                toText(item.get("situacaoProposta"))
        );
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

    private String toText(Object value) {
        return value == null ? null : value.toString();
    }
}
