package com.foxminded.hipsterfox.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="client_registration_form")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientRegistrationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientRegistrationGenerator")
    @SequenceGenerator(name = "clientRegistrationGenerator", sequenceName = "client_registration_sequence")
    private Long id;

    @NotNull
    @Column(name = "client_name")
    private String clientName;

    @Email
    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "phone")
    private String phone;

    @Column(name = "skype")
    private String skype;

    @Column(name = "country")
    private String country;

    @NotNull
    @Column(name = "course_type")
    private String courseType;

    @NotNull
    @Column(name = "course_name")
    private String courseName;

    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "licence_accepted")
    private Boolean licenceAccepted;

    @Column(name = "date")
    private Instant date;

    @NotNull
    @Column(name = "managed")
    private Boolean managed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientRegistrationForm clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public ClientRegistrationForm email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public ClientRegistrationForm phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSkype() {
        return skype;
    }

    public ClientRegistrationForm skype(String skype) {
        this.skype = skype;
        return this;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getCountry() {
        return country;
    }

    public ClientRegistrationForm country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCourseType() {
        return courseType;
    }

    public ClientRegistrationForm courseType(String courseType) {
        this.courseType = courseType;
        return this;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public ClientRegistrationForm courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getNote() {
        return note;
    }

    public ClientRegistrationForm note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isLicenceAccepted() {
        return licenceAccepted;
    }

    public ClientRegistrationForm licenceAccepted(boolean licenceAccepted) {
        this.licenceAccepted = licenceAccepted;
        return this;
    }

    public void setLicenceAccepted(boolean licenceAccepted) {
        this.licenceAccepted = licenceAccepted;
    }

    public Instant getDate() {
        return date;
    }

    public ClientRegistrationForm date(Instant instant) {
        this.date = instant;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public boolean isManaged() {
        return managed;
    }

    public ClientRegistrationForm managed(Boolean managed) {
        this.managed = managed;
        return this;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRegistrationForm that = (ClientRegistrationForm) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ClientRegistrationForm{" +
            "id=" + id +
            ", clientName='" + clientName + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", skype='" + skype + '\'' +
            ", country='" + country + '\'' +
            ", courseType='" + courseType + '\'' +
            ", courseName='" + courseName + '\'' +
            ", note='" + note + '\'' +
            ", licenceAccepted=" + licenceAccepted +
            ", date=" + date +
            ", managed=" + managed +
            '}';
    }

}
