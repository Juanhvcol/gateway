package com.themkers.notificacion.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.themkers.notificacion.domain.Notificacion} entity.
 */
public class NotificacionDTO implements Serializable {

    private String id;

    @NotNull
    private Instant fecha;

    private String mensaje;

    @NotNull
    private Instant fechaSend;

    private Long userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Instant getFechaSend() {
        return fechaSend;
    }

    public void setFechaSend(Instant fechaSend) {
        this.fechaSend = fechaSend;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificacionDTO notificacionDTO = (NotificacionDTO) o;
        if (notificacionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificacionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificacionDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", mensaje='" + getMensaje() + "'" +
            ", fechaSend='" + getFechaSend() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
