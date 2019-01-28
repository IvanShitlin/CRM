package ru.shitlin.service;

import ru.shitlin.service.dto.SalaryItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SalaryItem.
 */
public interface SalaryItemService {

    /**
     * Save a salaryItem.
     *
     * @param salaryItemDTO the entity to save
     * @return the persisted entity
     */
    SalaryItemDTO save(SalaryItemDTO salaryItemDTO);

    /**
     * Get all the salaryItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SalaryItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" salaryItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SalaryItemDTO> findOne(Long id);

    /**
     * Delete the "id" salaryItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
