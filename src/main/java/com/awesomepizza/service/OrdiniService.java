package com.awesomepizza.service;

import com.awesomepizza.config.enums.StatoOrdine;
import com.awesomepizza.entity.Ordine;
import com.awesomepizza.repository.OrdiniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Optional;

@Slf4j
@Service
public class OrdiniService {

    private final OrdiniRepository ordiniRepository;

    public OrdiniService(OrdiniRepository ordiniRepository) {
        this.ordiniRepository = ordiniRepository;
    }

    public synchronized Ordine creaOrdine(Ordine ordine) {
        log.info("Creazione di un nuovo ordine: {}", ordine);
        Optional<Ordine> ultimoOrdineOpt = ordiniRepository.findTopByOrderByIdDesc();

        ordine.setCodice(getCodiceSuccessivo(ultimoOrdineOpt));
        ordine.setStato(StatoOrdine.IN_ATTESA);
        Ordine nuovoOrdine = ordiniRepository.save(ordine);
        log.info("Nuovo ordine creato: {}", nuovoOrdine);
        return nuovoOrdine;
    }

    public Ordine getOrdine(String codice) {
        log.info("Recupero dell'ordine con codice: {}", codice);
        Ordine ordine = ordiniRepository.findByCodice(codice);
        if (ordine == null) {
            log.warn("Ordine con codice {} non trovato", codice);
            return null;
        }
        log.info("Ordine trovato: {}", ordine);
        return ordine;
    }

    public Ordine prossimoOrdine() {
        log.info("Recupero del prossimo ordine");
        Optional<Ordine> ordineInPrep = ordiniRepository.findFirstByStatoOrderByIdAsc(StatoOrdine.IN_PREPARAZIONE);
        if (ordineInPrep.isPresent()) {
            log.info("Ordine in preparazione trovato: {}", ordineInPrep.get());
            return ordineInPrep.get();
        }
        Optional<Ordine> prossimoOrdine = ordiniRepository.findFirstByStatoOrderByIdAsc(StatoOrdine.IN_ATTESA);
        if (prossimoOrdine.isPresent()) {
            Ordine ordine = prossimoOrdine.get();
            ordine.setStato(StatoOrdine.IN_PREPARAZIONE);
            Ordine ordineAggiornato = ordiniRepository.save(ordine);
            log.info("Prossimo ordine aggiornato a IN_PREPARAZIONE: {}", ordineAggiornato);
            return ordineAggiornato;
        }
        log.warn("Nessun ordine disponibile");
        return null;
    }

    public Ordine aggiornaStatoOrdine(String code, String stato) {
        log.info("Aggiornamento dello stato dell'ordine con codice {} a {}", code, stato);
        Ordine ordine = ordiniRepository.findByCodice(code);
        if (ordine != null) {
            ordine.setStato(StatoOrdine.valueOf(stato));
            ordine.setDataModifica(LocalDate.now());
            Ordine ordineAggiornato = ordiniRepository.save(ordine);
            log.info("Ordine aggiornato: {}", ordineAggiornato);
            return ordineAggiornato;
        }
        log.warn("Ordine con codice {} non trovato", code);
        return null;
    }

    private String getCodiceSuccessivo(Optional<Ordine> ultimoOrdine) {
        LocalDate now = LocalDate.now();
        String prefissoOggi = "" + now.get(ChronoField.YEAR) +
                              now.get(ChronoField.MONTH_OF_YEAR) +
                              now.get(ChronoField.DAY_OF_MONTH);
        if (ultimoOrdine.isPresent()) {
            String ultimoCodice = ultimoOrdine.get().getCodice();
            if (ultimoCodice.contains(prefissoOggi)) {
                int codiceNumero = Integer.parseInt(ultimoCodice.substring(prefissoOggi.length() + 1));
                return prefissoOggi + "-" + (codiceNumero + 1);
            } else {
                return prefissoOggi + "-" + 1;
            }
        } else {
            return prefissoOggi + "-" + 1;
        }
    }
}