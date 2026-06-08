package com.pettrack.modules.estoque.repository;

import com.pettrack.modules.estoque.domain.entity.ItemEstoque;
import com.pettrack.modules.estoque.domain.enums.StatusItemEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, UUID> {

    List<ItemEstoque> findByBaiaId(UUID baiaId);

    List<ItemEstoque> findByProdutoId(UUID produtoId);

    List<ItemEstoque> findByStatus(StatusItemEstoque status);

    @Query("""
            SELECT i FROM ItemEstoque i
            WHERE i.status = 'DISPONIVEL'
            AND i.produto.id = :produtoId
            ORDER BY i.dataValidade ASC NULLS LAST
            """)
    List<ItemEstoque> findDisponiveisPorProdutoOrdenadosPorValidade(
            @Param("produtoId") UUID produtoId);

    @Query("""
            SELECT i FROM ItemEstoque i
            WHERE i.dataValidade IS NOT NULL
            AND i.dataValidade < :hoje
            AND i.status NOT IN ('VENCIDO', 'DANIFICADO')
            """)
    List<ItemEstoque> findVencidos(@Param("hoje") LocalDate hoje);

    @Query("""
            SELECT i FROM ItemEstoque i
            WHERE i.dataValidade IS NOT NULL
            AND i.dataValidade BETWEEN :hoje AND :limite
            AND i.status = 'DISPONIVEL'
            """)
    List<ItemEstoque> findProximosDoVencimento(
            @Param("hoje") LocalDate hoje,
            @Param("limite") LocalDate limite);

    @Query("""
            SELECT SUM(i.quantidade) FROM ItemEstoque i
            WHERE i.produto.id = :produtoId
            AND i.status = 'DISPONIVEL'
            """)
    Integer totalDisponivelPorProduto(@Param("produtoId") UUID produtoId);
}