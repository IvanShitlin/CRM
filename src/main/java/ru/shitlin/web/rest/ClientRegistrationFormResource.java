package ru.shitlin.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.shitlin.service.ClientRegistrationFormService;
import ru.shitlin.web.rest.errors.LicenceNotAcceptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * REST controller for managing Client Registration.
 */
@RestController
@RequestMapping("/api")
public class ClientRegistrationFormResource {

    private final Logger log = LoggerFactory.getLogger(ClientRegistrationFormResource.class);

    private final ClientRegistrationFormService clientRegistrationFormService;

    public ClientRegistrationFormResource(ClientRegistrationFormService clientRegistrationFormService) {
        this.clientRegistrationFormService = clientRegistrationFormService;
    }

    /**
     * Post /registers/ : Create new client registration.
     *
     * @param clientName
     * @param email
     * @param phone
     * @param skype
     * @param country
     * @param note
     * @param courseType
     * @param courseName
     * @param licenceAccepted
     * @return
     */
    @PostMapping("/forms/course-registration")
    @Timed
    public ResponseEntity<Void> registerClientAppeal(@RequestParam(value = "your-name") String clientName,
                                                     @RequestParam(value = "your-email") String email,
                                                     @RequestParam(value = "tel-92") String phone,
                                                     @RequestParam(value = "your-skype", required = false) String skype,
                                                     @RequestParam(value = "your-country", required = false) String country,
                                                     @RequestParam(value = "your-note", required = false) String note,
                                                     @RequestParam(value = "tip-obucheniya") String courseType,
                                                     @RequestParam(value = "napravlenie-obuch") String courseName,      // Ask main site developers
                                                     @RequestParam(value = "acceptance-451") String licenceAccepted) {  // to add the field "napravlenie-obuch"
                                                                                                                        // to android, java and other forms.
        log.debug("REST request to register a new client {} to a course {}", clientName, courseName);
        HashMap<String, String> regParameters = new HashMap<>();
        regParameters.put("clientName", clientName);
        regParameters.put("email", email);
        regParameters.put("phone", phone);
        regParameters.put("skype", skype);
        regParameters.put("country", country);
        regParameters.put("note", note);
        regParameters.put("courseType", courseType);
        regParameters.put("courseName", courseName);
        regParameters.put("licenceAccepted", licenceAccepted);
        try {
            clientRegistrationFormService.save(regParameters);
        } catch (LicenceNotAcceptedException licenceNotAcceptedException) {
            throw licenceNotAcceptedException;
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
