package br.com.hernan.bndes.service;

import br.com.hernan.bndes.client.BndesApiClient;
import br.com.hernan.bndes.dto.AtualizacaoPropostaRequest;
import br.com.hernan.bndes.dto.AtualizacaoPropostaResponse;
import br.com.hernan.bndes.dto.BndesApiResult;
import br.com.hernan.bndes.model.AtualizacaoPropostaLog;
import br.com.hernan.bndes.model.PropostaLocal;
import br.com.hernan.bndes.repository.AtualizacaoPropostaLogRepository;
import br.com.hernan.bndes.repository.PropostaLocalRepository;
import br.com.hernan.bndes.util.AtualizacaoPayloadMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AtualizacaoPropostaService {

    private final AtualizacaoPropostaLogRepository logRepository;
    private final PropostaLocalRepository propostaRepository;
    private final BndesApiClient apiClient;
    private final ObjectMapper objectMapper;
    private final boolean enableRealPost;

    public AtualizacaoPropostaService(
            AtualizacaoPropostaLogRepository logRepository,
            PropostaLocalRepository propostaRepository,
            BndesApiClient apiClient,
            ObjectMapper objectMapper,
            @Value("${bndes.api.enable-real-post:false}") boolean enableRealPost
    ) {
        this.logRepository = logRepository;
        this.propostaRepository = propostaRepository;
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
        this.enableRealPost = enableRealPost;
    }

    @Transactional
    public AtualizacaoPropostaResponse atualizar(AtualizacaoPropostaRequest request) {
        String tipoEnvio = request.getTipoEnvio();
        String endpoint = endpointPorTipo(tipoEnvio);
        Map<String, Object> payload = AtualizacaoPayloadMapper.toBndesPayload(request);
        String requestJson = toJson(payload);

        if (!enableRealPost) {
            // Guardrail academico: por padrao o POST real nao e chamado, pois esses endpoints podem alterar propostas reais.
            AtualizacaoPropostaLog log = criarLogBase(request, endpoint, "DEMONSTRACAO", requestJson);
            log.setStatusHttp(200);
            log.setSucesso(true);
            log.setResponseJson("{\"modo\":\"DEMONSTRACAO\",\"endpoint\":\"" + endpoint + "\"}");
            log.setMensagem("Modo demonstracao: a atualizacao foi registrada localmente, mas nao foi enviada ao BNDES.");
            log = logRepository.save(log);
            atualizarSituacaoLocal(request);
            return toResponse(log);
        }

        BndesApiResult<String> resultado = "FINTECH".equalsIgnoreCase(tipoEnvio)
                ? apiClient.atualizarPropostaFintech(request)
                : apiClient.atualizarPropostaParceiroFinanceiro(request);

        AtualizacaoPropostaLog log = criarLogBase(request, endpoint, "REAL", requestJson);
        log.setStatusHttp(resultado.statusHttp());
        log.setSucesso(resultado.sucesso());
        log.setResponseJson(resultado.sucesso() ? resultado.body() : resultado.erroResumo());
        log.setMensagem(resultado.mensagem());
        log = logRepository.save(log);

        if (resultado.sucesso()) {
            atualizarSituacaoLocal(request);
        }

        return toResponse(log);
    }

    private AtualizacaoPropostaLog criarLogBase(AtualizacaoPropostaRequest request, String endpoint, String modo, String requestJson) {
        AtualizacaoPropostaLog log = new AtualizacaoPropostaLog();
        log.setIdPropostaBndes(request.getIdProposta());
        log.setTipoEnvio(request.getTipoEnvio());
        log.setEndpointUsado(endpoint);
        log.setModoExecucao(modo);
        log.setSituacaoProposta(request.getSituacaoProposta());
        log.setDataSituacaoProposta(request.getDataSituacaoProposta());
        log.setTipoApoio(request.getTipoApoio());
        log.setValorContratado(request.getValorContratado());
        log.setTaxaJuros(request.getTaxaJuros());
        log.setTaxaDesconto(request.getTaxaDesconto());
        log.setPrazoOperacao(request.getPrazoOperacao());
        log.setPrazoAntecipacao(request.getPrazoAntecipacao());
        log.setDataContratacao(request.getDataContratacao());
        log.setMotivoSituacaoProposta(request.getMotivoSituacaoProposta());
        log.setOpcaoGarantiaJson(toJson(request.getOpcaoGarantia()));
        log.setRequestJson(requestJson);
        log.setDataHoraEnvio(LocalDateTime.now());
        return log;
    }

    private void atualizarSituacaoLocal(AtualizacaoPropostaRequest request) {
        propostaRepository.findByIdPropostaBndes(request.getIdProposta()).ifPresent(proposta -> {
            proposta.setSituacaoAtual(request.getSituacaoProposta());
            proposta.setDataSituacaoAtual(request.getDataSituacaoProposta());
            propostaRepository.save(proposta);
        });
    }

    private String endpointPorTipo(String tipoEnvio) {
        if ("FINTECH".equalsIgnoreCase(tipoEnvio)) {
            return "/fintech/proposta";
        }
        return "/seguro/parceiro/proposta";
    }

    private AtualizacaoPropostaResponse toResponse(AtualizacaoPropostaLog log) {
        return new AtualizacaoPropostaResponse(
                log.getId(),
                log.getIdPropostaBndes(),
                log.getTipoEnvio(),
                log.getModoExecucao(),
                log.getEndpointUsado(),
                log.getStatusHttp(),
                log.getSucesso(),
                log.getMensagem(),
                log.getRequestJson(),
                log.getResponseJson()
        );
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
}
