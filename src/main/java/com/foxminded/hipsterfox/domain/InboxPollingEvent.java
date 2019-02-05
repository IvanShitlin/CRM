package com.foxminded.hipsterfox.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "inbox_polling_event")
public class InboxPollingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inboxPollingEventGenerator")
    @SequenceGenerator(name = "inboxPollingEventGenerator", sequenceName = "inbox_polling_event_sequence")
    private Long id;

    @NotNull
    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "imported_messages_amount")
    private Integer importedMessagesAmount;

    public InboxPollingEvent() {
    }

    public InboxPollingEvent(@NotNull ZonedDateTime dateTime, @NotNull Integer importedMessagesAmount) {
        this.dateTime = dateTime;
        this.importedMessagesAmount = importedMessagesAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NotNull ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getImportedMessagesAmount() {
        return importedMessagesAmount;
    }

    public void setImportedMessagesAmount(Integer importedMessagesAmount) {
        this.importedMessagesAmount = importedMessagesAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InboxPollingEvent that = (InboxPollingEvent) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "InboxPollingEvent{" +
            "id=" + id +
            ", dateTime=" + dateTime +
            ", importedMessagesAmount=" + importedMessagesAmount +
            '}';
    }
}
