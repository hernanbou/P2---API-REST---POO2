package br.com.hernan.bndes.service;

import br.com.hernan.bndes.dto.AtualizacaoPropostaLogResponse;
import br.com.hernan.bndes.repository.AtualizacaoPropostaLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtualizacaoPropostaLogService {

    private final AtualizacaoPropostaLogRepository repository;

    public AtualizacaoPropostaLogService(AtualizacaoPropostaLogRepository repository) {
        this.repository = repository;
    }

    public List<AtualizacaoPropostaLogResponse> listar() {
        return repository.findAll().stream().map(AtualizacaoPropostaLogResponse::from).toList();
    }

    public Optional<AtualizacaoPropostaLogResponse> buscarPorId(Long id) {
        return repository.findById(id).map(AtualizacaoPropostaLogResponse::from);
    }

    public List<AtualizacaoPropostaLogResponse> listarPorProposta(Long idPropostaBndes) {
        return repository.findByIdPropostaBndes(idPropostaBndes).stream().map(AtualizacaoPropostaLogResponse::from).toList();
    }
}
