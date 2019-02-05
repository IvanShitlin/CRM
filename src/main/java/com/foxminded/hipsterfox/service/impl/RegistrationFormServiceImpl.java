package com.foxminded.hipsterfox.service.impl;

import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.repository.*;
import com.foxminded.hipsterfox.service.ClientWithAgreementCreationService;
import com.foxminded.hipsterfox.service.RegistrationFormService;
import com.foxminded.hipsterfox.service.errors.LicenceNotAcceptedException;
import com.foxminded.hipsterfox.service.errors.RegistrationFormException;

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
public class RegistrationFormServiceImpl implements RegistrationFormService {

    private final Logger log = LoggerFactory.getLogger(RegistrationFormServiceImpl.class);

    private final ClientWithAgreementCreationService clientWithAgreementCreationService;

    private final ClientRegistrationFormRepository clientRegistrationFormRepository;

    public RegistrationFormServiceImpl(ClientWithAgreementCreationService clientWithAgreementCreationService,
                                       ClientRegistrationFormRepository clientRegistrationFormRepository) {
        this.clientWithAgreementCreationService = clientWithAgreementCreationService;
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
        ClientRegistrationForm clientRegistrationForm;
        checkLicenceIsAccepted(registrationParameters);
        try {
            clientWithAgreementCreationService.create(registrationParameters);
            registrationParameters.put("managed", "true");
        } catch (RegistrationFormException e) {
            registrationParameters.put("managed", "false");
        }
        clientRegistrationForm = createRegistrationForm(registrationParameters);
        return clientRegistrationForm;
    }

    private void checkLicenceIsAccepted(Map<String, String> registrationParameters) {
        if (!Boolean.valueOf(registrationParameters.get("licenceAccepted"))) {
            String exceptionMessage = "Licence is not accepted!";
            log.error(exceptionMessage);
            throw new LicenceNotAcceptedException(exceptionMessage);
        }
    }

    private ClientRegistrationForm createRegistrationForm(Map<String, String> registrationParameters) {
        boolean isManaged = Boolean.valueOf(registrationParameters.get("managed"));
        ClientRegistrationForm clientRegistrationForm = new ClientRegistrationForm();
        clientRegistrationForm
            .clientName(registrationParameters.get("clientName"))
            .email(registrationParameters.get("email"))
            .phone(registrationParameters.get("phone"))
            .skype(registrationParameters.get("skype"))
            .country(registrationParameters.get("country"))
            .courseType(registrationParameters.get("courseType"))
            .courseName(registrationParameters.get("courseName"))
            .note(registrationParameters.get("note"))
            .date(Instant.now())
            .licenceAccepted(true)
            .managed(isManaged);

        return clientRegistrationFormRepository.save(clientRegistrationForm);
    }

}
