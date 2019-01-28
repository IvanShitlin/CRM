package ru.shitlin.web.rest;

import ru.shitlin.HipsterfoxApp;
import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Money;
import ru.shitlin.domain.enumeration.Currency;
import ru.shitlin.repository.ContractRepository;
import ru.shitlin.repository.InvoiceRepository;
import ru.shitlin.service.InvoiceService;
import ru.shitlin.service.dto.InvoiceDTO;
import ru.shitlin.service.mapper.InvoiceMapper;
import ru.shitlin.web.rest.errors.ExceptionTranslator;
import ru.shitlin.service.InvoiceQueryService;
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
import java.time.Month;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class InvoiceResourceIntTest {

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_TO = DEFAULT_DATE_FROM.plusMonths(1).minusDays(1);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_AMOUNT = 3000L;
    private static final Currency DEFAULT_CURRENCY = Currency.UAH;

    private static final Long UPDATED_AMOUNT = 120L;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final LocalDate CONTRACT_DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L).plusMonths(1);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .sum(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);

        // Create the Invoice
        invoice.setContract(contract);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testInvoice.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testInvoice.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
        assertThat(testInvoice.getContract().getNextPayDate()).isEqualTo(CONTRACT_DEFAULT_DATE_TO);
    }

    @Test
    @Transactional
    public void createWhenDateFirstContractPayAreLastDays() throws Exception {
        Contract contract = ContractResourceIntTest.createEntity(em);
        contract.setFirstPayDate(LocalDate.of(1970, 1, 31));
        contractRepository.saveAndFlush(contract);

        // Create the Invoice
        invoice.setContract(contract);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getContract().getNextPayDate()).isEqualTo(LocalDate.of(1970, 2, 28));

        // Create second Invoice for checking the payment date
        invoice.setDateFrom(invoice.getDateFrom().plusMonths(1));
        invoice.setDateTo(invoice.getDateTo().plusMonths(1));
        InvoiceDTO invoiceDTO1 = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO1)))
            .andExpect(status().isCreated());

        Invoice testInvoice1 = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice1.getContract().getNextPayDate()).isEqualTo(LocalDate.of(1970, 3, 31));
    }

    @Test
    @Transactional
    public void saveFailWhenDateFromOverlapOneDayThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.setDateFrom(LocalDate.of(2018, Month.SEPTEMBER,15));
        invoice.setDateTo(LocalDate.of(2018, Month.OCTOBER,15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER,15));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER,15));
        overlappingInvoice.setContract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenDateFromOverlapSeveralDaysThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.setDateFrom(LocalDate.of(2018, Month.SEPTEMBER,15));
        invoice.setDateTo(LocalDate.of(2018, Month.OCTOBER,15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER,5));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER,5));
        overlappingInvoice.setContract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenDateToOverlapOneDayThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 15));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 15));
        overlappingInvoice.setContract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenDateToOverlapSeveralDaysThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 25));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 25));
        overlappingInvoice.setContract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenDatesTotallyOverlapThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 15));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER, 15));
        overlappingInvoice.contract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenInvoicePeriodIsInExisting() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of overlapping invoice with the same contract
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.SEPTEMBER, 16));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.OCTOBER, 14));
        overlappingInvoice.contract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Expecting that overlapping invoice did not saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveWhenDateToIsPreviousOfExisting() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of invoice with the same contract in existing
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 15));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 14));
        overlappingInvoice.contract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Expecting that the invoice is saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isLessThan(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveWhenDateFromIsNextToExistingPeriod() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of invoice with the same contract in existing
        Invoice overlappingInvoice = InvoiceResourceIntTest.createEntity(em);
        overlappingInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER, 16));
        overlappingInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER, 16));
        overlappingInvoice.contract(contract);

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(overlappingInvoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Expecting that the invoice is saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isLessThan(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void saveFailWhenUpdatingInvoiceDateToOverlapOneDayThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        // Initialization of an invoice with the same contract with earlier period
        Invoice earlierInvoice = InvoiceResourceIntTest.createEntity(em);
        earlierInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 15));
        earlierInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 14));
        earlierInvoice.contract(contract);
        invoiceRepository.saveAndFlush(earlierInvoice);

        Invoice updatedInvoice = invoiceRepository.findById(earlierInvoice.getId()).get();

        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 15));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void saveFailWhenUpdatingInvoiceDateToOverlapSeveralDaysThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        // Initialization of an invoice with the same contract with earlier period
        Invoice earlierInvoice = InvoiceResourceIntTest.createEntity(em);
        earlierInvoice.setDateFrom(LocalDate.of(2018, Month.AUGUST, 15));
        earlierInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 14));
        earlierInvoice.contract(contract);
        invoiceRepository.saveAndFlush(earlierInvoice);

        Invoice updatedInvoice = invoiceRepository.findById(earlierInvoice.getId()).get();

        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice.setDateTo(LocalDate.of(2018, Month.SEPTEMBER, 25));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void saveFailWhenUpdatingInvoiceDateFromOverlapOneDayThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        // Initialization of an invoice with the same contract with earlier period
        Invoice earlierInvoice = InvoiceResourceIntTest.createEntity(em);
        earlierInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER, 16));
        earlierInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER, 15));
        earlierInvoice.contract(contract);
        invoiceRepository.saveAndFlush(earlierInvoice);

        Invoice updatedInvoice = invoiceRepository.findById(earlierInvoice.getId()).get();

        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER, 15));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void saveFailWhenUpdatingInvoiceDateFromOverlapSeveralDaysThrowException() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        // Initialization of an invoice with the same contract with earlier period
        Invoice earlierInvoice = InvoiceResourceIntTest.createEntity(em);
        earlierInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER, 16));
        earlierInvoice.setDateTo(LocalDate.of(2018, Month.NOVEMBER, 15));
        earlierInvoice.contract(contract);
        invoiceRepository.saveAndFlush(earlierInvoice);

        Invoice updatedInvoice = invoiceRepository.findById(earlierInvoice.getId()).get();

        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice.setDateFrom(LocalDate.of(2018, Month.OCTOBER, 5));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSumIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSum(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.getSum().setAmount(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.getSum().setCurrency(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].sum.amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].sum.currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
            .andExpect(jsonPath("$.sum.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.sum.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateFromIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateFrom equals to DEFAULT_DATE_FROM
        defaultInvoiceShouldBeFound("dateFrom.equals=" + DEFAULT_DATE_FROM);

        // Get all the invoiceList where dateFrom equals to UPDATED_DATE_FROM
        defaultInvoiceShouldNotBeFound("dateFrom.equals=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateFromIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateFrom in DEFAULT_DATE_FROM or UPDATED_DATE_FROM
        defaultInvoiceShouldBeFound("dateFrom.in=" + DEFAULT_DATE_FROM + "," + UPDATED_DATE_FROM);

        // Get all the invoiceList where dateFrom equals to UPDATED_DATE_FROM
        defaultInvoiceShouldNotBeFound("dateFrom.in=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateFrom is not null
        defaultInvoiceShouldBeFound("dateFrom.specified=true");

        // Get all the invoiceList where dateFrom is null
        defaultInvoiceShouldNotBeFound("dateFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateFrom greater than or equals to DEFAULT_DATE_FROM
        defaultInvoiceShouldBeFound("dateFrom.greaterOrEqualThan=" + DEFAULT_DATE_FROM);

        // Get all the invoiceList where dateFrom greater than or equals to UPDATED_DATE_FROM
        defaultInvoiceShouldNotBeFound("dateFrom.greaterOrEqualThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateFromIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateFrom less than or equals to DEFAULT_DATE_FROM
        defaultInvoiceShouldNotBeFound("dateFrom.lessThan=" + DEFAULT_DATE_FROM);

        // Get all the invoiceList where dateFrom less than or equals to UPDATED_DATE_FROM
        defaultInvoiceShouldBeFound("dateFrom.lessThan=" + UPDATED_DATE_FROM);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateToIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateTo equals to DEFAULT_DATE_TO
        defaultInvoiceShouldBeFound("dateTo.equals=" + DEFAULT_DATE_TO);

        // Get all the invoiceList where dateTo equals to UPDATED_DATE_TO
        defaultInvoiceShouldNotBeFound("dateTo.equals=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateToIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateTo in DEFAULT_DATE_TO or UPDATED_DATE_TO
        defaultInvoiceShouldBeFound("dateTo.in=" + DEFAULT_DATE_TO + "," + UPDATED_DATE_TO);

        // Get all the invoiceList where dateTo equals to UPDATED_DATE_TO
        defaultInvoiceShouldNotBeFound("dateTo.in=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateToIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateTo is not null
        defaultInvoiceShouldBeFound("dateTo.specified=true");

        // Get all the invoiceList where dateTo is null
        defaultInvoiceShouldNotBeFound("dateTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateTo greater than or equals to DEFAULT_DATE_TO
        defaultInvoiceShouldBeFound("dateTo.greaterOrEqualThan=" + DEFAULT_DATE_TO);

        // Get all the invoiceList where dateTo greater than or equals to UPDATED_DATE_TO
        defaultInvoiceShouldNotBeFound("dateTo.greaterOrEqualThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateToIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateTo less than or equals to DEFAULT_DATE_TO
        defaultInvoiceShouldNotBeFound("dateTo.lessThan=" + DEFAULT_DATE_TO);

        // Get all the invoiceList where dateTo less than or equals to UPDATED_DATE_TO
        defaultInvoiceShouldBeFound("dateTo.lessThan=" + UPDATED_DATE_TO);
    }

    @Test
    @Transactional
    public void getAllInvoicesByContractIsEqualToSomething() throws Exception {
        // Initialize the database
        Contract contract = ContractResourceIntTest.createEntity(em);
        em.persist(contract);
        em.flush();
        invoice.setContract(contract);
        invoiceRepository.saveAndFlush(invoice);
        Long contractId = contract.getId();

        // Get all the invoiceList where contract equals to contractId
        defaultInvoiceShouldBeFound("contractId.equals=" + contractId);

        // Get all the invoiceList where contract equals to contractId + 1
        defaultInvoiceShouldNotBeFound("contractId.equals=" + (contractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .sum(invoice.getSum().amount(UPDATED_AMOUNT).currency(UPDATED_CURRENCY));
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testInvoice.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testInvoice.getSum()).isEqualTo(new Money(UPDATED_AMOUNT, UPDATED_CURRENCY));
    }

    @Test
    @Transactional
    public void UpdateInvoiceWhenDateFromIsChangedRight() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of invoice with the same contract in existing
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        updatedInvoice.setDateFrom(LocalDate.of(2018, Month.SEPTEMBER, 14));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Expecting that the invoice is saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void UpdateInvoiceWhenDateToIsChangedRight() throws Exception {
        // Initialization of existing invoice with contract
        Contract contract = ContractResourceIntTest.createEntity(em);
        contractRepository.saveAndFlush(contract);
        invoice.setContract(contract);
        invoice.dateFrom(LocalDate.of(2018, Month.SEPTEMBER, 15))
            .dateTo(LocalDate.of(2018, Month.OCTOBER, 15));
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeTest = invoiceRepository.findAll().size();

        // Initialization of invoice with the same contract in existing
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        updatedInvoice.setDateTo(LocalDate.of(2018, Month.OCTOBER, 16));

        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Expecting that the invoice is saved
        int databaseSizeAfterTest = invoiceRepository.findAll().size();
        assertThat(databaseSizeBeforeTest).isEqualTo(databaseSizeAfterTest);
    }

    @Test
    @Transactional
    public void checkSumIsRequiredForUpdate() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        // set field null
        updatedInvoice.sum(null);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testInvoice.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testInvoice.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        // set field null
        updatedInvoice.getSum().setAmount(null);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testInvoice.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testInvoice.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void checkSumCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        // set field null
        updatedInvoice.getSum().setCurrency(null);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testInvoice.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testInvoice.getSum()).isEqualTo(new Money(DEFAULT_AMOUNT, DEFAULT_CURRENCY));
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}
