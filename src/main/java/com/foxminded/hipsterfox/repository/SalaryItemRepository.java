package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.SalaryItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Spring Data  repository for the SalaryItem entity.
 */
@Repository
public interface SalaryItemRepository extends JpaRepository<SalaryItem, Long>, JpaSpecificationExecutor<SalaryItem> {
    Set<SalaryItem> findItemsBySalaryId(Long id);
}
