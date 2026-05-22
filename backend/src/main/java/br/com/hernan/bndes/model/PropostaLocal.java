package br.com.hernan.bndes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposta_local")
public class PropostaLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPropostaBndes;
    private String tipoOrigem;
    private String origemDados;
    private String nomeProponente;
    private String cpfCnpj;
    private String emailProponente;
    private String telefoneProponente;
    private BigDecimal valorPretendido;
    private String faixaFaturamento;
    private String finalidade;
    private String municipio;
    private String uf;
    private String situacaoAtual;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataSituacaoAtual;
    private Integer diasNaSituacaoAtual;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String opcoesApoioJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String historicoSituacaoJson;

    private LocalDateTime dataImportacao;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String observacao;

    @PrePersist
    public void prePersist() {
        if (dataImportacao == null) {
            dataImportacao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPropostaBndes() {
        return idPropostaBndes;
    }

    public void setIdPropostaBndes(Long idPropostaBndes) {
        this.idPropostaBndes = idPropostaBndes;
    }

    public String getTipoOrigem() {
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getOrigemDados() {
        return origemDados;
    }

    public void setOrigemDados(String origemDados) {
        this.origemDados = origemDados;
    }

    public String getNomeProponente() {
        return nomeProponente;
    }

    public void setNomeProponente(String nomeProponente) {
        this.nomeProponente = nomeProponente;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmailProponente() {
        return emailProponente;
    }

    public void setEmailProponente(String emailProponente) {
        this.emailProponente = emailProponente;
    }

    public String getTelefoneProponente() {
        return telefoneProponente;
    }

    public void setTelefoneProponente(String telefoneProponente) {
        this.telefoneProponente = telefoneProponente;
    }

    public BigDecimal getValorPretendido() {
        return valorPretendido;
    }

    public void setValorPretendido(BigDecimal valorPretendido) {
        this.valorPretendido = valorPretendido;
    }

    public String getFaixaFaturamento() {
        return faixaFaturamento;
    }

    public void setFaixaFaturamento(String faixaFaturamento) {
        this.faixaFaturamento = faixaFaturamento;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataSituacaoAtual() {
        return dataSituacaoAtual;
    }

    public void setDataSituacaoAtual(LocalDateTime dataSituacaoAtual) {
        this.dataSituacaoAtual = dataSituacaoAtual;
    }

    public Integer getDiasNaSituacaoAtual() {
        return diasNaSituacaoAtual;
    }

    public void setDiasNaSituacaoAtual(Integer diasNaSituacaoAtual) {
        this.diasNaSituacaoAtual = diasNaSituacaoAtual;
    }

    public String getOpcoesApoioJson() {
        return opcoesApoioJson;
    }

    public void setOpcoesApoioJson(String opcoesApoioJson) {
        this.opcoesApoioJson = opcoesApoioJson;
    }

    public String getHistoricoSituacaoJson() {
        return historicoSituacaoJson;
    }

    public void setHistoricoSituacaoJson(String historicoSituacaoJson) {
        this.historicoSituacaoJson = historicoSituacaoJson;
    }

    public LocalDateTime getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(LocalDateTime dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
