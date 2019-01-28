package ru.shitlin.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Client.class)
public abstract class Client_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Client, String> firstName;
	public static volatile SingularAttribute<Client, String> lastName;
	public static volatile SingularAttribute<Client, String> skype;
	public static volatile SingularAttribute<Client, String> country;
	public static volatile SingularAttribute<Client, String> note;
	public static volatile SingularAttribute<Client, String> patronymic;
	public static volatile SingularAttribute<Client, String> phone;
	public static volatile SingularAttribute<Client, String> city;
	public static volatile SingularAttribute<Client, Long> id;
	public static volatile SingularAttribute<Client, String> experience;
	public static volatile SingularAttribute<Client, Long> amocrmId;
	public static volatile SingularAttribute<Client, String> email;

}

