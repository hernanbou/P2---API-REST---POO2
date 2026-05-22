package br.com.hernan.bndes.dto;

import br.com.hernan.bndes.model.AtualizacaoPropostaLog;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizacaoPropostaLogResponse(
        Long id,
        Long idPropostaBndes,
        String tipoEnvio,
        String endpointUsado,
        String modoExecucao,
        String situacaoProposta,
        LocalDateTime dataSituacaoProposta,
        String tipoApoio,
        BigDecimal valorContratado,
        BigDecimal taxaJuros,
        BigDecimal taxaDesconto,
        Integer prazoOperacao,
        Integer prazoAntecipacao,
        LocalDateTime dataContratacao,
        String motivoSituacaoProposta,
        String opcaoGarantiaJson,
        String requestJson,
        String responseJson,
        Integer statusHttp,
        Boolean sucesso,
        String mensagem,
        LocalDateTime dataHoraEnvio
) {
    public static AtualizacaoPropostaLogResponse from(AtualizacaoPropostaLog log) {
        return new AtualizacaoPropostaLogResponse(
                log.getId(),
                log.getIdPropostaBndes(),
                log.getTipoEnvio(),
                log.getEndpointUsado(),
                log.getModoExecucao(),
                log.getSituacaoProposta(),
                log.getDataSituacaoProposta(),
                log.getTipoApoio(),
                log.getValorContratado(),
                log.getTaxaJuros(),
                log.getTaxaDesconto(),
                log.getPrazoOperacao(),
                log.getPrazoAntecipacao(),
                log.getDataContratacao(),
                log.getMotivoSituacaoProposta(),
                log.getOpcaoGarantiaJson(),
                log.getRequestJson(),
                log.getResponseJson(),
                log.getStatusHttp(),
                log.getSucesso(),
                log.getMensagem(),
                log.getDataHoraEnvio()
        );
    }
}
