package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;
import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.repository.ClientRepository;
import com.foxminded.hipsterfox.service.ClientQueryService;
import com.foxminded.hipsterfox.service.ClientService;
import com.foxminded.hipsterfox.service.dto.ClientDTO;
import com.foxminded.hipsterfox.service.mapper.ClientMapper;
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
import static org.assertj.core.api.Assertions.linesOf;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class ClientResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRONYMIC = "AAAAAAAAAA";
    private static final String UPDATED_PATRONYMIC = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA@AAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB@BBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA@AAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB@BBBBBBBBB";

    private static final String DEFAULT_SKYPE = "AAAAAAAAAA";
    private static final String UPDATED_SKYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientQueryService clientQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private Client client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientResource clientResource = new ClientResource(clientService, clientQueryService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .patronymic(DEFAULT_PATRONYMIC)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .skype(DEFAULT_SKYPE)
            .country(DEFAULT_COUNTRY)
            .city(DEFAULT_CITY)
            .experience(DEFAULT_EXPERIENCE)
            .note(DEFAULT_NOTE);
        return client;
    }

    @Before
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getPatronymic()).isEqualTo(DEFAULT_PATRONYMIC);
        assertThat(testClient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(testClient.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testClient.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testClient.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testClient.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setFirstName(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setLastName(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setEmail(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailDomainEndsOnDotWhenCreatingAndBadRequestIsExpected() throws Exception{
        client.setEmail("AAAAAAAA@DOMAINNAME.");
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEmailWithoutDomainWhenCreatingAndBadRequestIsExpected() throws Exception{
        client.setEmail("AAAAAAAAA@");
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEmailWithLocalPartOnlyWhenCreatingAndBadRequestIsExpected() throws Exception{
        client.setEmail("AAAAAAAAA");
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEmailDomainEndsOnDotWhenUpdatingAndBadRequestIsExpected() throws Exception{
        clientRepository.saveAndFlush(client);

        Client updateClient = clientRepository.findById(client.getId()).get();
        updateClient.setEmail("AAAAAAAA@DOMAINNAME.");

        ClientDTO clientDTO = clientMapper.toDto(updateClient);
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEmailWithoutDomainWhenUpdatingAndBadRequestIsExpected() throws Exception{
        clientRepository.saveAndFlush(client);

        Client updateClient = clientRepository.findById(client.getId()).get();
        updateClient.setEmail("AAAAAAAAA@");

        ClientDTO clientDTO = clientMapper.toDto(updateClient);
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEmailWithLocalPartOnlyWhenUpdatingAndBadRequestIsExpected() throws Exception{
        clientRepository.saveAndFlush(client);

        Client updateClient = clientRepository.findById(client.getId()).get();
        updateClient.setEmail("AAAAAAAAA");

        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getAllClientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName equals to DEFAULT_FIRST_NAME
        defaultClientShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the clientList where firstName equals to UPDATED_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultClientShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the clientList where firstName equals to UPDATED_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName is not null
        defaultClientShouldBeFound("firstName.specified=true");

        // Get all the clientList where firstName is null
        defaultClientShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName equals to DEFAULT_LAST_NAME
        defaultClientShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the clientList where lastName equals to UPDATED_LAST_NAME
        defaultClientShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultClientShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the clientList where lastName equals to UPDATED_LAST_NAME
        defaultClientShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllClientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName is not null
        defaultClientShouldBeFound("lastName.specified=true");

        // Get all the clientList where lastName is null
        defaultClientShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByPatronymicIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where patronymic equals to DEFAULT_PATRONYMIC
        defaultClientShouldBeFound("patronymic.equals=" + DEFAULT_PATRONYMIC);

        // Get all the clientList where patronymic equals to UPDATED_PATRONYMIC
        defaultClientShouldNotBeFound("patronymic.equals=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllClientsByPatronymicIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where patronymic in DEFAULT_PATRONYMIC or UPDATED_PATRONYMIC
        defaultClientShouldBeFound("patronymic.in=" + DEFAULT_PATRONYMIC + "," + UPDATED_PATRONYMIC);

        // Get all the clientList where patronymic equals to UPDATED_PATRONYMIC
        defaultClientShouldNotBeFound("patronymic.in=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllClientsByPatronymicIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where patronymic is not null
        defaultClientShouldBeFound("patronymic.specified=true");

        // Get all the clientList where patronymic is null
        defaultClientShouldNotBeFound("patronymic.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone equals to DEFAULT_PHONE
        defaultClientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultClientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone is not null
        defaultClientShouldBeFound("phone.specified=true");

        // Get all the clientList where phone is null
        defaultClientShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to DEFAULT_EMAIL
        defaultClientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientShouldBeFound("email.specified=true");

        // Get all the clientList where email is null
        defaultClientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsBySkypeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where skype equals to DEFAULT_SKYPE
        defaultClientShouldBeFound("skype.equals=" + DEFAULT_SKYPE);

        // Get all the clientList where skype equals to UPDATED_SKYPE
        defaultClientShouldNotBeFound("skype.equals=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllClientsBySkypeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where skype in DEFAULT_SKYPE or UPDATED_SKYPE
        defaultClientShouldBeFound("skype.in=" + DEFAULT_SKYPE + "," + UPDATED_SKYPE);

        // Get all the clientList where skype equals to UPDATED_SKYPE
        defaultClientShouldNotBeFound("skype.in=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllClientsBySkypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where skype is not null
        defaultClientShouldBeFound("skype.specified=true");

        // Get all the clientList where skype is null
        defaultClientShouldNotBeFound("skype.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country equals to DEFAULT_COUNTRY
        defaultClientShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the clientList where country equals to UPDATED_COUNTRY
        defaultClientShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllClientsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultClientShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the clientList where country equals to UPDATED_COUNTRY
        defaultClientShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllClientsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country is not null
        defaultClientShouldBeFound("country.specified=true");

        // Get all the clientList where country is null
        defaultClientShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where city equals to DEFAULT_CITY
        defaultClientShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the clientList where city equals to UPDATED_CITY
        defaultClientShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllClientsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where city in DEFAULT_CITY or UPDATED_CITY
        defaultClientShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the clientList where city equals to UPDATED_CITY
        defaultClientShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllClientsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where city is not null
        defaultClientShouldBeFound("city.specified=true");

        // Get all the clientList where city is null
        defaultClientShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where experience equals to DEFAULT_EXPERIENCE
        defaultClientShouldBeFound("experience.equals=" + DEFAULT_EXPERIENCE);

        // Get all the clientList where experience equals to UPDATED_EXPERIENCE
        defaultClientShouldNotBeFound("experience.equals=" + UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void getAllClientsByExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where experience in DEFAULT_EXPERIENCE or UPDATED_EXPERIENCE
        defaultClientShouldBeFound("experience.in=" + DEFAULT_EXPERIENCE + "," + UPDATED_EXPERIENCE);

        // Get all the clientList where experience equals to UPDATED_EXPERIENCE
        defaultClientShouldNotBeFound("experience.in=" + UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void getAllClientsByExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where experience is not null
        defaultClientShouldBeFound("experience.specified=true");

        // Get all the clientList where experience is null
        defaultClientShouldNotBeFound("experience.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where note equals to DEFAULT_NOTE
        defaultClientShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the clientList where note equals to UPDATED_NOTE
        defaultClientShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllClientsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultClientShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the clientList where note equals to UPDATED_NOTE
        defaultClientShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllClientsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where note is not null
        defaultClientShouldBeFound("note.specified=true");

        // Get all the clientList where note is null
        defaultClientShouldNotBeFound("note.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.patronymic").value(DEFAULT_PATRONYMIC.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.skype").value(DEFAULT_SKYPE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .patronymic(UPDATED_PATRONYMIC)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .skype(UPDATED_SKYPE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .experience(UPDATED_EXPERIENCE)
            .note(UPDATED_NOTE);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getSkype()).isEqualTo(UPDATED_SKYPE);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testClient.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testClient.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = new Client();
        client1.setId(1L);
        Client client2 = new Client();
        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);
        client2.setId(2L);
        assertThat(client1).isNotEqualTo(client2);
        client1.setId(null);
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientDTO.class);
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setId(1L);
        ClientDTO clientDTO2 = new ClientDTO();
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO2.setId(clientDTO1.getId());
        assertThat(clientDTO1).isEqualTo(clientDTO2);
        clientDTO2.setId(2L);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO1.setId(null);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientMapper.fromId(null)).isNull();
    }
}
