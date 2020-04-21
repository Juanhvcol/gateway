package com.themkers.notificacion.service.mapper;


import com.themkers.notificacion.domain.*;
import com.themkers.notificacion.service.dto.NotificacionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacion} and its DTO {@link NotificacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificacionMapper extends EntityMapper<NotificacionDTO, Notificacion> {



    default Notificacion fromId(String id) {
        if (id == null) {
            return null;
        }
        Notificacion notificacion = new Notificacion();
        notificacion.setId(id);
        return notificacion;
    }
}
