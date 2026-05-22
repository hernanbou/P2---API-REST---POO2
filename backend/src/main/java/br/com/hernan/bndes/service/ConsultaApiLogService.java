package br.com.hernan.bndes.service;

import br.com.hernan.bndes.dto.BndesApiResult;
import br.com.hernan.bndes.dto.ConsultaApiLogResponse;
import br.com.hernan.bndes.model.ConsultaApiLog;
import br.com.hernan.bndes.repository.ConsultaApiLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaApiLogService {

    private final ConsultaApiLogRepository repository;
    private final ObjectMapper objectMapper;

    public ConsultaApiLogService(ConsultaApiLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public ConsultaApiLog registrarResultado(String tipoConsulta, String metodoHttp, Object parametros, BndesApiResult<?> resultado) {
        return registrar(tipoConsulta, metodoHttp, resultado.endpoint(), parametros, resultado.statusHttp(), resultado.sucesso(),
                resultado.origemDados(), resultado.mensagem(), resultado.erroResumo(), resultado.respostaResumo());
    }

    public ConsultaApiLog registrarMock(String tipoConsulta, String metodoHttp, String endpoint, Object parametros, String mensagem, Object resposta) {
        return registrar(tipoConsulta, metodoHttp, endpoint, parametros, 200, true, "MOCK_APRESENTACAO", mensagem, null, toJson(resposta));
    }

    public ConsultaApiLog registrar(String tipoConsulta, String metodoHttp, String endpoint, Object parametros, Integer statusHttp,
                                   Boolean sucesso, String origemDados, String mensagem, String erroResumo, String respostaResumo) {
        ConsultaApiLog log = new ConsultaApiLog();
        log.setTipoConsulta(tipoConsulta);
        log.setMetodoHttp(metodoHttp);
        log.setEndpoint(endpoint);
        log.setParametros(toJson(parametros));
        log.setStatusHttp(statusHttp);
        log.setSucesso(sucesso);
        log.setOrigemDados(origemDados);
        log.setMensagem(mensagem);
        log.setErroResumo(erroResumo);
        log.setRespostaResumo(respostaResumo);
        return repository.save(log);
    }

    public List<ConsultaApiLogResponse> listar() {
        return repository.findAll().stream().map(ConsultaApiLogResponse::from).toList();
    }

    public Optional<ConsultaApiLogResponse> buscarPorId(Long id) {
        return repository.findById(id).map(ConsultaApiLogResponse::from);
    }

    public List<ConsultaApiLogResponse> filtrar(String tipoConsulta, Boolean sucesso) {
        return repository.findAll().stream()
                .filter(log -> tipoConsulta == null || tipoConsulta.isBlank() || tipoConsulta.equalsIgnoreCase(log.getTipoConsulta()))
                .filter(log -> sucesso == null || sucesso.equals(log.getSucesso()))
                .map(ConsultaApiLogResponse::from)
                .toList();
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String text) {
            return text;
        }
        try {
            String json = objectMapper.writeValueAsString(value);
            return json.length() > 4000 ? json.substring(0, 4000) : json;
        } catch (JsonProcessingException ex) {
            return value.toString();
        }
    }
}
