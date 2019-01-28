package ru.shitlin.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import ru.shitlin.domain.*;
import ru.shitlin.repository.UserRepository;

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
            cm.createCache(UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(User.class.getName(), jcacheConfiguration);
            cm.createCache(Authority.class.getName(), jcacheConfiguration);
            cm.createCache(User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(Client.class.getName(), jcacheConfiguration);
            cm.createCache(Mentor.class.getName(), jcacheConfiguration);
            cm.createCache(Money.class.getName(), jcacheConfiguration);
            cm.createCache(Course.class.getName(), jcacheConfiguration);
            cm.createCache(Course.class.getName() + ".prices", jcacheConfiguration);
            cm.createCache(CourseType.class.getName(), jcacheConfiguration);
            cm.createCache(Agreement.class.getName(), jcacheConfiguration);
            cm.createCache(Contract.class.getName(), jcacheConfiguration);
            cm.createCache(Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(Payment.class.getName(), jcacheConfiguration);
            cm.createCache(SalaryItem.class.getName(), jcacheConfiguration);
            cm.createCache(Salary.class.getName(), jcacheConfiguration);
            cm.createCache(Salary.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(Mentor.class.getName() + ".courses", jcacheConfiguration);
            cm.createCache(Course.class.getName() + ".mentors", jcacheConfiguration);
            cm.createCache(Task.class.getName(), jcacheConfiguration);
            cm.createCache(EntityAuditEvent.class.getName(), jcacheConfiguration);
            cm.createCache(ClientRegistrationForm.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
