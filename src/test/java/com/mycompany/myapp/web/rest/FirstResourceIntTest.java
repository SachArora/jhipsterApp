package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.First;
import com.mycompany.myapp.repository.FirstRepository;
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
 * Test class for the FirstResource REST controller.
 *
 * @see FirstResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class FirstResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH = "BBBBBBBBBB";

    @Autowired
    private FirstRepository firstRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFirstMockMvc;

    private First first;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FirstResource firstResource = new FirstResource(firstRepository);
        this.restFirstMockMvc = MockMvcBuilders.standaloneSetup(firstResource)
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
    public static First createEntity() {
        First first = new First()
            .name(DEFAULT_NAME)
            .branch(DEFAULT_BRANCH);
        return first;
    }

    @Before
    public void initTest() {
        firstRepository.deleteAll();
        first = createEntity();
    }

    @Test
    public void createFirst() throws Exception {
        int databaseSizeBeforeCreate = firstRepository.findAll().size();

        // Create the First
        restFirstMockMvc.perform(post("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isCreated());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeCreate + 1);
        First testFirst = firstList.get(firstList.size() - 1);
        assertThat(testFirst.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFirst.getBranch()).isEqualTo(DEFAULT_BRANCH);
    }

    @Test
    public void createFirstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = firstRepository.findAll().size();

        // Create the First with an existing ID
        first.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirstMockMvc.perform(post("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllFirsts() throws Exception {
        // Initialize the database
        firstRepository.save(first);

        // Get all the firstList
        restFirstMockMvc.perform(get("/api/firsts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(first.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].branch").value(hasItem(DEFAULT_BRANCH.toString())));
    }

    @Test
    public void getFirst() throws Exception {
        // Initialize the database
        firstRepository.save(first);

        // Get the first
        restFirstMockMvc.perform(get("/api/firsts/{id}", first.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(first.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.branch").value(DEFAULT_BRANCH.toString()));
    }

    @Test
    public void getNonExistingFirst() throws Exception {
        // Get the first
        restFirstMockMvc.perform(get("/api/firsts/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFirst() throws Exception {
        // Initialize the database
        firstRepository.save(first);
        int databaseSizeBeforeUpdate = firstRepository.findAll().size();

        // Update the first
        First updatedFirst = firstRepository.findOne(first.getId());
        updatedFirst
            .name(UPDATED_NAME)
            .branch(UPDATED_BRANCH);

        restFirstMockMvc.perform(put("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFirst)))
            .andExpect(status().isOk());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeUpdate);
        First testFirst = firstList.get(firstList.size() - 1);
        assertThat(testFirst.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFirst.getBranch()).isEqualTo(UPDATED_BRANCH);
    }

    @Test
    public void updateNonExistingFirst() throws Exception {
        int databaseSizeBeforeUpdate = firstRepository.findAll().size();

        // Create the First

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFirstMockMvc.perform(put("/api/firsts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isCreated());

        // Validate the First in the database
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteFirst() throws Exception {
        // Initialize the database
        firstRepository.save(first);
        int databaseSizeBeforeDelete = firstRepository.findAll().size();

        // Get the first
        restFirstMockMvc.perform(delete("/api/firsts/{id}", first.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<First> firstList = firstRepository.findAll();
        assertThat(firstList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(First.class);
        First first1 = new First();
        first1.setId(UUID.randomUUID());
        First first2 = new First();
        first2.setId(first1.getId());
        assertThat(first1).isEqualTo(first2);
        first2.setId(UUID.randomUUID());
        assertThat(first1).isNotEqualTo(first2);
        first1.setId(null);
        assertThat(first1).isNotEqualTo(first2);
    }
}
