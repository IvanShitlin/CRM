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

import com.foxminded.hipsterfox.domain.Salary;
import com.foxminded.hipsterfox.domain.*; // for static metamodels
import com.foxminded.hipsterfox.repository.SalaryRepository;
import com.foxminded.hipsterfox.service.dto.SalaryCriteria;

import com.foxminded.hipsterfox.service.dto.SalaryDTO;
import com.foxminded.hipsterfox.service.mapper.SalaryMapper;

/**
 * Service for executing complex queries for Salary entities in the database.
 * The main input is a {@link SalaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SalaryDTO} or a {@link Page} of {@link SalaryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalaryQueryService extends QueryService<Salary> {

    private final Logger log = LoggerFactory.getLogger(SalaryQueryService.class);

    private final SalaryRepository salaryRepository;

    private final SalaryMapper salaryMapper;

    public SalaryQueryService(SalaryRepository salaryRepository, SalaryMapper salaryMapper) {
        this.salaryRepository = salaryRepository;
        this.salaryMapper = salaryMapper;
    }

    /**
     * Return a {@link List} of {@link SalaryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SalaryDTO> findByCriteria(SalaryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Salary> specification = createSpecification(criteria);
        return salaryMapper.toDto(salaryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SalaryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalaryDTO> findByCriteria(SalaryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Salary> specification = createSpecification(criteria);
        return salaryRepository.findAll(specification, page)
            .map(salaryMapper::toDto);
    }

    /**
     * Function to convert SalaryCriteria to a {@link Specification}
     */
    private Specification<Salary> createSpecification(SalaryCriteria criteria) {
        Specification<Salary> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Salary_.id));
            }
            if (criteria.getDateFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFrom(), Salary_.dateFrom));
            }
            if (criteria.getDateTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTo(), Salary_.dateTo));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildSpecification(criteria.getPaid(), Salary_.paid));
            }
            if (criteria.getPaidDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaidDate(), Salary_.paidDate));
            }
            if (criteria.getMentorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMentorId(), Salary_.mentor, Mentor_.id));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getItemsId(), Salary_.items, SalaryItem_.id));
            }
        }
        return specification;
    }

}
