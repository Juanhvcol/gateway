package com.themakers.storeonline.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.themakers.storeonline.domain.ProductoDetalle} entity.
 */
public class ProductoDetalleDTO implements Serializable {

    private Long id;

    private String producto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductoDetalleDTO productoDetalleDTO = (ProductoDetalleDTO) o;
        if (productoDetalleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productoDetalleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductoDetalleDTO{" +
            "id=" + getId() +
            ", producto='" + getProducto() + "'" +
            "}";
    }
}
