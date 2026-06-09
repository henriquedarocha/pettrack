package com.pettrack.modules.filial.repository;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.filial.domain.entity.Filial;
import com.pettrack.modules.filial.domain.enums.StatusFilial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilialRepository extends JpaRepository<Filial, UUID> {

    List<Filial> findByStatus(StatusFilial status);

    Optional<Filial> findByRegiao(RegiaoCD regiao);

    List<Filial> findByStatusAndRegiao(StatusFilial status, RegiaoCD regiao);

}