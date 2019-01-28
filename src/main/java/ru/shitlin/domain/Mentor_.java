package ru.shitlin.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Mentor.class)
public abstract class Mentor_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Mentor, String> firstName;
	public static volatile SingularAttribute<Mentor, String> lastName;
	public static volatile SingularAttribute<Mentor, String> skype;
	public static volatile SingularAttribute<Mentor, String> country;
	public static volatile SetAttribute<Mentor, Course> courses;
	public static volatile SingularAttribute<Mentor, String> patronymic;
	public static volatile SingularAttribute<Mentor, String> phone;
	public static volatile SingularAttribute<Mentor, String> city;
	public static volatile SingularAttribute<Mentor, Long> maxStudents;
	public static volatile SingularAttribute<Mentor, Long> id;
	public static volatile SingularAttribute<Mentor, String> email;

}

