package com.foxminded.hipsterfox.service;

import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.repository.AgreementRepository;
import com.foxminded.hipsterfox.service.dto.AgreementCriteria;
import com.foxminded.hipsterfox.service.dto.AgreementDTO;
import com.foxminded.hipsterfox.service.mapper.AgreementMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for Agreement entities in the database.
 * The main input is a {@link AgreementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgreementDTO} or a {@link Page} of {@link AgreementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgreementQueryService extends QueryService<Agreement> {

    private final Logger log = LoggerFactory.getLogger(AgreementQueryService.class);

    private final AgreementRepository agreementRepository;

    private final AgreementMapper agreementMapper;

    public AgreementQueryService(AgreementRepository agreementRepository, AgreementMapper agreementMapper) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
    }

    /**
     * Return a {@link List} of {@link AgreementDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgreementDTO> findByCriteria(AgreementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Agreement> specification = createSpecification(criteria);
        return agreementMapper.toDto(agreementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgreementDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgreementDTO> findByCriteria(AgreementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agreement> specification = createSpecification(criteria);
        return agreementRepository.findAll(specification, page)
            .map(agreementMapper::toDto);
    }

    /**
     * Function to convert AgreementCriteria to a {@link Specification}
     */
    private Specification<Agreement> createSpecification(AgreementCriteria criteria) {
        Specification<Agreement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Agreement_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Agreement_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Agreement_.endDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Agreement_.status));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), Agreement_.client, Client_.id));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCourseId(), Agreement_.course, Course_.id));
            }
        }
        return specification;
    }

}
