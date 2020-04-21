package com.themkers.notificacion.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Notificacion.
 */
@Document(collection = "notificacion")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("fecha")
    private Instant fecha;

    @Field("mensaje")
    private String mensaje;

    @NotNull
    @Field("fecha_send")
    private Instant fechaSend;

    @Field("user_id")
    private Long userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Notificacion fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Notificacion mensaje(String mensaje) {
        this.mensaje = mensaje;
        return this;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Instant getFechaSend() {
        return fechaSend;
    }

    public Notificacion fechaSend(Instant fechaSend) {
        this.fechaSend = fechaSend;
        return this;
    }

    public void setFechaSend(Instant fechaSend) {
        this.fechaSend = fechaSend;
    }

    public Long getUserId() {
        return userId;
    }

    public Notificacion userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }
        return id != null && id.equals(((Notificacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", mensaje='" + getMensaje() + "'" +
            ", fechaSend='" + getFechaSend() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
