package com.foxminded.hipsterfox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "email_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmailMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailMessageGenerator")
    @SequenceGenerator(name = "emailMessageGenerator", sequenceName = "email_message_sequence")
    private Long id;

    @ManyToOne
    private EmailAddress from;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "email_message_to",
        joinColumns = {@JoinColumn(name = "email_message_id")},
        inverseJoinColumns = {@JoinColumn(name = "email_address_id")})
    private Set<EmailAddress> to;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "email_message_cc",
        joinColumns = {@JoinColumn(name = "email_message_id")},
        inverseJoinColumns = {@JoinColumn(name = "email_address_id")})
    private Set<EmailAddress> cc;

    @Column(name = "sent_date_time")
    private ZonedDateTime sentDateTime;

    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "body")
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }

    public Set<EmailAddress> getTo() {
        return to;
    }

    public void setTo(Set<EmailAddress> to) {
        this.to = to;
    }

    public Set<EmailAddress> getCc() {
        return cc;
    }

    public void setCc(Set<EmailAddress> cc) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailMessage that = (EmailMessage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", sentDate='" + getSentDateTime() + "'" +
            ", subject='" + getSubject() + "'" +
            "}";
    }
}
