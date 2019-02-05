package com.foxminded.hipsterfox.service.amocrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmocrmLead {

    private Long[] id;

    public Long[] getId() {
        return id;
    }

    public void setId(Long[] id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmocrmLead that = (AmocrmLead) o;
        return Arrays.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(id);
    }

    @Override
    public String toString() {
        return "AmocrmLead{" +
            "id=" + Arrays.toString(id) +
            '}';
    }
}
