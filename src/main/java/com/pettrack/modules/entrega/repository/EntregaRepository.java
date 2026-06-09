package com.pettrack.modules.entrega.repository;

import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import com.pettrack.modules.entrega.domain.entity.EntregaFinal;
import com.pettrack.modules.entrega.domain.enums.StatusEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntregaRepository extends JpaRepository<EntregaFinal, UUID> {

    Optional<EntregaFinal> findByPedidoId(UUID pedidoId);

    List<EntregaFinal> findByFilialIdAndStatus(UUID filialId, StatusEntrega status);

    List<EntregaFinal> findBySubregiaoAndStatus(SubregiaoFilial subregiao, StatusEntrega status);

}