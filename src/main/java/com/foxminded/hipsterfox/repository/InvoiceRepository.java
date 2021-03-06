package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.Contract;
import com.foxminded.hipsterfox.domain.Invoice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Spring Data  repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends RefreshableJpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {

    boolean existsByDateFromAndContract(LocalDate date, Contract contract);

    boolean existsByDateToIsGreaterThanEqualAndDateFromIsLessThanEqualAndContract(LocalDate dateFrom, LocalDate dateTo, Contract contract);

    boolean existsByDateToIsGreaterThanEqualAndDateFromIsLessThanEqualAndIdIsNotAndContract(LocalDate dateFrom, LocalDate dateTo, Long id, Contract contract);
}
