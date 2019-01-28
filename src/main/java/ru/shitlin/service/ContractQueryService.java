package ru.shitlin.service;

import ru.shitlin.domain.Agreement_;
import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Contract_;
import ru.shitlin.domain.Mentor_;
import ru.shitlin.repository.ContractRepository;
import ru.shitlin.service.dto.ContractCriteria;
import ru.shitlin.service.dto.ContractDTO;
import ru.shitlin.service.mapper.ContractMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for Contract entities in the database.
 * The main input is a {@link ContractCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractDTO} or a {@link Page} of {@link ContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractQueryService extends QueryService<Contract> {

    private final Logger log = LoggerFactory.getLogger(ContractQueryService.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    public ContractQueryService(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    /**
     * Return a {@link List} of {@link ContractDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractDTO> findByCriteria(ContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Contract> specification = createSpecification(criteria);
        return contractMapper.toDto(contractRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContractDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findByCriteria(ContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Contract> specification = createSpecification(criteria);
        final Page<Contract> result = contractRepository.findAll(specification, page);
        return result.map(contractMapper::toDto);
    }

    /**
     * Function to convert ContractCriteria to a {@link Specifications}
     */
    private Specifications<Contract> createSpecification(ContractCriteria criteria) {
        Specifications<Contract> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contract_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Contract_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Contract_.endDate));
            }
            if (criteria.getFirstPayDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFirstPayDate(), Contract_.firstPayDate));
            }
            if (criteria.getNextPayDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextPayDate(), Contract_.nextPayDate));
            }
            if (criteria.getCloseType() != null) {
                specification = specification.and(buildSpecification(criteria.getCloseType(), Contract_.closeType));
            }
            if (criteria.getMentorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMentorId(), Contract_.mentor, Mentor_.id));
            }
            if (criteria.getAgreementId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAgreementId(), Contract_.agreement, Agreement_.id));
            }
            // TODO Make money implementation
        }
        return specification;
    }

}
