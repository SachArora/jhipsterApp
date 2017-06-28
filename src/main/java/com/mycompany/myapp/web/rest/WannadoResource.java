package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Wannado;

import com.mycompany.myapp.repository.WannadoRepository;
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
 * REST controller for managing Wannado.
 */
@RestController
@RequestMapping("/api")
public class WannadoResource {

    private final Logger log = LoggerFactory.getLogger(WannadoResource.class);

    private static final String ENTITY_NAME = "wannado";

    private final WannadoRepository wannadoRepository;

    public WannadoResource(WannadoRepository wannadoRepository) {
        this.wannadoRepository = wannadoRepository;
    }

    /**
     * POST  /wannados : Create a new wannado.
     *
     * @param wannado the wannado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wannado, or with status 400 (Bad Request) if the wannado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wannados")
    @Timed
    public ResponseEntity<Wannado> createWannado(@RequestBody Wannado wannado) throws URISyntaxException {
        log.debug("REST request to save Wannado : {}", wannado);
        if (wannado.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new wannado cannot already have an ID")).body(null);
        }
        Wannado result = wannadoRepository.save(wannado);
        return ResponseEntity.created(new URI("/api/wannados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wannados : Updates an existing wannado.
     *
     * @param wannado the wannado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wannado,
     * or with status 400 (Bad Request) if the wannado is not valid,
     * or with status 500 (Internal Server Error) if the wannado couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wannados")
    @Timed
    public ResponseEntity<Wannado> updateWannado(@RequestBody Wannado wannado) throws URISyntaxException {
        log.debug("REST request to update Wannado : {}", wannado);
        if (wannado.getId() == null) {
            return createWannado(wannado);
        }
        Wannado result = wannadoRepository.save(wannado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wannado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wannados : get all the wannados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of wannados in body
     */
    @GetMapping("/wannados")
    @Timed
    public List<Wannado> getAllWannados() {
        log.debug("REST request to get all Wannados");
        return wannadoRepository.findAll();
    }

    /**
     * GET  /wannados/:id : get the "id" wannado.
     *
     * @param id the id of the wannado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wannado, or with status 404 (Not Found)
     */
    @GetMapping("/wannados/{id}")
    @Timed
    public ResponseEntity<Wannado> getWannado(@PathVariable String id) {
        log.debug("REST request to get Wannado : {}", id);
        Wannado wannado = wannadoRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wannado));
    }

    /**
     * DELETE  /wannados/:id : delete the "id" wannado.
     *
     * @param id the id of the wannado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wannados/{id}")
    @Timed
    public ResponseEntity<Void> deleteWannado(@PathVariable String id) {
        log.debug("REST request to delete Wannado : {}", id);
        wannadoRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
