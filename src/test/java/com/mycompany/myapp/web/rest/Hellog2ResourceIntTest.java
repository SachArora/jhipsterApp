package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Hellog2;
import com.mycompany.myapp.repository.Hellog2Repository;
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
 * Test class for the Hellog2Resource REST controller.
 *
 * @see Hellog2Resource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class Hellog2ResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_DEF = "AAAAAAAAAA";
    private static final String UPDATED_DEF = "BBBBBBBBBB";

    @Autowired
    private Hellog2Repository hellog2Repository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restHellog2MockMvc;

    private Hellog2 hellog2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Hellog2Resource hellog2Resource = new Hellog2Resource(hellog2Repository);
        this.restHellog2MockMvc = MockMvcBuilders.standaloneSetup(hellog2Resource)
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
    public static Hellog2 createEntity() {
        Hellog2 hellog2 = new Hellog2()
            .def(DEFAULT_DEF);
        return hellog2;
    }

    @Before
    public void initTest() {
        hellog2Repository.deleteAll();
        hellog2 = createEntity();
    }

    @Test
    public void createHellog2() throws Exception {
        int databaseSizeBeforeCreate = hellog2Repository.findAll().size();

        // Create the Hellog2
        restHellog2MockMvc.perform(post("/api/hellog-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog2)))
            .andExpect(status().isCreated());

        // Validate the Hellog2 in the database
        List<Hellog2> hellog2List = hellog2Repository.findAll();
        assertThat(hellog2List).hasSize(databaseSizeBeforeCreate + 1);
        Hellog2 testHellog2 = hellog2List.get(hellog2List.size() - 1);
        assertThat(testHellog2.getDef()).isEqualTo(DEFAULT_DEF);
    }

    @Test
    public void createHellog2WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hellog2Repository.findAll().size();

        // Create the Hellog2 with an existing ID
        hellog2.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHellog2MockMvc.perform(post("/api/hellog-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog2)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Hellog2> hellog2List = hellog2Repository.findAll();
        assertThat(hellog2List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllHellog2S() throws Exception {
        // Initialize the database
        hellog2Repository.save(hellog2);

        // Get all the hellog2List
        restHellog2MockMvc.perform(get("/api/hellog-2-s"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hellog2.getId().toString())))
            .andExpect(jsonPath("$.[*].def").value(hasItem(DEFAULT_DEF.toString())));
    }

    @Test
    public void getHellog2() throws Exception {
        // Initialize the database
        hellog2Repository.save(hellog2);

        // Get the hellog2
        restHellog2MockMvc.perform(get("/api/hellog-2-s/{id}", hellog2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hellog2.getId().toString()))
            .andExpect(jsonPath("$.def").value(DEFAULT_DEF.toString()));
    }

    @Test
    public void getNonExistingHellog2() throws Exception {
        // Get the hellog2
        restHellog2MockMvc.perform(get("/api/hellog-2-s/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateHellog2() throws Exception {
        // Initialize the database
        hellog2Repository.save(hellog2);
        int databaseSizeBeforeUpdate = hellog2Repository.findAll().size();

        // Update the hellog2
        Hellog2 updatedHellog2 = hellog2Repository.findOne(hellog2.getId());
        updatedHellog2
            .def(UPDATED_DEF);

        restHellog2MockMvc.perform(put("/api/hellog-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHellog2)))
            .andExpect(status().isOk());

        // Validate the Hellog2 in the database
        List<Hellog2> hellog2List = hellog2Repository.findAll();
        assertThat(hellog2List).hasSize(databaseSizeBeforeUpdate);
        Hellog2 testHellog2 = hellog2List.get(hellog2List.size() - 1);
        assertThat(testHellog2.getDef()).isEqualTo(UPDATED_DEF);
    }

    @Test
    public void updateNonExistingHellog2() throws Exception {
        int databaseSizeBeforeUpdate = hellog2Repository.findAll().size();

        // Create the Hellog2

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHellog2MockMvc.perform(put("/api/hellog-2-s")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hellog2)))
            .andExpect(status().isCreated());

        // Validate the Hellog2 in the database
        List<Hellog2> hellog2List = hellog2Repository.findAll();
        assertThat(hellog2List).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteHellog2() throws Exception {
        // Initialize the database
        hellog2Repository.save(hellog2);
        int databaseSizeBeforeDelete = hellog2Repository.findAll().size();

        // Get the hellog2
        restHellog2MockMvc.perform(delete("/api/hellog-2-s/{id}", hellog2.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hellog2> hellog2List = hellog2Repository.findAll();
        assertThat(hellog2List).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hellog2.class);
        Hellog2 hellog21 = new Hellog2();
        hellog21.setId(UUID.randomUUID());
        Hellog2 hellog22 = new Hellog2();
        hellog22.setId(hellog21.getId());
        assertThat(hellog21).isEqualTo(hellog22);
        hellog22.setId(UUID.randomUUID());
        assertThat(hellog21).isNotEqualTo(hellog22);
        hellog21.setId(null);
        assertThat(hellog21).isNotEqualTo(hellog22);
    }
}
