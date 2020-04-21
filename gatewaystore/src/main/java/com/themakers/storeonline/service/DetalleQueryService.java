package com.themakers.storeonline.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.themakers.storeonline.domain.Detalle;
import com.themakers.storeonline.domain.*; // for static metamodels
import com.themakers.storeonline.repository.DetalleRepository;
import com.themakers.storeonline.service.dto.DetalleCriteria;
import com.themakers.storeonline.service.dto.DetalleDTO;
import com.themakers.storeonline.service.mapper.DetalleMapper;

/**
 * Service for executing complex queries for {@link Detalle} entities in the database.
 * The main input is a {@link DetalleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DetalleDTO} or a {@link Page} of {@link DetalleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetalleQueryService extends QueryService<Detalle> {

    private final Logger log = LoggerFactory.getLogger(DetalleQueryService.class);

    private final DetalleRepository detalleRepository;

    private final DetalleMapper detalleMapper;

    public DetalleQueryService(DetalleRepository detalleRepository, DetalleMapper detalleMapper) {
        this.detalleRepository = detalleRepository;
        this.detalleMapper = detalleMapper;
    }

    /**
     * Return a {@link List} of {@link DetalleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DetalleDTO> findByCriteria(DetalleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Detalle> specification = createSpecification(criteria);
        return detalleMapper.toDto(detalleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DetalleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DetalleDTO> findByCriteria(DetalleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Detalle> specification = createSpecification(criteria);
        return detalleRepository.findAll(specification, page)
            .map(detalleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetalleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Detalle> specification = createSpecification(criteria);
        return detalleRepository.count(specification);
    }

    /**
     * Function to convert {@link DetalleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Detalle> createSpecification(DetalleCriteria criteria) {
        Specification<Detalle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Detalle_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Detalle_.nombre));
            }
        }
        return specification;
    }
}
