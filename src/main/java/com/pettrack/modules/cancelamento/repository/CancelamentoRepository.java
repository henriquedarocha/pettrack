package com.pettrack.modules.cancelamento.repository;

import com.pettrack.modules.cancelamento.domain.entity.SolicitacaoCancelamento;
import com.pettrack.modules.cancelamento.domain.enums.StatusCancelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CancelamentoRepository extends JpaRepository<SolicitacaoCancelamento, UUID> {

    Optional<SolicitacaoCancelamento> findByPedidoId(UUID pedidoId);

    List<SolicitacaoCancelamento> findByStatus(StatusCancelamento status);

    boolean existsByPedidoId(UUID pedidoId);

}