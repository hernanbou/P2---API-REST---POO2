package br.com.hernan.bndes.dto;

import br.com.hernan.bndes.model.ConsultaApiLog;

import java.time.LocalDateTime;

public record ConsultaApiLogResponse(
        Long id,
        String tipoConsulta,
        String metodoHttp,
        String endpoint,
        String parametros,
        Integer statusHttp,
        Boolean sucesso,
        String origemDados,
        String mensagem,
        String erroResumo,
        String respostaResumo,
        LocalDateTime dataHora
) {
    public static ConsultaApiLogResponse from(ConsultaApiLog log) {
        return new ConsultaApiLogResponse(
                log.getId(),
                log.getTipoConsulta(),
                log.getMetodoHttp(),
                log.getEndpoint(),
                log.getParametros(),
                log.getStatusHttp(),
                log.getSucesso(),
                log.getOrigemDados(),
                log.getMensagem(),
                log.getErroResumo(),
                log.getRespostaResumo(),
                log.getDataHora()
        );
    }
}
