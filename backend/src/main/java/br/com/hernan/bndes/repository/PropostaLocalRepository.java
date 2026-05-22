package br.com.hernan.bndes.repository;

import br.com.hernan.bndes.model.PropostaLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaLocalRepository extends JpaRepository<PropostaLocal, Long> {

    Optional<PropostaLocal> findByIdPropostaBndes(Long idPropostaBndes);

    List<PropostaLocal> findByUfIgnoreCase(String uf);

    List<PropostaLocal> findBySituacaoAtualIgnoreCase(String situacaoAtual);

    List<PropostaLocal> findByNomeProponenteContainingIgnoreCase(String nomeProponente);
}
