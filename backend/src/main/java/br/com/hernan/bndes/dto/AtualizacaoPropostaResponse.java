package br.com.hernan.bndes.dto;

public record AtualizacaoPropostaResponse(
        Long idLog,
        Long idPropostaBndes,
        String tipoEnvio,
        String modoExecucao,
        String endpointUsado,
        Integer statusHttp,
        Boolean sucesso,
        String mensagem,
        String requestJson,
        String responseJson
) {
}
