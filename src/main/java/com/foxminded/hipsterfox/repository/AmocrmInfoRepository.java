package com.foxminded.hipsterfox.repository;

import com.foxminded.hipsterfox.domain.AmocrmInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the AmocrmInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmocrmInfoRepository extends JpaRepository<AmocrmInfo, Long> {

    Optional<AmocrmInfo> findFirstByOrderByDateTimeDesc();

}
