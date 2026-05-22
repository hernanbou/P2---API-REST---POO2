package br.com.hernan.bndes.dto;

public record SituacaoPropostaResponse(
        String nome,
        Integer codigo,
        String situacaoProposta
) {
}
