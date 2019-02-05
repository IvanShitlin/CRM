package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.domain.Course;
import com.foxminded.hipsterfox.domain.CourseType;
import com.foxminded.hipsterfox.domain.enumeration.AgreementStatus;
import com.foxminded.hipsterfox.repository.CourseRepository;
import com.foxminded.hipsterfox.repository.CourseTypeRepository;
import com.foxminded.hipsterfox.service.errors.RegistrationFormException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RegistrationFormException.class)
public class ClientWithAgreementCreationService {

    private final Logger log = LoggerFactory.getLogger(ClientWithAgreementCreationService.class);

    private final static String DEFAULT_FIRST_NAME = "Noname";

    private final static String DEFAULT_LAST_NAME = "Doe";

    private final static String DEFAULT_COUNTRY = "Ukraine";

    private final ClientService clientService;

    private final AgreementService agreementService;

    private final CourseRepository courseRepository;

    private final CourseTypeRepository courseTypeRepository;

    public ClientWithAgreementCreationService(ClientService clientService,
                                              AgreementService agreementService,
                                              CourseRepository courseRepository,
                                              CourseTypeRepository courseTypeRepository) {
        this.clientService = clientService;
        this.agreementService = agreementService;
        this.courseRepository = courseRepository;
        this.courseTypeRepository = courseTypeRepository;
    }

    public void create(Map<String, String> registrationParameters) {
        Client client = createClient(registrationParameters);
        createAgreement(registrationParameters, client);
    }

    private Client createClient(Map<String, String> registrationParameters) {
        Client clientFromAmocrm = new Client();
        Pair<String, String> clientName = parseName(registrationParameters.get("clientName"));
        clientFromAmocrm
            .firstName(clientName.getFirst())
            .lastName(clientName.getSecond())
            .email(registrationParameters.get("email"))
            .skype(registrationParameters.get("skype"))
            .country(registrationParameters.get("country"))
            .note(registrationParameters.get("note"));

        return clientService.save(clientFromAmocrm);
    }

    private Agreement createAgreement(Map<String, String> registrationParameters, Client client) {
        Course course = getCourse(registrationParameters.get("courseName"));
        CourseType courseType = getCourseType(registrationParameters.get("courseType"), registrationParameters.getOrDefault("country", DEFAULT_COUNTRY));
        Agreement agreement = new Agreement();
        agreement
            .startDate(LocalDate.now())
            .status(AgreementStatus.NEW)
            .client(client)
            .course(course)
            .courseType(courseType);

        return agreementService.create(agreement);
    }

    private Pair<String, String> parseName(String fullName) {
        String[] name = fullName.trim().split(" ");
        String firstName = name.length < 2 ? DEFAULT_FIRST_NAME : name[1];
        String lastName = name.length < 2 ? fullName : name[0].isEmpty() ? DEFAULT_LAST_NAME : name[0];
        return Pair.of(firstName, lastName);
    }

    private Course getCourse(String courseName) {
        Optional<Course> course = courseRepository.findByName(courseName);
        if (course.isPresent()) {
            return course.get();
        } else {
            String exceptionMessage = "No such course found: '" + courseName + "'";
            log.error(exceptionMessage);
            throw new RegistrationFormException(exceptionMessage);
        }
    }

    private CourseType getCourseType(String courseTypeName, String location) {
        Optional<CourseType> courseType = courseTypeRepository.findByTypeAndLocation(courseTypeName, location);
        if (courseType.isPresent()) {
            return courseType.get();
        } else {
            String exceptionMessage = "No such courseType found: '" + courseTypeName + "' '" + location + "'";
            log.error(exceptionMessage);
            throw new RegistrationFormException(exceptionMessage);
        }
    }

}
