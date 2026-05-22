package br.com.hernan.bndes.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PropostaFiltroRequest {

    @NotNull(message = "dataInicio e obrigatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;

    @NotNull(message = "dataFim e obrigatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;

    private String cnpjOuCpf;
    private String situacao;

    @DecimalMin(value = "0.0", inclusive = true, message = "valorFinanciamentoMinimo deve ser positivo")
    private BigDecimal valorFinanciamentoMinimo;

    @DecimalMin(value = "0.0", inclusive = true, message = "valorFinanciamentoMaximo deve ser positivo")
    private BigDecimal valorFinanciamentoMaximo;

    @Pattern(regexp = "^$|^[A-Za-z]{2}$", message = "ufInvestimento deve ter 2 letras")
    private String ufInvestimento;

    private String municipioInvestimento;
    private String faixaFaturamento;
    private String origemProposta;
    private String codFinalidadeProposta;

    @AssertTrue(message = "dataInicio deve ser menor ou igual a dataFim")
    public boolean isPeriodoValido() {
        return dataInicio == null || dataFim == null || !dataInicio.isAfter(dataFim);
    }

    @AssertTrue(message = "valor minimo deve ser menor ou igual ao valor maximo")
    public boolean isIntervaloValorValido() {
        return valorFinanciamentoMinimo == null
                || valorFinanciamentoMaximo == null
                || valorFinanciamentoMinimo.compareTo(valorFinanciamentoMaximo) <= 0;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public String getCnpjOuCpf() {
        return cnpjOuCpf;
    }

    public void setCnpjOuCpf(String cnpjOuCpf) {
        this.cnpjOuCpf = cnpjOuCpf;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getValorFinanciamentoMinimo() {
        return valorFinanciamentoMinimo;
    }

    public void setValorFinanciamentoMinimo(BigDecimal valorFinanciamentoMinimo) {
        this.valorFinanciamentoMinimo = valorFinanciamentoMinimo;
    }

    public BigDecimal getValorFinanciamentoMaximo() {
        return valorFinanciamentoMaximo;
    }

    public void setValorFinanciamentoMaximo(BigDecimal valorFinanciamentoMaximo) {
        this.valorFinanciamentoMaximo = valorFinanciamentoMaximo;
    }

    public String getUfInvestimento() {
        return ufInvestimento;
    }

    public void setUfInvestimento(String ufInvestimento) {
        this.ufInvestimento = ufInvestimento;
    }

    public String getMunicipioInvestimento() {
        return municipioInvestimento;
    }

    public void setMunicipioInvestimento(String municipioInvestimento) {
        this.municipioInvestimento = municipioInvestimento;
    }

    public String getFaixaFaturamento() {
        return faixaFaturamento;
    }

    public void setFaixaFaturamento(String faixaFaturamento) {
        this.faixaFaturamento = faixaFaturamento;
    }

    public String getOrigemProposta() {
        return origemProposta;
    }

    public void setOrigemProposta(String origemProposta) {
        this.origemProposta = origemProposta;
    }

    public String getCodFinalidadeProposta() {
        return codFinalidadeProposta;
    }

    public void setCodFinalidadeProposta(String codFinalidadeProposta) {
        this.codFinalidadeProposta = codFinalidadeProposta;
    }
}
