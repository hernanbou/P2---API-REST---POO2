package br.com.hernan.bndes.controller;

import br.com.hernan.bndes.dto.TokenStatusResponse;
import br.com.hernan.bndes.repository.AtualizacaoPropostaLogRepository;
import br.com.hernan.bndes.repository.ConsultaApiLogRepository;
import br.com.hernan.bndes.repository.PropostaLocalRepository;
import br.com.hernan.bndes.service.BndesTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final BndesTokenService tokenService;
    private final PropostaLocalRepository propostaRepository;
    private final ConsultaApiLogRepository consultaApiLogRepository;
    private final AtualizacaoPropostaLogRepository atualizacaoRepository;

    public HealthController(
            BndesTokenService tokenService,
            PropostaLocalRepository propostaRepository,
            ConsultaApiLogRepository consultaApiLogRepository,
            AtualizacaoPropostaLogRepository atualizacaoRepository
    ) {
        this.tokenService = tokenService;
        this.propostaRepository = propostaRepository;
        this.consultaApiLogRepository = consultaApiLogRepository;
        this.atualizacaoRepository = atualizacaoRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "totalPropostasLocais", propostaRepository.count(),
                "totalConsultas", consultaApiLogRepository.count(),
                "totalAtualizacoes", atualizacaoRepository.count()
        ));
    }

    @GetMapping("/bndes-token")
    public ResponseEntity<TokenStatusResponse> tokenStatus() {
        return ResponseEntity.ok(tokenService.status());
    }
}
