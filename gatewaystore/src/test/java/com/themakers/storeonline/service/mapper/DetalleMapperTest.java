package com.themakers.storeonline.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DetalleMapperTest {

    private DetalleMapper detalleMapper;

    @BeforeEach
    public void setUp() {
        detalleMapper = new DetalleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(detalleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(detalleMapper.fromId(null)).isNull();
    }
}
