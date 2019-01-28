package ru.shitlin.service.impl;

import ru.shitlin.domain.Course;
import ru.shitlin.domain.Mentor;
import ru.shitlin.repository.CourseRepository;
import ru.shitlin.repository.MentorRepository;
import ru.shitlin.service.CourseService;
import ru.shitlin.service.dto.CourseDTO;
import ru.shitlin.service.dto.CourseWithMentorsDTO;
import ru.shitlin.service.mapper.CourseMapper;
import ru.shitlin.service.mapper.CourseWithMentorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final CourseWithMentorsMapper courseWithMentorsMapper;

    private MentorRepository mentorRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper, CourseWithMentorsMapper courseWithMentorsMapper, MentorRepository mentorRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseWithMentorsMapper = courseWithMentorsMapper;
        this.mentorRepository = mentorRepository;
    }

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CourseDTO save(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable)
            .map(courseMapper::toDto);
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseWithMentorsDTO> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id)
            .map(courseWithMentorsMapper::toDto);
    }

    /**
     * Assign a mentor to lead a course.
     *
     * @param courseId
     * @param mentorId
     */
    @Override
    public void addMentor(Long courseId, Long mentorId) {
        log.debug("Request to assign a mentor {} to a course: {},", mentorId, courseId);
        Optional<Mentor> mentor = mentorRepository.findById(mentorId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (mentor.isPresent() && course.isPresent()) {
            course.get().addMentor(mentor.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Delete an mentor assignment of a course
     *
     * @param courseId is the id of a course.
     * @param mentorId is the id of a mentor.
     */
    @Override
    public void removeMentor(Long courseId, Long mentorId) {
        log.debug("Request to delete a mentor {} from a course: {}", mentorId, courseId);
        Optional<Mentor> mentor = mentorRepository.findById(mentorId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (mentor.isPresent() && course.isPresent()) {
            course.get().removeMentor(mentor.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }
}
