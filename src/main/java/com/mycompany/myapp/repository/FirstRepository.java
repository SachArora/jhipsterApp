package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.First;
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
 * Cassandra repository for the First entity.
 */
@Repository
public class FirstRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<First> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public FirstRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(First.class);
        this.findAllStmt = session.prepare("SELECT * FROM first");
        this.truncateStmt = session.prepare("TRUNCATE first");
    }

    public List<First> findAll() {
        List<First> firstsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                First first = new First();
                first.setId(row.getUUID("id"));
                first.setName(row.getString("name"));
                first.setBranch(row.getString("branch"));
                return first;
            }
        ).forEach(firstsList::add);
        return firstsList;
    }

    public First findOne(UUID id) {
        return mapper.get(id);
    }

    public First save(First first) {
        if (first.getId() == null) {
            first.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<First>> violations = validator.validate(first);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(first);
        return first;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
