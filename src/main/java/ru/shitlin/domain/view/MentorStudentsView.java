package ru.shitlin.domain.view;

import ru.shitlin.domain.Mentor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MentorStudentsView.
 */
@Entity
@Table(name = "mentor_students_view")
@Immutable
public class MentorStudentsView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "mentor_id", updatable = false, nullable = false)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Mentor mentor;

    @Column(name = "active_students")
    private Long activeStudents;

    public MentorStudentsView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public Long getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(Long activeStudents) {
        this.activeStudents = activeStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MentorStudentsView mentorStudentsView = (MentorStudentsView) o;
        if (mentorStudentsView.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(id, mentorStudentsView.id) &&
            Objects.equals(mentor, mentorStudentsView.mentor) &&
            Objects.equals(activeStudents, mentorStudentsView.activeStudents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MentorStudentsView{" +
            "mentorId=" + id +
            ", mentor=" + mentor +
            ", activeStudents=" + activeStudents +
            '}';
    }
}
