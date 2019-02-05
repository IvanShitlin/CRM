package com.foxminded.hipsterfox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Course extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseGenerator")
    @SequenceGenerator(name = "courseGenerator", sequenceName = "course_sequence")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "description")
    private String description;

    @NotEmpty
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "course_price", joinColumns = @JoinColumn(name = "course_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<@Valid @NotNull Money> prices = new HashSet<>();

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "mentor_rate_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "mentor_rate_currency"))
    })
    private Money mentorRate;


    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "mentor_course",
        joinColumns = {@JoinColumn(name = "course_id")},
        inverseJoinColumns = {@JoinColumn(name = "mentor_id")})
    private Set<Mentor> mentors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public Course image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Course imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Money> getPrices() {
        return prices;
    }

    public Course prices(Set<Money> prices) {
        this.prices = prices;
        return this;
    }

    public Course addPrice(Money price) {
        this.prices.add(price);
        return this;
    }

    public Course removeItems(Money price) {
        this.prices.remove(price);
        return this;
    }

    public void setPrices(Set<Money> prices) {
        this.prices = prices;
    }

    public Money getMentorRate() {
        return mentorRate;
    }

    public Course mentorRate(Money mentorRate) {
        this.mentorRate = mentorRate;
        return this;
    }

    public void setMentorRate(Money mentorRate) {
        this.mentorRate = mentorRate;
    }

    public Set<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(Set<Mentor> mentors) {
        this.mentors = mentors;
    }

    public Course mentors(Set<Mentor> mentors) {
        this.mentors = mentors;
        return this;
    }

    public void addMentor(Mentor mentor) {
        this.mentors.add(mentor);
        mentor.getCourses().add(this);
    }

    public void removeMentor(Mentor mentor) {
        mentors.remove(mentor);
        mentor.getCourses().remove(this);
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
        Course course = (Course) o;
        if (course.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", mentorRate=" + mentorRate +
            '}';
    }
}
