package com.pettrack.modules.filial.service;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.filial.domain.entity.Filial;
import com.pettrack.modules.filial.domain.enums.StatusFilial;
import com.pettrack.modules.filial.dto.request.FilialRequest;
import com.pettrack.modules.filial.dto.response.FilialResponse;
import com.pettrack.modules.filial.repository.FilialRepository;
import com.pettrack.shared.exception.NegocioException;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilialService {

    private final FilialRepository filialRepository;

    @Transactional
    public FilialResponse cadastrar(FilialRequest request) {
        if (filialRepository.findByRegiao(request.getRegiao()).isPresent()) {
            throw new NegocioException("Já existe uma filial para a região: " + request.getRegiao());
        }

        Filial filial = Filial.builder()
                .nome(request.getNome())
                .regiao(request.getRegiao())
                .endereco(request.getEndereco())
                .cep(request.getCep())
                .cidade(request.getCidade())
                .uf(request.getUf())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .build();

        return toResponse(filialRepository.save(filial));
    }

    @Transactional(readOnly = true)
    public FilialResponse buscarPorId(UUID id) {
        return toResponse(buscarFilial(id));
    }

    @Transactional(readOnly = true)
    public FilialResponse buscarPorRegiao(RegiaoCD regiao) {
        Filial filial = filialRepository.findByRegiao(regiao)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filial não encontrada para a região: " + regiao));
        return toResponse(filial);
    }

    @Transactional(readOnly = true)
    public List<FilialResponse> listarAtivas() {
        return filialRepository.findByStatus(StatusFilial.ATIVA)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FilialResponse atualizarStatus(UUID id, StatusFilial novoStatus) {
        Filial filial = buscarFilial(id);
        filial.setStatus(novoStatus);
        return toResponse(filialRepository.save(filial));
    }

    private Filial buscarFilial(UUID id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Filial não encontrada: " + id));
    }

    private FilialResponse toResponse(Filial filial) {
        return FilialResponse.builder()
                .id(filial.getId())
                .nome(filial.getNome())
                .regiao(filial.getRegiao())
                .endereco(filial.getEndereco())
                .cep(filial.getCep())
                .cidade(filial.getCidade())
                .uf(filial.getUf())
                .telefone(filial.getTelefone())
                .email(filial.getEmail())
                .status(filial.getStatus())
                .criadoEm(filial.getCriadoEm())
                .atualizadoEm(filial.getAtualizadoEm())
                .build();
    }

}