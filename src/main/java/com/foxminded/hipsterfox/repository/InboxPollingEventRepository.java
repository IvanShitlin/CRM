package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.InboxPollingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InboxPollingEventRepository extends JpaRepository<InboxPollingEvent, Long> {

    Optional<InboxPollingEvent> findFirstByOrderByDateTimeDesc();

}
