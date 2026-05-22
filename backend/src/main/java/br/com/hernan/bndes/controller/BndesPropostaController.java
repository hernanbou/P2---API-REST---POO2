package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.ApiEnvelopeResponse;
import br.com.hernan.bndes.dto.AtualizacaoPropostaRequest;
import br.com.hernan.bndes.dto.AtualizacaoPropostaResponse;
import br.com.hernan.bndes.dto.PropostaFiltroRequest;
import br.com.hernan.bndes.dto.PropostaLocalResponse;
import br.com.hernan.bndes.service.AtualizacaoPropostaService;
import br.com.hernan.bndes.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/bndes/propostas")
public class BndesPropostaController {

    private final PropostaService propostaService;
    private final AtualizacaoPropostaService atualizacaoPropostaService;

    public BndesPropostaController(PropostaService propostaService, AtualizacaoPropostaService atualizacaoPropostaService) {
        this.propostaService = propostaService;
        this.atualizacaoPropostaService = atualizacaoPropostaService;
    }

    @GetMapping("/parceiro-financeiro")
    public ResponseEntity<ApiEnvelopeResponse<List<PropostaLocalResponse>>> consultarParceiroFinanceiro(
            @Valid @ModelAttribute PropostaFiltroRequest filtros
    ) {
        return ResponseEntity.ok(propostaService.consultarParceiroFinanceiro(filtros));
    }

    @GetMapping("/parceiro-institucional")
    public ResponseEntity<ApiEnvelopeResponse<List<PropostaLocalResponse>>> consultarParceiroInstitucional(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("dataInicio deve ser menor ou igual a dataFim");
        }
        return ResponseEntity.ok(propostaService.consultarParceiroInstitucional(dataInicio, dataFim));
    }

    @PostMapping("/parceiro-financeiro/atualizar")
    public ResponseEntity<AtualizacaoPropostaResponse> atualizarParceiroFinanceiro(
            @Valid @RequestBody AtualizacaoPropostaRequest request
    ) {
        request.setTipoEnvio("PARCEIRO_FINANCEIRO");
        return ResponseEntity.ok(atualizacaoPropostaService.atualizar(request));
    }

    @PostMapping("/fintech/atualizar")
    public ResponseEntity<AtualizacaoPropostaResponse> atualizarFintech(
            @Valid @RequestBody AtualizacaoPropostaRequest request
    ) {
        request.setTipoEnvio("FINTECH");
        return ResponseEntity.ok(atualizacaoPropostaService.atualizar(request));
    }
}
