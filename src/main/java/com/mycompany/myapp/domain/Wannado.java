package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Wannado.
 */
@Table(name = "wannado")
public class Wannado implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String opt1;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOpt1() {
        return opt1;
    }

    public Wannado opt1(String opt1) {
        this.opt1 = opt1;
        return this;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wannado wannado = (Wannado) o;
        if (wannado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wannado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wannado{" +
            "id=" + getId() +
            ", opt1='" + getOpt1() + "'" +
            "}";
    }
}
