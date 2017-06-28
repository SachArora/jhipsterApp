package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Again;
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
 * Cassandra repository for the Again entity.
 */
@Repository
public class AgainRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Again> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public AgainRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Again.class);
        this.findAllStmt = session.prepare("SELECT * FROM again");
        this.truncateStmt = session.prepare("TRUNCATE again");
    }

    public List<Again> findAll() {
        List<Again> againsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Again again = new Again();
                again.setId(row.getUUID("id"));
                again.setOneatt(row.getString("oneatt"));
                return again;
            }
        ).forEach(againsList::add);
        return againsList;
    }

    public Again findOne(UUID id) {
        return mapper.get(id);
    }

    public Again save(Again again) {
        if (again.getId() == null) {
            again.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Again>> violations = validator.validate(again);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(again);
        return again;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
