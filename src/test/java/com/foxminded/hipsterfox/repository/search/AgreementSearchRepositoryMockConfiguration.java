package com.foxminded.hipsterfox.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AgreementSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AgreementSearchRepositoryMockConfiguration {

    @MockBean
    private AgreementSearchRepository mockAgreementSearchRepository;

}
