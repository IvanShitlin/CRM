package ru.shitlin.service;

import ru.shitlin.service.dto.AgreementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Agreement.
 */
public interface AgreementService {

    /**
     * Save a agreement.
     *
     * @param agreementDTO the entity to save
     * @return the persisted entity
     */
    AgreementDTO save(AgreementDTO agreementDTO);

    /**
     * Get all the agreements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AgreementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" agreement.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AgreementDTO> findOne(Long id);

    /**
     * Delete the "id" agreement.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Send an email with greetings to a client from the agreement by id.
     *
     * @param id the id of the entity
     */
    void sendGreetingsEmail(Long id);

    /**
     * Search for the agreement corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AgreementDTO> search(String query, Pageable pageable);
}
