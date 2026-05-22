package br.com.hernan.bndes.dto;

public record ApiEnvelopeResponse<T>(
        T dados,
        String origemDados,
        String apiStatus,
        String mensagem
) {
}
