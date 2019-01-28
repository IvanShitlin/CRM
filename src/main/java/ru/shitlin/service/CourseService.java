package ru.shitlin.service;

import ru.shitlin.service.dto.CourseDTO;

import ru.shitlin.service.dto.CourseWithMentorsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Course.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save
     * @return the persisted entity
     */
    CourseDTO save(CourseDTO courseDTO);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CourseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" course.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CourseWithMentorsDTO> findOne(Long id);

    /**
     * Assign a mentor to the course.
     *
     * @param courseId
     * @param mentorId
     */
    void addMentor(Long courseId, Long mentorId);

    /**
     * Delete the mentor by "id" from a course.
     *
     * @param courseId
     * @param mentorId
     */
    void removeMentor(Long courseId, Long mentorId);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
