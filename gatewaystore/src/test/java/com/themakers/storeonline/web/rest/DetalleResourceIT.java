package com.themakers.storeonline.web.rest;

import com.themakers.storeonline.GatewaystoreApp;
import com.themakers.storeonline.domain.Detalle;
import com.themakers.storeonline.repository.DetalleRepository;
import com.themakers.storeonline.service.DetalleService;
import com.themakers.storeonline.service.dto.DetalleDTO;
import com.themakers.storeonline.service.mapper.DetalleMapper;
import com.themakers.storeonline.web.rest.errors.ExceptionTranslator;
import com.themakers.storeonline.service.dto.DetalleCriteria;
import com.themakers.storeonline.service.DetalleQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.themakers.storeonline.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DetalleResource} REST controller.
 */
@SpringBootTest(classes = GatewaystoreApp.class)
public class DetalleResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private DetalleRepository detalleRepository;

    @Autowired
    private DetalleMapper detalleMapper;

    @Autowired
    private DetalleService detalleService;

    @Autowired
    private DetalleQueryService detalleQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDetalleMockMvc;

    private Detalle detalle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DetalleResource detalleResource = new DetalleResource(detalleService, detalleQueryService);
        this.restDetalleMockMvc = MockMvcBuilders.standaloneSetup(detalleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detalle createEntity(EntityManager em) {
        Detalle detalle = new Detalle()
            .nombre(DEFAULT_NOMBRE);
        return detalle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Detalle createUpdatedEntity(EntityManager em) {
        Detalle detalle = new Detalle()
            .nombre(UPDATED_NOMBRE);
        return detalle;
    }

    @BeforeEach
    public void initTest() {
        detalle = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetalle() throws Exception {
        int databaseSizeBeforeCreate = detalleRepository.findAll().size();

        // Create the Detalle
        DetalleDTO detalleDTO = detalleMapper.toDto(detalle);
        restDetalleMockMvc.perform(post("/api/detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detalleDTO)))
            .andExpect(status().isCreated());

        // Validate the Detalle in the database
        List<Detalle> detalleList = detalleRepository.findAll();
        assertThat(detalleList).hasSize(databaseSizeBeforeCreate + 1);
        Detalle testDetalle = detalleList.get(detalleList.size() - 1);
        assertThat(testDetalle.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createDetalleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detalleRepository.findAll().size();

        // Create the Detalle with an existing ID
        detalle.setId(1L);
        DetalleDTO detalleDTO = detalleMapper.toDto(detalle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleMockMvc.perform(post("/api/detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Detalle in the database
        List<Detalle> detalleList = detalleRepository.findAll();
        assertThat(detalleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDetalles() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList
        restDetalleMockMvc.perform(get("/api/detalles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getDetalle() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get the detalle
        restDetalleMockMvc.perform(get("/api/detalles/{id}", detalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalle.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }


    @Test
    @Transactional
    public void getDetallesByIdFiltering() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        Long id = detalle.getId();

        defaultDetalleShouldBeFound("id.equals=" + id);
        defaultDetalleShouldNotBeFound("id.notEquals=" + id);

        defaultDetalleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDetalleShouldNotBeFound("id.greaterThan=" + id);

        defaultDetalleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDetalleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDetallesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre equals to DEFAULT_NOMBRE
        defaultDetalleShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the detalleList where nombre equals to UPDATED_NOMBRE
        defaultDetalleShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllDetallesByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre not equals to DEFAULT_NOMBRE
        defaultDetalleShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the detalleList where nombre not equals to UPDATED_NOMBRE
        defaultDetalleShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllDetallesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultDetalleShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the detalleList where nombre equals to UPDATED_NOMBRE
        defaultDetalleShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllDetallesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre is not null
        defaultDetalleShouldBeFound("nombre.specified=true");

        // Get all the detalleList where nombre is null
        defaultDetalleShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllDetallesByNombreContainsSomething() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre contains DEFAULT_NOMBRE
        defaultDetalleShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the detalleList where nombre contains UPDATED_NOMBRE
        defaultDetalleShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllDetallesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        // Get all the detalleList where nombre does not contain DEFAULT_NOMBRE
        defaultDetalleShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the detalleList where nombre does not contain UPDATED_NOMBRE
        defaultDetalleShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetalleShouldBeFound(String filter) throws Exception {
        restDetalleMockMvc.perform(get("/api/detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));

        // Check, that the count call also returns 1
        restDetalleMockMvc.perform(get("/api/detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetalleShouldNotBeFound(String filter) throws Exception {
        restDetalleMockMvc.perform(get("/api/detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetalleMockMvc.perform(get("/api/detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDetalle() throws Exception {
        // Get the detalle
        restDetalleMockMvc.perform(get("/api/detalles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetalle() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        int databaseSizeBeforeUpdate = detalleRepository.findAll().size();

        // Update the detalle
        Detalle updatedDetalle = detalleRepository.findById(detalle.getId()).get();
        // Disconnect from session so that the updates on updatedDetalle are not directly saved in db
        em.detach(updatedDetalle);
        updatedDetalle
            .nombre(UPDATED_NOMBRE);
        DetalleDTO detalleDTO = detalleMapper.toDto(updatedDetalle);

        restDetalleMockMvc.perform(put("/api/detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detalleDTO)))
            .andExpect(status().isOk());

        // Validate the Detalle in the database
        List<Detalle> detalleList = detalleRepository.findAll();
        assertThat(detalleList).hasSize(databaseSizeBeforeUpdate);
        Detalle testDetalle = detalleList.get(detalleList.size() - 1);
        assertThat(testDetalle.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingDetalle() throws Exception {
        int databaseSizeBeforeUpdate = detalleRepository.findAll().size();

        // Create the Detalle
        DetalleDTO detalleDTO = detalleMapper.toDto(detalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleMockMvc.perform(put("/api/detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(detalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Detalle in the database
        List<Detalle> detalleList = detalleRepository.findAll();
        assertThat(detalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDetalle() throws Exception {
        // Initialize the database
        detalleRepository.saveAndFlush(detalle);

        int databaseSizeBeforeDelete = detalleRepository.findAll().size();

        // Delete the detalle
        restDetalleMockMvc.perform(delete("/api/detalles/{id}", detalle.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Detalle> detalleList = detalleRepository.findAll();
        assertThat(detalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
