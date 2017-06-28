package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Again;
import com.mycompany.myapp.repository.AgainRepository;
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
 * Test class for the AgainResource REST controller.
 *
 * @see AgainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class AgainResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_ONEATT = "AAAAAAAAAA";
    private static final String UPDATED_ONEATT = "BBBBBBBBBB";

    @Autowired
    private AgainRepository againRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAgainMockMvc;

    private Again again;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgainResource againResource = new AgainResource(againRepository);
        this.restAgainMockMvc = MockMvcBuilders.standaloneSetup(againResource)
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
    public static Again createEntity() {
        Again again = new Again()
            .oneatt(DEFAULT_ONEATT);
        return again;
    }

    @Before
    public void initTest() {
        againRepository.deleteAll();
        again = createEntity();
    }

    @Test
    public void createAgain() throws Exception {
        int databaseSizeBeforeCreate = againRepository.findAll().size();

        // Create the Again
        restAgainMockMvc.perform(post("/api/agains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(again)))
            .andExpect(status().isCreated());

        // Validate the Again in the database
        List<Again> againList = againRepository.findAll();
        assertThat(againList).hasSize(databaseSizeBeforeCreate + 1);
        Again testAgain = againList.get(againList.size() - 1);
        assertThat(testAgain.getOneatt()).isEqualTo(DEFAULT_ONEATT);
    }

    @Test
    public void createAgainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = againRepository.findAll().size();

        // Create the Again with an existing ID
        again.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgainMockMvc.perform(post("/api/agains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(again)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Again> againList = againRepository.findAll();
        assertThat(againList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllAgains() throws Exception {
        // Initialize the database
        againRepository.save(again);

        // Get all the againList
        restAgainMockMvc.perform(get("/api/agains"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(again.getId().toString())))
            .andExpect(jsonPath("$.[*].oneatt").value(hasItem(DEFAULT_ONEATT.toString())));
    }

    @Test
    public void getAgain() throws Exception {
        // Initialize the database
        againRepository.save(again);

        // Get the again
        restAgainMockMvc.perform(get("/api/agains/{id}", again.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(again.getId().toString()))
            .andExpect(jsonPath("$.oneatt").value(DEFAULT_ONEATT.toString()));
    }

    @Test
    public void getNonExistingAgain() throws Exception {
        // Get the again
        restAgainMockMvc.perform(get("/api/agains/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAgain() throws Exception {
        // Initialize the database
        againRepository.save(again);
        int databaseSizeBeforeUpdate = againRepository.findAll().size();

        // Update the again
        Again updatedAgain = againRepository.findOne(again.getId());
        updatedAgain
            .oneatt(UPDATED_ONEATT);

        restAgainMockMvc.perform(put("/api/agains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgain)))
            .andExpect(status().isOk());

        // Validate the Again in the database
        List<Again> againList = againRepository.findAll();
        assertThat(againList).hasSize(databaseSizeBeforeUpdate);
        Again testAgain = againList.get(againList.size() - 1);
        assertThat(testAgain.getOneatt()).isEqualTo(UPDATED_ONEATT);
    }

    @Test
    public void updateNonExistingAgain() throws Exception {
        int databaseSizeBeforeUpdate = againRepository.findAll().size();

        // Create the Again

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgainMockMvc.perform(put("/api/agains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(again)))
            .andExpect(status().isCreated());

        // Validate the Again in the database
        List<Again> againList = againRepository.findAll();
        assertThat(againList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAgain() throws Exception {
        // Initialize the database
        againRepository.save(again);
        int databaseSizeBeforeDelete = againRepository.findAll().size();

        // Get the again
        restAgainMockMvc.perform(delete("/api/agains/{id}", again.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Again> againList = againRepository.findAll();
        assertThat(againList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Again.class);
        Again again1 = new Again();
        again1.setId(UUID.randomUUID());
        Again again2 = new Again();
        again2.setId(again1.getId());
        assertThat(again1).isEqualTo(again2);
        again2.setId(UUID.randomUUID());
        assertThat(again1).isNotEqualTo(again2);
        again1.setId(null);
        assertThat(again1).isNotEqualTo(again2);
    }
}
