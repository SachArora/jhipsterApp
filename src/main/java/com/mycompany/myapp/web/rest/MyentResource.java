package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Myent;

import com.mycompany.myapp.repository.MyentRepository;
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
 * REST controller for managing Myent.
 */
@RestController
@RequestMapping("/api")
public class MyentResource {

    private final Logger log = LoggerFactory.getLogger(MyentResource.class);

    private static final String ENTITY_NAME = "myent";

    private final MyentRepository myentRepository;

    public MyentResource(MyentRepository myentRepository) {
        this.myentRepository = myentRepository;
    }

    /**
     * POST  /myents : Create a new myent.
     *
     * @param myent the myent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new myent, or with status 400 (Bad Request) if the myent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/myents")
    @Timed
    public ResponseEntity<Myent> createMyent(@RequestBody Myent myent) throws URISyntaxException {
        log.debug("REST request to save Myent : {}", myent);
        if (myent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new myent cannot already have an ID")).body(null);
        }
        Myent result = myentRepository.save(myent);
        return ResponseEntity.created(new URI("/api/myents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /myents : Updates an existing myent.
     *
     * @param myent the myent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated myent,
     * or with status 400 (Bad Request) if the myent is not valid,
     * or with status 500 (Internal Server Error) if the myent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/myents")
    @Timed
    public ResponseEntity<Myent> updateMyent(@RequestBody Myent myent) throws URISyntaxException {
        log.debug("REST request to update Myent : {}", myent);
        if (myent.getId() == null) {
            return createMyent(myent);
        }
        Myent result = myentRepository.save(myent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, myent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /myents : get all the myents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of myents in body
     */
    @GetMapping("/myents")
    @Timed
    public List<Myent> getAllMyents() {
        log.debug("REST request to get all Myents");
        return myentRepository.findAll();
    }

    /**
     * GET  /myents/:id : get the "id" myent.
     *
     * @param id the id of the myent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the myent, or with status 404 (Not Found)
     */
    @GetMapping("/myents/{id}")
    @Timed
    public ResponseEntity<Myent> getMyent(@PathVariable String id) {
        log.debug("REST request to get Myent : {}", id);
        Myent myent = myentRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(myent));
    }

    /**
     * DELETE  /myents/:id : delete the "id" myent.
     *
     * @param id the id of the myent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/myents/{id}")
    @Timed
    public ResponseEntity<Void> deleteMyent(@PathVariable String id) {
        log.debug("REST request to delete Myent : {}", id);
        myentRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
