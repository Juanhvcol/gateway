package com.themakers.storeonline.service;

import com.themakers.storeonline.domain.Detalle;
import com.themakers.storeonline.repository.DetalleRepository;
import com.themakers.storeonline.service.dto.DetalleDTO;
import com.themakers.storeonline.service.mapper.DetalleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Detalle}.
 */
@Service
@Transactional
public class DetalleService {

    private final Logger log = LoggerFactory.getLogger(DetalleService.class);

    private final DetalleRepository detalleRepository;

    private final DetalleMapper detalleMapper;

    public DetalleService(DetalleRepository detalleRepository, DetalleMapper detalleMapper) {
        this.detalleRepository = detalleRepository;
        this.detalleMapper = detalleMapper;
    }

    /**
     * Save a detalle.
     *
     * @param detalleDTO the entity to save.
     * @return the persisted entity.
     */
    public DetalleDTO save(DetalleDTO detalleDTO) {
        log.debug("Request to save Detalle : {}", detalleDTO);
        Detalle detalle = detalleMapper.toEntity(detalleDTO);
        detalle = detalleRepository.save(detalle);
        return detalleMapper.toDto(detalle);
    }

    /**
     * Get all the detalles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DetalleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Detalles");
        return detalleRepository.findAll(pageable)
            .map(detalleMapper::toDto);
    }

    /**
     * Get one detalle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetalleDTO> findOne(Long id) {
        log.debug("Request to get Detalle : {}", id);
        return detalleRepository.findById(id)
            .map(detalleMapper::toDto);
    }

    /**
     * Delete the detalle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Detalle : {}", id);
        detalleRepository.deleteById(id);
    }
}
