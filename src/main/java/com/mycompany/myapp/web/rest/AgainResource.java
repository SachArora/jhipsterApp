package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Again;

import com.mycompany.myapp.repository.AgainRepository;
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
 * REST controller for managing Again.
 */
@RestController
@RequestMapping("/api")
public class AgainResource {

    private final Logger log = LoggerFactory.getLogger(AgainResource.class);

    private static final String ENTITY_NAME = "again";

    private final AgainRepository againRepository;

    public AgainResource(AgainRepository againRepository) {
        this.againRepository = againRepository;
    }

    /**
     * POST  /agains : Create a new again.
     *
     * @param again the again to create
     * @return the ResponseEntity with status 201 (Created) and with body the new again, or with status 400 (Bad Request) if the again has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agains")
    @Timed
    public ResponseEntity<Again> createAgain(@RequestBody Again again) throws URISyntaxException {
        log.debug("REST request to save Again : {}", again);
        if (again.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new again cannot already have an ID")).body(null);
        }
        Again result = againRepository.save(again);
        return ResponseEntity.created(new URI("/api/agains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agains : Updates an existing again.
     *
     * @param again the again to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated again,
     * or with status 400 (Bad Request) if the again is not valid,
     * or with status 500 (Internal Server Error) if the again couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agains")
    @Timed
    public ResponseEntity<Again> updateAgain(@RequestBody Again again) throws URISyntaxException {
        log.debug("REST request to update Again : {}", again);
        if (again.getId() == null) {
            return createAgain(again);
        }
        Again result = againRepository.save(again);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, again.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agains : get all the agains.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of agains in body
     */
    @GetMapping("/agains")
    @Timed
    public List<Again> getAllAgains() {
        log.debug("REST request to get all Agains");
        return againRepository.findAll();
    }

    /**
     * GET  /agains/:id : get the "id" again.
     *
     * @param id the id of the again to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the again, or with status 404 (Not Found)
     */
    @GetMapping("/agains/{id}")
    @Timed
    public ResponseEntity<Again> getAgain(@PathVariable String id) {
        log.debug("REST request to get Again : {}", id);
        Again again = againRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(again));
    }

    /**
     * DELETE  /agains/:id : delete the "id" again.
     *
     * @param id the id of the again to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agains/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgain(@PathVariable String id) {
        log.debug("REST request to delete Again : {}", id);
        againRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
