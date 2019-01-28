package ru.shitlin.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.shitlin.service.CourseQueryService;
import ru.shitlin.service.CourseService;
import ru.shitlin.service.dto.CourseCriteria;
import ru.shitlin.service.dto.CourseWithMentorsDTO;
import ru.shitlin.web.rest.errors.BadRequestAlertException;
import ru.shitlin.web.rest.util.HeaderUtil;
import ru.shitlin.web.rest.util.PaginationUtil;
import ru.shitlin.service.dto.CourseDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    private final CourseService courseService;

    private final CourseQueryService courseQueryService;

    public CourseResource(CourseService courseService, CourseQueryService courseQueryService) {
        this.courseService = courseService;
        this.courseQueryService = courseQueryService;
    }

    /**
     * POST  /courses : Create a new course.
     *
     * @param courseDTO the courseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseDTO, or with status 400 (Bad Request) if the course has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courses")
    @Timed
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to save Course : {}", courseDTO);
        if (courseDTO.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courses : Updates an existing course.
     *
     * @param courseDTO the courseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseDTO,
     * or with status 400 (Bad Request) if the courseDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courses")
    @Timed
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody @Valid CourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to update Course : {}", courseDTO);
        if (courseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courses : get all the courses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of courses in body
     */
    @GetMapping("/courses")
    @Timed
    public ResponseEntity<List<CourseDTO>> getAllCourses(CourseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Courses by criteria: {}, page: {}");
        Page<CourseDTO> page = courseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /courses/count : count all the courses.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/courses/count")
    @Timed
    public ResponseEntity<Long> countCourses (CourseCriteria criteria) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /courses/:id : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseWithMentorsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/courses/{id}")
    @Timed
    public ResponseEntity<CourseWithMentorsDTO> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<CourseWithMentorsDTO> courseWithMentorsDTO = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseWithMentorsDTO);
    }

    /**
     * PUT /courses/{courseId}/mentors/{mentorId} : assign a Mentor to a course
     *
     * @param courseId
     * @param mentorId
     * @return
     */
    @PutMapping("/courses/{courseId}/mentors/{mentorId}")
    @Timed
    public ResponseEntity<Void> addMentor(@PathVariable Long courseId, @PathVariable Long mentorId) {
        log.debug("REST request to assign a mentor {} to a course {}", mentorId, courseId);
        try {
            courseService.addMentor(courseId, mentorId);
        } catch (EntityNotFoundException e) {
            throw new BadRequestAlertException("The course or the mentor is not found", "addMentor","CourseOrMentorIsNotFound");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseId.toString())).build();
    }

    /**
     * DELETE /courses/{courseId}/mentors/{mentorId} : delete an mentor assignment from a course
     *
     * @param courseId
     * @param mentorId
     * @return
     */
    @DeleteMapping("/courses/{courseId}/mentors/{mentorId}")
    @Timed
    public ResponseEntity<Void> removeMentor(@PathVariable Long courseId, @PathVariable Long mentorId) {
        log.debug("REST request to delete a mentor {} from a course {}", mentorId, courseId);
        try {
            courseService.removeMentor(courseId, mentorId);
        } catch (EntityNotFoundException e) {
            throw new BadRequestAlertException("The course or the mentor is not found", "removeMentor","CourseOrMentorIsNotFound");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, courseId.toString())).build();
    }

    /**
     * DELETE  /courses/:id : delete the "id" course.
     *
     * @param id the id of the courseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
