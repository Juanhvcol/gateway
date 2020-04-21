package com.themkers.notificacion.web.rest;

import com.themkers.notificacion.service.NotificacionService;
import com.themkers.notificacion.web.rest.errors.BadRequestAlertException;
import com.themkers.notificacion.service.dto.NotificacionDTO;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.themkers.notificacion.domain.Notificacion}.
 */
@RestController
@RequestMapping("/api")
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private static final String ENTITY_NAME = "notificacionesmicroservicioNotificacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacionService notificacionService;

    public NotificacionResource(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * {@code POST  /notificacions} : Create a new notificacion.
     *
     * @param notificacionDTO the notificacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacionDTO, or with status {@code 400 (Bad Request)} if the notificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notificacions")
    public ResponseEntity<NotificacionDTO> createNotificacion(@Valid @RequestBody NotificacionDTO notificacionDTO) throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacionDTO);
        if (notificacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificacionDTO result = notificacionService.save(notificacionDTO);
        return ResponseEntity.created(new URI("/api/notificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacions} : Updates an existing notificacion.
     *
     * @param notificacionDTO the notificacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacionDTO,
     * or with status {@code 400 (Bad Request)} if the notificacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notificacions")
    public ResponseEntity<NotificacionDTO> updateNotificacion(@Valid @RequestBody NotificacionDTO notificacionDTO) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}", notificacionDTO);
        if (notificacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificacionDTO result = notificacionService.save(notificacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notificacions} : get all the notificacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacions in body.
     */
    @GetMapping("/notificacions")
    public ResponseEntity<List<NotificacionDTO>> getAllNotificacions(Pageable pageable) {
        log.debug("REST request to get a page of Notificacions");
        Page<NotificacionDTO> page = notificacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notificacions/:id} : get the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/{id}")
    public ResponseEntity<NotificacionDTO> getNotificacion(@PathVariable String id) {
        log.debug("REST request to get Notificacion : {}", id);
        Optional<NotificacionDTO> notificacionDTO = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificacionDTO);
    }

    /**
     * {@code DELETE  /notificacions/:id} : delete the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notificacions/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable String id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
