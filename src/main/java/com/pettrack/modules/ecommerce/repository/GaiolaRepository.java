package com.pettrack.modules.ecommerce.repository;

import com.pettrack.modules.ecommerce.domain.entity.Gaiola;
import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusGaiola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GaiolaRepository extends JpaRepository<Gaiola, UUID> {

    Optional<Gaiola> findByRegiaoCDAndStatus(RegiaoCD regiaoCD, StatusGaiola status);

    Optional<Gaiola> findByCodigo(String codigo);

    List<Gaiola> findByStatus(StatusGaiola status);

}