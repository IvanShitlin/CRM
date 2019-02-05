package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;

import com.foxminded.hipsterfox.domain.Mentor;
import com.foxminded.hipsterfox.repository.MentorRepository;
import com.foxminded.hipsterfox.service.MentorService;
import com.foxminded.hipsterfox.service.dto.MentorDTO;
import com.foxminded.hipsterfox.service.mapper.MentorMapper;
import com.foxminded.hipsterfox.web.rest.errors.ExceptionTranslator;

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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.foxminded.hipsterfox.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MentorResource REST controller.
 *
 * @see MentorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class MentorResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRONYMIC = "AAAAAAAAAA";
    private static final String UPDATED_PATRONYMIC = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SKYPE = "AAAAAAAAAA";
    private static final String UPDATED_SKYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Long DEFAULT_MAX_STUDENTS = 1L;
    private static final Long UPDATED_MAX_STUDENTS = 2L;

    @Autowired
    private MentorRepository mentorRepository;


    @Autowired
    private MentorMapper mentorMapper;
    

    @Autowired
    private MentorService mentorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMentorMockMvc;

    private Mentor mentor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MentorResource mentorResource = new MentorResource(mentorService);
        this.restMentorMockMvc = MockMvcBuilders.standaloneSetup(mentorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createEntity(EntityManager em) {
        Mentor mentor = new Mentor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .patronymic(DEFAULT_PATRONYMIC)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .skype(DEFAULT_SKYPE)
            .country(DEFAULT_COUNTRY)
            .city(DEFAULT_CITY)
            .maxStudents(DEFAULT_MAX_STUDENTS);
        return mentor;
    }

    @Before
    public void initTest() {
        mentor = createEntity(em);
    }

    @Test
    @Transactional
    public void createMentor() throws Exception {
        int databaseSizeBeforeCreate = mentorRepository.findAll().size();

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);
        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isCreated());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeCreate + 1);
        Mentor testMentor = mentorList.get(mentorList.size() - 1);
        assertThat(testMentor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMentor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMentor.getPatronymic()).isEqualTo(DEFAULT_PATRONYMIC);
        assertThat(testMentor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMentor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMentor.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(testMentor.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMentor.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMentor.getMaxStudents()).isEqualTo(DEFAULT_MAX_STUDENTS);
    }

    @Test
    @Transactional
    public void createMentorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mentorRepository.findAll().size();

        // Create the Mentor with an existing ID
        mentor.setId(1L);
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setFirstName(null);

        // Create the Mentor, which fails.
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setLastName(null);

        // Create the Mentor, which fails.
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = mentorRepository.findAll().size();
        // set the field null
        mentor.setEmail(null);

        // Create the Mentor, which fails.
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        restMentorMockMvc.perform(post("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMentors() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        // Get all the mentorList
        restMentorMockMvc.perform(get("/api/mentors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].maxStudents").value(hasItem(DEFAULT_MAX_STUDENTS.intValue())));
    }
    

    @Test
    @Transactional
    public void getMentor() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        // Get the mentor
        restMentorMockMvc.perform(get("/api/mentors/{id}", mentor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mentor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.patronymic").value(DEFAULT_PATRONYMIC.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.skype").value(DEFAULT_SKYPE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.maxStudents").value(DEFAULT_MAX_STUDENTS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingMentor() throws Exception {
        // Get the mentor
        restMentorMockMvc.perform(get("/api/mentors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMentor() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        int databaseSizeBeforeUpdate = mentorRepository.findAll().size();

        // Update the mentor
        Mentor updatedMentor = mentorRepository.findById(mentor.getId()).get();
        // Disconnect from session so that the updates on updatedMentor are not directly saved in db
        em.detach(updatedMentor);
        updatedMentor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .patronymic(UPDATED_PATRONYMIC)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .skype(UPDATED_SKYPE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .maxStudents(UPDATED_MAX_STUDENTS);
        MentorDTO mentorDTO = mentorMapper.toDto(updatedMentor);

        restMentorMockMvc.perform(put("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isOk());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeUpdate);
        Mentor testMentor = mentorList.get(mentorList.size() - 1);
        assertThat(testMentor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMentor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMentor.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testMentor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMentor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMentor.getSkype()).isEqualTo(UPDATED_SKYPE);
        assertThat(testMentor.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMentor.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMentor.getMaxStudents()).isEqualTo(UPDATED_MAX_STUDENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingMentor() throws Exception {
        int databaseSizeBeforeUpdate = mentorRepository.findAll().size();

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMentorMockMvc.perform(put("/api/mentors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMentor() throws Exception {
        // Initialize the database
        mentorRepository.saveAndFlush(mentor);

        int databaseSizeBeforeDelete = mentorRepository.findAll().size();

        // Get the mentor
        restMentorMockMvc.perform(delete("/api/mentors/{id}", mentor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mentor> mentorList = mentorRepository.findAll();
        assertThat(mentorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mentor.class);
        Mentor mentor1 = new Mentor();
        mentor1.setId(1L);
        Mentor mentor2 = new Mentor();
        mentor2.setId(mentor1.getId());
        assertThat(mentor1).isEqualTo(mentor2);
        mentor2.setId(2L);
        assertThat(mentor1).isNotEqualTo(mentor2);
        mentor1.setId(null);
        assertThat(mentor1).isNotEqualTo(mentor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MentorDTO.class);
        MentorDTO mentorDTO1 = new MentorDTO();
        mentorDTO1.setId(1L);
        MentorDTO mentorDTO2 = new MentorDTO();
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
        mentorDTO2.setId(mentorDTO1.getId());
        assertThat(mentorDTO1).isEqualTo(mentorDTO2);
        mentorDTO2.setId(2L);
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
        mentorDTO1.setId(null);
        assertThat(mentorDTO1).isNotEqualTo(mentorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mentorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mentorMapper.fromId(null)).isNull();
    }
}
