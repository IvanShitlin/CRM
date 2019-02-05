package com.foxminded.hipsterfox.service.dto;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;


/**
 * Criteria class for the Client entity. This class is used in ClientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter patronymic;

    private StringFilter phone;

    private StringFilter email;

    private StringFilter skype;

    private StringFilter country;

    private StringFilter city;

    private StringFilter experience;

    private StringFilter note;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(StringFilter patronymic) {
        this.patronymic = patronymic;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getSkype() {
        return skype;
    }

    public void setSkype(StringFilter skype) {
        this.skype = skype;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getExperience() {
        return experience;
    }

    public void setExperience(StringFilter experience) {
        this.experience = experience;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (patronymic != null ? "patronymic=" + patronymic + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (skype != null ? "skype=" + skype + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (experience != null ? "experience=" + experience + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
            "}";
    }

}
