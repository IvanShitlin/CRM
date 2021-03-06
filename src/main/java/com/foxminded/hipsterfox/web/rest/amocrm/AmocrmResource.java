package com.foxminded.hipsterfox.web.rest.amocrm;

import com.foxminded.hipsterfox.security.AuthoritiesConstants;
import com.foxminded.hipsterfox.service.amocrm.AmocrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/amocrm/clients")
public class AmocrmResource {

    private final Logger log = LoggerFactory.getLogger(AmocrmResource.class);

    private final AmocrmService amocrmService;

    public AmocrmResource(AmocrmService amocrmService) {
        this.amocrmService = amocrmService;
    }

    @GetMapping("/import/all")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> importAllClients() {
        Integer processedRows = amocrmService.importAllClients();
        return new ResponseEntity<>(String.valueOf(processedRows), HttpStatus.OK);
    }

    @GetMapping("/sync/all")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> synchronizeClients() {
        Integer processedRows = amocrmService.synchronizeAllClients();
        return new ResponseEntity<>(String.valueOf(processedRows), HttpStatus.OK);
    }
}
