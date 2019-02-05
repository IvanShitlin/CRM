package com.foxminded.hipsterfox.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.foxminded.hipsterfox.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Client.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Mentor.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Money.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Course.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Course.class.getName() + ".prices", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Agreement.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Contract.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Payment.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.SalaryItem.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Salary.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Salary.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Mentor.class.getName() + ".courses", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.Course.class.getName() + ".mentors", jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.EmailAddress.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.EmailMessage.class.getName(), jcacheConfiguration);
            cm.createCache(com.foxminded.hipsterfox.domain.InboxPollingEvent.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
