package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Hellog.
 */
@Table(name = "hellog")
public class Hellog implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String abc;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAbc() {
        return abc;
    }

    public Hellog abc(String abc) {
        this.abc = abc;
        return this;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hellog hellog = (Hellog) o;
        if (hellog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hellog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hellog{" +
            "id=" + getId() +
            ", abc='" + getAbc() + "'" +
            "}";
    }
}
