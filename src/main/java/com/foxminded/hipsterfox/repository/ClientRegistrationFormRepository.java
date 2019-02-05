package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.ClientRegistrationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClientRegistrationForm entity.
 */
@Repository
public interface ClientRegistrationFormRepository extends JpaRepository<ClientRegistrationForm,Long> {
}
