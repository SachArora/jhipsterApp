package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Hellog;
import com.mycompany.myapp.repository.HellogRepository;
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
 * Test class for the HellogResource REST controller.
 *
 * @see HellogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class HellogResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_ABC = "AAAAAAAAAA";
    private static final String UPDATED_ABC = "BBBBBBBBBB";

    @Autowired
    private HellogRepository hellogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restHellogMockMvc;

    private Hellog hellog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HellogResource hellogResource = new HellogResource(hellogRepository);
        this.restHellogMockMvc = MockMvcBuilders.standaloneSetup(hellogResource)
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
    public static Hellog createEntity() {
        Hellog hellog = new Hellog()
            .abc(DEFAULT_ABC);
        return hellog;
    }

    @Before
    public void initTest() {
        hellogRepository.deleteAll();
        hellog = createEntity();
    }

    @Test
    public void createHellog() throws Exception {
        int databaseSizeBeforeCreate = hellogRepository.findAll().size();

        // Create the Hellog
        restHellogMockMvc.perform(post("/api/hellogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog)))
            .andExpect(status().isCreated());

        // Validate the Hellog in the database
        List<Hellog> hellogList = hellogRepository.findAll();
        assertThat(hellogList).hasSize(databaseSizeBeforeCreate + 1);
        Hellog testHellog = hellogList.get(hellogList.size() - 1);
        assertThat(testHellog.getAbc()).isEqualTo(DEFAULT_ABC);
    }

    @Test
    public void createHellogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hellogRepository.findAll().size();

        // Create the Hellog with an existing ID
        hellog.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHellogMockMvc.perform(post("/api/hellogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Hellog> hellogList = hellogRepository.findAll();
        assertThat(hellogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllHellogs() throws Exception {
        // Initialize the database
        hellogRepository.save(hellog);

        // Get all the hellogList
        restHellogMockMvc.perform(get("/api/hellogs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hellog.getId().toString())))
            .andExpect(jsonPath("$.[*].abc").value(hasItem(DEFAULT_ABC.toString())));
    }

    @Test
    public void getHellog() throws Exception {
        // Initialize the database
        hellogRepository.save(hellog);

        // Get the hellog
        restHellogMockMvc.perform(get("/api/hellogs/{id}", hellog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hellog.getId().toString()))
            .andExpect(jsonPath("$.abc").value(DEFAULT_ABC.toString()));
    }

    @Test
    public void getNonExistingHellog() throws Exception {
        // Get the hellog
        restHellogMockMvc.perform(get("/api/hellogs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateHellog() throws Exception {
        // Initialize the database
        hellogRepository.save(hellog);
        int databaseSizeBeforeUpdate = hellogRepository.findAll().size();

        // Update the hellog
        Hellog updatedHellog = hellogRepository.findOne(hellog.getId());
        updatedHellog
            .abc(UPDATED_ABC);

        restHellogMockMvc.perform(put("/api/hellogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHellog)))
            .andExpect(status().isOk());

        // Validate the Hellog in the database
        List<Hellog> hellogList = hellogRepository.findAll();
        assertThat(hellogList).hasSize(databaseSizeBeforeUpdate);
        Hellog testHellog = hellogList.get(hellogList.size() - 1);
        assertThat(testHellog.getAbc()).isEqualTo(UPDATED_ABC);
    }

    @Test
    public void updateNonExistingHellog() throws Exception {
        int databaseSizeBeforeUpdate = hellogRepository.findAll().size();

        // Create the Hellog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHellogMockMvc.perform(put("/api/hellogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog)))
            .andExpect(status().isCreated());

        // Validate the Hellog in the database
        List<Hellog> hellogList = hellogRepository.findAll();
        assertThat(hellogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteHellog() throws Exception {
        // Initialize the database
        hellogRepository.save(hellog);
        int databaseSizeBeforeDelete = hellogRepository.findAll().size();

        // Get the hellog
        restHellogMockMvc.perform(delete("/api/hellogs/{id}", hellog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hellog> hellogList = hellogRepository.findAll();
        assertThat(hellogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hellog.class);
        Hellog hellog1 = new Hellog();
        hellog1.setId(UUID.randomUUID());
        Hellog hellog2 = new Hellog();
        hellog2.setId(hellog1.getId());
        assertThat(hellog1).isEqualTo(hellog2);
        hellog2.setId(UUID.randomUUID());
        assertThat(hellog1).isNotEqualTo(hellog2);
        hellog1.setId(null);
        assertThat(hellog1).isNotEqualTo(hellog2);
    }
}
