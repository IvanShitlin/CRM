package ru.shitlin.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClientRegistrationForm.class)
public abstract class ClientRegistrationForm_ {

	public static volatile SingularAttribute<ClientRegistrationForm, Instant> date;
	public static volatile SingularAttribute<ClientRegistrationForm, String> skype;
	public static volatile SingularAttribute<ClientRegistrationForm, String> country;
	public static volatile SingularAttribute<ClientRegistrationForm, String> note;
	public static volatile SingularAttribute<ClientRegistrationForm, String> courseType;
	public static volatile SingularAttribute<ClientRegistrationForm, String> courseName;
	public static volatile SingularAttribute<ClientRegistrationForm, String> clientName;
	public static volatile SingularAttribute<ClientRegistrationForm, String> phone;
	public static volatile SingularAttribute<ClientRegistrationForm, Boolean> licenceAccepted;
	public static volatile SingularAttribute<ClientRegistrationForm, Boolean> managed;
	public static volatile SingularAttribute<ClientRegistrationForm, Long> id;
	public static volatile SingularAttribute<ClientRegistrationForm, String> email;

}

