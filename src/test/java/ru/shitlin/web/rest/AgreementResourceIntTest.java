package ru.shitlin.web.rest;

import ru.shitlin.HipsterfoxApp;
import ru.shitlin.domain.Agreement;
import ru.shitlin.domain.Client;
import ru.shitlin.domain.Course;
import ru.shitlin.domain.CourseType;
import ru.shitlin.domain.Task;
import ru.shitlin.domain.enumeration.AgreementStatus;
import ru.shitlin.domain.enumeration.TaskPriority;
import ru.shitlin.domain.enumeration.TaskType;
import ru.shitlin.repository.AgreementRepository;
import ru.shitlin.repository.search.AgreementSearchRepository;
import ru.shitlin.repository.TaskRepository;
import ru.shitlin.service.AgreementQueryService;
import ru.shitlin.service.AgreementService;
import ru.shitlin.service.dto.AgreementDTO;
import ru.shitlin.service.mapper.AgreementMapper;
import ru.shitlin.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.shitlin.repository.search.AgreementSearchRepositoryMockConfiguration;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static ru.shitlin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgreementResource REST controller.
 *
 * @see AgreementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class AgreementResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final AgreementStatus DEFAULT_STATUS = AgreementStatus.NEW;
    private static final AgreementStatus UPDATED_STATUS = AgreementStatus.WAITING;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private AgreementMapper agreementMapper;

    @Autowired
    private AgreementService agreementService;

    /**
     * This repository is mocked in the com.foxminded.hipsterfox.repository.search test package.
     *
     * @see AgreementSearchRepositoryMockConfiguration
     */
    @Autowired
    private AgreementSearchRepository mockAgreementSearchRepository;

    @Autowired
    private AgreementQueryService agreementQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgreementMockMvc;

    private Agreement agreement;

    @Autowired
    private TaskRepository taskRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgreementResource agreementResource = new AgreementResource(agreementService, agreementQueryService);
        this.restAgreementMockMvc = MockMvcBuilders.standaloneSetup(agreementResource)
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
    public static Agreement createEntity(EntityManager em) {
        Client client = ru.shitlin.web.rest.ClientResourceIntTest.createEntity(em);
        Course course = ru.shitlin.web.rest.CourseResourceIntTest.createEntity(em);
        CourseType courseType = ru.shitlin.web.rest.CourseTypeResourceIntTest.createEntity(em);
        em.persist(client);
        em.persist(course);
        em.persist(courseType);
        em.flush();
        Agreement agreement = new Agreement()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .client(client)
            .course(course)
            .courseType(courseType);
        return agreement;
    }

    @Before
    public void initTest() {
        agreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgreement() throws Exception {
        int databaseSizeBeforeCreate = agreementRepository.findAll().size();

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);
        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isCreated());

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeCreate + 1);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAgreement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAgreement.getClient()).isEqualTo(agreement.getClient());
        assertThat(testAgreement.getCourse()).isEqualTo(agreement.getCourse());

        // Validate the Agreement in Elasticsearch
        verify(mockAgreementSearchRepository, times(1)).save(testAgreement);

    }

    @Test
    @Transactional
    public void checkDefaultStatusSetWhenCreateAgreementWithoutStatus() throws Exception {
        Client client = ru.shitlin.web.rest.ClientResourceIntTest.createEntity(em);
        Course course = ru.shitlin.web.rest.CourseResourceIntTest.createEntity(em);
        CourseType courseType = ru.shitlin.web.rest.CourseTypeResourceIntTest.createEntity(em);
        em.persist(client);
        em.persist(course);
        em.persist(courseType);
        em.flush();

        Agreement agreement = new Agreement()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .client(client)
            .course(course)
            .courseType(courseType);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();

        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isCreated());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest + 1);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createAgreementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agreementRepository.findAll().size();

        // Create the Agreement with an existing ID
        agreement.setId(1L);
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Agreement in Elasticsearch
        verify(mockAgreementSearchRepository, times(0)).save(agreement);

    }

    @Test
    @Transactional
    public void checkImpossibleToSaveDuplicateAgreement() throws Exception {
        Client client = ru.shitlin.web.rest.ClientResourceIntTest.createEntity(em);
        Course course = ru.shitlin.web.rest.CourseResourceIntTest.createEntity(em);
        CourseType courseType = CourseTypeResourceIntTest.createEntity(em);
        em.persist(client);
        em.persist(course);
        em.persist(courseType);
        em.flush();

        Agreement duplicateAgreement = new Agreement()
            .client(client)
            .course(course)
            .courseType(courseType);

        // Create unique entity
        agreementRepository.saveAndFlush(duplicateAgreement);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();

        // A try to save duplicate
        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementMapper.toDto(duplicateAgreement))))
            .andExpect(status().isBadRequest());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClientIsRequired() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();
        // set the field null
        Agreement updateAgreement = agreementRepository.findById(agreement.getId()).get();
        Client client = updateAgreement.getClient();
        em.detach(updateAgreement);
        updateAgreement.client(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(updateAgreement);

        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getClient()).isEqualTo(client);
    }

    @Test
    @Transactional
    public void checkClientIsRequiredForUpdate() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeUpdate = agreementRepository.findAll().size();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findById(agreement.getId()).get();
        Client client = updatedAgreement.getClient();
        Course course = updatedAgreement.getCourse();
        Course updatedCourse = ru.shitlin.web.rest.CourseResourceIntTest.createEntity(em);
        em.persist(updatedCourse);
        em.flush();
        // Disconnect from session so that the updates on updatedAgreement are not directly saved in db
        em.detach(updatedAgreement);
        em.detach(updatedCourse);
        updatedAgreement
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .client(null)
            .course(updatedCourse);

        AgreementDTO agreementDTO = agreementMapper.toDto(updatedAgreement);

        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        //Validate the Agreement is not updated
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAgreement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAgreement.getClient()).isEqualTo(client);
        assertThat(testAgreement.getCourse()).isEqualTo(course);
    }

    @Test
    @Transactional
    public void checkCourseIsRequired() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();
        // set the field null
        Agreement updateAgreement = agreementRepository.findById(agreement.getId()).get();
        Course course = updateAgreement.getCourse();
        em.detach(updateAgreement);
        updateAgreement.client(null);

        // Create the Agreement, which fails.
        AgreementDTO agreementDTO = agreementMapper.toDto(updateAgreement);

        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getCourse()).isEqualTo(course);
    }

    @Test
    @Transactional
    public void checkCourseIsRequiredForUpdate() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeUpdate = agreementRepository.findAll().size();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findById(agreement.getId()).get();
        Client client = updatedAgreement.getClient();
        Course course = updatedAgreement.getCourse();
        Client updatedClient = ru.shitlin.web.rest.ClientResourceIntTest.createEntity(em);
        em.persist(updatedClient);
        em.flush();
        // Disconnect from session so that the updates on updatedAgreement are not directly saved in db
        em.detach(updatedAgreement);
        em.detach(updatedClient);
        updatedAgreement
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .client(updatedClient)
            .course(null);

        AgreementDTO agreementDTO = agreementMapper.toDto(updatedAgreement);

        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAgreement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAgreement.getClient()).isEqualTo(client);
        assertThat(testAgreement.getCourse()).isEqualTo(course);
    }

    @Test
    @Transactional
    public void checkStatusIsImpossibleToUpdateInAgreementDTO() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();

        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);
        agreementDTO.setStatus(UPDATED_STATUS);

        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isOk());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkStatusIsImpossibleToChangeFromAgreementDTO() throws Exception {
        // Initialize the database
        agreement.setStatus(AgreementStatus.ACTIVE);
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeTest = agreementRepository.findAll().size();

        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);
        agreementDTO.setStatus(UPDATED_STATUS);

        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isOk());

        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeTest);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStatus()).isEqualTo(AgreementStatus.ACTIVE);
    }

    @Test
    @Transactional
    public void getAllAgreements() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList
        restAgreementMockMvc.perform(get("/api/agreements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get the agreement
        restAgreementMockMvc.perform(get("/api/agreements/{id}", agreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agreement.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllAgreementsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where startDate equals to DEFAULT_START_DATE
        defaultAgreementShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate equals to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultAgreementShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the agreementList where startDate equals to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where startDate is not null
        defaultAgreementShouldBeFound("startDate.specified=true");

        // Get all the agreementList where startDate is null
        defaultAgreementShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgreementsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where startDate greater than or equals to DEFAULT_START_DATE
        defaultAgreementShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate greater than or equals to UPDATED_START_DATE
        defaultAgreementShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where startDate less than or equals to DEFAULT_START_DATE
        defaultAgreementShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the agreementList where startDate less than or equals to UPDATED_START_DATE
        defaultAgreementShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where endDate equals to DEFAULT_END_DATE
        defaultAgreementShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate equals to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultAgreementShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the agreementList where endDate equals to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where endDate is not null
        defaultAgreementShouldBeFound("endDate.specified=true");

        // Get all the agreementList where endDate is null
        defaultAgreementShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgreementsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where endDate greater than or equals to DEFAULT_END_DATE
        defaultAgreementShouldBeFound("endDate.greaterOrEqualThan=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate greater than or equals to UPDATED_END_DATE
        defaultAgreementShouldNotBeFound("endDate.greaterOrEqualThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where endDate less than or equals to DEFAULT_END_DATE
        defaultAgreementShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the agreementList where endDate less than or equals to UPDATED_END_DATE
        defaultAgreementShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where status equals to DEFAULT_STATUS
        defaultAgreementShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the agreementList where status equals to UPDATED_STATUS
        defaultAgreementShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAgreementShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the agreementList where status equals to UPDATED_STATUS
        defaultAgreementShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAgreementsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList where status is not null
        defaultAgreementShouldBeFound("status.specified=true");

        // Get all the agreementList where status is null
        defaultAgreementShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllAgreementsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ru.shitlin.web.rest.ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        agreement.setClient(client);
        agreementRepository.saveAndFlush(agreement);
        Long clientId = client.getId();

        // Get all the agreementList where client equals to clientId
        defaultAgreementShouldBeFound("clientId.equals=" + clientId);

        // Get all the agreementList where client equals to clientId + 1
        defaultAgreementShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    public void getAllAgreementsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        Course course = ru.shitlin.web.rest.CourseResourceIntTest.createEntity(em);
        em.persist(course);
        em.flush();
        agreement.setCourse(course);
        agreementRepository.saveAndFlush(agreement);
        Long courseId = course.getId();

        // Get all the agreementList where course equals to courseId
        defaultAgreementShouldBeFound("courseId.equals=" + courseId);

        // Get all the agreementList where course equals to courseId + 1
        defaultAgreementShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAgreementShouldBeFound(String filter) throws Exception {
        restAgreementMockMvc.perform(get("/api/agreements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAgreementShouldNotBeFound(String filter) throws Exception {
        restAgreementMockMvc.perform(get("/api/agreements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAgreement() throws Exception {
        // Get the agreement
        restAgreementMockMvc.perform(get("/api/agreements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeUpdate = agreementRepository.findAll().size();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findById(agreement.getId()).get();
        Client updatedClient = ClientResourceIntTest.createEntity(em);
        Course updatedCourse = CourseResourceIntTest.createEntity(em);
        em.persist(updatedClient);
        em.persist(updatedCourse);
        em.flush();
        // Disconnect from session so that the updates on updatedAgreement are not directly saved in db
        em.detach(updatedAgreement);
        em.detach(updatedClient);
        em.detach(updatedCourse);
        updatedAgreement
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .client(updatedClient)
            .course(updatedCourse);

        AgreementDTO agreementDTO = agreementMapper.toDto(updatedAgreement);

        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isOk());

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreementList.get(agreementList.size() - 1);
        assertThat(testAgreement.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAgreement.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAgreement.getClient()).isEqualTo(updatedClient);
        assertThat(testAgreement.getCourse()).isEqualTo(updatedCourse);

        // Validate the Agreement in Elasticsearch
        verify(mockAgreementSearchRepository, times(1)).save(testAgreement);

    }

    @Test
    @Transactional
    public void updateNonExistingAgreement() throws Exception {
        int databaseSizeBeforeUpdate = agreementRepository.findAll().size();

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgreementMockMvc.perform(put("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Agreement in Elasticsearch
        verify(mockAgreementSearchRepository, times(0)).save(agreement);
    }

    @Test
    @Transactional
    public void deleteAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        int databaseSizeBeforeDelete = agreementRepository.findAll().size();

        // Get the agreement
        restAgreementMockMvc.perform(delete("/api/agreements/{id}", agreement.getId())
            .accept(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Agreement> agreementList = agreementRepository.findAll();
        assertThat(agreementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Agreement in Elasticsearch
        verify(mockAgreementSearchRepository, times(1)).deleteById(agreement.getId());
    }

    @Test
    @Transactional
    public void searchAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);
        when(mockAgreementSearchRepository.search(queryStringQuery("id:" + agreement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(agreement), PageRequest.of(0, 1), 1));
        // Search the agreement
        restAgreementMockMvc.perform(get("/api/_search/agreements?query=id:" + agreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        ru.shitlin.web.rest.TestUtil.equalsVerifier(Agreement.class);
        Agreement agreement1 = new Agreement();
        agreement1.setId(1L);
        Agreement agreement2 = new Agreement();
        agreement2.setId(agreement1.getId());
        assertThat(agreement1).isEqualTo(agreement2);
        agreement2.setId(2L);
        assertThat(agreement1).isNotEqualTo(agreement2);
        agreement1.setId(null);
        assertThat(agreement1).isNotEqualTo(agreement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        ru.shitlin.web.rest.TestUtil.equalsVerifier(AgreementDTO.class);
        AgreementDTO agreementDTO1 = new AgreementDTO();
        agreementDTO1.setId(1L);
        AgreementDTO agreementDTO2 = new AgreementDTO();
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
        agreementDTO2.setId(agreementDTO1.getId());
        assertThat(agreementDTO1).isEqualTo(agreementDTO2);
        agreementDTO2.setId(2L);
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
        agreementDTO1.setId(null);
        assertThat(agreementDTO1).isNotEqualTo(agreementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agreementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agreementMapper.fromId(null)).isNull();
    }

    @Test
    @Transactional
    public void shouldCreateTaskWhenAgreementCreated() throws Exception {
        //  Initialize a size of the task repository before tests.
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Agreement
        AgreementDTO agreementDTO = agreementMapper.toDto(agreement);
        restAgreementMockMvc.perform(post("/api/agreements")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agreementDTO)))
            .andExpect(status().isCreated());

        List<Agreement> agreementsList = agreementRepository.findAll();
        Agreement savedAgreement = agreementsList.get(agreementsList.size()-1);

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        Task task = taskList.get(taskList.size() - 1);
        assertThat(task.getType()).isEqualTo(TaskType.CONTACT);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.CRITICAL);
        assertThat(task.getDescription()).contains(String.valueOf(savedAgreement.getId()));
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void whenPassAgreementIdShouldSendEmailToClient() throws Exception {

        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get the contract
        restAgreementMockMvc.perform(post("/api/agreements/greetings-email")
            .param("id", agreement.getId().toString()))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void whenPassContractIdEqualsNullShouldNotSendEmailToClient() throws Exception {
        String id = null;

        // Get the contract
        // Get the contract
        restAgreementMockMvc.perform(post("/api/agreements/greetings-email")
            .param("id", id))
            .andExpect(status().isBadRequest());
    }
}
