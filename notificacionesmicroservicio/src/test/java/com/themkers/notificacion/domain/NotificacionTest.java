package com.themkers.notificacion.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themkers.notificacion.web.rest.TestUtil;

public class NotificacionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificacion.class);
        Notificacion notificacion1 = new Notificacion();
        notificacion1.setId("id1");
        Notificacion notificacion2 = new Notificacion();
        notificacion2.setId(notificacion1.getId());
        assertThat(notificacion1).isEqualTo(notificacion2);
        notificacion2.setId("id2");
        assertThat(notificacion1).isNotEqualTo(notificacion2);
        notificacion1.setId(null);
        assertThat(notificacion1).isNotEqualTo(notificacion2);
    }
}
