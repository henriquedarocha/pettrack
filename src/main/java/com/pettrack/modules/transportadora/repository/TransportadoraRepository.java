package com.pettrack.modules.transportadora.repository;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.domain.entity.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, UUID> {

    List<Transportadora> findByAtivaTrue();

    @Query("""
            SELECT t FROM Transportadora t
            WHERE :regiao MEMBER OF t.regioesAtendidas
            AND t.ativa = true
            """)
    List<Transportadora> findByRegiaoAtendida(@Param("regiao") RegiaoCD regiao);

}