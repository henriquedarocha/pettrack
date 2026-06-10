package com.pettrack.modules.estoque;

import com.pettrack.BaseIntegrationTest;
import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.estoque.dto.request.BaiaRequest;
import com.pettrack.modules.estoque.dto.request.ItemEstoqueRequest;
import com.pettrack.modules.estoque.dto.response.BaiaResponse;
import com.pettrack.modules.estoque.dto.response.ItemEstoqueResponse;
import com.pettrack.modules.estoque.repository.BaiaRepository;
import com.pettrack.modules.estoque.repository.ItemEstoqueRepository;
import com.pettrack.modules.estoque.service.EstoqueService;
import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.EspecieAnimal;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import com.pettrack.modules.produto.dto.request.ProdutoRequest;
import com.pettrack.modules.produto.repository.ProdutoRepository;
import com.pettrack.modules.produto.service.ProdutoService;
import com.pettrack.shared.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Estoque Service")
class EstoqueServiceTest extends BaseIntegrationTest {

    @Autowired private EstoqueService estoqueService;
    @Autowired private ProdutoService produtoService;
    @Autowired private BaiaRepository baiaRepository;
    @Autowired private ItemEstoqueRepository itemEstoqueRepository;
    @Autowired private ProdutoRepository produtoRepository;

    private UUID produtoId;
    private UUID baiaId;

    @BeforeEach
    void setup() {
        itemEstoqueRepository.deleteAll();
        baiaRepository.deleteAll();
        produtoRepository.deleteAll();

        ProdutoRequest produtoRequest = new ProdutoRequest();
        produtoRequest.setNome("Ração Premiere");
        produtoRequest.setCodigoSku("RAC-001");
        produtoRequest.setCategoria(CategoriaProduto.RACAO_SECA);
        produtoRequest.setEspecieAnimal(EspecieAnimal.CAO);
        produtoRequest.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
        produtoRequest.setPesoKg(new BigDecimal("20.000"));
        produtoRequest.setPrecoVenda(new BigDecimal("149.90"));
        produtoRequest.setFabricante("Premiere");
        produtoId = produtoService.cadastrar(produtoRequest).getId();

        BaiaRequest baiaRequest = new BaiaRequest();
        baiaRequest.setCodigo("D54");
        baiaRequest.setDescricao("Baia ração seca cães");
        baiaRequest.setCategoriaProduto(CategoriaProduto.RACAO_SECA);
        baiaRequest.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
        baiaRequest.setCapacidadeKg(new BigDecimal("500.000"));
        baiaRequest.setCapacidadeUnidades(25);
        baiaId = estoqueService.cadastrarBaia(baiaRequest).getId();
    }

    @Nested
    @DisplayName("Baias")
    class Baias {

        @Test
        @DisplayName("Deve cadastrar baia com sucesso")
        void deveCadastrarBaia() {
            BaiaRequest request = new BaiaRequest();
            request.setCodigo("A01");
            request.setDescricao("Baia teste");
            request.setCategoriaProduto(CategoriaProduto.RACAO_SECA);
            request.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
            request.setCapacidadeKg(new BigDecimal("200.000"));
            request.setCapacidadeUnidades(10);

            BaiaResponse response = estoqueService.cadastrarBaia(request);

            assertThat(response.getId()).isNotNull();
            assertThat(response.getCodigo()).isEqualTo("A01");
            assertThat(response.getStatus()).isEqualTo(StatusBaia.DISPONIVEL);
            assertThat(response.getPercentualOcupacao()).isZero();
        }

        @Test
        @DisplayName("Não deve cadastrar baia com código duplicado")
        void naoDeveCadastrarComCodigoDuplicado() {
            BaiaRequest request = new BaiaRequest();
            request.setCodigo("D54");
            request.setDescricao("Duplicada");
            request.setCategoriaProduto(CategoriaProduto.RACAO_SECA);
            request.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
            request.setCapacidadeKg(new BigDecimal("100.000"));
            request.setCapacidadeUnidades(5);

            assertThatThrownBy(() -> estoqueService.cadastrarBaia(request))
                    .isInstanceOf(NegocioException.class)
                    .hasMessageContaining("D54");
        }

        @Test
        @DisplayName("Deve exigir temperatura para baia refrigerada")
        void deveExigirTemperaturaParaBaiaRefrigerada() {
            BaiaRequest request = new BaiaRequest();
            request.setCodigo("R01");
            request.setDescricao("Baia refrigerada");
            request.setCategoriaProduto(CategoriaProduto.VACINA);
            request.setTipoArmazenamento(TipoArmazenamento.REFRIGERADO);
            request.setCapacidadeKg(new BigDecimal("100.000"));
            request.setCapacidadeUnidades(50);

            assertThatThrownBy(() -> estoqueService.cadastrarBaia(request))
                    .isInstanceOf(NegocioException.class)
                    .hasMessageContaining("temperatura");
        }
    }

    @Nested
    @DisplayName("Itens de Estoque")
    class ItensEstoque {

        @Test
        @DisplayName("Deve entrar produto no estoque e atualizar baia")
        void deveEntrarEstoqueEAtualizarBaia() {
            ItemEstoqueRequest request = new ItemEstoqueRequest();
            request.setProdutoId(produtoId);
            request.setBaiaId(baiaId);
            request.setNumeroLote("LOTE-001");
            request.setDataValidade(LocalDate.now().plusYears(1));
            request.setQuantidade(5);
            request.setNotaFiscalEntrada("NF-001");

            ItemEstoqueResponse response = estoqueService.entrarEstoque(request);

            assertThat(response.getId()).isNotNull();
            assertThat(response.getQuantidade()).isEqualTo(5);
            assertThat(response.getBaiaCodigo()).isEqualTo("D54");

            BaiaResponse baia = estoqueService.buscarBaiaPorId(baiaId);
            assertThat(baia.getUnidadesAtuais()).isEqualTo(5);
            assertThat(baia.getPesoAtualKg()).isEqualByComparingTo("100.000");
            assertThat(baia.getStatus()).isEqualTo(StatusBaia.OCUPADA);
        }

        @Test
        @DisplayName("Não deve entrar produto em baia de categoria diferente")
        void naoDeveEntrarProdutoEmBaiaDeCategoriaDiferente() {
            BaiaRequest baiaRequest = new BaiaRequest();
            baiaRequest.setCodigo("MED01");
            baiaRequest.setDescricao("Baia medicamentos");
            baiaRequest.setCategoriaProduto(CategoriaProduto.MEDICAMENTO_OTC);
            baiaRequest.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
            baiaRequest.setCapacidadeKg(new BigDecimal("100.000"));
            baiaRequest.setCapacidadeUnidades(50);
            UUID baiaErrada = estoqueService.cadastrarBaia(baiaRequest).getId();

            ItemEstoqueRequest request = new ItemEstoqueRequest();
            request.setProdutoId(produtoId);
            request.setBaiaId(baiaErrada);
            request.setNumeroLote("LOTE-001");
            request.setQuantidade(1);

            assertThatThrownBy(() -> estoqueService.entrarEstoque(request))
                    .isInstanceOf(NegocioException.class)
                    .hasMessageContaining("categoria");
        }

        @Test
        @DisplayName("Deve calcular total disponível por produto")
        void deveCalcularTotalDisponivel() {
            ItemEstoqueRequest request = new ItemEstoqueRequest();
            request.setProdutoId(produtoId);
            request.setBaiaId(baiaId);
            request.setNumeroLote("LOTE-001");
            request.setQuantidade(8);

            estoqueService.entrarEstoque(request);

            Integer total = estoqueService.totalDisponivelPorProduto(produtoId);
            assertThat(total).isEqualTo(8);
        }

    }

}