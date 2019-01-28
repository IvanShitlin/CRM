package ru.shitlin.domain;

import ru.shitlin.domain.enumeration.CloseType;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contract.class)
public abstract class Contract_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Contract, Money> mentorRate;
	public static volatile SingularAttribute<Contract, Mentor> mentor;
	public static volatile SingularAttribute<Contract, Agreement> agreement;
	public static volatile SingularAttribute<Contract, LocalDate> endDate;
	public static volatile SingularAttribute<Contract, Money> price;
	public static volatile SingularAttribute<Contract, LocalDate> firstPayDate;
	public static volatile SingularAttribute<Contract, Long> id;
	public static volatile SingularAttribute<Contract, CloseType> closeType;
	public static volatile SingularAttribute<Contract, LocalDate> startDate;
	public static volatile SingularAttribute<Contract, LocalDate> nextPayDate;

}

