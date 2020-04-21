package com.themakers.storeonline.service.mapper;


import com.themakers.storeonline.domain.*;
import com.themakers.storeonline.service.dto.ProductoDetalleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductoDetalle} and its DTO {@link ProductoDetalleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoDetalleMapper extends EntityMapper<ProductoDetalleDTO, ProductoDetalle> {



    default ProductoDetalle fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductoDetalle productoDetalle = new ProductoDetalle();
        productoDetalle.setId(id);
        return productoDetalle;
    }
}
