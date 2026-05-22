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
@Table(name = "atualizacao_proposta_log")
public class AtualizacaoPropostaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPropostaBndes;
    private String tipoEnvio;
    private String endpointUsado;
    private String modoExecucao;
    private String situacaoProposta;
    private LocalDateTime dataSituacaoProposta;
    private String tipoApoio;
    private BigDecimal valorContratado;
    private BigDecimal taxaJuros;
    private BigDecimal taxaDesconto;
    private Integer prazoOperacao;
    private Integer prazoAntecipacao;
    private LocalDateTime dataContratacao;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String motivoSituacaoProposta;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String opcaoGarantiaJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String requestJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String responseJson;

    private Integer statusHttp;
    private Boolean sucesso;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String mensagem;

    private LocalDateTime dataHoraEnvio;

    @PrePersist
    public void prePersist() {
        if (dataHoraEnvio == null) {
            dataHoraEnvio = LocalDateTime.now();
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

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getEndpointUsado() {
        return endpointUsado;
    }

    public void setEndpointUsado(String endpointUsado) {
        this.endpointUsado = endpointUsado;
    }

    public String getModoExecucao() {
        return modoExecucao;
    }

    public void setModoExecucao(String modoExecucao) {
        this.modoExecucao = modoExecucao;
    }

    public String getSituacaoProposta() {
        return situacaoProposta;
    }

    public void setSituacaoProposta(String situacaoProposta) {
        this.situacaoProposta = situacaoProposta;
    }

    public LocalDateTime getDataSituacaoProposta() {
        return dataSituacaoProposta;
    }

    public void setDataSituacaoProposta(LocalDateTime dataSituacaoProposta) {
        this.dataSituacaoProposta = dataSituacaoProposta;
    }

    public String getTipoApoio() {
        return tipoApoio;
    }

    public void setTipoApoio(String tipoApoio) {
        this.tipoApoio = tipoApoio;
    }

    public BigDecimal getValorContratado() {
        return valorContratado;
    }

    public void setValorContratado(BigDecimal valorContratado) {
        this.valorContratado = valorContratado;
    }

    public BigDecimal getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(BigDecimal taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public Integer getPrazoOperacao() {
        return prazoOperacao;
    }

    public void setPrazoOperacao(Integer prazoOperacao) {
        this.prazoOperacao = prazoOperacao;
    }

    public Integer getPrazoAntecipacao() {
        return prazoAntecipacao;
    }

    public void setPrazoAntecipacao(Integer prazoAntecipacao) {
        this.prazoAntecipacao = prazoAntecipacao;
    }

    public LocalDateTime getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDateTime dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public String getMotivoSituacaoProposta() {
        return motivoSituacaoProposta;
    }

    public void setMotivoSituacaoProposta(String motivoSituacaoProposta) {
        this.motivoSituacaoProposta = motivoSituacaoProposta;
    }

    public String getOpcaoGarantiaJson() {
        return opcaoGarantiaJson;
    }

    public void setOpcaoGarantiaJson(String opcaoGarantiaJson) {
        this.opcaoGarantiaJson = opcaoGarantiaJson;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public Integer getStatusHttp() {
        return statusHttp;
    }

    public void setStatusHttp(Integer statusHttp) {
        this.statusHttp = statusHttp;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }
}
