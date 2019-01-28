package ru.shitlin.service.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CourseType entity.
 */
public class CourseTypeDTO implements Serializable {

    private Long id;

    private String type;

    private String location;

    @Valid
    @NotNull
    private MoneyDto price;

    @Valid
    @NotNull
    private MoneyDto mentorRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MoneyDto getPrice() {
        return price;
    }

    public void setPrice(MoneyDto price) {
        this.price = price;
    }

    public MoneyDto getMentorRate() {
        return mentorRate;
    }

    public void setMentorRate(MoneyDto mentorRate) {
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

        CourseTypeDTO courseTypeDTO = (CourseTypeDTO) o;
        if (courseTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseTypeDTO{" +
            "id=" + id +
            ", type='" + type + '\'' +
            ", location='" + location + '\'' +
            ", price=" + price +
            ", mentorRate=" + mentorRate +
            '}';
    }
}
