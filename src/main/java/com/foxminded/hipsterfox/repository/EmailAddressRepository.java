package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

    Optional<EmailAddress> findByAddress(String address);
}
