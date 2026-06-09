package com.pettrack.modules.transportadora.repository;

import com.pettrack.modules.transportadora.domain.entity.Veiculo;
import com.pettrack.modules.transportadora.domain.enums.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {

    List<Veiculo> findByTransportadoraId(UUID transportadoraId);

    List<Veiculo> findByDisponivelTrue();

    List<Veiculo> findByTipoAndDisponivelTrue(TipoVeiculo tipo);

}