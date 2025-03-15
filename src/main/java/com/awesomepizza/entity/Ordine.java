package com.awesomepizza.entity;

import com.awesomepizza.config.enums.StatoOrdine;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codice;

    @CreationTimestamp
    private LocalDate dataInserimento;
    private LocalDate dataModifica;
    private String pizza;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato;



}
