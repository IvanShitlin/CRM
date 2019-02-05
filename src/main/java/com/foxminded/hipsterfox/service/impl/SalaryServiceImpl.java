package com.foxminded.hipsterfox.service.impl;

import com.foxminded.hipsterfox.domain.*;
import com.foxminded.hipsterfox.repository.SalaryItemRepository;
import com.foxminded.hipsterfox.service.SalaryService;
import com.foxminded.hipsterfox.repository.SalaryRepository;
import com.foxminded.hipsterfox.service.dto.SalaryDTO;
import com.foxminded.hipsterfox.service.mapper.SalaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Salary.
 */
@Service
@Transactional
public class SalaryServiceImpl implements SalaryService {

    private final Logger log = LoggerFactory.getLogger(SalaryServiceImpl.class);

    private final SalaryRepository salaryRepository;
    private final SalaryItemRepository salaryItemRepository;

    private final SalaryMapper salaryMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository, SalaryItemRepository salaryItemRepository, SalaryMapper salaryMapper) {
        this.salaryRepository = salaryRepository;
        this.salaryItemRepository = salaryItemRepository;
        this.salaryMapper = salaryMapper;
    }

    /**
     * Save a salary.
     *
     * @param salaryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SalaryDTO save(SalaryDTO salaryDTO) {
        log.debug("Request to save Salary : {}", salaryDTO);
        Salary salary = salaryMapper.toEntity(salaryDTO);
        Set<SalaryItem> itemsFromDTO = salary.getItems();

        if (salary.getId() != null) {
            Set<SalaryItem> itemsFromRepo = salaryItemRepository.findItemsBySalaryId(salary.getId());

            if (!(itemsFromDTO.containsAll(itemsFromRepo) && itemsFromDTO.size() == itemsFromRepo.size())) {
                for (SalaryItem item : itemsFromDTO) {
                    if (!itemsFromRepo.contains(item)) {
                        salary.addItem(item);
                    }
                }
                for (SalaryItem item : itemsFromRepo) {
                    if (!itemsFromDTO.contains(item)) {
                        salary.removeItem(item);
                    }
                }
            }
        }

        if (salary.getId() == null && itemsFromDTO != null) {
            for (SalaryItem item : itemsFromDTO) {
                SalaryItem itemToSave = salaryItemRepository.getOne(item.getId());
                salary.addItem(itemToSave);
            }
        }

        salary = salaryRepository.save(salary);
        return salaryMapper.toDto(salary);
    }

    /**
     * Get all the salaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Salaries");
        return salaryRepository.findAll(pageable)
            .map(salaryMapper::toDto);
    }

    /**
     * Get one salary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryDTO> findOne(Long id) {
        log.debug("Request to get Salary : {}", id);
        return salaryRepository.findById(id)
            .map(salaryMapper::toDto);
    }

    /**
     * Delete the salary by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Salary : {}", id);
        Set<SalaryItem> accountedItems = salaryItemRepository.findItemsBySalaryId(id);
        if(!accountedItems.isEmpty()) {
            for (SalaryItem item : accountedItems) {
                item.accounted(false);
            }
        }
        salaryRepository.deleteById(id);
    }
}
