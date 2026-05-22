package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.ApiEnvelopeResponse;
import br.com.hernan.bndes.dto.FaixaFaturamentoResponse;
import br.com.hernan.bndes.dto.SituacaoPropostaResponse;
import br.com.hernan.bndes.service.BndesReferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bndes")
public class BndesReferenciaController {

    private final BndesReferenciaService referenciaService;

    public BndesReferenciaController(BndesReferenciaService referenciaService) {
        this.referenciaService = referenciaService;
    }

    @GetMapping("/faixas-faturamento")
    public ResponseEntity<ApiEnvelopeResponse<List<FaixaFaturamentoResponse>>> listarFaixasFaturamento() {
        return ResponseEntity.ok(referenciaService.listarFaixasFaturamento());
    }

    @GetMapping("/situacoes")
    public ResponseEntity<ApiEnvelopeResponse<List<SituacaoPropostaResponse>>> listarSituacoes() {
        return ResponseEntity.ok(referenciaService.listarSituacoes());
    }
}
