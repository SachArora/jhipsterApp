package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sachu;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the Sachu entity.
 */
@Repository
public class SachuRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Sachu> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public SachuRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Sachu.class);
        this.findAllStmt = session.prepare("SELECT * FROM sachu");
        this.truncateStmt = session.prepare("TRUNCATE sachu");
    }

    public List<Sachu> findAll() {
        List<Sachu> sachusList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Sachu sachu = new Sachu();
                sachu.setId(row.getUUID("id"));
                sachu.setFriend_name(row.getString("friend_name"));
                sachu.setFriend_from(row.getString("friend_from"));
                sachu.setRoll_no(row.getInt("roll_no"));
                return sachu;
            }
        ).forEach(sachusList::add);
        return sachusList;
    }

    public Sachu findOne(UUID id) {
        return mapper.get(id);
    }

    public Sachu save(Sachu sachu) {
        if (sachu.getId() == null) {
            sachu.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Sachu>> violations = validator.validate(sachu);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(sachu);
        return sachu;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
