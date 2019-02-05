package com.foxminded.hipsterfox.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.foxminded.hipsterfox.domain.SalaryItem;
import com.foxminded.hipsterfox.domain.*; // for static metamodels
import com.foxminded.hipsterfox.repository.SalaryItemRepository;
import com.foxminded.hipsterfox.service.dto.SalaryItemCriteria;

import com.foxminded.hipsterfox.service.dto.SalaryItemDTO;
import com.foxminded.hipsterfox.service.mapper.SalaryItemMapper;

/**
 * Service for executing complex queries for SalaryItem entities in the database.
 * The main input is a {@link SalaryItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SalaryItemDTO} or a {@link Page} of {@link SalaryItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalaryItemQueryService extends QueryService<SalaryItem> {

    private final Logger log = LoggerFactory.getLogger(SalaryItemQueryService.class);

    private final SalaryItemRepository salaryItemRepository;

    private final SalaryItemMapper salaryItemMapper;

    public SalaryItemQueryService(SalaryItemRepository salaryItemRepository, SalaryItemMapper salaryItemMapper) {
        this.salaryItemRepository = salaryItemRepository;
        this.salaryItemMapper = salaryItemMapper;
    }

    /**
     * Return a {@link List} of {@link SalaryItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SalaryItemDTO> findByCriteria(SalaryItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SalaryItem> specification = createSpecification(criteria);
        return salaryItemMapper.toDto(salaryItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SalaryItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalaryItemDTO> findByCriteria(SalaryItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SalaryItem> specification = createSpecification(criteria);
        return salaryItemRepository.findAll(specification, page)
            .map(salaryItemMapper::toDto);
    }

    /**
     * Function to convert SalaryItemCriteria to a {@link Specification}
     */
    private Specification<SalaryItem> createSpecification(SalaryItemCriteria criteria) {
        Specification<SalaryItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SalaryItem_.id));
            }
            if (criteria.getDateFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFrom(), SalaryItem_.dateFrom));
            }
            if (criteria.getDateTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTo(), SalaryItem_.dateTo));
            }
            if (criteria.getAccounted() != null) {
                specification = specification.and(buildSpecification(criteria.getAccounted(), SalaryItem_.accounted));
            }
            if (criteria.getMentorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMentorId(), SalaryItem_.mentor, Mentor_.id));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceId(), SalaryItem_.invoice, Invoice_.id));
            }
            if (criteria.getSalaryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalaryId(), SalaryItem_.salary, Salary_.id));
            }
        }
        return specification;
    }

}
