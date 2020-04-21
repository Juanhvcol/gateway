package com.themkers.notificacion.web.rest;

import com.themkers.notificacion.NotificacionesmicroservicioApp;
import com.themkers.notificacion.domain.Notificacion;
import com.themkers.notificacion.repository.NotificacionRepository;
import com.themkers.notificacion.service.NotificacionService;
import com.themkers.notificacion.service.dto.NotificacionDTO;
import com.themkers.notificacion.service.mapper.NotificacionMapper;
import com.themkers.notificacion.web.rest.errors.ExceptionTranslator;

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
import org.springframework.validation.Validator;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.themkers.notificacion.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NotificacionResource} REST controller.
 */
@SpringBootTest(classes = NotificacionesmicroservicioApp.class)
public class NotificacionResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MENSAJE = "AAAAAAAAAA";
    private static final String UPDATED_MENSAJE = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_SEND = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SEND = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private NotificacionMapper notificacionMapper;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restNotificacionMockMvc;

    private Notificacion notificacion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificacionResource notificacionResource = new NotificacionResource(notificacionService);
        this.restNotificacionMockMvc = MockMvcBuilders.standaloneSetup(notificacionResource)
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
    public static Notificacion createEntity() {
        Notificacion notificacion = new Notificacion()
            .fecha(DEFAULT_FECHA)
            .mensaje(DEFAULT_MENSAJE)
            .fechaSend(DEFAULT_FECHA_SEND)
            .userId(DEFAULT_USER_ID);
        return notificacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createUpdatedEntity() {
        Notificacion notificacion = new Notificacion()
            .fecha(UPDATED_FECHA)
            .mensaje(UPDATED_MENSAJE)
            .fechaSend(UPDATED_FECHA_SEND)
            .userId(UPDATED_USER_ID);
        return notificacion;
    }

    @BeforeEach
    public void initTest() {
        notificacionRepository.deleteAll();
        notificacion = createEntity();
    }

    @Test
    public void createNotificacion() throws Exception {
        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);
        restNotificacionMockMvc.perform(post("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isCreated());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate + 1);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testNotificacion.getMensaje()).isEqualTo(DEFAULT_MENSAJE);
        assertThat(testNotificacion.getFechaSend()).isEqualTo(DEFAULT_FECHA_SEND);
        assertThat(testNotificacion.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    public void createNotificacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();

        // Create the Notificacion with an existing ID
        notificacion.setId("existing_id");
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacionMockMvc.perform(post("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacionRepository.findAll().size();
        // set the field null
        notificacion.setFecha(null);

        // Create the Notificacion, which fails.
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        restNotificacionMockMvc.perform(post("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isBadRequest());

        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFechaSendIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificacionRepository.findAll().size();
        // set the field null
        notificacion.setFechaSend(null);

        // Create the Notificacion, which fails.
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        restNotificacionMockMvc.perform(post("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isBadRequest());

        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllNotificacions() throws Exception {
        // Initialize the database
        notificacionRepository.save(notificacion);

        // Get all the notificacionList
        restNotificacionMockMvc.perform(get("/api/notificacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].mensaje").value(hasItem(DEFAULT_MENSAJE)))
            .andExpect(jsonPath("$.[*].fechaSend").value(hasItem(DEFAULT_FECHA_SEND.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }
    
    @Test
    public void getNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.save(notificacion);

        // Get the notificacion
        restNotificacionMockMvc.perform(get("/api/notificacions/{id}", notificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacion.getId()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.mensaje").value(DEFAULT_MENSAJE))
            .andExpect(jsonPath("$.fechaSend").value(DEFAULT_FECHA_SEND.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    public void getNonExistingNotificacion() throws Exception {
        // Get the notificacion
        restNotificacionMockMvc.perform(get("/api/notificacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.save(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion
        Notificacion updatedNotificacion = notificacionRepository.findById(notificacion.getId()).get();
        updatedNotificacion
            .fecha(UPDATED_FECHA)
            .mensaje(UPDATED_MENSAJE)
            .fechaSend(UPDATED_FECHA_SEND)
            .userId(UPDATED_USER_ID);
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(updatedNotificacion);

        restNotificacionMockMvc.perform(put("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testNotificacion.getMensaje()).isEqualTo(UPDATED_MENSAJE);
        assertThat(testNotificacion.getFechaSend()).isEqualTo(UPDATED_FECHA_SEND);
        assertThat(testNotificacion.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    public void updateNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc.perform(put("/api/notificacions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.save(notificacion);

        int databaseSizeBeforeDelete = notificacionRepository.findAll().size();

        // Delete the notificacion
        restNotificacionMockMvc.perform(delete("/api/notificacions/{id}", notificacion.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
