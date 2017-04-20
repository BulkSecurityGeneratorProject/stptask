package com.stp.task.service;

import com.stp.task.service.dto.TarefaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Tarefa.
 */
public interface TarefaService {

    /**
     * Save a tarefa.
     *
     * @param tarefaDTO the entity to save
     * @return the persisted entity
     */
    TarefaDTO save(TarefaDTO tarefaDTO);

    /**
     *  Get all the tarefas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TarefaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tarefa.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TarefaDTO findOne(Long id);

    /**
     *  Delete the "id" tarefa.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
