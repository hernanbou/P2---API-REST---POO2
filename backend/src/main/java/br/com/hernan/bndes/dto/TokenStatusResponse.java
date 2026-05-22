package br.com.hernan.bndes.dto;

import java.time.LocalDateTime;

public record TokenStatusResponse(
        boolean tokenEmMemoria,
        LocalDateTime ultimaRenovacao,
        LocalDateTime proximaRenovacaoAproximada,
        String mensagem
) {
}
