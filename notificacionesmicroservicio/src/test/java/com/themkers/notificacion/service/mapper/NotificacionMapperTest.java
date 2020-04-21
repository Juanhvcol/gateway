package com.themkers.notificacion.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificacionMapperTest {

    private NotificacionMapper notificacionMapper;

    @BeforeEach
    public void setUp() {
        notificacionMapper = new NotificacionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(notificacionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(notificacionMapper.fromId(null)).isNull();
    }
}
