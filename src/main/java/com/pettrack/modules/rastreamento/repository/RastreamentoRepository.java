package com.pettrack.modules.rastreamento.repository;

import com.pettrack.modules.rastreamento.domain.entity.HistoricoRastreamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RastreamentoRepository extends JpaRepository<HistoricoRastreamento, UUID> {

    List<HistoricoRastreamento> findByPedidoIdOrderByRegistradoEmAsc(UUID pedidoId);

}