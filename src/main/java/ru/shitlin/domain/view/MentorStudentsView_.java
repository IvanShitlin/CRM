package ru.shitlin.domain.view;

import ru.shitlin.domain.Mentor;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MentorStudentsView.class)
public abstract class MentorStudentsView_ {

	public static volatile SingularAttribute<MentorStudentsView, Mentor> mentor;
	public static volatile SingularAttribute<MentorStudentsView, Long> activeStudents;
	public static volatile SingularAttribute<MentorStudentsView, Long> id;

}

