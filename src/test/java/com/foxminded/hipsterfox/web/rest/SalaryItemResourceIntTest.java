package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;

import com.foxminded.hipsterfox.domain.Invoice;
import com.foxminded.hipsterfox.domain.Mentor;
import com.foxminded.hipsterfox.domain.Money;
import com.foxminded.hipsterfox.domain.Salary;
import com.foxminded.hipsterfox.domain.SalaryItem;
import com.foxminded.hipsterfox.domain.enumeration.Currency;
import com.foxminded.hipsterfox.repository.SalaryItemRepository;
import com.foxminded.hipsterfox.service.SalaryItemQueryService;
import com.foxminded.hipsterfox.service.SalaryItemService;
import com.foxminded.hipsterfox.service.dto.SalaryItemDTO;
import com.foxminded.hipsterfox.service.mapper.SalaryItemMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.foxminded.hipsterfox.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalaryItemResource REST controller.
 *
 * @see SalaryItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class SalaryItemResourceIntTest {

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACCOUNTED = false;
    private static final Boolean UPDATED_ACCOUNTED = true;

    private static final Long DEFAULT_AMOUNT = 3000L;
    private static final Currency DEFAULT_CURRENCY = Currency.UAH;

    private static final Long UPDATED_AMOUNT = 120L;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    @Autowired
    private SalaryItemRepository salaryItemRepository;

    @Autowired
    private SalaryItemMapper salaryItemMapper;

    @Autowired
    private SalaryItemService salaryItemService;

    @Autowired
    private SalaryItemQueryService salaryItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalaryItemMockMvc;

    private SalaryItem salaryItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalaryItemResource salaryItemResource = new SalaryItemResource(salaryItemService, salaryItemQueryService);
        this.restSalaryItemMockMvc = MockMvcBuilders.standaloneSetup(salaryItemResource)
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
    public static SalaryItem createEntity(EntityManager em) {
        SalaryItem salaryItem = new SalaryItem()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .accounted(DEFAULT_ACCOUNTED)
            .sum(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
        return salaryItem;
    }

    @Before
    public void initTest() {
        salaryItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalaryItem() throws Exception {
        int databaseSizeBeforeCreate = salaryItemRepository.findAll().size();

        // Create the SalaryItem
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);
        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isCreated());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeCreate + 1);
        SalaryItem testSalaryItem = salaryItemList.get(salaryItemList.size() - 1);
        assertThat(testSalaryItem.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalaryItem.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalaryItem.isAccounted()).isEqualTo(DEFAULT_ACCOUNTED);
        assertThat(testSalaryItem.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void createSalaryItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salaryItemRepository.findAll().size();

        // Create the SalaryItem with an existing ID
        salaryItem.setId(1L);
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSumIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryItemRepository.findAll().size();
        // set the field null
        salaryItem.setSum(null);

        // Create the salaryItem, which fails.
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryItemRepository.findAll().size();
        // set the field null
        salaryItem.getSum().setAmount(null);

        // Create the salaryItem, which fails.
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryItemRepository.findAll().size();
        // set the field null
        salaryItem.getSum().setCurrency(null);

        // Create the salaryItem, which fails.
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalaryItems() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList
        restSalaryItemMockMvc.perform(get("/api/salary-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].accounted").value(hasItem(DEFAULT_ACCOUNTED.booleanValue())))
            .andExpect(jsonPath("$.[*].sum.amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].sum.currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getSalaryItem() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get the salaryItem
        restSalaryItemMockMvc.perform(get("/api/salary-items/{id}", salaryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salaryItem.getId().intValue()))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
            .andExpect(jsonPath("$.accounted").value(DEFAULT_ACCOUNTED.booleanValue()))
            .andExpect(jsonPath("$.sum.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.sum.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateFromIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateFrom equals to DEFAULT_DATE_FROM
        defaultSalaryItemShouldBeFound("dateFrom.equals=" + DEFAULT_DATE_FROM);

        // Get all the salaryItemList where dateFrom equals to UPDATED_DATE_FROM
        defaultSalaryItemShouldNotBeFound("dateFrom.equals=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateFromIsInShouldWork() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateFrom in DEFAULT_DATE_FROM or UPDATED_DATE_FROM
        defaultSalaryItemShouldBeFound("dateFrom.in=" + DEFAULT_DATE_FROM + "," + UPDATED_DATE_FROM);

        // Get all the salaryItemList where dateFrom equals to UPDATED_DATE_FROM
        defaultSalaryItemShouldNotBeFound("dateFrom.in=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateFrom is not null
        defaultSalaryItemShouldBeFound("dateFrom.specified=true");

        // Get all the salaryItemList where dateFrom is null
        defaultSalaryItemShouldNotBeFound("dateFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateFrom greater than or equals to DEFAULT_DATE_FROM
        defaultSalaryItemShouldBeFound("dateFrom.greaterOrEqualThan=" + DEFAULT_DATE_FROM);

        // Get all the salaryItemList where dateFrom greater than or equals to UPDATED_DATE_FROM
        defaultSalaryItemShouldNotBeFound("dateFrom.greaterOrEqualThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateFromIsLessThanSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateFrom less than or equals to DEFAULT_DATE_FROM
        defaultSalaryItemShouldNotBeFound("dateFrom.lessThan=" + DEFAULT_DATE_FROM);

        // Get all the salaryItemList where dateFrom less than or equals to UPDATED_DATE_FROM
        defaultSalaryItemShouldBeFound("dateFrom.lessThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateToIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateTo equals to DEFAULT_DATE_TO
        defaultSalaryItemShouldBeFound("dateTo.equals=" + DEFAULT_DATE_TO);

        // Get all the salaryItemList where dateTo equals to UPDATED_DATE_TO
        defaultSalaryItemShouldNotBeFound("dateTo.equals=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateToIsInShouldWork() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateTo in DEFAULT_DATE_TO or UPDATED_DATE_TO
        defaultSalaryItemShouldBeFound("dateTo.in=" + DEFAULT_DATE_TO + "," + UPDATED_DATE_TO);

        // Get all the salaryItemList where dateTo equals to UPDATED_DATE_TO
        defaultSalaryItemShouldNotBeFound("dateTo.in=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateToIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateTo is not null
        defaultSalaryItemShouldBeFound("dateTo.specified=true");

        // Get all the salaryItemList where dateTo is null
        defaultSalaryItemShouldNotBeFound("dateTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateTo greater than or equals to DEFAULT_DATE_TO
        defaultSalaryItemShouldBeFound("dateTo.greaterOrEqualThan=" + DEFAULT_DATE_TO);

        // Get all the salaryItemList where dateTo greater than or equals to UPDATED_DATE_TO
        defaultSalaryItemShouldNotBeFound("dateTo.greaterOrEqualThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByDateToIsLessThanSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where dateTo less than or equals to DEFAULT_DATE_TO
        defaultSalaryItemShouldNotBeFound("dateTo.lessThan=" + DEFAULT_DATE_TO);

        // Get all the salaryItemList where dateTo less than or equals to UPDATED_DATE_TO
        defaultSalaryItemShouldBeFound("dateTo.lessThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByAccountedIsEqualToSomething() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where accounted equals to DEFAULT_ACCOUNTED
        defaultSalaryItemShouldBeFound("accounted.equals=" + DEFAULT_ACCOUNTED);

        // Get all the salaryItemList where accounted equals to UPDATED_ACCOUNTED
        defaultSalaryItemShouldNotBeFound("accounted.equals=" + UPDATED_ACCOUNTED);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByAccountedIsInShouldWork() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where accounted in DEFAULT_ACCOUNTED or UPDATED_ACCOUNTED
        defaultSalaryItemShouldBeFound("accounted.in=" + DEFAULT_ACCOUNTED + "," + UPDATED_ACCOUNTED);

        // Get all the salaryItemList where accounted equals to UPDATED_ACCOUNTED
        defaultSalaryItemShouldNotBeFound("accounted.in=" + UPDATED_ACCOUNTED);
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByAccountedIsNullOrNotNull() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        // Get all the salaryItemList where accounted is not null
        defaultSalaryItemShouldBeFound("accounted.specified=true");

        // Get all the salaryItemList where accounted is null
        defaultSalaryItemShouldNotBeFound("accounted.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByMentorIsEqualToSomething() throws Exception {
        // Initialize the database
        Mentor mentor = MentorResourceIntTest.createEntity(em);
        em.persist(mentor);
        em.flush();
        salaryItem.setMentor(mentor);
        salaryItemRepository.saveAndFlush(salaryItem);
        Long mentorId = mentor.getId();

        // Get all the salaryItemList where mentor equals to mentorId
        defaultSalaryItemShouldBeFound("mentorId.equals=" + mentorId);

        // Get all the salaryItemList where mentor equals to mentorId + 1
        defaultSalaryItemShouldNotBeFound("mentorId.equals=" + (mentorId + 1));
    }

    @Test
    @Transactional
    public void getAllSalaryItemsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        salaryItem.setInvoice(invoice);
        salaryItemRepository.saveAndFlush(salaryItem);
        Long invoiceId = invoice.getId();

        // Get all the salaryItemList where invoice equals to invoiceId
        defaultSalaryItemShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the salaryItemList where invoice equals to invoiceId + 1
        defaultSalaryItemShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    public void getAllSalaryItemsBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        Salary salary = SalaryResourceIntTest.createEntity(em);
        em.persist(salary);
        em.flush();
        salaryItem.setSalary(salary);
        salaryItemRepository.saveAndFlush(salaryItem);
        Long salaryId = salary.getId();

        // Get all the salaryItemList where salary equals to salaryId
        defaultSalaryItemShouldBeFound("salaryId.equals=" + salaryId);

        // Get all the salaryItemList where salary equals to salaryId + 1
        defaultSalaryItemShouldNotBeFound("salaryId.equals=" + (salaryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSalaryItemShouldBeFound(String filter) throws Exception {
        restSalaryItemMockMvc.perform(get("/api/salary-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].accounted").value(hasItem(DEFAULT_ACCOUNTED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSalaryItemShouldNotBeFound(String filter) throws Exception {
        restSalaryItemMockMvc.perform(get("/api/salary-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingSalaryItem() throws Exception {
        // Get the salaryItem
        restSalaryItemMockMvc.perform(get("/api/salary-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalaryItem() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        int databaseSizeBeforeUpdate = salaryItemRepository.findAll().size();

        // Update the salaryItem
        SalaryItem updatedSalaryItem = salaryItemRepository.findById(salaryItem.getId()).get();
        // Disconnect from session so that the updates on updatedSalaryItem are not directly saved in db
        em.detach(updatedSalaryItem);
        updatedSalaryItem
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .accounted(UPDATED_ACCOUNTED)
            .sum(salaryItem.getSum().amount(UPDATED_AMOUNT).currency(UPDATED_CURRENCY));
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(updatedSalaryItem);

        restSalaryItemMockMvc.perform(put("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isOk());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeUpdate);
        SalaryItem testSalaryItem = salaryItemList.get(salaryItemList.size() - 1);
        assertThat(testSalaryItem.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testSalaryItem.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testSalaryItem.isAccounted()).isEqualTo(UPDATED_ACCOUNTED);
        assertThat(testSalaryItem.getSum()).isEqualTo(new Money(UPDATED_AMOUNT, UPDATED_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        int databaseSizeBeforeUpdate = salaryItemRepository.findAll().size();

        // Update the salaryItem
        SalaryItem updatedsalaryItem = salaryItemRepository.findById(salaryItem.getId()).get();
        // Disconnect from session so that the updates on updatedsalaryItem are not directly saved in db
        em.detach(updatedsalaryItem);
        // set field null
        updatedsalaryItem.sum(null);
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(updatedsalaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeUpdate);
        SalaryItem testSalaryItem = salaryItemList.get(salaryItemList.size() - 1);
        assertThat(testSalaryItem.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalaryItem.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalaryItem.isAccounted()).isEqualTo(DEFAULT_ACCOUNTED);
        assertThat(testSalaryItem.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        int databaseSizeBeforeUpdate = salaryItemRepository.findAll().size();

        // Update the salaryItem
        SalaryItem updatedsalaryItem = salaryItemRepository.findById(salaryItem.getId()).get();
        // Disconnect from session so that the updates on updatedsalaryItem are not directly saved in db
        em.detach(updatedsalaryItem);
        // set field null
        updatedsalaryItem.getSum().setAmount(null);
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(updatedsalaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeUpdate);
        SalaryItem testSalaryItem = salaryItemList.get(salaryItemList.size() - 1);
        assertThat(testSalaryItem.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalaryItem.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalaryItem.isAccounted()).isEqualTo(DEFAULT_ACCOUNTED);
        assertThat(testSalaryItem.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        int databaseSizeBeforeUpdate = salaryItemRepository.findAll().size();

        // Update the salaryItem
        SalaryItem updatedsalaryItem = salaryItemRepository.findById(salaryItem.getId()).get();
        // Disconnect from session so that the updates on updatedsalaryItem are not directly saved in db
        em.detach(updatedsalaryItem);
        // set field null
        updatedsalaryItem.getSum().setCurrency(null);
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(updatedsalaryItem);

        restSalaryItemMockMvc.perform(post("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeUpdate);
        SalaryItem testSalaryItem = salaryItemList.get(salaryItemList.size() - 1);
        assertThat(testSalaryItem.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testSalaryItem.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testSalaryItem.isAccounted()).isEqualTo(DEFAULT_ACCOUNTED);
        assertThat(testSalaryItem.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void updateNonExistingSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = salaryItemRepository.findAll().size();

        // Create the SalaryItem
        SalaryItemDTO salaryItemDTO = salaryItemMapper.toDto(salaryItem);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalaryItemMockMvc.perform(put("/api/salary-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salaryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalaryItem in the database
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalaryItem() throws Exception {
        // Initialize the database
        salaryItemRepository.saveAndFlush(salaryItem);

        int databaseSizeBeforeDelete = salaryItemRepository.findAll().size();

        // Get the salaryItem
        restSalaryItemMockMvc.perform(delete("/api/salary-items/{id}", salaryItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SalaryItem> salaryItemList = salaryItemRepository.findAll();
        assertThat(salaryItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryItem.class);
        SalaryItem salaryItem1 = new SalaryItem();
        salaryItem1.setId(1L);
        SalaryItem salaryItem2 = new SalaryItem();
        salaryItem2.setId(salaryItem1.getId());
        assertThat(salaryItem1).isEqualTo(salaryItem2);
        salaryItem2.setId(2L);
        assertThat(salaryItem1).isNotEqualTo(salaryItem2);
        salaryItem1.setId(null);
        assertThat(salaryItem1).isNotEqualTo(salaryItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryItemDTO.class);
        SalaryItemDTO salaryItemDTO1 = new SalaryItemDTO();
        salaryItemDTO1.setId(1L);
        SalaryItemDTO salaryItemDTO2 = new SalaryItemDTO();
        assertThat(salaryItemDTO1).isNotEqualTo(salaryItemDTO2);
        salaryItemDTO2.setId(salaryItemDTO1.getId());
        assertThat(salaryItemDTO1).isEqualTo(salaryItemDTO2);
        salaryItemDTO2.setId(2L);
        assertThat(salaryItemDTO1).isNotEqualTo(salaryItemDTO2);
        salaryItemDTO1.setId(null);
        assertThat(salaryItemDTO1).isNotEqualTo(salaryItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(salaryItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(salaryItemMapper.fromId(null)).isNull();
    }
}
