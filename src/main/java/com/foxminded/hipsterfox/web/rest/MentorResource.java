package com.foxminded.hipsterfox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxminded.hipsterfox.service.MentorService;
import com.foxminded.hipsterfox.web.rest.errors.BadRequestAlertException;
import com.foxminded.hipsterfox.web.rest.util.HeaderUtil;
import com.foxminded.hipsterfox.web.rest.util.PaginationUtil;
import com.foxminded.hipsterfox.service.dto.MentorDTO;
import com.foxminded.hipsterfox.service.view.dto.MentorViewDTO;
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
 * REST controller for managing Mentor.
 */
@RestController
@RequestMapping("/api")
public class MentorResource {

    private final Logger log = LoggerFactory.getLogger(MentorResource.class);

    private static final String ENTITY_NAME = "mentor";

    private final MentorService mentorService;

    public MentorResource(MentorService mentorService) {
        this.mentorService = mentorService;
    }

    /**
     * POST  /mentors : Create a new mentor.
     *
     * @param mentorDTO the mentorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mentorDTO, or with status 400 (Bad Request) if the mentor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mentors")
    @Timed
    public ResponseEntity<MentorDTO> createMentor(@Valid @RequestBody MentorDTO mentorDTO) throws URISyntaxException {
        log.debug("REST request to save Mentor : {}", mentorDTO);
        if (mentorDTO.getId() != null) {
            throw new BadRequestAlertException("A new mentor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MentorDTO result = mentorService.save(mentorDTO);
        return ResponseEntity.created(new URI("/api/mentors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mentors : Updates an existing mentor.
     *
     * @param mentorDTO the mentorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mentorDTO,
     * or with status 400 (Bad Request) if the mentorDTO is not valid,
     * or with status 500 (Internal Server Error) if the mentorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mentors")
    @Timed
    public ResponseEntity<MentorDTO> updateMentor(@Valid @RequestBody MentorDTO mentorDTO) throws URISyntaxException {
        log.debug("REST request to update Mentor : {}", mentorDTO);
        if (mentorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MentorDTO result = mentorService.save(mentorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mentorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mentors : get all the mentors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mentors in body
     */
    @GetMapping("/mentors")
    @Timed
    public ResponseEntity<List<MentorViewDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get a page of Mentor");
        Page<MentorViewDTO> page = mentorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mentors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mentors/:id : get the "id" mentor.
     *
     * @param id the id of the mentorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mentorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/mentors/{id}")
    @Timed
    public ResponseEntity<MentorViewDTO> getMentor(@PathVariable Long id) {
        log.debug("REST request to get Mentor : {}", id);
        Optional<MentorViewDTO> mentorStudentsDTO = mentorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mentorStudentsDTO);
    }

    /**
     * DELETE  /mentors/:id : delete the "id" mentor.
     *
     * @param id the id of the mentorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mentors/{id}")
    @Timed
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        log.debug("REST request to delete Mentor : {}", id);
        mentorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
