package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hellog2;
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
 * Cassandra repository for the Hellog2 entity.
 */
@Repository
public class Hellog2Repository {

    private final Session session;

    private final Validator validator;

    private Mapper<Hellog2> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public Hellog2Repository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Hellog2.class);
        this.findAllStmt = session.prepare("SELECT * FROM hellog2");
        this.truncateStmt = session.prepare("TRUNCATE hellog2");
    }

    public List<Hellog2> findAll() {
        List<Hellog2> hellog2SList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Hellog2 hellog2 = new Hellog2();
                hellog2.setId(row.getUUID("id"));
                hellog2.setDef(row.getString("def"));
                return hellog2;
            }
        ).forEach(hellog2SList::add);
        return hellog2SList;
    }

    public Hellog2 findOne(UUID id) {
        return mapper.get(id);
    }

    public Hellog2 save(Hellog2 hellog2) {
        if (hellog2.getId() == null) {
            hellog2.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Hellog2>> violations = validator.validate(hellog2);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(hellog2);
        return hellog2;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
