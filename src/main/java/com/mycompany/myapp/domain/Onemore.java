package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Onemore.
 */
@Table(name = "onemore")
public class Onemore implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    @NotNull
    private String dept;

    @Size(min = 5)
    private String block;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public Onemore dept(String dept) {
        this.dept = dept;
        return this;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBlock() {
        return block;
    }

    public Onemore block(String block) {
        this.block = block;
        return this;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Onemore onemore = (Onemore) o;
        if (onemore.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), onemore.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Onemore{" +
            "id=" + getId() +
            ", dept='" + getDept() + "'" +
            ", block='" + getBlock() + "'" +
            "}";
    }
}
