package com.awesomepizza.api.impl;

import com.awesomepizza.api.OrdiniApiDelegate;
import com.awesomepizza.api.dto.OrdineApiDTO;
import com.awesomepizza.config.mapper.OrdineMapper;
import com.awesomepizza.entity.Ordine;
import com.awesomepizza.service.OrdiniService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrdiniApiDelegateImpl implements OrdiniApiDelegate {

    private final OrdiniService ordiniService;
    private final OrdineMapper ordineMapper;

    public OrdiniApiDelegateImpl(OrdiniService ordiniService, OrdineMapper ordineMapper) {
        this.ordiniService = ordiniService;
        this.ordineMapper = ordineMapper;
    }

    @Override
    public ResponseEntity<OrdineApiDTO> creaOrdine(OrdineApiDTO ordine) {
        try {
            Ordine nuovoOrdine = ordiniService.creaOrdine(ordineMapper.toEntity(ordine));
            return ResponseEntity.ok(ordineMapper.toDto(nuovoOrdine));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<OrdineApiDTO> getOrdine(String codice) {
        try {
            Ordine ordine = ordiniService.getOrdine(codice);
            if (ordine != null) {
                return ResponseEntity.ok(ordineMapper.toDto(ordine));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<OrdineApiDTO> prossimoOrdine() {
        try {
            Ordine prossimoOrdine = ordiniService.prossimoOrdine();
            if (prossimoOrdine != null) {
                return ResponseEntity.ok(ordineMapper.toDto(prossimoOrdine));
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<OrdineApiDTO> aggiornaStatoOrdine(String codice, String stato) {
        try {
            Ordine ordineAggiornato = ordiniService.aggiornaStatoOrdine(codice, stato);
            if (ordineAggiornato == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ordineMapper.toDto(ordineAggiornato));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}