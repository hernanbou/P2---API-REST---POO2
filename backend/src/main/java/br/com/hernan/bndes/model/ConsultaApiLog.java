package br.com.hernan.bndes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "consulta_api_log")
public class ConsultaApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoConsulta;
    private String metodoHttp;
    private String endpoint;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String parametros;

    private Integer statusHttp;
    private Boolean sucesso;
    private String origemDados;
    private String mensagem;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String erroResumo;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String respostaResumo;

    private LocalDateTime dataHora;

    @PrePersist
    public void prePersist() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getMetodoHttp() {
        return metodoHttp;
    }

    public void setMetodoHttp(String metodoHttp) {
        this.metodoHttp = metodoHttp;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
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

    public String getOrigemDados() {
        return origemDados;
    }

    public void setOrigemDados(String origemDados) {
        this.origemDados = origemDados;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getErroResumo() {
        return erroResumo;
    }

    public void setErroResumo(String erroResumo) {
        this.erroResumo = erroResumo;
    }

    public String getRespostaResumo() {
        return respostaResumo;
    }

    public void setRespostaResumo(String respostaResumo) {
        this.respostaResumo = respostaResumo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
