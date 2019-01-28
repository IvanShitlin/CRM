package ru.shitlin.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalaryItem.class)
public abstract class SalaryItem_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<SalaryItem, Mentor> mentor;
	public static volatile SingularAttribute<SalaryItem, Boolean> accounted;
	public static volatile SingularAttribute<SalaryItem, LocalDate> dateTo;
	public static volatile SingularAttribute<SalaryItem, Money> sum;
	public static volatile SingularAttribute<SalaryItem, Long> id;
	public static volatile SingularAttribute<SalaryItem, Invoice> invoice;
	public static volatile SingularAttribute<SalaryItem, LocalDate> dateFrom;
	public static volatile SingularAttribute<SalaryItem, Salary> salary;

}

