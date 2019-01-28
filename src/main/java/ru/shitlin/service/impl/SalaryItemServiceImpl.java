package ru.shitlin.service.impl;

import ru.shitlin.service.SalaryItemService;
import ru.shitlin.domain.SalaryItem;
import ru.shitlin.repository.SalaryItemRepository;
import ru.shitlin.service.dto.SalaryItemDTO;
import ru.shitlin.service.mapper.SalaryItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing SalaryItem.
 */
@Service
@Transactional
public class SalaryItemServiceImpl implements SalaryItemService {

    private final Logger log = LoggerFactory.getLogger(SalaryItemServiceImpl.class);

    private final SalaryItemRepository salaryItemRepository;

    private final SalaryItemMapper salaryItemMapper;

    public SalaryItemServiceImpl(SalaryItemRepository salaryItemRepository, SalaryItemMapper salaryItemMapper) {
        this.salaryItemRepository = salaryItemRepository;
        this.salaryItemMapper = salaryItemMapper;
    }

    /**
     * Save a salaryItem.
     *
     * @param salaryItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SalaryItemDTO save(SalaryItemDTO salaryItemDTO) {
        log.debug("Request to save SalaryItem : {}", salaryItemDTO);
        SalaryItem salaryItem = salaryItemMapper.toEntity(salaryItemDTO);
        salaryItem = salaryItemRepository.save(salaryItem);
        return salaryItemMapper.toDto(salaryItem);
    }

    /**
     * Get all the salaryItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SalaryItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SalaryItems");
        return salaryItemRepository.findAll(pageable)
            .map(salaryItemMapper::toDto);
    }


    /**
     * Get one salaryItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryItemDTO> findOne(Long id) {
        log.debug("Request to get SalaryItem : {}", id);
        return salaryItemRepository.findById(id)
            .map(salaryItemMapper::toDto);
    }

    /**
     * Delete the salaryItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SalaryItem : {}", id);
        salaryItemRepository.deleteById(id);
    }
}
