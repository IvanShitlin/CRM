package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.domain.ClientRegistrationForm;

import java.util.Map;

/**
 * Service Interface for managing Client Registration Form.
 */
public interface RegistrationFormService {

    /**
     * Save a new Client Registration
     *
     * @param registration
     */
    ClientRegistrationForm save(Map<String, String> registration);
}
