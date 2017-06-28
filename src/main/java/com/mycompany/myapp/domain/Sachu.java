package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Sachu.
 */
@Table(name = "sachu")
public class Sachu implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    @NotNull
    private String friend_name;

    @NotNull
    private String friend_from;

    private Integer roll_no;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public Sachu friend_name(String friend_name) {
        this.friend_name = friend_name;
        return this;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_from() {
        return friend_from;
    }

    public Sachu friend_from(String friend_from) {
        this.friend_from = friend_from;
        return this;
    }

    public void setFriend_from(String friend_from) {
        this.friend_from = friend_from;
    }

    public Integer getRoll_no() {
        return roll_no;
    }

    public Sachu roll_no(Integer roll_no) {
        this.roll_no = roll_no;
        return this;
    }

    public void setRoll_no(Integer roll_no) {
        this.roll_no = roll_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sachu sachu = (Sachu) o;
        if (sachu.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sachu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sachu{" +
            "id=" + getId() +
            ", friend_name='" + getFriend_name() + "'" +
            ", friend_from='" + getFriend_from() + "'" +
            ", roll_no='" + getRoll_no() + "'" +
            "}";
    }
}
