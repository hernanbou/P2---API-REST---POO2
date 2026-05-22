package br.com.hernan.bndes.dto;

public record BndesApiResult<T>(
        T body,
        Integer statusHttp,
        boolean sucesso,
        String origemDados,
        String mensagem,
        String endpoint,
        String erroResumo,
        String respostaResumo
) {
    public static <T> BndesApiResult<T> sucesso(T body, Integer statusHttp, String endpoint, String respostaResumo) {
        return new BndesApiResult<>(body, statusHttp, true, "API_BNDES", "Chamada realizada com sucesso.", endpoint, null, respostaResumo);
    }

    public static <T> BndesApiResult<T> falha(Integer statusHttp, String endpoint, String mensagem, String erroResumo) {
        return new BndesApiResult<>(null, statusHttp, false, "ERRO_API", mensagem, endpoint, erroResumo, null);
    }
}
