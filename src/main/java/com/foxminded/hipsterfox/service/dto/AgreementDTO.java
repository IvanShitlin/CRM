package com.foxminded.hipsterfox.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

import com.foxminded.hipsterfox.domain.enumeration.AgreementStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the Agreement entity.
 */
public class AgreementDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private AgreementStatus status;

    @NotNull
    private Long clientId;

    @NotNull
    private Long courseId;

    private String courseName;

    private String clientFirstName;

    private String clientLastName;

    @Valid
    @NotNull
    private CourseTypeDTO courseType;

    public CourseTypeDTO getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeDTO courseType) {
        this.courseType = courseType;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgreementDTO agreementDTO = (AgreementDTO) o;
        if (agreementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agreementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgreementDTO{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", status=" + status +
            ", clientId=" + clientId +
            ", courseId=" + courseId +
            ", courseName='" + courseName + '\'' +
            ", clientFirstName='" + clientFirstName + '\'' +
            ", clientLastName='" + clientLastName + '\'' +
            '}';
    }
}
