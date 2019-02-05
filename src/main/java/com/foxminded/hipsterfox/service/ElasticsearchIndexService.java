package com.foxminded.hipsterfox.service;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foxminded.hipsterfox.domain.Agreement;
import com.foxminded.hipsterfox.domain.Client;
import com.foxminded.hipsterfox.domain.User;
import com.foxminded.hipsterfox.repository.AgreementRepository;
import com.foxminded.hipsterfox.repository.ClientRepository;
import com.foxminded.hipsterfox.repository.UserRepository;
import com.foxminded.hipsterfox.repository.search.AgreementSearchRepository;
import com.foxminded.hipsterfox.repository.search.ClientSearchRepository;
import com.foxminded.hipsterfox.repository.search.UserSearchRepository;
import io.github.jhipster.config.JHipsterConstants;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final Environment env;

    private final ClientRepository clientRepository;

    private final ClientSearchRepository clientSearchRepository;

    private final AgreementRepository agreementRepository;

    private final AgreementSearchRepository agreementSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchIndexService(
        Environment env,
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        ClientRepository clientRepository,
        ClientSearchRepository clientSearchRepository,
        AgreementRepository agreementRepository,
        AgreementSearchRepository agreementSearchRepository,
        ElasticsearchOperations elasticsearchOperations) {
        this.env = env;
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.clientRepository = clientRepository;
        this.clientSearchRepository = clientSearchRepository;
        this.agreementRepository = agreementRepository;
        this.agreementSearchRepository = agreementSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Client.class, clientRepository, clientSearchRepository);
                reindexForClass(Agreement.class, agreementRepository, agreementSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchOperations.deleteIndex(entityClass);
        try {
            elasticsearchOperations.createIndex(entityClass);
        } catch (ResourceAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchOperations.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = new PageRequest(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                    relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.saveAll(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)) {
            reindexAll();
        }
    }
}
