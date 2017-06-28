package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Hellog2.
 */
@Table(name = "hellog2")
public class Hellog2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String def;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDef() {
        return def;
    }

    public Hellog2 def(String def) {
        this.def = def;
        return this;
    }

    public void setDef(String def) {
        this.def = def;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hellog2 hellog2 = (Hellog2) o;
        if (hellog2.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hellog2.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hellog2{" +
            "id=" + getId() +
            ", def='" + getDef() + "'" +
            "}";
    }
}
