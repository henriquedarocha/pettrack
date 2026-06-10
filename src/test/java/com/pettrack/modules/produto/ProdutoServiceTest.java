package com.pettrack.modules.produto;

import com.pettrack.BaseIntegrationTest;
import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.EspecieAnimal;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import com.pettrack.modules.produto.dto.request.ProdutoRequest;
import com.pettrack.modules.produto.dto.response.ProdutoResponse;
import com.pettrack.modules.produto.repository.ProdutoRepository;
import com.pettrack.modules.produto.service.ProdutoService;
import com.pettrack.shared.exception.NegocioException;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Produto Service")
class ProdutoServiceTest extends BaseIntegrationTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void limparBanco() {
        produtoRepository.deleteAll();
    }

    private ProdutoRequest buildRequest() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Ração Premiere Cão Adulto");
        request.setCodigoSku("RAC-PREM-CAO-20KG");
        request.setCodigoEan("7896072310012");
        request.setCategoria(CategoriaProduto.RACAO_SECA);
        request.setEspecieAnimal(EspecieAnimal.CAO);
        request.setTipoArmazenamento(TipoArmazenamento.TEMPERATURA_AMBIENTE);
        request.setPesoKg(new BigDecimal("20.000"));
        request.setPrecoCusto(new BigDecimal("89.90"));
        request.setPrecoVenda(new BigDecimal("149.90"));
        request.setFabricante("Premiere");
        request.setTempoValidadeDias(365);
        return request;
    }

    @Nested
    @DisplayName("Cadastrar produto")
    class CadastrarProduto {

        @Test
        @DisplayName("Deve cadastrar produto com sucesso")
        void deveCadastrarProdutoComSucesso() {
            ProdutoResponse response = produtoService.cadastrar(buildRequest());

            assertThat(response.getId()).isNotNull();
            assertThat(response.getNome()).isEqualTo("Ração Premiere Cão Adulto");
            assertThat(response.getCodigoSku()).isEqualTo("RAC-PREM-CAO-20KG");
            assertThat(response.isAtivo()).isTrue();
            assertThat(response.getCriadoEm()).isNotNull();
        }

        @Test
        @DisplayName("Não deve cadastrar produto com SKU duplicado")
        void naoDeveCadastrarComSkuDuplicado() {
            produtoService.cadastrar(buildRequest());

            assertThatThrownBy(() -> produtoService.cadastrar(buildRequest()))
                    .isInstanceOf(NegocioException.class)
                    .hasMessageContaining("RAC-PREM-CAO-20KG");
        }

        @Test
        @DisplayName("Não deve expor preço de custo na resposta")
        void naoDeveExporPrecoCusto() {
            ProdutoResponse response = produtoService.cadastrar(buildRequest());
            assertThat(response).hasNoNullFieldsOrPropertiesExcept("precoCusto", "descricao",
                    "codigoEan", "tempoValidadeDias");
        }
    }

    @Nested
    @DisplayName("Buscar produto")
    class BuscarProduto {

        @Test
        @DisplayName("Deve buscar produto por ID com sucesso")
        void deveBuscarPorId() {
            ProdutoResponse cadastrado = produtoService.cadastrar(buildRequest());
            ProdutoResponse encontrado = produtoService.buscarPorId(cadastrado.getId());

            assertThat(encontrado.getId()).isEqualTo(cadastrado.getId());
            assertThat(encontrado.getNome()).isEqualTo(cadastrado.getNome());
        }

        @Test
        @DisplayName("Deve lançar exceção para ID inexistente")
        void deveLancarExcecaoParaIdInexistente() {
            assertThatThrownBy(() -> produtoService.buscarPorId(UUID.randomUUID()))
                    .isInstanceOf(RecursoNaoEncontradoException.class);
        }

        @Test
        @DisplayName("Deve buscar produto por SKU")
        void deveBuscarPorSku() {
            produtoService.cadastrar(buildRequest());
            ProdutoResponse encontrado = produtoService.buscarPorSku("RAC-PREM-CAO-20KG");

            assertThat(encontrado.getCodigoSku()).isEqualTo("RAC-PREM-CAO-20KG");
        }

        @Test
        @DisplayName("Deve listar apenas produtos ativos")
        void deveListarApenasAtivos() {
            ProdutoResponse cadastrado = produtoService.cadastrar(buildRequest());
            produtoService.inativar(cadastrado.getId());

            List<ProdutoResponse> ativos = produtoService.listarAtivos();
            assertThat(ativos).isEmpty();
        }
    }

    @Nested
    @DisplayName("Atualizar produto")
    class AtualizarProduto {

        @Test
        @DisplayName("Deve atualizar produto com sucesso")
        void deveAtualizarComSucesso() {
            ProdutoResponse cadastrado = produtoService.cadastrar(buildRequest());

            ProdutoRequest update = buildRequest();
            update.setNome("Ração Premiere Cão Senior");
            update.setPrecoVenda(new BigDecimal("159.90"));

            ProdutoResponse atualizado = produtoService.atualizar(cadastrado.getId(), update);

            assertThat(atualizado.getNome()).isEqualTo("Ração Premiere Cão Senior");
            assertThat(atualizado.getPrecoVenda()).isEqualByComparingTo("159.90");
        }
    }

    @Nested
    @DisplayName("Inativar produto")
    class InativarProduto {

        @Test
        @DisplayName("Deve inativar produto com sucesso")
        void deveInativarComSucesso() {
            ProdutoResponse cadastrado = produtoService.cadastrar(buildRequest());
            produtoService.inativar(cadastrado.getId());

            ProdutoResponse inativado = produtoService.buscarPorId(cadastrado.getId());
            assertThat(inativado.isAtivo()).isFalse();
        }
    }

}