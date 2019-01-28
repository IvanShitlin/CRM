package ru.shitlin.domain;

import ru.shitlin.domain.enumeration.AmocrmActionType;
import ru.shitlin.domain.enumeration.AmocrmEntitytype;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AmocrmInfo.class)
public abstract class AmocrmInfo_ {

	public static volatile SingularAttribute<AmocrmInfo, ZonedDateTime> dateTime;
	public static volatile SingularAttribute<AmocrmInfo, AmocrmActionType> actionType;
	public static volatile SingularAttribute<AmocrmInfo, AmocrmEntitytype> entityType;
	public static volatile SingularAttribute<AmocrmInfo, Integer> importedEntitiesAmount;
	public static volatile SingularAttribute<AmocrmInfo, Long> id;

}

