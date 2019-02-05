package com.foxminded.hipsterfox.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

public class EmailMessageDTO implements Serializable {

    private Long id;

    private Long from;

    @NotEmpty
    private Set<EmailAddressDTO> to;

    private Set<EmailAddressDTO> cc;

    private ZonedDateTime sentDateTime;

    @NotNull
    private String subject;

    @NotNull
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Set<EmailAddressDTO> getTo() {
        return to;
    }

    public void setTo(Set<EmailAddressDTO> to) {
        this.to = to;
    }

    public Set<EmailAddressDTO> getCc() {
        return cc;
    }

    public void setCc(Set<EmailAddressDTO> cc) {
        this.cc = cc;
    }

    public ZonedDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(ZonedDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailMessageDTO that = (EmailMessageDTO) o;
        if (that.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "EmailMessageDTO{" +
            "id=" + id +
            ", from=" + from +
            ", to=" + to +
            ", cc=" + cc +
            ", sentDateTime=" + sentDateTime +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
