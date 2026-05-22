package br.com.hernan.bndes.service;

import br.com.hernan.bndes.dto.TokenStatusResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class BndesTokenService {

    private static final Logger log = LoggerFactory.getLogger(BndesTokenService.class);

    private final RestClient restClient;

    @Value("${API_KEY:}")
    private String apiKey;

    @Value("${API_SECRET:}")
    private String apiSecret;

    @Value("${bndes.api.token-url}")
    private String tokenUrl;

    @Value("${bndes.auth.refresh-rate-ms:3600000}")
    private long refreshRateMs;

    private volatile String accessToken;
    private volatile LocalDateTime ultimaRenovacao;
    private volatile String ultimaMensagem = "Token ainda nao carregado.";

    public BndesTokenService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @PostConstruct
    public void gerarTokenAoIniciar() {
        // O token fica somente no backend: o frontend nunca precisa conhecer credenciais ou Authorization Bearer.
        renovarToken("startup");
    }

    @Scheduled(fixedDelayString = "${bndes.auth.refresh-rate-ms:3600000}")
    public void renovarTokenAgendado() {
        // A API informa expiracao aproximada de 1 hora; renovar automaticamente evita falhas durante a apresentacao.
        renovarToken("scheduled");
    }

    public Optional<String> getAccessToken() {
        if (accessToken == null || accessToken.isBlank()) {
            renovarToken("on-demand");
        }
        return Optional.ofNullable(accessToken).filter(token -> !token.isBlank());
    }

    public TokenStatusResponse status() {
        LocalDateTime proxima = ultimaRenovacao == null ? null : ultimaRenovacao.plusNanos(refreshRateMs * 1_000_000);
        return new TokenStatusResponse(accessToken != null && !accessToken.isBlank(), ultimaRenovacao, proxima, ultimaMensagem);
    }

    private synchronized void renovarToken(String origem) {
        if (apiKey == null || apiKey.isBlank() || apiSecret == null || apiSecret.isBlank()) {
            accessToken = null;
            ultimaMensagem = "API_KEY/API_SECRET nao configurados. Configure o .env para chamadas autenticadas.";
            log.warn("{}: {}", origem, ultimaMensagem);
            return;
        }

        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "client_credentials");

            String basic = Base64.getEncoder()
                    .encodeToString((apiKey + ":" + apiSecret).getBytes(StandardCharsets.UTF_8));

            ResponseEntity<Map<String, Object>> response = restClient.post()
                    .uri(tokenUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(form)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {
                    });

            Map<String, Object> body = response.getBody();
            Object token = body == null ? null : body.get("access_token");
            if (token == null || token.toString().isBlank()) {
                accessToken = null;
                ultimaMensagem = "Resposta de token recebida sem access_token.";
                log.warn(ultimaMensagem);
                return;
            }

            accessToken = token.toString();
            ultimaRenovacao = LocalDateTime.now();
            ultimaMensagem = "Token renovado com sucesso pelo backend.";
            log.info("Token BNDES renovado com sucesso via {}. Valor do token nao sera exposto.", origem);
        } catch (RestClientResponseException ex) {
            accessToken = null;
            ultimaMensagem = "Falha ao gerar token BNDES. HTTP " + ex.getStatusCode().value();
            log.warn("{}: {}", ultimaMensagem, resumo(ex.getResponseBodyAsString()));
        } catch (RestClientException ex) {
            accessToken = null;
            ultimaMensagem = "Falha de rede/SSL ao gerar token BNDES: " + ex.getMessage();
            log.warn(ultimaMensagem);
        }
    }

    private String resumo(String value) {
        if (value == null) {
            return "";
        }
        return value.length() > 500 ? value.substring(0, 500) : value;
    }
}
