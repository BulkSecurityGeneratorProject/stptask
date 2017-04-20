package com.stp.task.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stp.task.service.TarefaService;
import com.stp.task.web.rest.util.HeaderUtil;
import com.stp.task.web.rest.util.PaginationUtil;
import com.stp.task.service.dto.TarefaDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Tarefa.
 */
@RestController
@RequestMapping("/api")
public class TarefaResource {

    private final Logger log = LoggerFactory.getLogger(TarefaResource.class);

    private static final String ENTITY_NAME = "tarefa";
        
    private final TarefaService tarefaService;

    public TarefaResource(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    /**
     * POST  /tarefas : Create a new tarefa.
     *
     * @param tarefaDTO the tarefaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tarefaDTO, or with status 400 (Bad Request) if the tarefa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tarefas")
    @Timed
    public ResponseEntity<TarefaDTO> createTarefa(@Valid @RequestBody TarefaDTO tarefaDTO) throws URISyntaxException {
        log.debug("REST request to save Tarefa : {}", tarefaDTO);
        if (tarefaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tarefa cannot already have an ID")).body(null);
        }
        TarefaDTO result = tarefaService.save(tarefaDTO);
        return ResponseEntity.created(new URI("/api/tarefas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tarefas : Updates an existing tarefa.
     *
     * @param tarefaDTO the tarefaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tarefaDTO,
     * or with status 400 (Bad Request) if the tarefaDTO is not valid,
     * or with status 500 (Internal Server Error) if the tarefaDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tarefas")
    @Timed
    public ResponseEntity<TarefaDTO> updateTarefa(@Valid @RequestBody TarefaDTO tarefaDTO) throws URISyntaxException {
        log.debug("REST request to update Tarefa : {}", tarefaDTO);
        if (tarefaDTO.getId() == null) {
            return createTarefa(tarefaDTO);
        }
        TarefaDTO result = tarefaService.save(tarefaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tarefaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tarefas : get all the tarefas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tarefas in body
     */
    @GetMapping("/tarefas")
    @Timed
    public ResponseEntity<List<TarefaDTO>> getAllTarefas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Tarefas");
        Page<TarefaDTO> page = tarefaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tarefas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tarefas/:id : get the "id" tarefa.
     *
     * @param id the id of the tarefaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tarefaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tarefas/{id}")
    @Timed
    public ResponseEntity<TarefaDTO> getTarefa(@PathVariable Long id) {
        log.debug("REST request to get Tarefa : {}", id);
        TarefaDTO tarefaDTO = tarefaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tarefaDTO));
    }

    /**
     * DELETE  /tarefas/:id : delete the "id" tarefa.
     *
     * @param id the id of the tarefaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tarefas/{id}")
    @Timed
    public ResponseEntity<Void> deleteTarefa(@PathVariable Long id) {
        log.debug("REST request to delete Tarefa : {}", id);
        tarefaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
