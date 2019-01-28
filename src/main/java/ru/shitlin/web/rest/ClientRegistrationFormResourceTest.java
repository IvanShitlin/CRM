package ru.shitlin.web.rest;

import ru.shitlin.HipsterfoxApp;
import ru.shitlin.domain.ClientRegistrationForm;
import ru.shitlin.repository.ClientRegistrationFormRepository;
import ru.shitlin.web.rest.errors.ExceptionTranslator;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shitlin.service.ClientRegistrationFormService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import java.util.List;


/**
 * Test class for the ClientRegistrationFormResource REST controller.
 *
 * @see ClientRegistrationFormResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class ClientRegistrationFormResourceTest {

    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_EMAIL = "DEFAULT@EMAIL";
    private static final String DEFAULT_PHONE = "123456789";
    private static final String DEFAULT_SKYPE = "DEFAULT_SKYPE";
    private static final String DEFAULT_COUNTRY = "DEFAULT_COUNTRY";
    private static final String DEFAULT_NOTE = "DEFAULT_NOTE";
    private static final String DEFAULT_COURSE_TYPE = "DEFAULT_COURSE_TYPE";
    private static final String DEFAULT_COURSE_NAME = "DEFAULT_COURSE_NAME";
    private static final boolean DEFAULT_LICENCE_ACCEPTED = true;

    @Autowired
    private ClientRegistrationFormService clientRegistrationFormService;

    @Autowired
    private ClientRegistrationFormRepository clientRegistrationFormRepository;

    @Autowired
    EntityManager em;

    private MockMvc restClientRegistrationFormMockMvc;

    private ClientRegistrationForm clientRegistrationForm;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

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
        final ClientRegistrationFormResource clientRegistrationFormResource = new ClientRegistrationFormResource(clientRegistrationFormService);
        this.restClientRegistrationFormMockMvc = MockMvcBuilders.standaloneSetup(clientRegistrationFormResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void createClientRegistrationForm() {
        clientRegistrationForm = createEntity(em);
    }

    @Test
    @Transactional
    public void saveClientRegistrationForm() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        ClientRegistrationForm savedRegistration = registrationList.get(registrationList.size()-1);
        assertThat(savedRegistration.getClientName()).isEqualTo(DEFAULT_NAME);
        assertThat(savedRegistration.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(savedRegistration.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(savedRegistration.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(savedRegistration.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(savedRegistration.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(savedRegistration.getCourseType()).isEqualTo(DEFAULT_COURSE_TYPE);
        assertThat(savedRegistration.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(savedRegistration.isLicenceAccepted()).isEqualTo(DEFAULT_LICENCE_ACCEPTED);
        assertThat(registrationList).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenOnlyRequiredFieldsFilled() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
        restClientRegistrationFormMockMvc.perform(post("/api/forms/course-registration")
            .param("your-name", DEFAULT_NAME)
            .param("tel-92", DEFAULT_PHONE)
            .param("your-email", DEFAULT_EMAIL)
            .param("tip-obucheniya", DEFAULT_COURSE_TYPE)
            .param("napravlenie-obuch", DEFAULT_COURSE_NAME)
            .param("acceptance-451", String.valueOf(DEFAULT_LICENCE_ACCEPTED)))
            .andExpect(status().isCreated());
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenNameIsNullExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenEmailIsNullExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenPhoneIsNullExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseTypeIsNullExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenCourseNameIsNullExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    public void saveClientRegistrationFormWhenLicenceAcceptedIsFalseExceptionThrows() throws Exception {
        int databaseSizeBeforeCreate = clientRegistrationFormRepository.findAll().size();
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
        int databaseSizeAfterCreate = clientRegistrationFormRepository.findAll().size();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }
}
