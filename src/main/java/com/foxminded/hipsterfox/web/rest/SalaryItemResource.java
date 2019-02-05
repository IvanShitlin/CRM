package com.foxminded.hipsterfox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxminded.hipsterfox.service.SalaryItemQueryService;
import com.foxminded.hipsterfox.service.SalaryItemService;
import com.foxminded.hipsterfox.service.dto.SalaryItemCriteria;
import com.foxminded.hipsterfox.web.rest.errors.BadRequestAlertException;
import com.foxminded.hipsterfox.web.rest.util.HeaderUtil;
import com.foxminded.hipsterfox.web.rest.util.PaginationUtil;
import com.foxminded.hipsterfox.service.dto.SalaryItemDTO;
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
 * REST controller for managing SalaryItem.
 */
@RestController
@RequestMapping("/api")
public class SalaryItemResource {

    private final Logger log = LoggerFactory.getLogger(SalaryItemResource.class);

    private static final String ENTITY_NAME = "salaryItem";

    private final SalaryItemService salaryItemService;

    private final SalaryItemQueryService salaryItemQueryService;

    public SalaryItemResource(SalaryItemService salaryItemService, SalaryItemQueryService salaryItemQueryService) {
        this.salaryItemService = salaryItemService;
        this.salaryItemQueryService = salaryItemQueryService;
    }

    /**
     * POST  /salary-items : Create a new salaryItem.
     *
     * @param salaryItemDTO the salaryItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salaryItemDTO, or with status 400 (Bad Request) if the salaryItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/salary-items")
    @Timed
    public ResponseEntity<SalaryItemDTO> createSalaryItem(@RequestBody @Valid SalaryItemDTO salaryItemDTO) throws URISyntaxException {
        log.debug("REST request to save SalaryItem : {}", salaryItemDTO);
        if (salaryItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalaryItemDTO result = salaryItemService.save(salaryItemDTO);
        return ResponseEntity.created(new URI("/api/salary-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salary-items : Updates an existing salaryItem.
     *
     * @param salaryItemDTO the salaryItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salaryItemDTO,
     * or with status 400 (Bad Request) if the salaryItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the salaryItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/salary-items")
    @Timed
    public ResponseEntity<SalaryItemDTO> updateSalaryItem(@RequestBody @Valid SalaryItemDTO salaryItemDTO) throws URISyntaxException {
        log.debug("REST request to update SalaryItem : {}", salaryItemDTO);
        if (salaryItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalaryItemDTO result = salaryItemService.save(salaryItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salaryItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salary-items : get all the salaryItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of salaryItems in body
     */
    @GetMapping("/salary-items")
    @Timed
    public ResponseEntity<List<SalaryItemDTO>> getAllSalaryItems(SalaryItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SalaryItems by criteria: {}", criteria);
        Page<SalaryItemDTO> page = salaryItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/salary-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /salary-items/:id : get the "id" salaryItem.
     *
     * @param id the id of the salaryItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salaryItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/salary-items/{id}")
    @Timed
    public ResponseEntity<SalaryItemDTO> getSalaryItem(@PathVariable Long id) {
        log.debug("REST request to get SalaryItem : {}", id);
        Optional<SalaryItemDTO> salaryItemDTO = salaryItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salaryItemDTO);
    }

    /**
     * DELETE  /salary-items/:id : delete the "id" salaryItem.
     *
     * @param id the id of the salaryItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/salary-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalaryItem(@PathVariable Long id) {
        log.debug("REST request to delete SalaryItem : {}", id);
        salaryItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
