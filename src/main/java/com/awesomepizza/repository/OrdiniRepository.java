package com.awesomepizza.repository;

import com.awesomepizza.config.enums.StatoOrdine;
import com.awesomepizza.entity.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdiniRepository extends JpaRepository<Ordine, Long> {

    Ordine findByCodice(@Param("codice") String codice);

    Optional<Ordine> findFirstByStatoOrderByIdAsc(@Param("stato") StatoOrdine stato);

    Optional<Ordine> findTopByOrderByIdDesc();
}