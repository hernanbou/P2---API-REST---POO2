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
import java.time.Duration;
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

    // Compatibilidade com o .env atual e com nomes antigos em application.properties/application.yml.
    @Value("${BNDES_TOKEN_URL:${bndes.api.token-url:https://apis-gateway.bndes.gov.br/token}}")
    private String tokenUrl;

    @Value("${BNDES_TOKEN_REFRESH_RATE_MS:${bndes.auth.refresh-rate-ms:3600000}}")
    private long refreshRateMs;

    private volatile String accessToken;
    private volatile LocalDateTime ultimaRenovacao;
    private volatile String ultimaMensagem = "Token ainda nao carregado.";

    public BndesTokenService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @PostConstruct
    public void gerarTokenAoIniciar() {
        log.info(
                "Configuracao BNDES carregada. API_KEY presente: {} ({} chars). API_SECRET presente: {} ({} chars). Token URL: {}",
                hasText(apiKey),
                safeLength(apiKey),
                hasText(apiSecret),
                safeLength(apiSecret),
                tokenUrl
        );

        // O token fica somente no backend: o frontend nunca precisa conhecer credenciais ou Authorization Bearer.
        renovarToken("startup");
    }

    @Scheduled(fixedDelayString = "${BNDES_TOKEN_REFRESH_RATE_MS:${bndes.auth.refresh-rate-ms:3600000}}")
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
        LocalDateTime proxima = ultimaRenovacao == null ? null : ultimaRenovacao.plus(Duration.ofMillis(refreshRateMs));
        return new TokenStatusResponse(accessToken != null && !accessToken.isBlank(), ultimaRenovacao, proxima, ultimaMensagem);
    }

    private synchronized void renovarToken(String origem) {
        String consumerKey = normalize(apiKey);
        String consumerSecret = normalize(apiSecret);
        String url = normalize(tokenUrl);

        // Nao validar tamanho fixo. Consumer Key/Secret do BNDES podem ter tamanho diferente de 32 caracteres.
        if (consumerKey.isBlank() || consumerSecret.isBlank()) {
            accessToken = null;
            ultimaMensagem = "API_KEY/API_SECRET nao configurados. Configure o .env para chamadas autenticadas.";
            log.warn("{}: {}", origem, ultimaMensagem);
            return;
        }

        if (url.isBlank()) {
            accessToken = null;
            ultimaMensagem = "BNDES_TOKEN_URL nao configurada. Configure a URL de geracao de token no .env.";
            log.warn("{}: {}", origem, ultimaMensagem);
            return;
        }

        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "client_credentials");

            String basic = Base64.getEncoder()
                    .encodeToString((consumerKey + ":" + consumerSecret).getBytes(StandardCharsets.UTF_8));

            ResponseEntity<Map<String, Object>> response = restClient.post()
                    .uri(url)
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
                log.warn("{}: {}", origem, ultimaMensagem);
                return;
            }

            // Nao validar tamanho fixo do token. O backend deve aceitar qualquer access_token nao vazio retornado pela API.
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

    private boolean hasText(String value) {
        return value != null && !value.trim().isBlank();
    }

    private int safeLength(String value) {
        return value == null ? 0 : value.trim().length();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String resumo(String value) {
        if (value == null) {
            return "";
        }

        String sanitized = value.replaceAll("(?i)(\\\"access_token\\\"\\s*:\\s*\\\")[^\\\"]+(\\\")", "$1<redacted>$2");
        return sanitized.length() > 500 ? sanitized.substring(0, 500) : sanitized;
    }
}
