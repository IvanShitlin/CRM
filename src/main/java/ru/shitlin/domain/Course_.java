package ru.shitlin.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Course.class)
public abstract class Course_ extends AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Course, byte[]> image;
	public static volatile SingularAttribute<Course, String> emailTemplate;
	public static volatile SingularAttribute<Course, String> imageContentType;
	public static volatile SetAttribute<Course, Mentor> mentors;
	public static volatile SingularAttribute<Course, String> name;
	public static volatile SingularAttribute<Course, String> description;
	public static volatile SingularAttribute<Course, Long> id;

}

