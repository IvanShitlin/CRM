package ru.shitlin.service;

import ru.shitlin.service.dto.SalaryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Salary.
 */
public interface SalaryService {

    /**
     * Save a salary.
     *
     * @param salaryDTO the entity to save
     * @return the persisted entity
     */
    SalaryDTO save(SalaryDTO salaryDTO);

    /**
     * Get all the salaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SalaryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" salary.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SalaryDTO> findOne(Long id);

    /**
     * Delete the "id" salary.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
