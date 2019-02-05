package com.foxminded.hipsterfox.web.rest.view;

import com.foxminded.hipsterfox.service.view.dto.DebtorViewDTO;
import com.foxminded.hipsterfox.service.view.service.DebtorViewService;
import com.foxminded.hipsterfox.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DebtorViewResource {

    private final Logger log = LoggerFactory.getLogger(DebtorViewResource.class);

    private final DebtorViewService debtorViewService;

    public DebtorViewResource(DebtorViewService debtorViewService) {
        this.debtorViewService = debtorViewService;
    }

    @GetMapping("/debtors")
    ResponseEntity<List<DebtorViewDTO>> getAll(Pageable pageable) {
        log.debug("REST request to get a page of Debtors");
        Page<DebtorViewDTO> page = debtorViewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/debtors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
