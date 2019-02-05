package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.CourseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the CourseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseTypeRepository extends JpaRepository<CourseType, Long> {

    Optional<CourseType> findByTypeAndLocation(String type, String location);

}
