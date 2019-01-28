package ru.shitlin.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.shitlin.service.CourseTypeService;
import ru.shitlin.web.rest.errors.BadRequestAlertException;
import ru.shitlin.web.rest.util.HeaderUtil;
import ru.shitlin.web.rest.util.PaginationUtil;
import ru.shitlin.service.dto.CourseTypeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CourseType.
 */
@RestController
@RequestMapping("/api")
public class CourseTypeResource {

    private final Logger log = LoggerFactory.getLogger(CourseTypeResource.class);

    private static final String ENTITY_NAME = "courseType";

    private final CourseTypeService courseTypeService;

    public CourseTypeResource(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    /**
     * POST  /course-types : Create a new courseType.
     *
     * @param courseTypeDTO the courseTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseTypeDTO, or with status 400 (Bad Request) if the courseType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-types")
    @Timed
    public ResponseEntity<CourseTypeDTO> createCourseType(@RequestBody @Valid CourseTypeDTO courseTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CourseType : {}", courseTypeDTO);
        if (courseTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseTypeDTO result = courseTypeService.save(courseTypeDTO);
        return ResponseEntity.created(new URI("/api/course-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /course-types : Updates an existing courseType.
     *
     * @param courseTypeDTO the courseTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseTypeDTO,
     * or with status 400 (Bad Request) if the courseTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-types")
    @Timed
    public ResponseEntity<CourseTypeDTO> updateCourseType(@RequestBody @Valid CourseTypeDTO courseTypeDTO) throws URISyntaxException {
        log.debug("REST request to update CourseType : {}", courseTypeDTO);
        if (courseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseTypeDTO result = courseTypeService.save(courseTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /course-types : get all the courseTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseTypes in body
     */
    @GetMapping("/course-types")
    @Timed
    public ResponseEntity<List<CourseTypeDTO>> getAllCourseTypes(Pageable pageable) {
        log.debug("REST request to get a page of CourseTypes");
        Page<CourseTypeDTO> page = courseTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /course-types/:id : get the "id" courseType.
     *
     * @param id the id of the courseTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/course-types/{id}")
    @Timed
    public ResponseEntity<CourseTypeDTO> getCourseType(@PathVariable Long id) {
        log.debug("REST request to get CourseType : {}", id);
        Optional<CourseTypeDTO> courseTypeDTO = courseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseTypeDTO);
    }

    /**
     * DELETE  /course-types/:id : delete the "id" courseType.
     *
     * @param id the id of the courseTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseType(@PathVariable Long id) {
        log.debug("REST request to delete CourseType : {}", id);
        courseTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
