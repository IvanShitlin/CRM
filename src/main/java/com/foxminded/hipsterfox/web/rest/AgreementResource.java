package com.foxminded.hipsterfox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.foxminded.hipsterfox.service.AgreementQueryService;
import com.foxminded.hipsterfox.service.AgreementService;
import com.foxminded.hipsterfox.service.dto.AgreementCriteria;
import com.foxminded.hipsterfox.service.dto.AgreementDTO;
import com.foxminded.hipsterfox.service.errors.DuplicateAgreementException;
import com.foxminded.hipsterfox.web.rest.errors.BadRequestAlertException;
import com.foxminded.hipsterfox.web.rest.util.HeaderUtil;
import com.foxminded.hipsterfox.web.rest.util.PaginationUtil;
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
 * REST controller for managing Agreement.
 */
@RestController
@RequestMapping("/api")
public class AgreementResource {

    private final Logger log = LoggerFactory.getLogger(AgreementResource.class);

    private static final String ENTITY_NAME = "agreement";

    private final AgreementService agreementService;

    private final AgreementQueryService agreementQueryService;

    public AgreementResource(AgreementService agreementService, AgreementQueryService agreementQueryService) {
        this.agreementService = agreementService;
        this.agreementQueryService = agreementQueryService;
    }

    /**
     * POST  /agreements : Create a new agreement.
     *
     * @param agreementDTO the agreementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agreementDTO, or with status 400 (Bad Request) if the agreement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agreements")
    @Timed
    public ResponseEntity<AgreementDTO> createAgreement(@RequestBody @Valid AgreementDTO agreementDTO) throws URISyntaxException {
        log.debug("REST request to save Agreement : {}", agreementDTO);
        if (agreementDTO.getId() != null) {
            throw new BadRequestAlertException("A new agreement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgreementDTO result = null;
        try {
            result = agreementService.save(agreementDTO);
        } catch (DuplicateAgreementException e) {
            throw new BadRequestAlertException("Cannot create duplicate agreement", ENTITY_NAME, "agreementDuplicate");
        }
        return ResponseEntity.created(new URI("/api/agreements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agreements : Updates an existing agreement.
     *
     * @param agreementDTO the agreementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agreementDTO,
     * or with status 400 (Bad Request) if the agreementDTO is not valid,
     * or with status 500 (Internal Server Error) if the agreementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agreements")
    @Timed
    public ResponseEntity<AgreementDTO> updateAgreement(@RequestBody @Valid AgreementDTO agreementDTO) throws URISyntaxException {
        log.debug("REST request to update Agreement : {}", agreementDTO);
        if (agreementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AgreementDTO result = agreementService.save(agreementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agreementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agreements : get all the agreements.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of agreements in body
     */
    @GetMapping("/agreements")
    @Timed
    public ResponseEntity<List<AgreementDTO>> getAllAgreements(AgreementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agreements by criteria: {}", criteria);
        Page<AgreementDTO> page = agreementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agreements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agreements/:id : get the "id" agreement.
     *
     * @param id the id of the agreementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agreementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agreements/{id}")
    @Timed
    public ResponseEntity<AgreementDTO> getAgreement(@PathVariable Long id) {
        log.debug("REST request to get Agreement : {}", id);
        Optional<AgreementDTO> agreementDTO = agreementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agreementDTO);
    }

    /**
     * DELETE  /agreements/:id : delete the "id" agreement.
     *
     * @param id the id of the agreementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agreements/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long id) {
        log.debug("REST request to delete Agreement : {}", id);
        agreementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
