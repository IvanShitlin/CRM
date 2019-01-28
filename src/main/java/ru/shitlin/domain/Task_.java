package ru.shitlin.domain;

import ru.shitlin.domain.enumeration.TaskPriority;
import ru.shitlin.domain.enumeration.TaskType;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Task, LocalDate> date;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Boolean> closed;
	public static volatile SingularAttribute<Task, Long> id;
	public static volatile SingularAttribute<Task, TaskType> type;
	public static volatile SingularAttribute<Task, TaskPriority> priority;
	public static volatile SingularAttribute<Task, User> user;

}

