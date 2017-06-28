package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hellog;
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
 * Cassandra repository for the Hellog entity.
 */
@Repository
public class HellogRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Hellog> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public HellogRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Hellog.class);
        this.findAllStmt = session.prepare("SELECT * FROM hellog");
        this.truncateStmt = session.prepare("TRUNCATE hellog");
    }

    public List<Hellog> findAll() {
        List<Hellog> hellogsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Hellog hellog = new Hellog();
                hellog.setId(row.getUUID("id"));
                hellog.setAbc(row.getString("abc"));
                return hellog;
            }
        ).forEach(hellogsList::add);
        return hellogsList;
    }

    public Hellog findOne(UUID id) {
        return mapper.get(id);
    }

    public Hellog save(Hellog hellog) {
        if (hellog.getId() == null) {
            hellog.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Hellog>> violations = validator.validate(hellog);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(hellog);
        return hellog;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
