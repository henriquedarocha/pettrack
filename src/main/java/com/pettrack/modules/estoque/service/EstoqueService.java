package com.pettrack.modules.estoque.service;

import com.pettrack.modules.estoque.domain.entity.Baia;
import com.pettrack.modules.estoque.domain.entity.ItemEstoque;
import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.estoque.domain.enums.StatusItemEstoque;
import com.pettrack.modules.estoque.dto.request.BaiaRequest;
import com.pettrack.modules.estoque.dto.request.ItemEstoqueRequest;
import com.pettrack.modules.estoque.dto.response.BaiaResponse;
import com.pettrack.modules.estoque.dto.response.ItemEstoqueResponse;
import com.pettrack.modules.estoque.repository.BaiaRepository;
import com.pettrack.modules.estoque.repository.ItemEstoqueRepository;
import com.pettrack.modules.produto.domain.entity.Produto;
import com.pettrack.modules.produto.repository.ProdutoRepository;
import com.pettrack.shared.exception.NegocioException;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstoqueService {

    private static final int DIAS_ALERTA_VENCIMENTO = 30;

    private final BaiaRepository baiaRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final ProdutoRepository produtoRepository;

    // ==================== BAIAS ====================

    @Transactional
    public BaiaResponse cadastrarBaia(BaiaRequest request) {
        if (baiaRepository.existsByCodigo(request.getCodigo())) {
            throw new NegocioException("Já existe uma baia com o código: " + request.getCodigo());
        }

        if (request.getTipoArmazenamento().name().equals("REFRIGERADO") ||
                request.getTipoArmazenamento().name().equals("CONGELADO")) {
            if (request.getTemperaturaMinima() == null || request.getTemperaturaMaxima() == null) {
                throw new NegocioException("Baias refrigeradas e congeladas exigem temperatura mínima e máxima");
            }
        }

        Baia baia = Baia.builder()
                .codigo(request.getCodigo())
                .descricao(request.getDescricao())
                .categoriaProduto(request.getCategoriaProduto())
                .tipoArmazenamento(request.getTipoArmazenamento())
                .capacidadeKg(request.getCapacidadeKg())
                .capacidadeUnidades(request.getCapacidadeUnidades())
                .temperaturaMinima(request.getTemperaturaMinima())
                .temperaturaMaxima(request.getTemperaturaMaxima())
                .build();

        return toBaiaResponse(baiaRepository.save(baia));
    }

    @Transactional(readOnly = true)
    public BaiaResponse buscarBaiaPorId(UUID id) {
        Baia baia = baiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Baia não encontrada com id: " + id));
        return toBaiaResponse(baia);
    }

    @Transactional(readOnly = true)
    public BaiaResponse buscarBaiaPorCodigo(String codigo) {
        Baia baia = baiaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Baia não encontrada com código: " + codigo));
        return toBaiaResponse(baia);
    }

    @Transactional(readOnly = true)
    public List<BaiaResponse> listarBaias() {
        return baiaRepository.findAll()
                .stream()
                .map(this::toBaiaResponse)
                .toList();
    }

    @Transactional
    public BaiaResponse atualizarStatusBaia(UUID id, StatusBaia novoStatus) {
        Baia baia = baiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Baia não encontrada com id: " + id));
        baia.setStatus(novoStatus);
        return toBaiaResponse(baiaRepository.save(baia));
    }

    // ==================== ITENS ====================

    @Transactional
    public ItemEstoqueResponse entrarEstoque(ItemEstoqueRequest request) {
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        Baia baia = baiaRepository.findById(request.getBaiaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Baia não encontrada"));

        if (baia.getStatus() == StatusBaia.BLOQUEADA || baia.getStatus() == StatusBaia.MANUTENCAO) {
            throw new NegocioException("Baia " + baia.getCodigo() + " está " + baia.getStatus());
        }

        if (!baia.getCategoriaProduto().equals(produto.getCategoria())) {
            throw new NegocioException("Produto da categoria " + produto.getCategoria()
                    + " não pode ser armazenado na baia " + baia.getCodigo()
                    + " destinada para " + baia.getCategoriaProduto());
        }

        if (!baia.temCapacidade(produto.getPesoKg().multiply(
                java.math.BigDecimal.valueOf(request.getQuantidade())), request.getQuantidade())) {
            throw new NegocioException("Baia " + baia.getCodigo() + " não tem capacidade suficiente");
        }

        ItemEstoque item = ItemEstoque.builder()
                .produto(produto)
                .baia(baia)
                .numeroLote(request.getNumeroLote())
                .dataFabricacao(request.getDataFabricacao())
                .dataValidade(request.getDataValidade())
                .quantidade(request.getQuantidade())
                .numeroSerie(request.getNumeroSerie())
                .notaFiscalEntrada(request.getNotaFiscalEntrada())
                .build();

        itemEstoqueRepository.save(item);

        baia.setPesoAtualKg(baia.getPesoAtualKg().add(
                produto.getPesoKg().multiply(java.math.BigDecimal.valueOf(request.getQuantidade()))));
        baia.setUnidadesAtuais(baia.getUnidadesAtuais() + request.getQuantidade());
        atualizarStatusBaiaAutomatico(baia);
        baiaRepository.save(baia);

        return toItemResponse(item);
    }

    @Transactional(readOnly = true)
    public List<ItemEstoqueResponse> listarItensPorBaia(UUID baiaId) {
        return itemEstoqueRepository.findByBaiaId(baiaId)
                .stream()
                .map(this::toItemResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemEstoqueResponse> listarItensPorProduto(UUID produtoId) {
        return itemEstoqueRepository.findDisponiveisPorProdutoOrdenadosPorValidade(produtoId)
                .stream()
                .map(this::toItemResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer totalDisponivelPorProduto(UUID produtoId) {
        Integer total = itemEstoqueRepository.totalDisponivelPorProduto(produtoId);
        return total != null ? total : 0;
    }

    // ==================== SCHEDULER ====================

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void verificarVencimentos() {
        log.info("Iniciando verificação de vencimentos...");

        List<ItemEstoque> vencidos = itemEstoqueRepository.findVencidos(LocalDate.now());
        vencidos.forEach(item -> {
            item.setStatus(StatusItemEstoque.VENCIDO);
            log.warn("Item vencido — Produto: {}, Lote: {}, Baia: {}, Validade: {}",
                    item.getProduto().getNome(),
                    item.getNumeroLote(),
                    item.getBaia().getCodigo(),
                    item.getDataValidade());
        });
        itemEstoqueRepository.saveAll(vencidos);

        List<ItemEstoque> proximosVencer = itemEstoqueRepository.findProximosDoVencimento(
                LocalDate.now(),
                LocalDate.now().plusDays(DIAS_ALERTA_VENCIMENTO));
        proximosVencer.forEach(item ->
                log.warn("ALERTA — Produto: {}, Lote: {}, Baia: {}, Vence em: {}",
                        item.getProduto().getNome(),
                        item.getNumeroLote(),
                        item.getBaia().getCodigo(),
                        item.getDataValidade()));

        log.info("Verificação concluída. {} vencidos, {} próximos do vencimento.",
                vencidos.size(), proximosVencer.size());
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private void atualizarStatusBaiaAutomatico(Baia baia) {
        double ocupacao = baia.percentualOcupacao();
        if (ocupacao >= 100) {
            baia.setStatus(StatusBaia.LOTADA);
        } else if (ocupacao > 0) {
            baia.setStatus(StatusBaia.OCUPADA);
        } else {
            baia.setStatus(StatusBaia.DISPONIVEL);
        }
    }

    private BaiaResponse toBaiaResponse(Baia baia) {
        return BaiaResponse.builder()
                .id(baia.getId())
                .codigo(baia.getCodigo())
                .descricao(baia.getDescricao())
                .categoriaProduto(baia.getCategoriaProduto())
                .tipoArmazenamento(baia.getTipoArmazenamento())
                .capacidadeKg(baia.getCapacidadeKg())
                .capacidadeUnidades(baia.getCapacidadeUnidades())
                .pesoAtualKg(baia.getPesoAtualKg())
                .unidadesAtuais(baia.getUnidadesAtuais())
                .status(baia.getStatus())
                .temperaturaMinima(baia.getTemperaturaMinima())
                .temperaturaMaxima(baia.getTemperaturaMaxima())
                .percentualOcupacao(baia.percentualOcupacao())
                .criadoEm(baia.getCriadoEm())
                .atualizadoEm(baia.getAtualizadoEm())
                .build();
    }

    private ItemEstoqueResponse toItemResponse(ItemEstoque item) {
        return ItemEstoqueResponse.builder()
                .id(item.getId())
                .produtoId(item.getProduto().getId())
                .produtoNome(item.getProduto().getNome())
                .produtoSku(item.getProduto().getCodigoSku())
                .baiaId(item.getBaia().getId())
                .baiaCodigo(item.getBaia().getCodigo())
                .numeroLote(item.getNumeroLote())
                .dataFabricacao(item.getDataFabricacao())
                .dataValidade(item.getDataValidade())
                .quantidade(item.getQuantidade())
                .numeroSerie(item.getNumeroSerie())
                .notaFiscalEntrada(item.getNotaFiscalEntrada())
                .status(item.getStatus())
                .proximoDoVencimento(item.venceEm(DIAS_ALERTA_VENCIMENTO))
                .criadoEm(item.getCriadoEm())
                .atualizadoEm(item.getAtualizadoEm())
                .build();
    }

}