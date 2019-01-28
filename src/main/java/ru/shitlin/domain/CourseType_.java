package ru.shitlin.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CourseType.class)
public abstract class CourseType_ {

	public static volatile SingularAttribute<CourseType, Money> mentorRate;
	public static volatile SingularAttribute<CourseType, Money> price;
	public static volatile SingularAttribute<CourseType, String> location;
	public static volatile SingularAttribute<CourseType, Long> id;
	public static volatile SingularAttribute<CourseType, String> type;

}

