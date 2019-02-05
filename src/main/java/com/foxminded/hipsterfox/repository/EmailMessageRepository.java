package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
}
