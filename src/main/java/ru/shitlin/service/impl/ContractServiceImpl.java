package ru.shitlin.service.impl;

import ru.shitlin.config.Constants;
import ru.shitlin.domain.Agreement;
import ru.shitlin.service.ContractService;
import ru.shitlin.domain.Contract;
import ru.shitlin.repository.ContractRepository;
import ru.shitlin.service.MailService;
import ru.shitlin.service.dto.ContractDTO;
import ru.shitlin.service.mapper.ContractMapper;
import ru.shitlin.web.rest.errors.ActiveContractExistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.Optional;

/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    private final MailService mailService;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper, MailService mailService) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.mailService = mailService;
    }

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        if (contract.getId() == null && contract.getCloseType() == null &&
            isActiveContractExists(contract.getAgreement())) {
            throw new ActiveContractExistenceException();
        }
        contract = contractRepository.save(contract);
        return contractMapper.toDto(contract);
    }

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable)
            .map(contractMapper::toDto);
    }


    /**
     * Get one contract by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        return contractRepository.findById(id)
            .map(contractMapper::toDto);
    }

    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }

    @Override
    public void updateNextPaymentDate(Contract contract) {
        log.debug("Request to update the NextPaymentDate param of Contract : {}", contract);

        int diffMonth = Period.between(contract.getFirstPayDate().withDayOfMonth(1), contract.getNextPayDate().withDayOfMonth(1)).getMonths();
        contract.setNextPayDate(contract.getFirstPayDate().plusMonths(diffMonth + Constants.PAYMENT_PERIOD_MONTHS));

        contractRepository.save(contract);
    }

    /**
     * Send an email with a start learning notification to a client from the contract by id.
     *
     * @param id the id of the entity
     */
    public void sendStartLearningEmail(Long id, String emailTemplate) {
        log.debug("Request to Send an email to a client from the agreement by id : {}", id);
        Contract contract = contractRepository.findById(id).get();
        mailService.sendStartLearningEmail(contract, emailTemplate);
    }

    private boolean isActiveContractExists(Agreement agreement) {
        return contractRepository.existsByAgreementAndCloseTypeIsNull(agreement);
    }

}
