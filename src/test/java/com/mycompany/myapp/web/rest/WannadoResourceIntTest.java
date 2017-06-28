package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Wannado;
import com.mycompany.myapp.repository.WannadoRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WannadoResource REST controller.
 *
 * @see WannadoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class WannadoResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_OPT_1 = "AAAAAAAAAA";
    private static final String UPDATED_OPT_1 = "BBBBBBBBBB";

    @Autowired
    private WannadoRepository wannadoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restWannadoMockMvc;

    private Wannado wannado;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WannadoResource wannadoResource = new WannadoResource(wannadoRepository);
        this.restWannadoMockMvc = MockMvcBuilders.standaloneSetup(wannadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wannado createEntity() {
        Wannado wannado = new Wannado()
            .opt1(DEFAULT_OPT_1);
        return wannado;
    }

    @Before
    public void initTest() {
        wannadoRepository.deleteAll();
        wannado = createEntity();
    }

    @Test
    public void createWannado() throws Exception {
        int databaseSizeBeforeCreate = wannadoRepository.findAll().size();

        // Create the Wannado
        restWannadoMockMvc.perform(post("/api/wannados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wannado)))
            .andExpect(status().isCreated());

        // Validate the Wannado in the database
        List<Wannado> wannadoList = wannadoRepository.findAll();
        assertThat(wannadoList).hasSize(databaseSizeBeforeCreate + 1);
        Wannado testWannado = wannadoList.get(wannadoList.size() - 1);
        assertThat(testWannado.getOpt1()).isEqualTo(DEFAULT_OPT_1);
    }

    @Test
    public void createWannadoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wannadoRepository.findAll().size();

        // Create the Wannado with an existing ID
        wannado.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWannadoMockMvc.perform(post("/api/wannados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wannado)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Wannado> wannadoList = wannadoRepository.findAll();
        assertThat(wannadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllWannados() throws Exception {
        // Initialize the database
        wannadoRepository.save(wannado);

        // Get all the wannadoList
        restWannadoMockMvc.perform(get("/api/wannados"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wannado.getId().toString())))
            .andExpect(jsonPath("$.[*].opt1").value(hasItem(DEFAULT_OPT_1.toString())));
    }

    @Test
    public void getWannado() throws Exception {
        // Initialize the database
        wannadoRepository.save(wannado);

        // Get the wannado
        restWannadoMockMvc.perform(get("/api/wannados/{id}", wannado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wannado.getId().toString()))
            .andExpect(jsonPath("$.opt1").value(DEFAULT_OPT_1.toString()));
    }

    @Test
    public void getNonExistingWannado() throws Exception {
        // Get the wannado
        restWannadoMockMvc.perform(get("/api/wannados/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateWannado() throws Exception {
        // Initialize the database
        wannadoRepository.save(wannado);
        int databaseSizeBeforeUpdate = wannadoRepository.findAll().size();

        // Update the wannado
        Wannado updatedWannado = wannadoRepository.findOne(wannado.getId());
        updatedWannado
            .opt1(UPDATED_OPT_1);

        restWannadoMockMvc.perform(put("/api/wannados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWannado)))
            .andExpect(status().isOk());

        // Validate the Wannado in the database
        List<Wannado> wannadoList = wannadoRepository.findAll();
        assertThat(wannadoList).hasSize(databaseSizeBeforeUpdate);
        Wannado testWannado = wannadoList.get(wannadoList.size() - 1);
        assertThat(testWannado.getOpt1()).isEqualTo(UPDATED_OPT_1);
    }

    @Test
    public void updateNonExistingWannado() throws Exception {
        int databaseSizeBeforeUpdate = wannadoRepository.findAll().size();

        // Create the Wannado

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWannadoMockMvc.perform(put("/api/wannados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wannado)))
            .andExpect(status().isCreated());

        // Validate the Wannado in the database
        List<Wannado> wannadoList = wannadoRepository.findAll();
        assertThat(wannadoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteWannado() throws Exception {
        // Initialize the database
        wannadoRepository.save(wannado);
        int databaseSizeBeforeDelete = wannadoRepository.findAll().size();

        // Get the wannado
        restWannadoMockMvc.perform(delete("/api/wannados/{id}", wannado.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wannado> wannadoList = wannadoRepository.findAll();
        assertThat(wannadoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wannado.class);
        Wannado wannado1 = new Wannado();
        wannado1.setId(UUID.randomUUID());
        Wannado wannado2 = new Wannado();
        wannado2.setId(wannado1.getId());
        assertThat(wannado1).isEqualTo(wannado2);
        wannado2.setId(UUID.randomUUID());
        assertThat(wannado1).isNotEqualTo(wannado2);
        wannado1.setId(null);
        assertThat(wannado1).isNotEqualTo(wannado2);
    }
}
