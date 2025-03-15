package com.awesomepizza;

import com.awesomepizza.config.enums.StatoOrdine;
import com.awesomepizza.entity.Ordine;
import com.awesomepizza.repository.OrdiniRepository;
import com.awesomepizza.service.OrdiniService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AwesomePizzaApplicationTests {

    @Autowired
    private OrdiniService ordiniService;

    @Autowired
    private OrdiniRepository ordineRepository;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        ordineRepository.deleteAll();
    }

    @Test
    void testCreateOrder() {
        Ordine ordine = new Ordine();
        ordine.setPizza("Margherita");
        var ordineCreato = ordiniService.creaOrdine(ordine);
        assertNotNull(ordineCreato.getCodice());
        assertEquals(StatoOrdine.IN_ATTESA, ordineCreato.getStato());
    }

    @Test
    void testProssimoOrdine() {
        Ordine ordine = new Ordine();
        ordine.setPizza("Margherita");
        ordiniService.creaOrdine(ordine);

        Ordine takenOrdine = ordiniService.prossimoOrdine();
        assertNotNull(takenOrdine);
        assertEquals(StatoOrdine.IN_PREPARAZIONE, takenOrdine.getStato());
    }

    @Test
    void testProssimoOrdineNessunOrdine() {
        Ordine prossimoOrdine = ordiniService.prossimoOrdine();
        assertNull(prossimoOrdine);
    }

    @Test
    void testAggiornaStatoOrdine() {
        Ordine ordine = new Ordine();
        ordine.setPizza("Margherita");
        Ordine createdOrdine = ordiniService.creaOrdine(ordine);

        ordiniService.aggiornaStatoOrdine(createdOrdine.getCodice(), StatoOrdine.PRONTO.name());
        Ordine updatedOrdine = ordiniService.getOrdine(createdOrdine.getCodice());
        assertEquals(StatoOrdine.PRONTO, updatedOrdine.getStato());
    }

    @Test
    void testAggiornaStatoOrdineCodiceNonPresente() {
        String codiceNonPresente = "9999999-1";
        Ordine ordineAggiornato = ordiniService.aggiornaStatoOrdine(codiceNonPresente, StatoOrdine.PRONTO.name());
        assertNull(ordineAggiornato);
    }

    @Test
    void testGetOrdineNonTrovato() {
        String codiceNonPresente = "9999999-1";
        Ordine ordine = ordiniService.getOrdine(codiceNonPresente);
        assertNull(ordine);
    }

}
