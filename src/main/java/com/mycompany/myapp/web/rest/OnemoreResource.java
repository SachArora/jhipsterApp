package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Onemore;

import com.mycompany.myapp.repository.OnemoreRepository;
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
 * REST controller for managing Onemore.
 */
@RestController
@RequestMapping("/api")
public class OnemoreResource {

    private final Logger log = LoggerFactory.getLogger(OnemoreResource.class);

    private static final String ENTITY_NAME = "onemore";

    private final OnemoreRepository onemoreRepository;

    public OnemoreResource(OnemoreRepository onemoreRepository) {
        this.onemoreRepository = onemoreRepository;
    }

    /**
     * POST  /onemores : Create a new onemore.
     *
     * @param onemore the onemore to create
     * @return the ResponseEntity with status 201 (Created) and with body the new onemore, or with status 400 (Bad Request) if the onemore has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/onemores")
    @Timed
    public ResponseEntity<Onemore> createOnemore(@Valid @RequestBody Onemore onemore) throws URISyntaxException {
        log.debug("REST request to save Onemore : {}", onemore);
        if (onemore.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new onemore cannot already have an ID")).body(null);
        }
        Onemore result = onemoreRepository.save(onemore);
        return ResponseEntity.created(new URI("/api/onemores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /onemores : Updates an existing onemore.
     *
     * @param onemore the onemore to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated onemore,
     * or with status 400 (Bad Request) if the onemore is not valid,
     * or with status 500 (Internal Server Error) if the onemore couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/onemores")
    @Timed
    public ResponseEntity<Onemore> updateOnemore(@Valid @RequestBody Onemore onemore) throws URISyntaxException {
        log.debug("REST request to update Onemore : {}", onemore);
        if (onemore.getId() == null) {
            return createOnemore(onemore);
        }
        Onemore result = onemoreRepository.save(onemore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, onemore.getId().toString()))
            .body(result);
    }

    /**
     * GET  /onemores : get all the onemores.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of onemores in body
     */
    @GetMapping("/onemores")
    @Timed
    public List<Onemore> getAllOnemores() {
        log.debug("REST request to get all Onemores");
        return onemoreRepository.findAll();
    }

    /**
     * GET  /onemores/:id : get the "id" onemore.
     *
     * @param id the id of the onemore to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the onemore, or with status 404 (Not Found)
     */
    @GetMapping("/onemores/{id}")
    @Timed
    public ResponseEntity<Onemore> getOnemore(@PathVariable String id) {
        log.debug("REST request to get Onemore : {}", id);
        Onemore onemore = onemoreRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(onemore));
    }

    /**
     * DELETE  /onemores/:id : delete the "id" onemore.
     *
     * @param id the id of the onemore to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/onemores/{id}")
    @Timed
    public ResponseEntity<Void> deleteOnemore(@PathVariable String id) {
        log.debug("REST request to delete Onemore : {}", id);
        onemoreRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
