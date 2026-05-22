package br.com.hernan.bndes.util;

import br.com.hernan.bndes.dto.FaixaFaturamentoResponse;
import br.com.hernan.bndes.dto.SituacaoPropostaResponse;
import br.com.hernan.bndes.model.PropostaLocal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class MockDataFactory {

    private MockDataFactory() {
    }

    public static List<FaixaFaturamentoResponse> faixasFaturamento() {
        return List.of(
                new FaixaFaturamentoResponse(1, "Inferior a R$ 360 mil"),
                new FaixaFaturamentoResponse(2, "Entre R$ 360 mil e R$ 4,8 milhoes"),
                new FaixaFaturamentoResponse(3, "Entre R$ 4,8 milhoes e R$ 90 milhoes"),
                new FaixaFaturamentoResponse(4, "Entre R$ 90 milhoes e R$ 200 milhoes"),
                new FaixaFaturamentoResponse(5, "Entre R$ 200 milhoes e R$ 300 milhoes")
        );
    }

    public static List<SituacaoPropostaResponse> situacoes() {
        return List.of(
                new SituacaoPropostaResponse("Enviada", 1, "ENVIADA"),
                new SituacaoPropostaResponse("Recebida", 2, "RECEBIDA"),
                new SituacaoPropostaResponse("Em analise", 3, "EM_ANALISE"),
                new SituacaoPropostaResponse("Em negociacao", 4, "EM_NEGOCIACAO"),
                new SituacaoPropostaResponse("Contratada", 5, "CONTRATADA"),
                new SituacaoPropostaResponse("Recusada", 6, "RECUSADA"),
                new SituacaoPropostaResponse("Expirada", 7, "EXPIRADA"),
                new SituacaoPropostaResponse("Cancelada", 8, "CANCELADA"),
                new SituacaoPropostaResponse("Contratada com linha BNDES", 9, "CONTRATADA_COM_LINHA_BNDES"),
                new SituacaoPropostaResponse("Contratada com linhas proprias", 10, "CONTRATADA_COM_LINHAS_PROPRIAS"),
                new SituacaoPropostaResponse("Contratada com FIDC BNDES", 11, "CONTRATADA_COM_FIDC_BNDES"),
                new SituacaoPropostaResponse("Contratada BNDES Microcredito", 12, "CONTRATADA_BNDES_MICROCREDITO")
        );
    }

    public static List<PropostaLocal> propostasFinanceiras() {
        return List.of(
                proposta(910001L, "PARCEIRO_FINANCEIRO", "Padaria Boa Massa Ltda", "12345678000190",
                        "financeiro@boamassa.com.br", "11988887777", "Sao Paulo", "SP",
                        "Capital de giro", "Entre R$ 360 mil e R$ 4,8 milhoes", "ENVIADA",
                        new BigDecimal("180000.00"), 2, "[\"FINANCIAMENTO\"]"),
                proposta(910002L, "PARCEIRO_FINANCEIRO", "Metalurgica Horizonte ME", "45678912000144",
                        "contato@horizonteme.com.br", "31977776666", "Contagem", "MG",
                        "Modernizacao de maquinas", "Entre R$ 4,8 milhoes e R$ 90 milhoes", "EM_ANALISE",
                        new BigDecimal("750000.00"), 8, "[\"FINANCIAMENTO\", \"GARANTIA\"]"),
                proposta(910003L, "PARCEIRO_FINANCEIRO", "Clinica Vida Plena Ltda", "98765432000155",
                        "gestao@vidaplena.com.br", "41966665555", "Curitiba", "PR",
                        "Expansao da unidade", "Entre R$ 360 mil e R$ 4,8 milhoes", "RECEBIDA",
                        new BigDecimal("320000.00"), 5, "[\"FINANCIAMENTO\"]")
        );
    }

    public static List<PropostaLocal> propostasInstitucionais() {
        return List.of(
                proposta(920001L, "PARCEIRO_INSTITUCIONAL", "Agro Vale Comercio Ltda", "11222333000188",
                        "diretoria@agrovale.com.br", "67999990000", "Dourados", "MS",
                        "Compra de equipamentos agricolas", "Entre R$ 4,8 milhoes e R$ 90 milhoes", "RECEBIDA",
                        new BigDecimal("1200000.00"), 11, "[\"LINHA_CREDITO_SUGERIDA_BNDES\"]"),
                proposta(920002L, "PARCEIRO_INSTITUCIONAL", "Tech Norte Servicos Digitais", "33444555000122",
                        "operacoes@technorte.com.br", "92988881234", "Manaus", "AM",
                        "Digitalizacao de processos", "Inferior a R$ 360 mil", "EM_ANALISE",
                        new BigDecimal("95000.00"), 3, "[\"ORIENTACAO_FINANCEIRA\"]"),
                proposta(920003L, "PARCEIRO_INSTITUCIONAL", "Confeccoes AtlantiCo Ltda", "55666777000111",
                        "adm@atlantico.com.br", "81977778888", "Recife", "PE",
                        "Capital de giro para pedidos", "Entre R$ 360 mil e R$ 4,8 milhoes", "ENVIADA",
                        new BigDecimal("260000.00"), 1, "[\"LINHA_CREDITO_SUGERIDA_BNDES\"]")
        );
    }

    private static PropostaLocal proposta(
            Long idBndes,
            String tipoOrigem,
            String nome,
            String cpfCnpj,
            String email,
            String telefone,
            String municipio,
            String uf,
            String finalidade,
            String faixa,
            String situacao,
            BigDecimal valor,
            int dias,
            String opcoesApoioJson
    ) {
        PropostaLocal proposta = new PropostaLocal();
        proposta.setIdPropostaBndes(idBndes);
        proposta.setTipoOrigem(tipoOrigem);
        proposta.setOrigemDados("MOCK_APRESENTACAO");
        proposta.setNomeProponente(nome);
        proposta.setCpfCnpj(cpfCnpj);
        proposta.setEmailProponente(email);
        proposta.setTelefoneProponente(telefone);
        proposta.setMunicipio(municipio);
        proposta.setUf(uf);
        proposta.setFinalidade(finalidade);
        proposta.setFaixaFaturamento(faixa);
        proposta.setSituacaoAtual(situacao);
        proposta.setValorPretendido(valor);
        proposta.setDataSolicitacao(LocalDateTime.now().minusDays(dias + 4L));
        proposta.setDataSituacaoAtual(LocalDateTime.now().minusDays(dias));
        proposta.setDiasNaSituacaoAtual(dias);
        proposta.setOpcoesApoioJson(opcoesApoioJson);
        proposta.setHistoricoSituacaoJson("[{\"situacao\":\"ENVIADA\",\"observacao\":\"Mock academico para apresentacao\"}]");
        proposta.setObservacao("Dados mockados usados somente quando a API BNDES falha ou nao autoriza a chamada.");
        return proposta;
    }
}
