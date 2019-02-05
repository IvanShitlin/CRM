package com.foxminded.hipsterfox.domain;

import com.foxminded.hipsterfox.HipsterfoxApp;
import com.foxminded.hipsterfox.repository.CourseRepository;
import com.foxminded.hipsterfox.repository.MentorRepository;
import com.foxminded.hipsterfox.web.rest.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class MentorTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String DEFAULT_EMAIL_TEMPLATE = "BBBBB";

    private static final String DEFAUTL_CITY = "defCity";
    private static final String DEFAUTL_COUNTRY = "defCountry";
    private static final String DEFAUTL_EMAIL = "def@email";
    private static final String DEFAUTL_FIRST_NAME = "defFirst";
    private static final String DEFAUTL_LAST_NAME = "defLast";
    private static final Long DEFAUTL_MAX_STUDENTS = 1L;
    private static final String DEFAUTL_PATRONYMIC = "defPatron";
    private static final String DEFAUTL_PHONE = "123456789";
    private static final String DEFAUTL_SKYPE = "defSkype";

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    private Mentor mentor;

    @Before
    public void setup() {
        course = new Course()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .emailTemplate(DEFAULT_EMAIL_TEMPLATE);

        mentor = new Mentor()
            .city(DEFAUTL_CITY)
            .country(DEFAUTL_COUNTRY)
            .email(DEFAUTL_EMAIL)
            .firstName(DEFAUTL_FIRST_NAME)
            .lastName(DEFAUTL_LAST_NAME)
            .maxStudents(DEFAUTL_MAX_STUDENTS)
            .patronymic(DEFAUTL_PATRONYMIC)
            .phone(DEFAUTL_PHONE)
            .skype(DEFAUTL_SKYPE);
    }

    @Test
    @Transactional
    public void addCourse() {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        mentorRepository.saveAndFlush(mentor);

        int mentorsCount = course.getMentors().size();
        int coursesCount = mentor.getCourses().size();

        // When adding a mentor to a course, the course have to be added to the mentor also.
        assertThat(course.getMentors()).hasSize(mentorsCount);
        assertThat(mentor.getCourses()).hasSize(coursesCount);
        mentor.addCourse(course);
        assertThat(course.getMentors()).hasSize(++mentorsCount);
        assertThat(mentor.getCourses()).hasSize(++coursesCount);
    }

    @Test
    @Transactional
    public void removeCourse() {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        mentorRepository.saveAndFlush(mentor);

        course.addMentor(mentor);
        int mentorsCount = course.getMentors().size();
        int coursesCount = mentor.getCourses().size();

        // When removing mentor from course, the course have to be removed form the mentor also.
        assertThat(course.getMentors()).hasSize(mentorsCount);
        assertThat(mentor.getCourses()).hasSize(coursesCount);
        mentor.removeCourse(course);
        assertThat(course.getMentors()).hasSize(--mentorsCount);
        assertThat(mentor.getCourses()).hasSize(--coursesCount);
    }
}
