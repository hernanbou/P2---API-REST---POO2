package br.com.hernan.bndes.repository;

import br.com.hernan.bndes.model.AtualizacaoPropostaLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtualizacaoPropostaLogRepository extends JpaRepository<AtualizacaoPropostaLog, Long> {

    List<AtualizacaoPropostaLog> findByIdPropostaBndes(Long idPropostaBndes);

    List<AtualizacaoPropostaLog> findByTipoEnvioIgnoreCase(String tipoEnvio);

    List<AtualizacaoPropostaLog> findByModoExecucaoIgnoreCase(String modoExecucao);
}
