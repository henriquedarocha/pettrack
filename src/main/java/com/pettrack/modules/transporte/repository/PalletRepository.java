package com.pettrack.modules.transporte.repository;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transporte.domain.entity.Pallet;
import com.pettrack.modules.transporte.domain.enums.StatusPallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PalletRepository extends JpaRepository<Pallet, UUID> {

    Optional<Pallet> findByCodigoPallet(String codigoPallet);

    List<Pallet> findByStatus(StatusPallet status);

    List<Pallet> findByRegiaoDestinoAndStatus(RegiaoCD regiaoDestino, StatusPallet status);

    boolean existsByCodigoPallet(String codigoPallet);

}