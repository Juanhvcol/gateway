package com.themakers.storeonline.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.storeonline.web.rest.TestUtil;

public class DetalleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Detalle.class);
        Detalle detalle1 = new Detalle();
        detalle1.setId(1L);
        Detalle detalle2 = new Detalle();
        detalle2.setId(detalle1.getId());
        assertThat(detalle1).isEqualTo(detalle2);
        detalle2.setId(2L);
        assertThat(detalle1).isNotEqualTo(detalle2);
        detalle1.setId(null);
        assertThat(detalle1).isNotEqualTo(detalle2);
    }
}
