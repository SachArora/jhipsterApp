package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Myent;
import com.mycompany.myapp.repository.MyentRepository;
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
 * Test class for the MyentResource REST controller.
 *
 * @see MyentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class MyentResourceIntTest extends AbstractCassandraTest {

    @Autowired
    private MyentRepository myentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restMyentMockMvc;

    private Myent myent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MyentResource myentResource = new MyentResource(myentRepository);
        this.restMyentMockMvc = MockMvcBuilders.standaloneSetup(myentResource)
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
    public static Myent createEntity() {
        Myent myent = new Myent();
        return myent;
    }

    @Before
    public void initTest() {
        myentRepository.deleteAll();
        myent = createEntity();
    }

    @Test
    public void createMyent() throws Exception {
        int databaseSizeBeforeCreate = myentRepository.findAll().size();

        // Create the Myent
        restMyentMockMvc.perform(post("/api/myents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myent)))
            .andExpect(status().isCreated());

        // Validate the Myent in the database
        List<Myent> myentList = myentRepository.findAll();
        assertThat(myentList).hasSize(databaseSizeBeforeCreate + 1);
        Myent testMyent = myentList.get(myentList.size() - 1);
    }

    @Test
    public void createMyentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myentRepository.findAll().size();

        // Create the Myent with an existing ID
        myent.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyentMockMvc.perform(post("/api/myents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Myent> myentList = myentRepository.findAll();
        assertThat(myentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllMyents() throws Exception {
        // Initialize the database
        myentRepository.save(myent);

        // Get all the myentList
        restMyentMockMvc.perform(get("/api/myents"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myent.getId().toString())));
    }

    @Test
    public void getMyent() throws Exception {
        // Initialize the database
        myentRepository.save(myent);

        // Get the myent
        restMyentMockMvc.perform(get("/api/myents/{id}", myent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myent.getId().toString()));
    }

    @Test
    public void getNonExistingMyent() throws Exception {
        // Get the myent
        restMyentMockMvc.perform(get("/api/myents/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMyent() throws Exception {
        // Initialize the database
        myentRepository.save(myent);
        int databaseSizeBeforeUpdate = myentRepository.findAll().size();

        // Update the myent
        Myent updatedMyent = myentRepository.findOne(myent.getId());

        restMyentMockMvc.perform(put("/api/myents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMyent)))
            .andExpect(status().isOk());

        // Validate the Myent in the database
        List<Myent> myentList = myentRepository.findAll();
        assertThat(myentList).hasSize(databaseSizeBeforeUpdate);
        Myent testMyent = myentList.get(myentList.size() - 1);
    }

    @Test
    public void updateNonExistingMyent() throws Exception {
        int databaseSizeBeforeUpdate = myentRepository.findAll().size();

        // Create the Myent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMyentMockMvc.perform(put("/api/myents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myent)))
            .andExpect(status().isCreated());

        // Validate the Myent in the database
        List<Myent> myentList = myentRepository.findAll();
        assertThat(myentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteMyent() throws Exception {
        // Initialize the database
        myentRepository.save(myent);
        int databaseSizeBeforeDelete = myentRepository.findAll().size();

        // Get the myent
        restMyentMockMvc.perform(delete("/api/myents/{id}", myent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Myent> myentList = myentRepository.findAll();
        assertThat(myentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Myent.class);
        Myent myent1 = new Myent();
        myent1.setId(UUID.randomUUID());
        Myent myent2 = new Myent();
        myent2.setId(myent1.getId());
        assertThat(myent1).isEqualTo(myent2);
        myent2.setId(UUID.randomUUID());
        assertThat(myent1).isNotEqualTo(myent2);
        myent1.setId(null);
        assertThat(myent1).isNotEqualTo(myent2);
    }
}
