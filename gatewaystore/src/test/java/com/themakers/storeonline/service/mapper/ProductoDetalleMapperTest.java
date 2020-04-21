package com.themakers.storeonline.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductoDetalleMapperTest {

    private ProductoDetalleMapper productoDetalleMapper;

    @BeforeEach
    public void setUp() {
        productoDetalleMapper = new ProductoDetalleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(productoDetalleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productoDetalleMapper.fromId(null)).isNull();
    }
}
