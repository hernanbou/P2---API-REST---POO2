package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.PropostaLocalRequest;
import br.com.hernan.bndes.dto.PropostaLocalResponse;
import br.com.hernan.bndes.exception.RecursoNaoEncontradoException;
import br.com.hernan.bndes.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/propostas-locais")
public class PropostaLocalController {

    private final PropostaService propostaService;

    public PropostaLocalController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @GetMapping
    public ResponseEntity<List<PropostaLocalResponse>> listar() {
        return ResponseEntity.ok(propostaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaLocalResponse> buscarPorId(@PathVariable Long id) {
        return propostaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Proposta local nao encontrada"));
    }

    @PostMapping
    public ResponseEntity<PropostaLocalResponse> criar(@Valid @RequestBody PropostaLocalRequest request) {
        PropostaLocalResponse response = propostaService.criar(request);
        return ResponseEntity.created(URI.create("/api/propostas-locais/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropostaLocalResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PropostaLocalRequest request) {
        return propostaService.atualizar(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Proposta local nao encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        propostaService.deletar(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Proposta local nao encontrada"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<PropostaLocalResponse>> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String situacao
    ) {
        return ResponseEntity.ok(propostaService.filtrar(nome, uf, situacao));
    }
}
