package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.domain.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

    Page<Contract> findAllByAgreement_Id(Pageable pageable, Long id);

    List<Contract> findAllByNextPayDate(LocalDate date);

    Boolean existsByAgreementAndCloseTypeIsNull(Agreement agreement);
}
