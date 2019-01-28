package ru.shitlin.domain;

import ru.shitlin.domain.enumeration.AgreementStatus;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Agreement.class)
public abstract class Agreement_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Agreement, CourseType> courseType;
	public static volatile SingularAttribute<Agreement, LocalDate> endDate;
	public static volatile SingularAttribute<Agreement, Client> client;
	public static volatile SingularAttribute<Agreement, Course> course;
	public static volatile SingularAttribute<Agreement, Long> id;
	public static volatile SingularAttribute<Agreement, LocalDate> startDate;
	public static volatile SingularAttribute<Agreement, AgreementStatus> status;

}

