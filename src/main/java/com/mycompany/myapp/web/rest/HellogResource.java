package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Hellog;

import com.mycompany.myapp.repository.HellogRepository;
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
 * REST controller for managing Hellog.
 */
@RestController
@RequestMapping("/api")
public class HellogResource {

    private final Logger log = LoggerFactory.getLogger(HellogResource.class);

    private static final String ENTITY_NAME = "hellog";

    private final HellogRepository hellogRepository;

    public HellogResource(HellogRepository hellogRepository) {
        this.hellogRepository = hellogRepository;
    }

    /**
     * POST  /hellogs : Create a new hellog.
     *
     * @param hellog the hellog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hellog, or with status 400 (Bad Request) if the hellog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hellogs")
    @Timed
    public ResponseEntity<Hellog> createHellog(@RequestBody Hellog hellog) throws URISyntaxException {
        log.debug("REST request to save Hellog : {}", hellog);
        if (hellog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hellog cannot already have an ID")).body(null);
        }
        Hellog result = hellogRepository.save(hellog);
        return ResponseEntity.created(new URI("/api/hellogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hellogs : Updates an existing hellog.
     *
     * @param hellog the hellog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hellog,
     * or with status 400 (Bad Request) if the hellog is not valid,
     * or with status 500 (Internal Server Error) if the hellog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hellogs")
    @Timed
    public ResponseEntity<Hellog> updateHellog(@RequestBody Hellog hellog) throws URISyntaxException {
        log.debug("REST request to update Hellog : {}", hellog);
        if (hellog.getId() == null) {
            return createHellog(hellog);
        }
        Hellog result = hellogRepository.save(hellog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hellog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hellogs : get all the hellogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hellogs in body
     */
    @GetMapping("/hellogs")
    @Timed
    public List<Hellog> getAllHellogs() {
        log.debug("REST request to get all Hellogs");
        return hellogRepository.findAll();
    }

    /**
     * GET  /hellogs/:id : get the "id" hellog.
     *
     * @param id the id of the hellog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hellog, or with status 404 (Not Found)
     */
    @GetMapping("/hellogs/{id}")
    @Timed
    public ResponseEntity<Hellog> getHellog(@PathVariable String id) {
        log.debug("REST request to get Hellog : {}", id);
        Hellog hellog = hellogRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hellog));
    }

    /**
     * DELETE  /hellogs/:id : delete the "id" hellog.
     *
     * @param id the id of the hellog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hellogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteHellog(@PathVariable String id) {
        log.debug("REST request to delete Hellog : {}", id);
        hellogRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
