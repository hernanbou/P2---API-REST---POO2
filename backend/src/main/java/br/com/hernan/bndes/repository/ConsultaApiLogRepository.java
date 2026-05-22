package br.com.hernan.bndes.repository;

import br.com.hernan.bndes.model.ConsultaApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaApiLogRepository extends JpaRepository<ConsultaApiLog, Long> {

    List<ConsultaApiLog> findByTipoConsultaIgnoreCase(String tipoConsulta);

    List<ConsultaApiLog> findBySucesso(Boolean sucesso);
}
