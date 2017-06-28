package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Sachu;

import com.mycompany.myapp.repository.SachuRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Sachu.
 */
@RestController
@RequestMapping("/api")
public class SachuResource {

    private final Logger log = LoggerFactory.getLogger(SachuResource.class);

    private static final String ENTITY_NAME = "sachu";

    private final SachuRepository sachuRepository;

    public SachuResource(SachuRepository sachuRepository) {
        this.sachuRepository = sachuRepository;
    }

    /**
     * POST  /sachus : Create a new sachu.
     *
     * @param sachu the sachu to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sachu, or with status 400 (Bad Request) if the sachu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sachus")
    @Timed
    public ResponseEntity<Sachu> createSachu(@Valid @RequestBody Sachu sachu) throws URISyntaxException {
        log.debug("REST request to save Sachu : {}", sachu);
        if (sachu.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sachu cannot already have an ID")).body(null);
        }
        Sachu result = sachuRepository.save(sachu);
        return ResponseEntity.created(new URI("/api/sachus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sachus : Updates an existing sachu.
     *
     * @param sachu the sachu to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sachu,
     * or with status 400 (Bad Request) if the sachu is not valid,
     * or with status 500 (Internal Server Error) if the sachu couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sachus")
    @Timed
    public ResponseEntity<Sachu> updateSachu(@Valid @RequestBody Sachu sachu) throws URISyntaxException {
        log.debug("REST request to update Sachu : {}", sachu);
        if (sachu.getId() == null) {
            return createSachu(sachu);
        }
        Sachu result = sachuRepository.save(sachu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sachu.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sachus : get all the sachus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sachus in body
     */
    @GetMapping("/sachus")
    @Timed
    public List<Sachu> getAllSachus() {
        log.debug("REST request to get all Sachus");
        return sachuRepository.findAll();
    }

    /**
     * GET  /sachus/:id : get the "id" sachu.
     *
     * @param id the id of the sachu to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sachu, or with status 404 (Not Found)
     */
    @GetMapping("/sachus/{id}")
    @Timed
    public ResponseEntity<Sachu> getSachu(@PathVariable String id) {
        log.debug("REST request to get Sachu : {}", id);
        Sachu sachu = sachuRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sachu));
    }

    /**
     * DELETE  /sachus/:id : delete the "id" sachu.
     *
     * @param id the id of the sachu to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sachus/{id}")
    @Timed
    public ResponseEntity<Void> deleteSachu(@PathVariable String id) {
        log.debug("REST request to delete Sachu : {}", id);
        sachuRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
