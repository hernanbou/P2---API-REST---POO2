package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.AtualizacaoPropostaLogResponse;
import br.com.hernan.bndes.exception.RecursoNaoEncontradoException;
import br.com.hernan.bndes.service.AtualizacaoPropostaLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/atualizacoes")
public class AtualizacaoPropostaLogController {

    private final AtualizacaoPropostaLogService service;

    public AtualizacaoPropostaLogController(AtualizacaoPropostaLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AtualizacaoPropostaLogResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoPropostaLogResponse> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atualizacao nao encontrada"));
    }

    @GetMapping("/proposta/{idPropostaBndes}")
    public ResponseEntity<List<AtualizacaoPropostaLogResponse>> listarPorProposta(@PathVariable Long idPropostaBndes) {
        return ResponseEntity.ok(service.listarPorProposta(idPropostaBndes));
    }
}
