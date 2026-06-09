package com.pettrack.modules.ecommerce.repository;

import com.pettrack.modules.ecommerce.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {

    List<ItemPedido> findByPedidoId(UUID pedidoId);

    @Query("""
            SELECT i FROM ItemPedido i
            WHERE i.pedido.id = :pedidoId
            AND i.itemEstoque IS NULL
            """)
    List<ItemPedido> findItensPendentesSeparacao(@Param("pedidoId") UUID pedidoId);

}