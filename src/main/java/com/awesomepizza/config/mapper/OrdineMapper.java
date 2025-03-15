package com.awesomepizza.config.mapper;

import com.awesomepizza.api.dto.OrdineApiDTO;
import com.awesomepizza.config.enums.StatoOrdine;
import com.awesomepizza.entity.Ordine;
import org.springframework.stereotype.Component;

@Component
public class OrdineMapper {

    public Ordine toEntity(OrdineApiDTO ordineApiDTO) {
        if (ordineApiDTO == null) {
            return null;
        }

        Ordine ordine = new Ordine();
        ordine.setId(ordineApiDTO.getId());
        ordine.setCodice(ordineApiDTO.getCodice());
        ordine.setDataInserimento(ordineApiDTO.getDataInserimento());
        ordine.setPizza(ordineApiDTO.getPizza());
        if(ordineApiDTO.getStato() != null) {
            ordine.setStato(StatoOrdine.valueOf(ordineApiDTO.getStato().getValue()));
        }

        return ordine;
    }

    public OrdineApiDTO toDto(Ordine ordine) {
        if (ordine == null) {
            return null;
        }

        OrdineApiDTO ordineApiDTO = new OrdineApiDTO();
        ordineApiDTO.setId(ordine.getId());
        ordineApiDTO.setCodice(ordine.getCodice());
        ordineApiDTO.setDataInserimento(ordine.getDataInserimento());
        ordineApiDTO.setPizza(ordine.getPizza());
        if(ordine.getStato() != null) {
            ordineApiDTO.setStato(OrdineApiDTO.StatoEnum.valueOf(ordine.getStato().name()));
        }

        return ordineApiDTO;
    }
}