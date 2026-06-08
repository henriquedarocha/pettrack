package com.pettrack.modules.estoque.repository;

import com.pettrack.modules.estoque.domain.entity.Baia;
import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BaiaRepository extends JpaRepository<Baia, UUID> {

    Optional<Baia> findByCodigo(String codigo);

    List<Baia> findByStatus(StatusBaia status);

    List<Baia> findByCategoriaProduto(CategoriaProduto categoria);

    List<Baia> findByTipoArmazenamento(TipoArmazenamento tipoArmazenamento);

    List<Baia> findByCategoriaProdutoAndStatus(CategoriaProduto categoria, StatusBaia status);

    boolean existsByCodigo(String codigo);

}