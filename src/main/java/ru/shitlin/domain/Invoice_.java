package ru.shitlin.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Invoice.class)
public abstract class Invoice_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Invoice, Contract> contract;
	public static volatile SingularAttribute<Invoice, LocalDate> dateTo;
	public static volatile SingularAttribute<Invoice, Money> sum;
	public static volatile SingularAttribute<Invoice, Payment> payment;
	public static volatile SingularAttribute<Invoice, Long> id;
	public static volatile SingularAttribute<Invoice, LocalDate> dateFrom;

}

