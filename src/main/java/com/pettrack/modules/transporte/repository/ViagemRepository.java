package com.pettrack.modules.transporte.repository;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transporte.domain.entity.Viagem;
import com.pettrack.modules.transporte.domain.enums.StatusViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, UUID> {

    Optional<Viagem> findByCodigoViagem(String codigoViagem);

    List<Viagem> findByStatus(StatusViagem status);

    List<Viagem> findByRegiaoCDAndStatus(RegiaoCD regiaoCD, StatusViagem status);

    boolean existsByCodigoViagem(String codigoViagem);

}