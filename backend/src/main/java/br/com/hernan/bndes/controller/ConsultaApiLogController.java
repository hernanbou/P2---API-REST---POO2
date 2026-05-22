package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.ConsultaApiLogResponse;
import br.com.hernan.bndes.exception.RecursoNaoEncontradoException;
import br.com.hernan.bndes.service.ConsultaApiLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ConsultaApiLogController {

    private final ConsultaApiLogService service;

    public ConsultaApiLogController(ConsultaApiLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConsultaApiLogResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaApiLogResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Log de consulta nao encontrado"));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<ConsultaApiLogResponse>> filtrar(
            @RequestParam(required = false) String tipoConsulta,
            @RequestParam(required = false) Boolean sucesso
    ) {
        return ResponseEntity.ok(service.filtrar(tipoConsulta, sucesso));
    }
}
