package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;

import com.foxminded.hipsterfox.domain.CourseType;
import com.foxminded.hipsterfox.domain.Money;
import com.foxminded.hipsterfox.domain.enumeration.Currency;
import com.foxminded.hipsterfox.repository.CourseTypeRepository;
import com.foxminded.hipsterfox.service.CourseTypeService;
import com.foxminded.hipsterfox.service.dto.CourseTypeDTO;
import com.foxminded.hipsterfox.service.mapper.CourseTypeMapper;
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
 * Test class for the CourseTypeResource REST controller.
 *
 * @see CourseTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class CourseTypeResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final Long DEFAULT_PRICE_AMOUNT = 3000L;
    private static final Currency DEFAULT_PRICE_CURRENCY = Currency.UAH;

    private static final Long UPDATED_PRICE_AMOUNT = 120L;
    private static final Currency UPDATED_PRICE_CURRENCY = Currency.USD;

    private static final Long DEFAULT_MENTOR_RATE_AMOUNT = 1500L;
    private static final Currency DEFAULT_MENTOR_RATE_CURRENCY = Currency.UAH;

    private static final Long UPDATED_MENTOR_RATE_AMOUNT = 60L;
    private static final Currency UPDATED_MENTOR_RATE_CURRENCY = Currency.USD;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private CourseTypeMapper courseTypeMapper;

    @Autowired
    private CourseTypeService courseTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseTypeMockMvc;

    private CourseType courseType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseTypeResource courseTypeResource = new CourseTypeResource(courseTypeService);
        this.restCourseTypeMockMvc = MockMvcBuilders.standaloneSetup(courseTypeResource)
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
    public static CourseType createEntity(EntityManager em) {
        CourseType courseType = new CourseType()
            .type(DEFAULT_TYPE)
            .location(DEFAULT_LOCATION)
            .price(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY))
            .mentorRate(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
        return courseType;
    }

    @Before
    public void initTest() {
        courseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseType() throws Exception {
        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Create the CourseType
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);
        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void createCourseTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Create the CourseType with an existing ID
        courseType.setId(1L);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.setPrice(null);

        // Create the CourseType, which fails.
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.getPrice().setAmount(null);

        // Create the Course, which fails.
        CourseTypeDTO courseDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.getPrice().setCurrency(null);

        // Create the Course, which fails.
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.setMentorRate(null);

        // Create the CourseType, which fails.
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.getMentorRate().setAmount(null);

        // Create the Course, which fails.
        CourseTypeDTO courseDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMentorRateCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseTypeRepository.findAll().size();
        // set the field null
        courseType.getMentorRate().setCurrency(null);

        // Create the Course, which fails.
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourseTypes() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        // Get all the courseTypeList
        restCourseTypeMockMvc.perform(get("/api/course-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getCourseType() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        // Get the courseType
        restCourseTypeMockMvc.perform(get("/api/course-types/{id}", courseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseType() throws Exception {
        // Get the courseType
        restCourseTypeMockMvc.perform(get("/api/course-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseType() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeUpdate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        updatedCourseType
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION)
            .price(new Money(UPDATED_PRICE_AMOUNT, UPDATED_PRICE_CURRENCY))
            .mentorRate(new Money(UPDATED_MENTOR_RATE_AMOUNT, UPDATED_MENTOR_RATE_CURRENCY));
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(put("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeUpdate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(UPDATED_PRICE_AMOUNT, UPDATED_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(UPDATED_MENTOR_RATE_AMOUNT, UPDATED_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.mentorRate(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.getMentorRate().setAmount(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkMentorRateCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.getMentorRate().setCurrency(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.price(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceAmountIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.getPrice().setAmount(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void checkPriceCurrencyIsRequiredForUpdate() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeCreate = courseTypeRepository.findAll().size();

        // Update the courseType
        CourseType updatedCourseType = courseTypeRepository.findById(courseType.getId()).get();
        // Disconnect from session so that the updates on updatedCourseType are not directly saved in db
        em.detach(updatedCourseType);
        // set field null
        updatedCourseType.getPrice().setCurrency(null);
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(updatedCourseType);

        restCourseTypeMockMvc.perform(post("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();

        assertThat(courseTypeList).hasSize(databaseSizeBeforeCreate);
        CourseType testCourseType = courseTypeList.get(courseTypeList.size() - 1);
        assertThat(testCourseType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourseType.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourseType.getPrice()).isEqualTo(new Money(DEFAULT_PRICE_AMOUNT, DEFAULT_PRICE_CURRENCY));
        assertThat(testCourseType.getMentorRate()).isEqualTo(new Money(DEFAULT_MENTOR_RATE_AMOUNT, DEFAULT_MENTOR_RATE_CURRENCY));
    }

    @Test
    @Transactional
    public void updateNonExistingCourseType() throws Exception {
        int databaseSizeBeforeUpdate = courseTypeRepository.findAll().size();

        // Create the CourseType
        CourseTypeDTO courseTypeDTO = courseTypeMapper.toDto(courseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseTypeMockMvc.perform(put("/api/course-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseType in the database
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseType() throws Exception {
        // Initialize the database
        courseTypeRepository.saveAndFlush(courseType);

        int databaseSizeBeforeDelete = courseTypeRepository.findAll().size();

        // Get the courseType
        restCourseTypeMockMvc.perform(delete("/api/course-types/{id}", courseType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseType> courseTypeList = courseTypeRepository.findAll();
        assertThat(courseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseType.class);
        CourseType courseType1 = new CourseType();
        courseType1.setId(1L);
        CourseType courseType2 = new CourseType();
        courseType2.setId(courseType1.getId());
        assertThat(courseType1).isEqualTo(courseType2);
        courseType2.setId(2L);
        assertThat(courseType1).isNotEqualTo(courseType2);
        courseType1.setId(null);
        assertThat(courseType1).isNotEqualTo(courseType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseTypeDTO.class);
        CourseTypeDTO courseTypeDTO1 = new CourseTypeDTO();
        courseTypeDTO1.setId(1L);
        CourseTypeDTO courseTypeDTO2 = new CourseTypeDTO();
        assertThat(courseTypeDTO1).isNotEqualTo(courseTypeDTO2);
        courseTypeDTO2.setId(courseTypeDTO1.getId());
        assertThat(courseTypeDTO1).isEqualTo(courseTypeDTO2);
        courseTypeDTO2.setId(2L);
        assertThat(courseTypeDTO1).isNotEqualTo(courseTypeDTO2);
        courseTypeDTO1.setId(null);
        assertThat(courseTypeDTO1).isNotEqualTo(courseTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(courseTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(courseTypeMapper.fromId(null)).isNull();
    }
}
