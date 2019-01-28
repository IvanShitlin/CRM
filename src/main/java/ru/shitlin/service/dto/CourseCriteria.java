package ru.shitlin.service.dto;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the Course entity. This class is used in CourseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /courses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter emailTemplate;

    private LongFilter mentorsId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(StringFilter emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public LongFilter getMentorsId() {
        return mentorsId;
    }

    public void setMentorsId(LongFilter mentorsId) {
        this.mentorsId = mentorsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(emailTemplate, that.emailTemplate) &&
                Objects.equals(mentorsId, that.mentorsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            emailTemplate,
            mentorsId
        );
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (emailTemplate != null ? "emailTemplate=" + emailTemplate + ", " : "") +
            (mentorsId != null ? "mentorsId=" + mentorsId + ", " : "") +
            "}";
    }

}
