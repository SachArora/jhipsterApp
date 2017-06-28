package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Again.
 */
@Table(name = "again")
public class Again implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String oneatt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOneatt() {
        return oneatt;
    }

    public Again oneatt(String oneatt) {
        this.oneatt = oneatt;
        return this;
    }

    public void setOneatt(String oneatt) {
        this.oneatt = oneatt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Again again = (Again) o;
        if (again.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), again.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Again{" +
            "id=" + getId() +
            ", oneatt='" + getOneatt() + "'" +
            "}";
    }
}
