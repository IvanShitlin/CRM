package ru.shitlin.repository.search;

import ru.shitlin.domain.Agreement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Agreement entity.
 */
public interface AgreementSearchRepository extends ElasticsearchRepository<Agreement, Long> {
}
