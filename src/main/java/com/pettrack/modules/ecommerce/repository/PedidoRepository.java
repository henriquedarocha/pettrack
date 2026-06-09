package com.pettrack.modules.ecommerce.repository;

import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByRegiaoCD(RegiaoCD regiaoCD);

    List<Pedido> findByGaiolaId(UUID gaiolaId);

    boolean existsByNumeroPedido(String numeroPedido);

}