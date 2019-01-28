package ru.shitlin.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RefreshableJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements RefreshableJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public RefreshableJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void refresh(T t) {
        entityManager.refresh(t);
    }
}
