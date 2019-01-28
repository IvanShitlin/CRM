package ru.shitlin.web.rest.view;

import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Invoice;
import ru.shitlin.domain.Payment;
import ru.shitlin.domain.enumeration.CloseType;
import ru.shitlin.domain.view.DebtorView;
import ru.shitlin.repository.view.DebtorViewRepository;
import ru.shitlin.service.view.mapper.DebtorViewMapper;
import ru.shitlin.service.view.service.DebtorViewService;
import ru.shitlin.web.rest.ContractResourceIntTest;
import ru.shitlin.web.rest.InvoiceResourceIntTest;
import ru.shitlin.web.rest.PaymentResourceIntTest;
import ru.shitlin.web.rest.TestUtil;
import ru.shitlin.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ru.shitlin.web.rest.TestUtil.createFormattingConversionService;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DebtorViewResourceIntTest {

    @Autowired
    private DebtorViewRepository debtorViewRepository;

    @Autowired
    private DebtorViewMapper debtorViewMapper;

    @Autowired
    private DebtorViewService debtorViewService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMentorMockMvc;

    private static LocalDate getDateByInterval(int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return convertToLocalDateViaMillisecond(calendar.getTime());
    }

    private static LocalDate convertToLocalDateViaMillisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DebtorViewResource debtorViewResource = new DebtorViewResource(debtorViewService);
        this.restMentorMockMvc = MockMvcBuilders.standaloneSetup(debtorViewResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void checkClientWithUnpaidInvoiceAndActiveContractIsDebtor() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice
            .payment(null)
            .contract(activeContract);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(unpaidInvoice);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtors = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest + 1, debtors.size());

        DebtorView debtor = debtors.get(0);
        assertEquals(debtor.getContract(), activeContract);
        assertEquals(debtor.getInvoice(), unpaidInvoice);
    }

    @Test
    @Transactional
    public void checkClientWithPaidInvoiceIsNotDebtor() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);

        Invoice paidInvoice = InvoiceResourceIntTest.createEntity(em);
        paidInvoice.setContract(activeContract);

        Payment payment = PaymentResourceIntTest.createEntity(em);
        payment.setInvoice(paidInvoice);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(paidInvoice);
        em.persist(payment);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtors = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest, debtors.size());
    }

    @Test
    @Transactional
    public void checkClientWithNotActiveContractIsNotDebtor() throws Exception {
        Contract notActiveContract = ContractResourceIntTest.createEntity(em);
        notActiveContract.setCloseType(CloseType.COMPLETED);

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice.setContract(notActiveContract);
        unpaidInvoice.setPayment(null);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(notActiveContract);
        em.persist(unpaidInvoice);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtors = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest, debtors.size());
    }

    @Test
    @Transactional
    public void checkClientWithActiveContractAndDifferentInvoices() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);

        Invoice paidInvoice = InvoiceResourceIntTest.createEntity(em);
        paidInvoice.setContract(activeContract);

        Payment payment = PaymentResourceIntTest.createEntity(em);
        payment.setInvoice(paidInvoice);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(paidInvoice);
        em.persist(payment);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> emptyList = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest, emptyList.size());

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice
            .payment(null)
            .contract(activeContract);

        em.persist(unpaidInvoice);
        em.flush();

        List<DebtorView> debtorList = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest + 1, debtorList.size());

        DebtorView debtor = debtorList.get(0);
        assertEquals(debtor.getInvoice(), unpaidInvoice);
        assertEquals(debtor.getContract(), activeContract);
    }

    @Test
    @Transactional
    public void checkClientWithInvoiceDatePaymentIsTodayIsNotDebtor() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);
        activeContract.setNextPayDate(getDateByInterval(0));

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice.setContract(activeContract);
        unpaidInvoice.setDateFrom(getDateByInterval(0));
        unpaidInvoice.setPayment(null);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(unpaidInvoice);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtors = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest, debtors.size());
    }

    @Test
    @Transactional
    public void checkClientWithInvoiceDatePaymentIsYesterdayIsDebtor() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);
        activeContract.setNextPayDate(getDateByInterval(-1));

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice.setContract(activeContract);
        unpaidInvoice.setPayment(null);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(unpaidInvoice);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtorList = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest + 1, debtorList.size());

        DebtorView debtor = debtorList.get(0);
        assertEquals(debtor.getInvoice(), unpaidInvoice);
        assertEquals(debtor.getContract(), activeContract);

    }

    @Test
    @Transactional
    public void checkClientWithInvoiceDatePaymentIsTomorrowIsNotDebtor() throws Exception {
        Contract activeContract = ContractResourceIntTest.createEntity(em);
        activeContract.setCloseType(null);
        activeContract.setNextPayDate(getDateByInterval(+1));

        Invoice unpaidInvoice = InvoiceResourceIntTest.createEntity(em);
        unpaidInvoice.setContract(activeContract);
        unpaidInvoice.setPayment(null);

        int debtorsInDatabaseBeforeTest = debtorViewRepository.findAll().size();

        em.persist(activeContract);
        em.persist(unpaidInvoice);
        em.flush();

        restMentorMockMvc.perform(get("/api/debtors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        List<DebtorView> debtors = debtorViewRepository.findAll();
        assertEquals(debtorsInDatabaseBeforeTest, debtors.size());
    }

}
