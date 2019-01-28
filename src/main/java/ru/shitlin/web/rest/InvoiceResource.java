package ru.shitlin.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.shitlin.service.InvoiceService;
import ru.shitlin.service.errors.OverlappingDateException;
import ru.shitlin.web.rest.errors.BadRequestAlertException;
import ru.shitlin.web.rest.util.HeaderUtil;
import ru.shitlin.web.rest.util.PaginationUtil;
import ru.shitlin.service.dto.InvoiceDTO;
import ru.shitlin.service.dto.InvoiceCriteria;
import ru.shitlin.service.InvoiceQueryService;
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
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    private final InvoiceService invoiceService;

    private final InvoiceQueryService invoiceQueryService;

    public InvoiceResource(InvoiceService invoiceService, InvoiceQueryService invoiceQueryService) {
        this.invoiceService = invoiceService;
        this.invoiceQueryService = invoiceQueryService;
    }

    /**
     * POST  /invoices : Create a new invoice.
     *
     * @param invoiceDTO the invoiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceDTO, or with status 400 (Bad Request) if the invoice has already an ID
     * or invoice dateFrom is less or equal of an existing invoice.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoices")
    @Timed
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoiceDTO);
        if (invoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoice cannot already have an ID", ENTITY_NAME, "id exists");
        }
        InvoiceDTO result = null;
        try {
            result = invoiceService.save(invoiceDTO);
        } catch (OverlappingDateException e){
            throw new BadRequestAlertException("The invoice period cannot overlap of an existing one.", ENTITY_NAME, "dateOverlapped");
        }

        return ResponseEntity.created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoices : Updates an existing invoice.
     *
     * @param invoiceDTO the invoiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceDTO,
     * or with status 400 (Bad Request) if the invoiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoices")
    @Timed
    public ResponseEntity<InvoiceDTO> updateInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}", invoiceDTO);
        if (invoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceDTO result = null;
        try {
            result = invoiceService.save(invoiceDTO);
        } catch (OverlappingDateException e){
            throw new BadRequestAlertException("The invoice period cannot overlap of an existing one.", ENTITY_NAME, "dateOverlapped");
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoices : get all the invoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
     */
    @GetMapping("/invoices")
    @Timed
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(InvoiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Invoices by criteria: {}", criteria);
        Page<InvoiceDTO> page = invoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoices/:id : get the "id" invoice.
     *
     * @param id the id of the invoiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoices/{id}")
    @Timed
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDTO);
    }

    /**
     * DELETE  /invoices/:id : delete the "id" invoice.
     *
     * @param id the id of the invoiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
