package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Hellog2;

import com.mycompany.myapp.repository.Hellog2Repository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Hellog2.
 */
@RestController
@RequestMapping("/api")
public class Hellog2Resource {

    private final Logger log = LoggerFactory.getLogger(Hellog2Resource.class);

    private static final String ENTITY_NAME = "hellog2";

    private final Hellog2Repository hellog2Repository;

    public Hellog2Resource(Hellog2Repository hellog2Repository) {
        this.hellog2Repository = hellog2Repository;
    }

    /**
     * POST  /hellog-2-s : Create a new hellog2.
     *
     * @param hellog2 the hellog2 to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hellog2, or with status 400 (Bad Request) if the hellog2 has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hellog-2-s")
    @Timed
    public ResponseEntity<Hellog2> createHellog2(@RequestBody Hellog2 hellog2) throws URISyntaxException {
        log.debug("REST request to save Hellog2 : {}", hellog2);
        if (hellog2.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hellog2 cannot already have an ID")).body(null);
        }
        Hellog2 result = hellog2Repository.save(hellog2);
        return ResponseEntity.created(new URI("/api/hellog-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hellog-2-s : Updates an existing hellog2.
     *
     * @param hellog2 the hellog2 to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hellog2,
     * or with status 400 (Bad Request) if the hellog2 is not valid,
     * or with status 500 (Internal Server Error) if the hellog2 couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hellog-2-s")
    @Timed
    public ResponseEntity<Hellog2> updateHellog2(@RequestBody Hellog2 hellog2) throws URISyntaxException {
        log.debug("REST request to update Hellog2 : {}", hellog2);
        if (hellog2.getId() == null) {
            return createHellog2(hellog2);
        }
        Hellog2 result = hellog2Repository.save(hellog2);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hellog2.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hellog-2-s : get all the hellog2S.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hellog2S in body
     */
    @GetMapping("/hellog-2-s")
    @Timed
    public List<Hellog2> getAllHellog2S() {
        log.debug("REST request to get all Hellog2S");
        return hellog2Repository.findAll();
    }

    /**
     * GET  /hellog-2-s/:id : get the "id" hellog2.
     *
     * @param id the id of the hellog2 to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hellog2, or with status 404 (Not Found)
     */
    @GetMapping("/hellog-2-s/{id}")
    @Timed
    public ResponseEntity<Hellog2> getHellog2(@PathVariable String id) {
        log.debug("REST request to get Hellog2 : {}", id);
        Hellog2 hellog2 = hellog2Repository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hellog2));
    }

    /**
     * DELETE  /hellog-2-s/:id : delete the "id" hellog2.
     *
     * @param id the id of the hellog2 to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hellog-2-s/{id}")
    @Timed
    public ResponseEntity<Void> deleteHellog2(@PathVariable String id) {
        log.debug("REST request to delete Hellog2 : {}", id);
        hellog2Repository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
