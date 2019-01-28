package ru.shitlin.domain.view;

import ru.shitlin.domain.Contract;
import ru.shitlin.domain.Invoice;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DebtorView.class)
public abstract class DebtorView_ {

	public static volatile SingularAttribute<DebtorView, Contract> contract;
	public static volatile SingularAttribute<DebtorView, Long> id;
	public static volatile SingularAttribute<DebtorView, Invoice> invoice;

}

