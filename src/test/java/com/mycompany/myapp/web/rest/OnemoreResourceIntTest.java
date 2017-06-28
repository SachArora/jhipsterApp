package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Onemore;
import com.mycompany.myapp.repository.OnemoreRepository;
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
 * Test class for the OnemoreResource REST controller.
 *
 * @see OnemoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class OnemoreResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_DEPT = "AAAAAAAAAA";
    private static final String UPDATED_DEPT = "BBBBBBBBBB";

    private static final String DEFAULT_BLOCK = "AAAAAAAAAA";
    private static final String UPDATED_BLOCK = "BBBBBBBBBB";

    @Autowired
    private OnemoreRepository onemoreRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOnemoreMockMvc;

    private Onemore onemore;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OnemoreResource onemoreResource = new OnemoreResource(onemoreRepository);
        this.restOnemoreMockMvc = MockMvcBuilders.standaloneSetup(onemoreResource)
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
    public static Onemore createEntity() {
        Onemore onemore = new Onemore()
            .dept(DEFAULT_DEPT)
            .block(DEFAULT_BLOCK);
        return onemore;
    }

    @Before
    public void initTest() {
        onemoreRepository.deleteAll();
        onemore = createEntity();
    }

    @Test
    public void createOnemore() throws Exception {
        int databaseSizeBeforeCreate = onemoreRepository.findAll().size();

        // Create the Onemore
        restOnemoreMockMvc.perform(post("/api/onemores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onemore)))
            .andExpect(status().isCreated());

        // Validate the Onemore in the database
        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeCreate + 1);
        Onemore testOnemore = onemoreList.get(onemoreList.size() - 1);
        assertThat(testOnemore.getDept()).isEqualTo(DEFAULT_DEPT);
        assertThat(testOnemore.getBlock()).isEqualTo(DEFAULT_BLOCK);
    }

    @Test
    public void createOnemoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = onemoreRepository.findAll().size();

        // Create the Onemore with an existing ID
        onemore.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOnemoreMockMvc.perform(post("/api/onemores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onemore)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDeptIsRequired() throws Exception {
        int databaseSizeBeforeTest = onemoreRepository.findAll().size();
        // set the field null
        onemore.setDept(null);

        // Create the Onemore, which fails.

        restOnemoreMockMvc.perform(post("/api/onemores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onemore)))
            .andExpect(status().isBadRequest());

        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOnemores() throws Exception {
        // Initialize the database
        onemoreRepository.save(onemore);

        // Get all the onemoreList
        restOnemoreMockMvc.perform(get("/api/onemores"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(onemore.getId().toString())))
            .andExpect(jsonPath("$.[*].dept").value(hasItem(DEFAULT_DEPT.toString())))
            .andExpect(jsonPath("$.[*].block").value(hasItem(DEFAULT_BLOCK.toString())));
    }

    @Test
    public void getOnemore() throws Exception {
        // Initialize the database
        onemoreRepository.save(onemore);

        // Get the onemore
        restOnemoreMockMvc.perform(get("/api/onemores/{id}", onemore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(onemore.getId().toString()))
            .andExpect(jsonPath("$.dept").value(DEFAULT_DEPT.toString()))
            .andExpect(jsonPath("$.block").value(DEFAULT_BLOCK.toString()));
    }

    @Test
    public void getNonExistingOnemore() throws Exception {
        // Get the onemore
        restOnemoreMockMvc.perform(get("/api/onemores/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOnemore() throws Exception {
        // Initialize the database
        onemoreRepository.save(onemore);
        int databaseSizeBeforeUpdate = onemoreRepository.findAll().size();

        // Update the onemore
        Onemore updatedOnemore = onemoreRepository.findOne(onemore.getId());
        updatedOnemore
            .dept(UPDATED_DEPT)
            .block(UPDATED_BLOCK);

        restOnemoreMockMvc.perform(put("/api/onemores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOnemore)))
            .andExpect(status().isOk());

        // Validate the Onemore in the database
        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeUpdate);
        Onemore testOnemore = onemoreList.get(onemoreList.size() - 1);
        assertThat(testOnemore.getDept()).isEqualTo(UPDATED_DEPT);
        assertThat(testOnemore.getBlock()).isEqualTo(UPDATED_BLOCK);
    }

    @Test
    public void updateNonExistingOnemore() throws Exception {
        int databaseSizeBeforeUpdate = onemoreRepository.findAll().size();

        // Create the Onemore

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOnemoreMockMvc.perform(put("/api/onemores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(onemore)))
            .andExpect(status().isCreated());

        // Validate the Onemore in the database
        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteOnemore() throws Exception {
        // Initialize the database
        onemoreRepository.save(onemore);
        int databaseSizeBeforeDelete = onemoreRepository.findAll().size();

        // Get the onemore
        restOnemoreMockMvc.perform(delete("/api/onemores/{id}", onemore.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Onemore> onemoreList = onemoreRepository.findAll();
        assertThat(onemoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Onemore.class);
        Onemore onemore1 = new Onemore();
        onemore1.setId(UUID.randomUUID());
        Onemore onemore2 = new Onemore();
        onemore2.setId(onemore1.getId());
        assertThat(onemore1).isEqualTo(onemore2);
        onemore2.setId(UUID.randomUUID());
        assertThat(onemore1).isNotEqualTo(onemore2);
        onemore1.setId(null);
        assertThat(onemore1).isNotEqualTo(onemore2);
    }
}
