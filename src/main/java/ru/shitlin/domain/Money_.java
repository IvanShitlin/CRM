package ru.shitlin.domain;

import ru.shitlin.domain.enumeration.Currency;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Money.class)
public abstract class Money_ {

	public static volatile SingularAttribute<Money, Long> amount;
	public static volatile SingularAttribute<Money, Currency> currency;

}

