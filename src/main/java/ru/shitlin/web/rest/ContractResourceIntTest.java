package ru.shitlin.web.rest;

import ru.shitlin.HipsterfoxApp;
import com.foxminded.hipsterfox.domain.*;
import ru.shitlin.domain.*;
import ru.shitlin.domain.enumeration.CloseType;
import ru.shitlin.domain.enumeration.Currency;
import ru.shitlin.repository.ContractRepository;
import ru.shitlin.service.ContractQueryService;
import ru.shitlin.service.ContractService;
import ru.shitlin.service.dto.ContractDTO;
import ru.shitlin.service.mapper.ContractMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContractResource REST controller.
 *
 * @see ContractResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class ContractResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FIRST_PAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_PAY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_NEXT_PAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_PAY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final CloseType DEFAULT_CLOSE_TYPE = CloseType.FROZEN;
    private static final CloseType UPDATED_CLOSE_TYPE = CloseType.COMPLETED;

    private static final Long DEFAULT_PRICE_AMOUNT = 3000L;
    private static final Currency DEFAULT_PRICE_CURRENCY = Currency.UAH;

    private static final Long UPDATED_PRICE_AMOUNT = 120L;
    private static final Currency UPDATED_PRICE_CURRENCY = Currency.USD;

    private static final Long DEFAULT_MENTOR_RATE_AMOUNT = 1500L;
    private static final Currency DEFAULT_MENTOR_RATE_CURRENCY = Currency.UAH;

    private static final Long UPDATED_MENTOR_RATE_AMOUNT = 60L;
    private static final Currency UPDATED_MENTOR_RATE_CURRENCY = Currency.USD;

    private static final String EMAIL_TEMPLATE = "javaee";

    @Autowired
    private ContractRepository contractRepository;


    @Autowired
    private ContractMapper contractMapper;


    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractQueryService contractQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContractResource contractResource = new ContractResource(contractService, contractQueryService);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .firstPayDate(DEFAULT_FIRST_PAY_DATE)
            .nextPayDate(DEFAULT_NEXT_PAY_DATE)
            .closeType(DEFAULT_CLOSE_TYPE)
            .price(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY))
            .mentorRate(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
        return contract;
    }

    @Before
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void createContractWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setPrice(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.getPrice().setAmount(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.getPrice().setCurrency(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setMentorRate(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.getMentorRate().setAmount(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.getMentorRate().setCurrency(null);

        // Create the contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].firstPayDate").value(hasItem(DEFAULT_FIRST_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextPayDate").value(hasItem(DEFAULT_NEXT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].closeType").value(hasItem(DEFAULT_CLOSE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price.amount").value(hasItem(DEFAULT_PRICE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price.currency").value(hasItem(DEFAULT_PRICE_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].mentorRate.amount").value(hasItem(DEFAULT_MENTOR_RATE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].mentorRate.currency").value(hasItem(DEFAULT_MENTOR_RATE_CURRENCY.toString())));
    }


    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.firstPayDate").value(DEFAULT_FIRST_PAY_DATE.toString()))
            .andExpect(jsonPath("$.nextPayDate").value(DEFAULT_NEXT_PAY_DATE.toString()))
            .andExpect(jsonPath("$.closeType").value(DEFAULT_CLOSE_TYPE.toString()))
            .andExpect(jsonPath("$.price.amount").value(DEFAULT_PRICE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.price.currency").value(DEFAULT_PRICE_CURRENCY.toString()))
            .andExpect(jsonPath("$.mentorRate.amount").value(DEFAULT_MENTOR_RATE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.mentorRate.currency").value(DEFAULT_MENTOR_RATE_CURRENCY.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .firstPayDate(UPDATED_FIRST_PAY_DATE)
            .nextPayDate(UPDATED_NEXT_PAY_DATE)
            .closeType(UPDATED_CLOSE_TYPE)
            .price(contract.getPrice().amount(UPDATED_PRICE_AMOUNT).currency(UPDATED_PRICE_CURRENCY))
            .mentorRate(contract.getMentorRate().amount(UPDATED_MENTOR_RATE_AMOUNT).currency(UPDATED_MENTOR_RATE_CURRENCY));
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(UPDATED_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(UPDATED_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(UPDATED_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(UPDATED_PRICE_AMOUNT, UPDATED_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(UPDATED_MENTOR_RATE_AMOUNT, UPDATED_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.price(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.getPrice().setAmount(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.getPrice().setCurrency(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.mentorRate(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.getMentorRate().setAmount(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the Contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        // set field null
        updatedContract.getMentorRate().setCurrency(null);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContract.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testContract.getFirstPayDate()).isEqualTo(DEFAULT_FIRST_PAY_DATE);
        assertThat(testContract.getNextPayDate()).isEqualTo(DEFAULT_NEXT_PAY_DATE);
        assertThat(testContract.getCloseType()).isEqualTo(DEFAULT_CLOSE_TYPE);
        assertThat(testContract.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testContract.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void updateNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Get the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = new Contract();
        contract1.setId(1L);
        Contract contract2 = new Contract();
        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);
        contract2.setId(2L);
        assertThat(contract1).isNotEqualTo(contract2);
        contract1.setId(null);
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractDTO.class);
        ContractDTO contractDTO1 = new ContractDTO();
        contractDTO1.setId(1L);
        ContractDTO contractDTO2 = new ContractDTO();
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO2.setId(contractDTO1.getId());
        assertThat(contractDTO1).isEqualTo(contractDTO2);
        contractDTO2.setId(2L);
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
        contractDTO1.setId(null);
        assertThat(contractDTO1).isNotEqualTo(contractDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contractMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contractMapper.fromId(null)).isNull();
    }    @Test
    @Transactional
    public void getAllContractsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate equals to DEFAULT_START_DATE
        defaultContractShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the contractList where startDate equals to UPDATED_START_DATE
        defaultContractShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultContractShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the contractList where startDate equals to UPDATED_START_DATE
        defaultContractShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is not null
        defaultContractShouldBeFound("startDate.specified=true");

        // Get all the contractList where startDate is null
        defaultContractShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate greater than or equals to DEFAULT_START_DATE
        defaultContractShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the contractList where startDate greater than or equals to UPDATED_START_DATE
        defaultContractShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate less than or equals to DEFAULT_START_DATE
        defaultContractShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the contractList where startDate less than or equals to UPDATED_START_DATE
        defaultContractShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }


    @Test
    @Transactional
    public void getAllContractsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate equals to DEFAULT_END_DATE
        defaultContractShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the contractList where endDate equals to UPDATED_END_DATE
        defaultContractShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultContractShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the contractList where endDate equals to UPDATED_END_DATE
        defaultContractShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is not null
        defaultContractShouldBeFound("endDate.specified=true");

        // Get all the contractList where endDate is null
        defaultContractShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate greater than or equals to DEFAULT_END_DATE
        defaultContractShouldBeFound("endDate.greaterOrEqualThan=" + DEFAULT_END_DATE);

        // Get all the contractList where endDate greater than or equals to UPDATED_END_DATE
        defaultContractShouldNotBeFound("endDate.greaterOrEqualThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate less than or equals to DEFAULT_END_DATE
        defaultContractShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the contractList where endDate less than or equals to UPDATED_END_DATE
        defaultContractShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }


    @Test
    @Transactional
    public void getAllContractsByFirstPayDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where firstPayDate equals to DEFAULT_FIRST_PAY_DATE
        defaultContractShouldBeFound("firstPayDate.equals=" + DEFAULT_FIRST_PAY_DATE);

        // Get all the contractList where firstPayDate equals to UPDATED_FIRST_PAY_DATE
        defaultContractShouldNotBeFound("firstPayDate.equals=" + UPDATED_FIRST_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByFirstPayDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where firstPayDate in DEFAULT_FIRST_PAY_DATE or UPDATED_FIRST_PAY_DATE
        defaultContractShouldBeFound("firstPayDate.in=" + DEFAULT_FIRST_PAY_DATE + "," + UPDATED_FIRST_PAY_DATE);

        // Get all the contractList where firstPayDate equals to UPDATED_FIRST_PAY_DATE
        defaultContractShouldNotBeFound("firstPayDate.in=" + UPDATED_FIRST_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByFirstPayDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where firstPayDate is not null
        defaultContractShouldBeFound("firstPayDate.specified=true");

        // Get all the contractList where firstPayDate is null
        defaultContractShouldNotBeFound("firstPayDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByFirstPayDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where firstPayDate greater than or equals to DEFAULT_FIRST_PAY_DATE
        defaultContractShouldBeFound("firstPayDate.greaterOrEqualThan=" + DEFAULT_FIRST_PAY_DATE);

        // Get all the contractList where firstPayDate greater than or equals to UPDATED_FIRST_PAY_DATE
        defaultContractShouldNotBeFound("firstPayDate.greaterOrEqualThan=" + UPDATED_FIRST_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByFirstPayDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where firstPayDate less than or equals to DEFAULT_FIRST_PAY_DATE
        defaultContractShouldNotBeFound("firstPayDate.lessThan=" + DEFAULT_FIRST_PAY_DATE);

        // Get all the contractList where firstPayDate less than or equals to UPDATED_FIRST_PAY_DATE
        defaultContractShouldBeFound("firstPayDate.lessThan=" + UPDATED_FIRST_PAY_DATE);
    }


    @Test
    @Transactional
    public void getAllContractsByNextPayDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where nextPayDate equals to DEFAULT_NEXT_PAY_DATE
        defaultContractShouldBeFound("nextPayDate.equals=" + DEFAULT_NEXT_PAY_DATE);

        // Get all the contractList where nextPayDate equals to UPDATED_NEXT_PAY_DATE
        defaultContractShouldNotBeFound("nextPayDate.equals=" + UPDATED_NEXT_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByNextPayDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where nextPayDate in DEFAULT_NEXT_PAY_DATE or UPDATED_NEXT_PAY_DATE
        defaultContractShouldBeFound("nextPayDate.in=" + DEFAULT_NEXT_PAY_DATE + "," + UPDATED_NEXT_PAY_DATE);

        // Get all the contractList where nextPayDate equals to UPDATED_NEXT_PAY_DATE
        defaultContractShouldNotBeFound("nextPayDate.in=" + UPDATED_NEXT_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByNextPayDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where nextPayDate is not null
        defaultContractShouldBeFound("nextPayDate.specified=true");

        // Get all the contractList where nextPayDate is null
        defaultContractShouldNotBeFound("nextPayDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByNextPayDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where nextPayDate greater than or equals to DEFAULT_NEXT_PAY_DATE
        defaultContractShouldBeFound("nextPayDate.greaterOrEqualThan=" + DEFAULT_NEXT_PAY_DATE);

        // Get all the contractList where nextPayDate greater than or equals to UPDATED_NEXT_PAY_DATE
        defaultContractShouldNotBeFound("nextPayDate.greaterOrEqualThan=" + UPDATED_NEXT_PAY_DATE);
    }

    @Test
    @Transactional
    public void getAllContractsByNextPayDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where nextPayDate less than or equals to DEFAULT_NEXT_PAY_DATE
        defaultContractShouldNotBeFound("nextPayDate.lessThan=" + DEFAULT_NEXT_PAY_DATE);

        // Get all the contractList where nextPayDate less than or equals to UPDATED_NEXT_PAY_DATE
        defaultContractShouldBeFound("nextPayDate.lessThan=" + UPDATED_NEXT_PAY_DATE);
    }


    @Test
    @Transactional
    public void getAllContractsByCloseTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where closeType equals to DEFAULT_CLOSE_TYPE
        defaultContractShouldBeFound("closeType.equals=" + DEFAULT_CLOSE_TYPE);

        // Get all the contractList where closeType equals to UPDATED_CLOSE_TYPE
        defaultContractShouldNotBeFound("closeType.equals=" + UPDATED_CLOSE_TYPE);
    }

    @Test
    @Transactional
    public void getAllContractsByCloseTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where closeType in DEFAULT_CLOSE_TYPE or UPDATED_CLOSE_TYPE
        defaultContractShouldBeFound("closeType.in=" + DEFAULT_CLOSE_TYPE + "," + UPDATED_CLOSE_TYPE);

        // Get all the contractList where closeType equals to UPDATED_CLOSE_TYPE
        defaultContractShouldNotBeFound("closeType.in=" + UPDATED_CLOSE_TYPE);
    }

    @Test
    @Transactional
    public void getAllContractsByCloseTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where closeType is not null
        defaultContractShouldBeFound("closeType.specified=true");

        // Get all the contractList where closeType is null
        defaultContractShouldNotBeFound("closeType.specified=false");
    }


    @Test
    @Transactional
    public void getAllContractsByMentorIsEqualToSomething() throws Exception {
        // Initialize the database
        Mentor mentor = MentorResourceIntTest.createEntity(em);
        em.persist(mentor);
        em.flush();
        contract.setMentor(mentor);
        contractRepository.saveAndFlush(contract);
        Long mentorId = mentor.getId();

        // Get all the contractList where mentor equals to mentorId
        defaultContractShouldBeFound("mentorId.equals=" + mentorId);

        // Get all the contractList where mentor equals to mentorId + 1
        defaultContractShouldNotBeFound("mentorId.equals=" + (mentorId + 1));
    }


    @Test
    @Transactional
    public void getAllContractsByAgreementIsEqualToSomething() throws Exception {
        // Initialize the database
        Agreement agreement = AgreementResourceIntTest.createEntity(em);
        em.persist(agreement);
        em.flush();
        contract.setAgreement(agreement);
        contractRepository.saveAndFlush(contract);
        Long agreementId = agreement.getId();

        // Get all the contractList where agreement equals to agreementId
        defaultContractShouldBeFound("agreementId.equals=" + agreementId);

        // Get all the contractList where agreement equals to agreementId + 1
        defaultContractShouldNotBeFound("agreementId.equals=" + (agreementId + 1));
    }

    @Test
    @Transactional
    public void shouldNotCreateActiveContractIfActiveContractAlreadyExists() throws Exception {
        // Create the Contract with an agreementId and Null closeType
        Agreement agreement = AgreementResourceIntTest.createEntity(em);
        em.persist(agreement);
        contract.setAgreement(agreement);
        contract.setCloseType(null);
        em.persist(contract);
        em.flush();
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // A new Contract created with the same agreementId and Null closeType shouldn't be saved in DB, so this API call must fail
        Contract contract = createEntity(em);
        contract.setAgreement(agreement);
        contract.setCloseType(null);
        ContractDTO contractDTO = contractMapper.toDto(contract);
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Check that the contract has not been saved.
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void whenPassContractIdAndEmailTemplateShouldSendEmailToClient() throws Exception {
        Agreement agreement = new Agreement();
        Client client = new Client();
        client.setFirstName("Йенн");
        client.setLastName("Фредерикс");
        client.setEmail("john.doe@example.com");
        em.persist(client);
        agreement.setClient(client);
        em.persist(agreement);
        Mentor mentor = new Mentor();
        mentor.setFirstName("Alex");
        mentor.setLastName("Bosch");
        em.persist(mentor);
        contract.setMentor(mentor);
        contract.setAgreement(agreement);

        // Initialize the database
        contractRepository.save(contract);

        // Get the contract
        restContractMockMvc.perform(post("/api/contracts/start-learning-email")
            .param("id", contract.getId().toString())
            .param("emailTemplate", EMAIL_TEMPLATE))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void whenPassContractIdEqualsNullAndEmailTemplateShouldNotSendEmailToClient() throws Exception {
        String id = null;

        // Get the contract
        restContractMockMvc.perform(post("/api/contracts/start-learning-email")
            .param("id", id)
            .param("emailTemplate", EMAIL_TEMPLATE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void whenPassContractIdAndEmailTemplateEqualsNullShouldSendEmailToClient() throws Exception {
        String emailTemplate = null;

        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(post("/api/contracts/start-learning-email")
            .param("id", contract.getId().toString())
            .param("emailTemplate", emailTemplate))
            .andExpect(status().isBadRequest());
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].firstPayDate").value(hasItem(DEFAULT_FIRST_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].nextPayDate").value(hasItem(DEFAULT_NEXT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].closeType").value(hasItem(DEFAULT_CLOSE_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }
}
