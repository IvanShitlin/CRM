package ru.shitlin.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Payment.class)
public abstract class Payment_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Payment, LocalDate> date;
	public static volatile SingularAttribute<Payment, Money> sum;
	public static volatile SingularAttribute<Payment, Long> id;
	public static volatile SingularAttribute<Payment, Invoice> invoice;

}

