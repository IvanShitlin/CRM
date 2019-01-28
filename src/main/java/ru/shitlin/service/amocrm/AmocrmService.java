package ru.shitlin.service.amocrm;

import ru.shitlin.config.AmocrmProperties;
import ru.shitlin.domain.AmocrmInfo;
import ru.shitlin.domain.enumeration.AmocrmActionType;
import ru.shitlin.domain.enumeration.AmocrmEntitytype;
import ru.shitlin.repository.AmocrmInfoRepository;
import ru.shitlin.service.ClientService;
import ru.shitlin.service.amocrm.mapper.AmocrmClientMapper;
import ru.shitlin.service.amocrm.model.AmocrmContact;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.shitlin.service.amocrm.util.AmocrmTimeConverter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for import Clients from Amocrm.
 */
@Service
@Transactional
public class AmocrmService {

    private static final String CONTACTS = "/api/v2/contacts";

    private final AmocrmAuthHandler authHandler;
    private final AmocrmProperties properties;
    private final RestTemplate restTemplate;
    private final AmocrmClientMapper clientMapper;
    private final ClientService clientService;
    private final AmocrmInfoRepository amocrmInfoRepository;

    public AmocrmService(AmocrmAuthHandler authHandler, AmocrmProperties properties,
                         @Qualifier("amocrmRestTemplate") RestTemplate restTemplate,
                         AmocrmClientMapper clientMapper, ClientService clientService,
                         AmocrmInfoRepository amocrmInfoRepository) {

        this.authHandler = authHandler;
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
        this.amocrmInfoRepository = amocrmInfoRepository;
    }

    /**
     * Imports all client from Amocrm
     *
     * @return number of imported clients
     */
    public Integer importAllClients() {
        int limitRows = 500;
        int offset = 0;
        ZonedDateTime currentImportDate = null;
        List<AmocrmContact> contacts = new ArrayList<>();
        do {
            ContactImportData data = getAllContacts(limitRows, offset);
            contacts.addAll(data.contacts);
            if (currentImportDate == null) {
                currentImportDate = data.dateTime;
            }
            offset += 500;
        } while (contacts.size() == offset);

        AmocrmInfo info = new AmocrmInfo(currentImportDate, contacts.size(), AmocrmEntitytype.CONTACT, AmocrmActionType.IMPORT);

        clientService.saveAll(clientMapper.toClientDTOList(contacts));
        amocrmInfoRepository.save(info);
        return contacts.size();
    }

    /**
     * Synchronize all clients from last synchronization
     */
    public Integer synchronizeAllClients() {
        ZonedDateTime lastSyncDate = null;
        Optional<AmocrmInfo> lastInfo = amocrmInfoRepository.findFirstByOrderByDateTimeDesc();
        if (lastInfo.isPresent()) {
            lastSyncDate = lastInfo.get().getDateTime();
        }

        int limitRows = 500;
        int offset = 0;
        ZonedDateTime currentSyncDate = null;
        List<AmocrmContact> contacts = new ArrayList<>();
        do {
            ContactImportData data = getAllContacts(limitRows, offset, lastSyncDate);
            contacts.addAll(data.contacts);
            if (currentSyncDate == null) {
                currentSyncDate = data.dateTime;
            }
            offset += 500;
        } while (contacts.size() == limitRows);

        if (currentSyncDate != null) {
            AmocrmInfo info = new AmocrmInfo(currentSyncDate, contacts.size(), AmocrmEntitytype.CONTACT, AmocrmActionType.SYNCHRONIZE);
            amocrmInfoRepository.save(info);

        }

        clientService.updateAllByAmocrmId(clientMapper.toClientDTOList(contacts));
        return contacts.size();
    }

    private ContactImportData getAllContacts(Integer limitRows, Integer limitOffset) {
        HttpEntity<?> request = new HttpEntity<>(authHandler.getHeaders());

        ResponseEntity<Resources<AmocrmContact>> response = restTemplate.exchange(
            properties.getConnection().getBaseUrl() + CONTACTS + "?limit_rows=" + limitRows +
                (limitOffset != null ? "&limit_offset=" + limitOffset : ""),
            HttpMethod.GET,
            request,
            new ResourcesReturnType());
        if (response.getStatusCodeValue() == 200 && response.getBody() != null) {
            return new ContactImportData(extractAmocrmImportDate(response), new ArrayList<>(response.getBody().getContent()));
        }

        return new ContactImportData(null, Collections.emptyList());
    }

    private ContactImportData getAllContacts(Integer limitRows, Integer limitOffset, ZonedDateTime syncSince) {
        if (syncSince == null) {
            return getAllContacts(limitRows, limitOffset);
        }
        HttpHeaders headers = authHandler.getHeaders();
        headers.add("IF-MODIFIED-SINCE", AmocrmTimeConverter.toAmocrmRequestFormat(syncSince));
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Resources<AmocrmContact>> response = restTemplate.exchange(
            properties.getConnection().getBaseUrl() + CONTACTS + "?limit_rows=" + limitRows +
                (limitOffset != null ? "&limit_offset=" + limitOffset : ""),
            HttpMethod.GET,
            request,
            new ResourcesReturnType());
        if (response.getStatusCodeValue() == 200 && response.getBody() != null) {
            return new ContactImportData(extractAmocrmImportDate(response), new ArrayList<>(response.getBody().getContent()));
        }

        return new ContactImportData(null, Collections.emptyList());
    }

    private static ZonedDateTime extractAmocrmImportDate(ResponseEntity<Resources<AmocrmContact>> response) {
        List<String> headerValues = response.getHeaders().get("Runtime-Timestamp");
        ZonedDateTime amocrmImportDate = null;
        if (headerValues != null && headerValues.size() != 0) {
            amocrmImportDate = AmocrmTimeConverter.toZoneDateTime(headerValues.get(0));
        }
        return amocrmImportDate;
    }

    private static final class ResourcesReturnType extends TypeReferences.ResourcesType<AmocrmContact> {
    }

    private final class ContactImportData {
        private ZonedDateTime dateTime;
        private List<AmocrmContact> contacts;

        ContactImportData(ZonedDateTime dateTime, List<AmocrmContact> contacts) {
            this.dateTime = dateTime;
            this.contacts = contacts;
        }
    }
}
