package ru.shitlin.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Salary.class)
public abstract class Salary_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Salary, Mentor> mentor;
	public static volatile SingularAttribute<Salary, LocalDate> paidDate;
	public static volatile SingularAttribute<Salary, LocalDate> dateTo;
	public static volatile SingularAttribute<Salary, Boolean> paid;
	public static volatile SingularAttribute<Salary, Money> sum;
	public static volatile SingularAttribute<Salary, Long> id;
	public static volatile SingularAttribute<Salary, LocalDate> dateFrom;
	public static volatile SetAttribute<Salary, SalaryItem> items;

}

