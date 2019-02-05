package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.domain.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Agreement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long>, JpaSpecificationExecutor<Agreement> {

    Optional<Agreement> findByClientAndCourse(Client client, Course course);

}
