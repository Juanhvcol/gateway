package com.themkers.notificacion.service;

import com.themkers.notificacion.domain.Notificacion;
import com.themkers.notificacion.repository.NotificacionRepository;
import com.themkers.notificacion.service.dto.NotificacionDTO;
import com.themkers.notificacion.service.mapper.NotificacionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Notificacion}.
 */
@Service
public class NotificacionService {

    private final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;

    private final NotificacionMapper notificacionMapper;

    public NotificacionService(NotificacionRepository notificacionRepository, NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    /**
     * Save a notificacion.
     *
     * @param notificacionDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificacionDTO save(NotificacionDTO notificacionDTO) {
        log.debug("Request to save Notificacion : {}", notificacionDTO);
        Notificacion notificacion = notificacionMapper.toEntity(notificacionDTO);
        notificacion = notificacionRepository.save(notificacion);
        return notificacionMapper.toDto(notificacion);
    }

    /**
     * Get all the notificacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<NotificacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notificacions");
        return notificacionRepository.findAll(pageable)
            .map(notificacionMapper::toDto);
    }

    /**
     * Get one notificacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<NotificacionDTO> findOne(String id) {
        log.debug("Request to get Notificacion : {}", id);
        return notificacionRepository.findById(id)
            .map(notificacionMapper::toDto);
    }

    /**
     * Delete the notificacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Notificacion : {}", id);
        notificacionRepository.deleteById(id);
    }
}
