package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;
import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.repository.*;
import com.foxminded.hipsterfox.service.ClientWithAgreementCreationService;
import com.foxminded.hipsterfox.web.rest.errors.ExceptionTranslator;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.foxminded.hipsterfox.service.RegistrationFormService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.foxminded.hipsterfox.web.rest.TestUtil.createFormattingConversionService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import java.util.List;


/**
 * Test class for the ClientRegistrationFormResource REST controller.
 *
 * @see RegistrationFormResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class RegistrationFormResourceIntTest {

    private final static String FIRST_NAME = "TEST_FIRST_NAME";
    private final static String LAST_NAME = "TEST_LAST_NAME";
    private final static String DEFAULT_NAME = LAST_NAME + " " + FIRST_NAME;
    private static final String DEFAULT_EMAIL = "DEFAULT@EMAIL";
    private static final String DEFAULT_PHONE = "123456789";
    private static final String DEFAULT_SKYPE = "DEFAULT_SKYPE";
    private static final String DEFAULT_COUNTRY = "Ukraine";
    private static final String DEFAULT_NOTE = "DEFAULT_NOTE";
    private static final String DEFAULT_COURSE_TYPE = "Mentoring";
    private static final String NOT_EXISTING_COURSE_TYPE = "NOT_EXISTING_COURSE_TYPE";
    private static final String DEFAULT_COURSE_NAME = "Java";
    private static final String NOT_EXISTING_COURSE_NAME = "NOT_EXISTING_COURSE_NAME";
    private static final boolean DEFAULT_LICENCE_ACCEPTED = true;
    private static final boolean MANAGED = true;
    private static final boolean NOTMANAGED = false;

    @Autowired
    private RegistrationFormService registrationFormService;

    @Autowired
    private ClientWithAgreementCreationService clientWithAgreementCreationService;

    @Autowired
    private ClientRegistrationFormRepository clientRegistrationFormRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    EntityManager em;

    private MockMvc restClientRegistrationFormMockMvc;


    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientRegistrationForm createEntity(EntityManager em) {
        return new ClientRegistrationForm()
            .clientName(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .skype(DEFAULT_SKYPE)
            .country(DEFAULT_COUNTRY)
            .note(DEFAULT_NOTE)
            .courseType(DEFAULT_COURSE_TYPE)
            .courseName(DEFAULT_COURSE_NAME)
            .licenceAccepted(DEFAULT_LICENCE_ACCEPTED);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegistrationFormResource registrationFormResource = new RegistrationFormResource(registrationFormService);
        this.restClientRegistrationFormMockMvc = MockMvcBuilders.standaloneSetup(registrationFormResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void saveClientRegistrationForm() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isCreated());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        ClientRegistrationForm savedRegistration = registrationList.get(registrationList.size() - 1);
        Client savedClient = clients.get(clients.size() - 1);

        assertThat(savedRegistration.getClientName()).isEqualTo(DEFAULT_NAME);
        assertThat(savedRegistration.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(savedRegistration.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(savedRegistration.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(savedRegistration.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(savedRegistration.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(savedRegistration.getCourseType()).isEqualTo(DEFAULT_COURSE_TYPE);
        assertThat(savedRegistration.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(savedRegistration.isLicenceAccepted()).isEqualTo(DEFAULT_LICENCE_ACCEPTED);
        assertThat(savedRegistration.isManaged()).isEqualTo(MANAGED);
        assertThat(savedClient.getLastName()).isEqualTo(LAST_NAME);
        assertThat(savedClient.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(registrationList).hasSize(formsBeforeTest + 1);
        assertThat(agreements).hasSize(agreementsBeforeTest + 1);
        assertThat(clients).hasSize(clientsBeforeTest + 1);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenOnlyRequiredFieldsFilled() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isCreated());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        ClientRegistrationForm savedRegistration = registrationList.get(registrationList.size() - 1);

        assertThat(savedRegistration.isManaged()).isEqualTo(MANAGED);
        assertThat(registrationList).hasSize(formsBeforeTest + 1);
        assertThat(agreements).hasSize(agreementsBeforeTest + 1);
        assertThat(clients).hasSize(clientsBeforeTest + 1);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenNameIsNull() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenEmailIsNull() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenPhoneIsNull() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseTypeIsNull() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseTypeNotExists() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", NOT_EXISTING_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isCreated());

        List<ClientRegistrationForm> registrationForms = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        ClientRegistrationForm savedRegistration = registrationForms.get(registrationForms.size() - 1);

        assertThat(savedRegistration.isManaged()).isEqualTo(NOTMANAGED);
        assertThat(registrationForms).hasSize(formsBeforeTest + 1);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseNameIsNull() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseNameNotExists() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", NOT_EXISTING_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isCreated());

        List<ClientRegistrationForm> registrationForms = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        ClientRegistrationForm savedRegistration = registrationForms.get(registrationForms.size() - 1);

        assertThat(savedRegistration.isManaged()).isEqualTo(NOTMANAGED);
        assertThat(registrationForms).hasSize(formsBeforeTest + 1);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenLicenceAcceptedIsFalse() throws Exception {
        int formsBeforeTest = clientRegistrationFormRepository.findAll().size();
        int clientsBeforeTest = clientRepository.findAll().size();
        int agreementsBeforeTest = agreementRepository.findAll().size();

        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("your-skype", DEFAULT_SKYPE)
            .param("your-country", DEFAULT_COUNTRY)
            .param("your-note", DEFAULT_NOTE)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(!DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isBadRequest());

        List<ClientRegistrationForm> registrationList = clientRegistrationFormRepository.findAll();
        List<Agreement> agreements = agreementRepository.findAll();
        List<Client> clients = clientRepository.findAll();

        assertThat(registrationList).hasSize(formsBeforeTest);
        assertThat(agreements).hasSize(agreementsBeforeTest);
        assertThat(clients).hasSize(clientsBeforeTest);
    }

}
