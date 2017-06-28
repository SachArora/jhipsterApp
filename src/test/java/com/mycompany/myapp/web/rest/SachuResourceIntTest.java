package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.SevakApp;

import com.mycompany.myapp.domain.Sachu;
import com.mycompany.myapp.repository.SachuRepository;
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
 * Test class for the SachuResource REST controller.
 *
 * @see SachuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SevakApp.class)
public class SachuResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_FRIEND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FRIEND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FRIEND_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FRIEND_FROM = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROLL_NO = 1;
    private static final Integer UPDATED_ROLL_NO = 2;

    @Autowired
    private SachuRepository sachuRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restSachuMockMvc;

    private Sachu sachu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SachuResource sachuResource = new SachuResource(sachuRepository);
        this.restSachuMockMvc = MockMvcBuilders.standaloneSetup(sachuResource)
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
    public static Sachu createEntity() {
        Sachu sachu = new Sachu()
            .friend_name(DEFAULT_FRIEND_NAME)
            .friend_from(DEFAULT_FRIEND_FROM)
            .roll_no(DEFAULT_ROLL_NO);
        return sachu;
    }

    @Before
    public void initTest() {
        sachuRepository.deleteAll();
        sachu = createEntity();
    }

    @Test
    public void createSachu() throws Exception {
        int databaseSizeBeforeCreate = sachuRepository.findAll().size();

        // Create the Sachu
        restSachuMockMvc.perform(post("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sachu)))
            .andExpect(status().isCreated());

        // Validate the Sachu in the database
        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeCreate + 1);
        Sachu testSachu = sachuList.get(sachuList.size() - 1);
        assertThat(testSachu.getFriend_name()).isEqualTo(DEFAULT_FRIEND_NAME);
        assertThat(testSachu.getFriend_from()).isEqualTo(DEFAULT_FRIEND_FROM);
        assertThat(testSachu.getRoll_no()).isEqualTo(DEFAULT_ROLL_NO);
    }

    @Test
    public void createSachuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sachuRepository.findAll().size();

        // Create the Sachu with an existing ID
        sachu.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSachuMockMvc.perform(post("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sachu)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkFriend_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sachuRepository.findAll().size();
        // set the field null
        sachu.setFriend_name(null);

        // Create the Sachu, which fails.

        restSachuMockMvc.perform(post("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sachu)))
            .andExpect(status().isBadRequest());

        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFriend_fromIsRequired() throws Exception {
        int databaseSizeBeforeTest = sachuRepository.findAll().size();
        // set the field null
        sachu.setFriend_from(null);

        // Create the Sachu, which fails.

        restSachuMockMvc.perform(post("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sachu)))
            .andExpect(status().isBadRequest());

        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSachus() throws Exception {
        // Initialize the database
        sachuRepository.save(sachu);

        // Get all the sachuList
        restSachuMockMvc.perform(get("/api/sachus"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sachu.getId().toString())))
            .andExpect(jsonPath("$.[*].friend_name").value(hasItem(DEFAULT_FRIEND_NAME.toString())))
            .andExpect(jsonPath("$.[*].friend_from").value(hasItem(DEFAULT_FRIEND_FROM.toString())))
            .andExpect(jsonPath("$.[*].roll_no").value(hasItem(DEFAULT_ROLL_NO)));
    }

    @Test
    public void getSachu() throws Exception {
        // Initialize the database
        sachuRepository.save(sachu);

        // Get the sachu
        restSachuMockMvc.perform(get("/api/sachus/{id}", sachu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sachu.getId().toString()))
            .andExpect(jsonPath("$.friend_name").value(DEFAULT_FRIEND_NAME.toString()))
            .andExpect(jsonPath("$.friend_from").value(DEFAULT_FRIEND_FROM.toString()))
            .andExpect(jsonPath("$.roll_no").value(DEFAULT_ROLL_NO));
    }

    @Test
    public void getNonExistingSachu() throws Exception {
        // Get the sachu
        restSachuMockMvc.perform(get("/api/sachus/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSachu() throws Exception {
        // Initialize the database
        sachuRepository.save(sachu);
        int databaseSizeBeforeUpdate = sachuRepository.findAll().size();

        // Update the sachu
        Sachu updatedSachu = sachuRepository.findOne(sachu.getId());
        updatedSachu
            .friend_name(UPDATED_FRIEND_NAME)
            .friend_from(UPDATED_FRIEND_FROM)
            .roll_no(UPDATED_ROLL_NO);

        restSachuMockMvc.perform(put("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSachu)))
            .andExpect(status().isOk());

        // Validate the Sachu in the database
        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeUpdate);
        Sachu testSachu = sachuList.get(sachuList.size() - 1);
        assertThat(testSachu.getFriend_name()).isEqualTo(UPDATED_FRIEND_NAME);
        assertThat(testSachu.getFriend_from()).isEqualTo(UPDATED_FRIEND_FROM);
        assertThat(testSachu.getRoll_no()).isEqualTo(UPDATED_ROLL_NO);
    }

    @Test
    public void updateNonExistingSachu() throws Exception {
        int databaseSizeBeforeUpdate = sachuRepository.findAll().size();

        // Create the Sachu

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSachuMockMvc.perform(put("/api/sachus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sachu)))
            .andExpect(status().isCreated());

        // Validate the Sachu in the database
        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteSachu() throws Exception {
        // Initialize the database
        sachuRepository.save(sachu);
        int databaseSizeBeforeDelete = sachuRepository.findAll().size();

        // Get the sachu
        restSachuMockMvc.perform(delete("/api/sachus/{id}", sachu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sachu> sachuList = sachuRepository.findAll();
        assertThat(sachuList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sachu.class);
        Sachu sachu1 = new Sachu();
        sachu1.setId(UUID.randomUUID());
        Sachu sachu2 = new Sachu();
        sachu2.setId(sachu1.getId());
        assertThat(sachu1).isEqualTo(sachu2);
        sachu2.setId(UUID.randomUUID());
        assertThat(sachu1).isNotEqualTo(sachu2);
        sachu1.setId(null);
        assertThat(sachu1).isNotEqualTo(sachu2);
    }
}
