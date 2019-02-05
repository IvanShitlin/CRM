package com.foxminded.hipsterfox.service.impl;

import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.domain.enumeration.AgreementStatus;
import com.foxminded.hipsterfox.repository.AgreementRepository;
import com.foxminded.hipsterfox.repository.search.AgreementSearchRepository;
import com.foxminded.hipsterfox.service.AgreementService;
import com.foxminded.hipsterfox.service.MailService;
import com.foxminded.hipsterfox.service.TaskService;
import com.foxminded.hipsterfox.service.dto.AgreementDTO;
import com.foxminded.hipsterfox.service.errors.DuplicateAgreementException;
import com.foxminded.hipsterfox.service.mapper.AgreementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Agreement.
 */
@Service
@Transactional
public class AgreementServiceImpl implements AgreementService {

    private final Logger log = LoggerFactory.getLogger(AgreementServiceImpl.class);

    private final static String GREETING_EMAIL_TEMPLATE = "greetings";

    private final AgreementRepository agreementRepository;

    private final AgreementSearchRepository agreementSearchRepository;

    private final AgreementMapper agreementMapper;

    private final TaskService taskService;

    private final MailService mailService;

    public AgreementServiceImpl(AgreementRepository agreementRepository, AgreementMapper agreementMapper,
                                TaskService taskService, MailService mailService, AgreementSearchRepository agreementSearchRepository) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
        this.taskService = taskService;
        this.mailService = mailService;
        this.agreementSearchRepository = agreementSearchRepository;
    }

    /**
     * Save a agreement.
     *
     * @param agreementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AgreementDTO save(AgreementDTO agreementDTO) {
        log.debug("Request to save Agreement : {}", agreementDTO);
        Agreement agreement = agreementMapper.toEntity(agreementDTO);
        if (agreement.getId() == null) {
            agreementDTO = agreementMapper.toDto(create(agreement));
        } else {
            agreementDTO = agreementMapper.toDto(update(agreement));
        }
        return agreementDTO;
    }

    /**
     * Create new agreement.
     *
     * @param agreement the entity to create
     * @return the persisted entity
     */
    @Override
    public Agreement create(Agreement agreement) {
        checkAgreementForDuplicate(agreement);
        setProperStatus(agreement);
        agreementRepository.save(agreement);
        agreementSearchRepository.save(agreement);
        taskService.create(agreement);
        return agreement;
    }

    /**
     * Get all the agreements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgreementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agreements");
        return agreementRepository.findAll(pageable)
            .map(agreementMapper::toDto);
    }

    /**
     * Get one agreement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AgreementDTO> findOne(Long id) {
        log.debug("Request to get Agreement : {}", id);
        return agreementRepository.findById(id)
            .map(agreementMapper::toDto);
    }

    /**
     * Delete the agreement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agreement : {}", id);
        agreementRepository.deleteById(id);
        agreementSearchRepository.deleteById(id);
    }

    /**
     * Send an email with greetings to a client from the agreement by id.
     *
     * @param id the id of the entity
     */
    public void sendGreetingsEmail(Long id) {
        log.debug("Request to Send an email to a client from the agreement by id : {}", id);
        Client client = agreementRepository.findById(id).map(Agreement::getClient).get();
        mailService.sendGreetingsEmail(client, GREETING_EMAIL_TEMPLATE);
    }

    /**
     * Search for the agreement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AgreementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Agreements for query {}", query);
        return agreementSearchRepository.search(queryStringQuery(query), pageable)
            .map(agreementMapper::toDto);
    }

    private void checkAgreementForDuplicate(Agreement agreement) {
        Optional<Agreement> duplicateAgreement = agreementRepository.findByClientAndCourse(agreement.getClient(), agreement.getCourse());
        if (duplicateAgreement.isPresent()) {
            throw new DuplicateAgreementException("This agreement already exists: " + duplicateAgreement.get().toString());
        }
    }

    private Agreement update(Agreement agreement) {
        setProperStatus(agreement);
        agreementSearchRepository.save(agreement);
        return agreementRepository.save(agreement);
    }

    private void setProperStatus(Agreement agreement) {
        if (agreement.getId() == null) {
            agreement.setStatus(AgreementStatus.NEW);
        } else {
            Agreement updatableAgreement = agreementRepository.getOne(agreement.getId());
            agreement.setStatus(updatableAgreement.getStatus());
        }
    }
}
