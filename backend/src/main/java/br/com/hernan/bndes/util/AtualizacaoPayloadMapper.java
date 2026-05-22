package br.com.hernan.bndes.util;

import br.com.hernan.bndes.dto.AtualizacaoPropostaRequest;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AtualizacaoPayloadMapper {

    private AtualizacaoPayloadMapper() {
    }

    public static Map<String, Object> toBndesPayload(AtualizacaoPropostaRequest request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        putIfPresent(payload, "idProposta", request.getIdProposta());
        putIfPresent(payload, "situacaoProposta", request.getSituacaoProposta());
        putIfPresent(payload, "dataSituacaoProposta", request.getDataSituacaoProposta());
        putIfPresent(payload, "tipoApoio", request.getTipoApoio());
        putIfPresent(payload, "valorContratado", request.getValorContratado());
        putIfPresent(payload, "taxaJuros", request.getTaxaJuros());
        putIfPresent(payload, "taxaDesconto", request.getTaxaDesconto());
        putIfPresent(payload, "prazoOperacao", request.getPrazoOperacao());
        putIfPresent(payload, "prazoAntecipacao", request.getPrazoAntecipacao());
        putIfPresent(payload, "dataContratacao", request.getDataContratacao());
        putIfPresent(payload, "motivoSituacaoProposta", request.getMotivoSituacaoProposta());
        putIfPresent(payload, "opcaoGarantia", request.getOpcaoGarantia());
        return payload;
    }

    private static void putIfPresent(Map<String, Object> payload, String key, Object value) {
        if (value != null) {
            payload.put(key, value);
        }
    }
}
