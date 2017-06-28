package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Wannado;
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
 * Cassandra repository for the Wannado entity.
 */
@Repository
public class WannadoRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Wannado> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public WannadoRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Wannado.class);
        this.findAllStmt = session.prepare("SELECT * FROM wannado");
        this.truncateStmt = session.prepare("TRUNCATE wannado");
    }

    public List<Wannado> findAll() {
        List<Wannado> wannadosList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Wannado wannado = new Wannado();
                wannado.setId(row.getUUID("id"));
                wannado.setOpt1(row.getString("opt1"));
                return wannado;
            }
        ).forEach(wannadosList::add);
        return wannadosList;
    }

    public Wannado findOne(UUID id) {
        return mapper.get(id);
    }

    public Wannado save(Wannado wannado) {
        if (wannado.getId() == null) {
            wannado.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Wannado>> violations = validator.validate(wannado);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(wannado);
        return wannado;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
