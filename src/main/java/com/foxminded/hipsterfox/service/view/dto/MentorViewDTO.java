package com.foxminded.hipsterfox.service.view.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MentorStudentsView entity.
 */
public class MentorViewDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String patronymic;

    private String phone;

    @NotNull
    private String email;

    private String skype;

    private String country;

    private String city;

    private Long activeStudents;

    private Long maxStudents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(Long activeStudents) {
        activeStudents = activeStudents == null ? 0 : activeStudents;
        this.activeStudents = activeStudents;
    }

    public Long getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Long maxStudents) {
        this.maxStudents = maxStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MentorViewDTO mentorViewDTO = (MentorViewDTO) o;
        if (mentorViewDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(id, mentorViewDTO.id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MentorViewDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", patronymic='" + patronymic + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", skype='" + skype + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", activeStudents=" + activeStudents +
            ", maxStudents=" + maxStudents +
            '}';
    }
}
