package com.themakers.storeonline.repository;

import com.themakers.storeonline.domain.Detalle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Detalle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleRepository extends JpaRepository<Detalle, Long>, JpaSpecificationExecutor<Detalle> {

}
