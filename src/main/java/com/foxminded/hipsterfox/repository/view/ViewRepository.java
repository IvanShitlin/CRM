package com.foxminded.hipsterfox.repository.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ViewRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

}
