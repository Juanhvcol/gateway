package com.themakers.storeonline.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.storeonline.web.rest.TestUtil;

public class DetalleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetalleDTO.class);
        DetalleDTO detalleDTO1 = new DetalleDTO();
        detalleDTO1.setId(1L);
        DetalleDTO detalleDTO2 = new DetalleDTO();
        assertThat(detalleDTO1).isNotEqualTo(detalleDTO2);
        detalleDTO2.setId(detalleDTO1.getId());
        assertThat(detalleDTO1).isEqualTo(detalleDTO2);
        detalleDTO2.setId(2L);
        assertThat(detalleDTO1).isNotEqualTo(detalleDTO2);
        detalleDTO1.setId(null);
        assertThat(detalleDTO1).isNotEqualTo(detalleDTO2);
    }
}
