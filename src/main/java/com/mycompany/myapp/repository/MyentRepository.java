package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Myent;
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
 * Cassandra repository for the Myent entity.
 */
@Repository
public class MyentRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Myent> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public MyentRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Myent.class);
        this.findAllStmt = session.prepare("SELECT * FROM myent");
        this.truncateStmt = session.prepare("TRUNCATE myent");
    }

    public List<Myent> findAll() {
        List<Myent> myentsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Myent myent = new Myent();
                myent.setId(row.getUUID("id"));
                return myent;
            }
        ).forEach(myentsList::add);
        return myentsList;
    }

    public Myent findOne(UUID id) {
        return mapper.get(id);
    }

    public Myent save(Myent myent) {
        if (myent.getId() == null) {
            myent.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Myent>> violations = validator.validate(myent);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(myent);
        return myent;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
