package br.com.hernan.bndes.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AtualizacaoPropostaRequest {

    @NotNull(message = "idProposta e obrigatorio")
    private Long idProposta;

    @NotBlank(message = "situacaoProposta e obrigatoria")
    private String situacaoProposta;

    @NotNull(message = "dataSituacaoProposta e obrigatoria")
    private LocalDateTime dataSituacaoProposta;

    @NotBlank(message = "tipoEnvio e obrigatorio")
    @Pattern(regexp = "PARCEIRO_FINANCEIRO|FINTECH", message = "tipoEnvio deve ser PARCEIRO_FINANCEIRO ou FINTECH")
    private String tipoEnvio;

    private String tipoApoio;

    @DecimalMin(value = "0.0", inclusive = true, message = "valorContratado deve ser maior ou igual a zero")
    private BigDecimal valorContratado;

    @DecimalMin(value = "0.0", inclusive = true, message = "taxaJuros deve ser maior ou igual a zero")
    private BigDecimal taxaJuros;

    @DecimalMin(value = "0.0", inclusive = true, message = "taxaDesconto deve ser maior ou igual a zero")
    private BigDecimal taxaDesconto;

    @Min(value = 1, message = "prazoOperacao deve ser maior que zero")
    private Integer prazoOperacao;

    private Integer prazoAntecipacao;
    private LocalDateTime dataContratacao;
    private String motivoSituacaoProposta;
    private List<String> opcaoGarantia = new ArrayList<>();

    public Long getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(Long idProposta) {
        this.idProposta = idProposta;
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

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
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

    public List<String> getOpcaoGarantia() {
        return opcaoGarantia;
    }

    public void setOpcaoGarantia(List<String> opcaoGarantia) {
        this.opcaoGarantia = opcaoGarantia;
    }
}
