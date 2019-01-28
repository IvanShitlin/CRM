package ru.shitlin.domain;

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
    private boolean licenceAccepted;

    @Column(name = "date")
    private Instant date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isLicenceAccepted() {
        return licenceAccepted;
    }

    public void setLicenceAccepted(boolean licenceAccepted) {
        this.licenceAccepted = licenceAccepted;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public ClientRegistrationForm clientName(String Clientname) {
        this.clientName = Clientname;
        return this;
    }

    public ClientRegistrationForm email(String email) {
        this.email = email;
        return this;
    }

    public ClientRegistrationForm phone(String phone) {
        this.phone = phone;
        return this;
    }
    public ClientRegistrationForm skype(String skype) {
        this.skype = skype;
        return this;
    }
    public ClientRegistrationForm country(String country) {
        this.country = country;
        return this;
    }
    public ClientRegistrationForm courseType(String courseType) {
        this.courseType = courseType;
        return this;
    }

    public ClientRegistrationForm courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public ClientRegistrationForm note(String note) {
        this.note = note;
        return this;
    }

    public ClientRegistrationForm licenceAccepted(boolean licenceAccepted) {
        this.licenceAccepted = licenceAccepted;
        return this;
    }

    public ClientRegistrationForm date(Instant instant) {
        this.date = instant;
        return this;
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
            '}';
    }
}
