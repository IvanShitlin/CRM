package ru.shitlin.web.rest;

import ru.shitlin.HipsterfoxApp;

import ru.shitlin.domain.Mentor;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.Salary;
import ru.shitlin.domain.SalaryItem;
import ru.shitlin.domain.enumeration.Currency;
import ru.shitlin.repository.SalaryRepository;
import ru.shitlin.service.SalaryQueryService;
import ru.shitlin.service.SalaryService;
import ru.shitlin.service.dto.SalaryDTO;
import ru.shitlin.service.mapper.SalaryMapper;
import ru.shitlin.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static ru.shitlin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalaryResource REST controller.
 *
 * @see SalaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class SalaryResourceIntTest {

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final LocalDate DEFAULT_PAID_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAID_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_AMOUNT = 3000L;
    private static final Currency DEFAULT_CURRENCY = Currency.UAH;

    private static final Long UPDATED_AMOUNT = 120L;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private SalaryMapper salaryMapper;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private SalaryQueryService salaryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalaryMockMvc;

    private Salary salary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalaryResource salaryResource = new SalaryResource(salaryService, salaryQueryService);
        this.restSalaryMockMvc = MockMvcBuilders.standaloneSetup(salaryResource)
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
    public static Salary createEntity(EntityManager em) {
        Salary salary = new Salary()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .paid(DEFAULT_PAID)
            .paidDate(DEFAULT_PAID_DATE)
            .sum(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
        return salary;
    }

    @Before
    public void initTest() {
        salary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalary() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().size();

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);
        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate + 1);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalary.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalary.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testSalary.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testSalary.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void createSalaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().size();

        // Create the Salary with an existing ID
        salary.setId(1L);
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSumIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.setSum(null);

        // Create the salary, which fails.
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.getSum().setAmount(null);

        // Create the salary, which fails.
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.getSum().setCurrency(null);

        // Create the salary, which fails.
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalaries() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList
        restSalaryMockMvc.perform(get("/api/salaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].sum.amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].sum.currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get the salary
        restSalaryMockMvc.perform(get("/api/salaries/{id}", salary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salary.getId().intValue()))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE.toString()))
            .andExpect(jsonPath("$.sum.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.sum.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getAllSalariesByDateFromIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateFrom equals to DEFAULT_DATE_FROM
        defaultSalaryShouldBeFound("dateFrom.equals=" + DEFAULT_DATE_FROM);

        // Get all the salaryList where dateFrom equals to UPDATED_DATE_FROM
        defaultSalaryShouldNotBeFound("dateFrom.equals=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateFromIsInShouldWork() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateFrom in DEFAULT_DATE_FROM or UPDATED_DATE_FROM
        defaultSalaryShouldBeFound("dateFrom.in=" + DEFAULT_DATE_FROM + "," + UPDATED_DATE_FROM);

        // Get all the salaryList where dateFrom equals to UPDATED_DATE_FROM
        defaultSalaryShouldNotBeFound("dateFrom.in=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateFrom is not null
        defaultSalaryShouldBeFound("dateFrom.specified=true");

        // Get all the salaryList where dateFrom is null
        defaultSalaryShouldNotBeFound("dateFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalariesByDateFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateFrom greater than or equals to DEFAULT_DATE_FROM
        defaultSalaryShouldBeFound("dateFrom.greaterOrEqualThan=" + DEFAULT_DATE_FROM);

        // Get all the salaryList where dateFrom greater than or equals to UPDATED_DATE_FROM
        defaultSalaryShouldNotBeFound("dateFrom.greaterOrEqualThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateFromIsLessThanSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateFrom less than or equals to DEFAULT_DATE_FROM
        defaultSalaryShouldNotBeFound("dateFrom.lessThan=" + DEFAULT_DATE_FROM);

        // Get all the salaryList where dateFrom less than or equals to UPDATED_DATE_FROM
        defaultSalaryShouldBeFound("dateFrom.lessThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateToIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateTo equals to DEFAULT_DATE_TO
        defaultSalaryShouldBeFound("dateTo.equals=" + DEFAULT_DATE_TO);

        // Get all the salaryList where dateTo equals to UPDATED_DATE_TO
        defaultSalaryShouldNotBeFound("dateTo.equals=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateToIsInShouldWork() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateTo in DEFAULT_DATE_TO or UPDATED_DATE_TO
        defaultSalaryShouldBeFound("dateTo.in=" + DEFAULT_DATE_TO + "," + UPDATED_DATE_TO);

        // Get all the salaryList where dateTo equals to UPDATED_DATE_TO
        defaultSalaryShouldNotBeFound("dateTo.in=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateToIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateTo is not null
        defaultSalaryShouldBeFound("dateTo.specified=true");

        // Get all the salaryList where dateTo is null
        defaultSalaryShouldNotBeFound("dateTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalariesByDateToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateTo greater than or equals to DEFAULT_DATE_TO
        defaultSalaryShouldBeFound("dateTo.greaterOrEqualThan=" + DEFAULT_DATE_TO);

        // Get all the salaryList where dateTo greater than or equals to UPDATED_DATE_TO
        defaultSalaryShouldNotBeFound("dateTo.greaterOrEqualThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalariesByDateToIsLessThanSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where dateTo less than or equals to DEFAULT_DATE_TO
        defaultSalaryShouldNotBeFound("dateTo.lessThan=" + DEFAULT_DATE_TO);

        // Get all the salaryList where dateTo less than or equals to UPDATED_DATE_TO
        defaultSalaryShouldBeFound("dateTo.lessThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paid equals to DEFAULT_PAID
        defaultSalaryShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the salaryList where paid equals to UPDATED_PAID
        defaultSalaryShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultSalaryShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the salaryList where paid equals to UPDATED_PAID
        defaultSalaryShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paid is not null
        defaultSalaryShouldBeFound("paid.specified=true");

        // Get all the salaryList where paid is null
        defaultSalaryShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidDateIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paidDate equals to DEFAULT_PAID_DATE
        defaultSalaryShouldBeFound("paidDate.equals=" + DEFAULT_PAID_DATE);

        // Get all the salaryList where paidDate equals to UPDATED_PAID_DATE
        defaultSalaryShouldNotBeFound("paidDate.equals=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidDateIsInShouldWork() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paidDate in DEFAULT_PAID_DATE or UPDATED_PAID_DATE
        defaultSalaryShouldBeFound("paidDate.in=" + DEFAULT_PAID_DATE + "," + UPDATED_PAID_DATE);

        // Get all the salaryList where paidDate equals to UPDATED_PAID_DATE
        defaultSalaryShouldNotBeFound("paidDate.in=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paidDate is not null
        defaultSalaryShouldBeFound("paidDate.specified=true");

        // Get all the salaryList where paidDate is null
        defaultSalaryShouldNotBeFound("paidDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paidDate greater than or equals to DEFAULT_PAID_DATE
        defaultSalaryShouldBeFound("paidDate.greaterOrEqualThan=" + DEFAULT_PAID_DATE);

        // Get all the salaryList where paidDate greater than or equals to UPDATED_PAID_DATE
        defaultSalaryShouldNotBeFound("paidDate.greaterOrEqualThan=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    public void getAllSalariesByPaidDateIsLessThanSomething() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList where paidDate less than or equals to DEFAULT_PAID_DATE
        defaultSalaryShouldNotBeFound("paidDate.lessThan=" + DEFAULT_PAID_DATE);

        // Get all the salaryList where paidDate less than or equals to UPDATED_PAID_DATE
        defaultSalaryShouldBeFound("paidDate.lessThan=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    public void getAllSalariesByMentorIsEqualToSomething() throws Exception {
        // Initialize the database
        Mentor mentor = MentorResourceIntTest.createEntity(em);
        em.persist(mentor);
        em.flush();
        salary.setMentor(mentor);
        salaryRepository.saveAndFlush(salary);
        Long mentorId = mentor.getId();

        // Get all the salaryList where mentor equals to mentorId
        defaultSalaryShouldBeFound("mentorId.equals=" + mentorId);

        // Get all the salaryList where mentor equals to mentorId + 1
        defaultSalaryShouldNotBeFound("mentorId.equals=" + (mentorId + 1));
    }


    @Test
    @Transactional
    public void getAllSalariesByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        SalaryItem item = SalaryItemResourceIntTest.createEntity(em);
        em.persist(item);
        em.flush();
        salary.addItem(item);
        salaryRepository.saveAndFlush(salary);
        Long itemId = item.getId();

        // Get all the salaryList where item equals to itemId
        defaultSalaryShouldBeFound("itemsId.equals=" + itemId);

        // Get all the salaryList where item equals to itemId + 1
        defaultSalaryShouldNotBeFound("itemsId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSalaryShouldBeFound(String filter) throws Exception {
        restSalaryMockMvc.perform(get("/api/salaries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSalaryShouldNotBeFound(String filter) throws Exception {
        restSalaryMockMvc.perform(get("/api/salaries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingSalary() throws Exception {
        // Get the salary
        restSalaryMockMvc.perform(get("/api/salaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedSalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedSalary are not directly saved in db
        em.detach(updatedSalary);
        updatedSalary
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .paid(UPDATED_PAID)
            .paidDate(UPDATED_PAID_DATE)
            .sum(salary.getSum().amount(UPDATED_AMOUNT).currency(UPDATED_CURRENCY));
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedSalary);

        restSalaryMockMvc.perform(put("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isOk());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testSalary.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testSalary.isPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testSalary.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testSalary.getSum()).isEqualTo(new Money(UPDATED_AMOUNT, UPDATED_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedsalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedsalary are not directly saved in db
        em.detach(updatedsalary);
        // set field null
        updatedsalary.sum(null);
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedsalary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalary.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalary.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testSalary.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testSalary.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedsalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedsalary are not directly saved in db
        em.detach(updatedsalary);
        // set field null
        updatedsalary.getSum().setAmount(null);
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedsalary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalary.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalary.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testSalary.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testSalary.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedsalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedsalary are not directly saved in db
        em.detach(updatedsalary);
        // set field null
        updatedsalary.getSum().setCurrency(null);
        SalaryDTO salaryDTO = salaryMapper.toDto(updatedsalary);

        restSalaryMockMvc.perform(post("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalary.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalary.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testSalary.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testSalary.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void updateNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Create the Salary
        SalaryDTO salaryDTO = salaryMapper.toDto(salary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalaryMockMvc.perform(put("/api/salaries")
            .contentType(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8)
            .content(ru.shitlin.web.rest.TestUtil.convertObjectToJsonBytes(salaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeDelete = salaryRepository.findAll().size();

        // Get the salary
        restSalaryMockMvc.perform(delete("/api/salaries/{id}", salary.getId())
            .accept(ru.shitlin.web.rest.TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        ru.shitlin.web.rest.TestUtil.equalsVerifier(Salary.class);
        Salary salary1 = new Salary();
        salary1.setId(1L);
        Salary salary2 = new Salary();
        salary2.setId(salary1.getId());
        assertThat(salary1).isEqualTo(salary2);
        salary2.setId(2L);
        assertThat(salary1).isNotEqualTo(salary2);
        salary1.setId(null);
        assertThat(salary1).isNotEqualTo(salary2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryDTO.class);
        SalaryDTO salaryDTO1 = new SalaryDTO();
        salaryDTO1.setId(1L);
        SalaryDTO salaryDTO2 = new SalaryDTO();
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
        salaryDTO2.setId(salaryDTO1.getId());
        assertThat(salaryDTO1).isEqualTo(salaryDTO2);
        salaryDTO2.setId(2L);
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
        salaryDTO1.setId(null);
        assertThat(salaryDTO1).isNotEqualTo(salaryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(salaryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(salaryMapper.fromId(null)).isNull();
    }
}
