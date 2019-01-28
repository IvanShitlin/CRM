package ru.shitlin.service.impl;

import ru.shitlin.domain.ClientRegistrationForm;
import ru.shitlin.repository.ClientRegistrationFormRepository;
import ru.shitlin.service.ClientRegistrationFormService;
import ru.shitlin.web.rest.errors.LicenceNotAcceptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

/**
 * Service Implementation for managing Client Registration.
 */
@Service
@Transactional
public class ClientRegistrationFormServiceImpl implements ClientRegistrationFormService {

    private static final String ENTITY_NAME = "ClientRegistrationForm";

    private final ClientRegistrationFormRepository clientRegistrationFormRepository;

    private final Logger log = LoggerFactory.getLogger(ClientRegistrationFormServiceImpl.class);

    public ClientRegistrationFormServiceImpl(ClientRegistrationFormRepository clientRegistrationFormRepository) {
        this.clientRegistrationFormRepository = clientRegistrationFormRepository;
    }

    /**
     * Save Client Registration
     *
     * @param registrationParameters
     */
    @Override
    public ClientRegistrationForm save(Map<String, String> registrationParameters) {
        log.debug("Request to save client registration.");
        ClientRegistrationForm clientRegistrationForm = createRegistration(registrationParameters);
        return clientRegistrationFormRepository.save(clientRegistrationForm);
    }

    private ClientRegistrationForm createRegistration(Map<String, String> registrationParameters) {
        if (!Boolean.valueOf(registrationParameters.get("licenceAccepted"))) {
            throw new LicenceNotAcceptedException("Licence is not accepted.", ENTITY_NAME, "licenceNotAccepted");
        }
        return new ClientRegistrationForm()
            .clientName(registrationParameters.get("clientName"))
            .email(registrationParameters.get("email"))
            .phone(registrationParameters.get("phone"))
            .skype(registrationParameters.get("skype"))
            .country(registrationParameters.get("country"))
            .courseType(registrationParameters.get("courseType"))
            .courseName(registrationParameters.get("courseName"))
            .note(registrationParameters.get("note"))
            .date(Instant.now())
            .licenceAccepted(true);
    }
}
