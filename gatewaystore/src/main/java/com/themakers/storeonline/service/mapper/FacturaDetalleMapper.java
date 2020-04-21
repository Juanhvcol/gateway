package com.themakers.storeonline.service.mapper;


import com.themakers.storeonline.domain.*;
import com.themakers.storeonline.service.dto.FacturaDetalleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacturaDetalle} and its DTO {@link FacturaDetalleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturaDetalleMapper extends EntityMapper<FacturaDetalleDTO, FacturaDetalle> {



    default FacturaDetalle fromId(Long id) {
        if (id == null) {
            return null;
        }
        FacturaDetalle facturaDetalle = new FacturaDetalle();
        facturaDetalle.setId(id);
        return facturaDetalle;
    }
}
