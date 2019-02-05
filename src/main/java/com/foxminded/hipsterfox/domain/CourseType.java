package com.foxminded.hipsterfox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "course_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseTypeGenerator")
    @SequenceGenerator(name = "courseTypeGenerator", sequenceName = "course_type_sequence")
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "location")
    private String location;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Valid
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "mentor_rate_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "mentor_rate_currency"))
    })
    private Money mentorRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public CourseType type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public CourseType location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public CourseType price(Money price) {
        this.price = price;
        return this;
    }

    public CourseType mentorRate(Money mentorRate) {
        this.mentorRate = mentorRate;
        return this;
    }

    public Money getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(Money mentorRate) {
        this.mentorRate = mentorRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseType courseType = (CourseType) o;
        if (courseType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseType{" +
            "id=" + id +
            ", type='" + type + '\'' +
            ", location='" + location + '\'' +
            ", price=" + price +
            ", mentorRate=" + mentorRate +
            '}';
    }
}
