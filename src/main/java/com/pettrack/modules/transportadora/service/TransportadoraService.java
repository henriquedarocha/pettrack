package com.pettrack.modules.transportadora.service;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.domain.entity.Transportadora;
import com.pettrack.modules.transportadora.domain.entity.Veiculo;
import com.pettrack.modules.transportadora.dto.request.TransportadoraRequest;
import com.pettrack.modules.transportadora.dto.request.VeiculoRequest;
import com.pettrack.modules.transportadora.dto.response.TransportadoraResponse;
import com.pettrack.modules.transportadora.dto.response.VeiculoResponse;
import com.pettrack.modules.transportadora.repository.TransportadoraRepository;
import com.pettrack.modules.transportadora.repository.VeiculoRepository;
import com.pettrack.shared.exception.NegocioException;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportadoraService {

    private final TransportadoraRepository transportadoraRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional
    public TransportadoraResponse cadastrar(TransportadoraRequest request) {
        Transportadora transportadora = Transportadora.builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .tipo(request.getTipo())
                .regioesAtendidas(request.getRegioesAtendidas())
                .telefone(request.getTelefone())
                .emailContato(request.getEmailContato())
                .build();
        return toResponse(transportadoraRepository.save(transportadora));
    }

    @Transactional(readOnly = true)
    public TransportadoraResponse buscarPorId(UUID id) {
        return toResponse(buscarTransportadora(id));
    }

    @Transactional(readOnly = true)
    public List<TransportadoraResponse> listarAtivas() {
        return transportadoraRepository.findByAtivaTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransportadoraResponse> listarPorRegiao(RegiaoCD regiao) {
        return transportadoraRepository.findByRegiaoAtendida(regiao)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TransportadoraResponse inativar(UUID id) {
        Transportadora transportadora = buscarTransportadora(id);
        transportadora.setAtiva(false);
        return toResponse(transportadoraRepository.save(transportadora));
    }

    @Transactional
    public VeiculoResponse cadastrarVeiculo(VeiculoRequest request) {
        Transportadora transportadora = buscarTransportadora(request.getTransportadoraId());

        Veiculo veiculo = Veiculo.builder()
                .placa(request.getPlaca())
                .tipo(request.getTipo())
                .transportadora(transportadora)
                .capacidadeKg(request.getCapacidadeKg())
                .capacidadePallets(request.getCapacidadePallets())
                .build();

        return toVeiculoResponse(veiculoRepository.save(veiculo));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> listarVeiculosDisponiveis() {
        return veiculoRepository.findByDisponivelTrue()
                .stream()
                .map(this::toVeiculoResponse)
                .toList();
    }

    @Transactional
    public VeiculoResponse atualizarDisponibilidade(UUID veiculoId, boolean disponivel) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
        veiculo.setDisponivel(disponivel);
        return toVeiculoResponse(veiculoRepository.save(veiculo));
    }

    private Transportadora buscarTransportadora(UUID id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Transportadora não encontrada: " + id));
    }

    private TransportadoraResponse toResponse(Transportadora t) {
        return TransportadoraResponse.builder()
                .id(t.getId())
                .nome(t.getNome())
                .cnpj(t.getCnpj())
                .tipo(t.getTipo())
                .regioesAtendidas(t.getRegioesAtendidas())
                .telefone(t.getTelefone())
                .emailContato(t.getEmailContato())
                .ativa(t.isAtiva())
                .criadoEm(t.getCriadoEm())
                .atualizadoEm(t.getAtualizadoEm())
                .build();
    }

    private VeiculoResponse toVeiculoResponse(Veiculo v) {
        return VeiculoResponse.builder()
                .id(v.getId())
                .placa(v.getPlaca())
                .tipo(v.getTipo())
                .transportadoraId(v.getTransportadora().getId())
                .transportadoraNome(v.getTransportadora().getNome())
                .capacidadeKg(v.getCapacidadeKg())
                .capacidadePallets(v.getCapacidadePallets())
                .disponivel(v.isDisponivel())
                .criadoEm(v.getCriadoEm())
                .atualizadoEm(v.getAtualizadoEm())
                .build();
    }

}