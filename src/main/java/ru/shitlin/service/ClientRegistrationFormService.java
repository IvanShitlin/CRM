package ru.shitlin.service;

import ru.shitlin.domain.ClientRegistrationForm;

import java.util.Map;

/**
 * Service Interface for managing Client Registration Form.
 */
public interface ClientRegistrationFormService {

    /**
     * Save a new Client Registration
     *
     * @param registration
     */
    ClientRegistrationForm save(Map<String, String> registration);
}
