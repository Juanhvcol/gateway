package com.themakers.storeonline.web.rest;

import com.themakers.storeonline.GatewaystoreApp;
import com.themakers.storeonline.domain.ProductoDetalle;
import com.themakers.storeonline.repository.ProductoDetalleRepository;
import com.themakers.storeonline.service.ProductoDetalleService;
import com.themakers.storeonline.service.dto.ProductoDetalleDTO;
import com.themakers.storeonline.service.mapper.ProductoDetalleMapper;
import com.themakers.storeonline.web.rest.errors.ExceptionTranslator;
import com.themakers.storeonline.service.dto.ProductoDetalleCriteria;
import com.themakers.storeonline.service.ProductoDetalleQueryService;

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
 * Integration tests for the {@link ProductoDetalleResource} REST controller.
 */
@SpringBootTest(classes = GatewaystoreApp.class)
public class ProductoDetalleResourceIT {

    private static final String DEFAULT_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTO = "BBBBBBBBBB";

    @Autowired
    private ProductoDetalleRepository productoDetalleRepository;

    @Autowired
    private ProductoDetalleMapper productoDetalleMapper;

    @Autowired
    private ProductoDetalleService productoDetalleService;

    @Autowired
    private ProductoDetalleQueryService productoDetalleQueryService;

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

    private MockMvc restProductoDetalleMockMvc;

    private ProductoDetalle productoDetalle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductoDetalleResource productoDetalleResource = new ProductoDetalleResource(productoDetalleService, productoDetalleQueryService);
        this.restProductoDetalleMockMvc = MockMvcBuilders.standaloneSetup(productoDetalleResource)
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
    public static ProductoDetalle createEntity(EntityManager em) {
        ProductoDetalle productoDetalle = new ProductoDetalle()
            .producto(DEFAULT_PRODUCTO);
        return productoDetalle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoDetalle createUpdatedEntity(EntityManager em) {
        ProductoDetalle productoDetalle = new ProductoDetalle()
            .producto(UPDATED_PRODUCTO);
        return productoDetalle;
    }

    @BeforeEach
    public void initTest() {
        productoDetalle = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductoDetalle() throws Exception {
        int databaseSizeBeforeCreate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);
        restProductoDetalleMockMvc.perform(post("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoDetalle testProductoDetalle = productoDetalleList.get(productoDetalleList.size() - 1);
        assertThat(testProductoDetalle.getProducto()).isEqualTo(DEFAULT_PRODUCTO);
    }

    @Test
    @Transactional
    public void createProductoDetalleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle with an existing ID
        productoDetalle.setId(1L);
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoDetalleMockMvc.perform(post("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductoDetalles() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].producto").value(hasItem(DEFAULT_PRODUCTO)));
    }
    
    @Test
    @Transactional
    public void getProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get the productoDetalle
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/{id}", productoDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productoDetalle.getId().intValue()))
            .andExpect(jsonPath("$.producto").value(DEFAULT_PRODUCTO));
    }


    @Test
    @Transactional
    public void getProductoDetallesByIdFiltering() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        Long id = productoDetalle.getId();

        defaultProductoDetalleShouldBeFound("id.equals=" + id);
        defaultProductoDetalleShouldNotBeFound("id.notEquals=" + id);

        defaultProductoDetalleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoDetalleShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoDetalleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoDetalleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductoDetallesByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto equals to DEFAULT_PRODUCTO
        defaultProductoDetalleShouldBeFound("producto.equals=" + DEFAULT_PRODUCTO);

        // Get all the productoDetalleList where producto equals to UPDATED_PRODUCTO
        defaultProductoDetalleShouldNotBeFound("producto.equals=" + UPDATED_PRODUCTO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByProductoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto not equals to DEFAULT_PRODUCTO
        defaultProductoDetalleShouldNotBeFound("producto.notEquals=" + DEFAULT_PRODUCTO);

        // Get all the productoDetalleList where producto not equals to UPDATED_PRODUCTO
        defaultProductoDetalleShouldBeFound("producto.notEquals=" + UPDATED_PRODUCTO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByProductoIsInShouldWork() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto in DEFAULT_PRODUCTO or UPDATED_PRODUCTO
        defaultProductoDetalleShouldBeFound("producto.in=" + DEFAULT_PRODUCTO + "," + UPDATED_PRODUCTO);

        // Get all the productoDetalleList where producto equals to UPDATED_PRODUCTO
        defaultProductoDetalleShouldNotBeFound("producto.in=" + UPDATED_PRODUCTO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByProductoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto is not null
        defaultProductoDetalleShouldBeFound("producto.specified=true");

        // Get all the productoDetalleList where producto is null
        defaultProductoDetalleShouldNotBeFound("producto.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductoDetallesByProductoContainsSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto contains DEFAULT_PRODUCTO
        defaultProductoDetalleShouldBeFound("producto.contains=" + DEFAULT_PRODUCTO);

        // Get all the productoDetalleList where producto contains UPDATED_PRODUCTO
        defaultProductoDetalleShouldNotBeFound("producto.contains=" + UPDATED_PRODUCTO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByProductoNotContainsSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where producto does not contain DEFAULT_PRODUCTO
        defaultProductoDetalleShouldNotBeFound("producto.doesNotContain=" + DEFAULT_PRODUCTO);

        // Get all the productoDetalleList where producto does not contain UPDATED_PRODUCTO
        defaultProductoDetalleShouldBeFound("producto.doesNotContain=" + UPDATED_PRODUCTO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoDetalleShouldBeFound(String filter) throws Exception {
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].producto").value(hasItem(DEFAULT_PRODUCTO)));

        // Check, that the count call also returns 1
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoDetalleShouldNotBeFound(String filter) throws Exception {
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductoDetalle() throws Exception {
        // Get the productoDetalle
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        int databaseSizeBeforeUpdate = productoDetalleRepository.findAll().size();

        // Update the productoDetalle
        ProductoDetalle updatedProductoDetalle = productoDetalleRepository.findById(productoDetalle.getId()).get();
        // Disconnect from session so that the updates on updatedProductoDetalle are not directly saved in db
        em.detach(updatedProductoDetalle);
        updatedProductoDetalle
            .producto(UPDATED_PRODUCTO);
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(updatedProductoDetalle);

        restProductoDetalleMockMvc.perform(put("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isOk());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeUpdate);
        ProductoDetalle testProductoDetalle = productoDetalleList.get(productoDetalleList.size() - 1);
        assertThat(testProductoDetalle.getProducto()).isEqualTo(UPDATED_PRODUCTO);
    }

    @Test
    @Transactional
    public void updateNonExistingProductoDetalle() throws Exception {
        int databaseSizeBeforeUpdate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoDetalleMockMvc.perform(put("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        int databaseSizeBeforeDelete = productoDetalleRepository.findAll().size();

        // Delete the productoDetalle
        restProductoDetalleMockMvc.perform(delete("/api/producto-detalles/{id}", productoDetalle.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
