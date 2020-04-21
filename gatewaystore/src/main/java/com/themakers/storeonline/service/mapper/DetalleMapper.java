package com.themakers.storeonline.service.mapper;


import com.themakers.storeonline.domain.*;
import com.themakers.storeonline.service.dto.DetalleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Detalle} and its DTO {@link DetalleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetalleMapper extends EntityMapper<DetalleDTO, Detalle> {



    default Detalle fromId(Long id) {
        if (id == null) {
            return null;
        }
        Detalle detalle = new Detalle();
        detalle.setId(id);
        return detalle;
    }
}
