package br.com.hernan.bndes.dto;

import br.com.hernan.bndes.model.PropostaLocal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PropostaLocalResponse(
        Long id,
        Long idPropostaBndes,
        String tipoOrigem,
        String origemDados,
        String nomeProponente,
        String cpfCnpj,
        String emailProponente,
        String telefoneProponente,
        BigDecimal valorPretendido,
        String faixaFaturamento,
        String finalidade,
        String municipio,
        String uf,
        String situacaoAtual,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataSituacaoAtual,
        Integer diasNaSituacaoAtual,
        String opcoesApoioJson,
        String historicoSituacaoJson,
        LocalDateTime dataImportacao,
        String observacao
) {
    public static PropostaLocalResponse from(PropostaLocal proposta) {
        return new PropostaLocalResponse(
                proposta.getId(),
                proposta.getIdPropostaBndes(),
                proposta.getTipoOrigem(),
                proposta.getOrigemDados(),
                proposta.getNomeProponente(),
                proposta.getCpfCnpj(),
                proposta.getEmailProponente(),
                proposta.getTelefoneProponente(),
                proposta.getValorPretendido(),
                proposta.getFaixaFaturamento(),
                proposta.getFinalidade(),
                proposta.getMunicipio(),
                proposta.getUf(),
                proposta.getSituacaoAtual(),
                proposta.getDataSolicitacao(),
                proposta.getDataSituacaoAtual(),
                proposta.getDiasNaSituacaoAtual(),
                proposta.getOpcoesApoioJson(),
                proposta.getHistoricoSituacaoJson(),
                proposta.getDataImportacao(),
                proposta.getObservacao()
        );
    }
}
