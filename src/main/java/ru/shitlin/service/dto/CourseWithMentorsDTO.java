package ru.shitlin.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the CourseWithMentors entity.
 */
public class CourseWithMentorsDTO implements Serializable {

    private Long id;

    private String name;

    @Lob
    private byte[] image;
    private String imageContentType;

    private String description;

    private String emailTemplate;

    private Set<MentorDTO> mentors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public Set<MentorDTO> getMentors() {
        return mentors;
    }

    public void setMentors(Set<MentorDTO> mentors) {
        this.mentors = mentors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseWithMentorsDTO courseWithMentorsDTO = (CourseWithMentorsDTO) o;
        return Objects.equals(id, courseWithMentorsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseWithMentorsDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", mentors count=" + mentors.size() +
            '}';
    }
}
