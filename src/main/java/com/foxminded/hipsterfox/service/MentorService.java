package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.service.dto.MentorDTO;
import com.foxminded.hipsterfox.service.view.dto.MentorViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Mentor.
 */
public interface MentorService {

    /**
     * Save a mentor.
     *
     * @param mentorDTO the entity to save
     * @return the persisted entity
     */
    MentorDTO save(MentorDTO mentorDTO);

    /**
     * Get all the mentors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MentorViewDTO> findAll(Pageable pageable);


    /**
     * Get the "id" mentor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MentorViewDTO> findOne(Long id);

    /**
     * Delete the "id" mentor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
