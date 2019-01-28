package ru.shitlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import ru.shitlin.domain.enumeration.AgreementStatus;

/**
 * A Agreement.
 */
@Entity
@Table(name = "agreement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agreement")
public class Agreement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agreementGenerator")
    @SequenceGenerator(name = "agreementGenerator", sequenceName = "agreement_sequence")
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgreementStatus status = AgreementStatus.NEW;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties("")
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties("")
    private Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties("")
    private CourseType courseType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Agreement startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Agreement endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public Agreement status(AgreementStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public Agreement client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Course getCourse() {
        return course;
    }

    public Agreement course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Agreement courseType(CourseType courseType) {
        this.courseType = courseType;
        return this;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Agreement agreement = (Agreement) o;
        if (agreement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agreement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Agreement{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
