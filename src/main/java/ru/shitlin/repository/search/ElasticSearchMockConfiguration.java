package ru.shitlin.repository.search;

import ru.shitlin.service.ElasticsearchIndexService;
import io.searchbox.client.JestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UserSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ElasticSearchMockConfiguration {

    @MockBean
    private ElasticsearchIndexService mockElasticsearchIndexService;

    @MockBean
    private UserSearchRepository mockUserSearchRepository;

    @MockBean
    private ClientSearchRepository mockClientSearchRepository;

    @MockBean
    private TaskSearchRepository mockTaskSearchRepository;

    @MockBean
    private JestClient jestClient;

}
