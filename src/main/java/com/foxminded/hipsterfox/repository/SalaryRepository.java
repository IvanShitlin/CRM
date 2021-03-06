package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.Salary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Salary entity.
 */
@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long>, JpaSpecificationExecutor<Salary> {

}
