package ru.shitlin.service.impl;

import ru.shitlin.service.ContractService;
import ru.shitlin.service.InvoiceService;
import ru.shitlin.domain.Invoice;
import ru.shitlin.repository.InvoiceRepository;
import ru.shitlin.service.dto.InvoiceDTO;
import ru.shitlin.service.errors.OverlappingDateException;
import ru.shitlin.service.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final ContractService contractService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, ContractService contractService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.contractService = contractService;
    }

    /**
     * Save a invoice.
     *
     * @param invoiceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        if (isOverlapDate(invoice)) {
            throw new OverlappingDateException("The invoice period is overlap existing invoice");
        }
        invoice = invoiceRepository.saveAndFlush(invoice);
        invoiceRepository.refresh(invoice);

        if (invoiceDTO.getId() == null) contractService.updateNextPaymentDate(invoice.getContract());

        return invoiceMapper.toDto(invoice);
    }

    private boolean isOverlapDate(Invoice invoice) {
        if(invoice.getId() != null) {
            return invoiceRepository.existsByDateToIsGreaterThanEqualAndDateFromIsLessThanEqualAndIdIsNotAndContract(
                invoice.getDateFrom(),
                invoice.getDateTo(),
                invoice.getId(),
                invoice.getContract());
        }
        return invoiceRepository.existsByDateToIsGreaterThanEqualAndDateFromIsLessThanEqualAndContract(
            invoice.getDateFrom(),
            invoice.getDateTo(),
            invoice.getContract());
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable)
            .map(invoiceMapper::toDto);
    }


    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id)
            .map(invoiceMapper::toDto);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
