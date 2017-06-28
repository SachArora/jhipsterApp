package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Onemore;
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
 * Cassandra repository for the Onemore entity.
 */
@Repository
public class OnemoreRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Onemore> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public OnemoreRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Onemore.class);
        this.findAllStmt = session.prepare("SELECT * FROM onemore");
        this.truncateStmt = session.prepare("TRUNCATE onemore");
    }

    public List<Onemore> findAll() {
        List<Onemore> onemoresList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Onemore onemore = new Onemore();
                onemore.setId(row.getUUID("id"));
                onemore.setDept(row.getString("dept"));
                onemore.setBlock(row.getString("block"));
                return onemore;
            }
        ).forEach(onemoresList::add);
        return onemoresList;
    }

    public Onemore findOne(UUID id) {
        return mapper.get(id);
    }

    public Onemore save(Onemore onemore) {
        if (onemore.getId() == null) {
            onemore.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Onemore>> violations = validator.validate(onemore);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(onemore);
        return onemore;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
