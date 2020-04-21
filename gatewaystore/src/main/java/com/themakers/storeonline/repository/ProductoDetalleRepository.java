package com.themakers.storeonline.repository;

import com.themakers.storeonline.domain.ProductoDetalle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProductoDetalle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoDetalleRepository extends JpaRepository<ProductoDetalle, Long>, JpaSpecificationExecutor<ProductoDetalle> {

}
