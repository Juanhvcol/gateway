package com.themakers.storeonline.web.rest;

import com.themakers.storeonline.service.DetalleService;
import com.themakers.storeonline.web.rest.errors.BadRequestAlertException;
import com.themakers.storeonline.service.dto.DetalleDTO;
import com.themakers.storeonline.service.dto.DetalleCriteria;
import com.themakers.storeonline.service.DetalleQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.themakers.storeonline.domain.Detalle}.
 */
@RestController
@RequestMapping("/api")
public class DetalleResource {

    private final Logger log = LoggerFactory.getLogger(DetalleResource.class);

    private static final String ENTITY_NAME = "detalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleService detalleService;

    private final DetalleQueryService detalleQueryService;

    public DetalleResource(DetalleService detalleService, DetalleQueryService detalleQueryService) {
        this.detalleService = detalleService;
        this.detalleQueryService = detalleQueryService;
    }

    /**
     * {@code POST  /detalles} : Create a new detalle.
     *
     * @param detalleDTO the detalleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleDTO, or with status {@code 400 (Bad Request)} if the detalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detalles")
    public ResponseEntity<DetalleDTO> createDetalle(@RequestBody DetalleDTO detalleDTO) throws URISyntaxException {
        log.debug("REST request to save Detalle : {}", detalleDTO);
        if (detalleDTO.getId() != null) {
            throw new BadRequestAlertException("A new detalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetalleDTO result = detalleService.save(detalleDTO);
        return ResponseEntity.created(new URI("/api/detalles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detalles} : Updates an existing detalle.
     *
     * @param detalleDTO the detalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleDTO,
     * or with status {@code 400 (Bad Request)} if the detalleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detalles")
    public ResponseEntity<DetalleDTO> updateDetalle(@RequestBody DetalleDTO detalleDTO) throws URISyntaxException {
        log.debug("REST request to update Detalle : {}", detalleDTO);
        if (detalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DetalleDTO result = detalleService.save(detalleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /detalles} : get all the detalles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalles in body.
     */
    @GetMapping("/detalles")
    public ResponseEntity<List<DetalleDTO>> getAllDetalles(DetalleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Detalles by criteria: {}", criteria);
        Page<DetalleDTO> page = detalleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detalles/count} : count all the detalles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/detalles/count")
    public ResponseEntity<Long> countDetalles(DetalleCriteria criteria) {
        log.debug("REST request to count Detalles by criteria: {}", criteria);
        return ResponseEntity.ok().body(detalleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /detalles/:id} : get the "id" detalle.
     *
     * @param id the id of the detalleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalles/{id}")
    public ResponseEntity<DetalleDTO> getDetalle(@PathVariable Long id) {
        log.debug("REST request to get Detalle : {}", id);
        Optional<DetalleDTO> detalleDTO = detalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleDTO);
    }

    /**
     * {@code DELETE  /detalles/:id} : delete the "id" detalle.
     *
     * @param id the id of the detalleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Long id) {
        log.debug("REST request to delete Detalle : {}", id);
        detalleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
