package com.stp.task.service.impl;

import com.stp.task.domain.Tarefa;
import com.stp.task.repository.TarefaRepository;
import com.stp.task.service.TarefaService;
import com.stp.task.service.dto.TarefaDTO;
import com.stp.task.service.mapper.TarefaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Tarefa.
 */
@Service
@Transactional
public class TarefaServiceImpl implements TarefaService{

    private final Logger log = LoggerFactory.getLogger(TarefaServiceImpl.class);

    private final TarefaRepository tarefaRepository;

    private final TarefaMapper tarefaMapper;

    public TarefaServiceImpl(TarefaRepository tarefaRepository, TarefaMapper tarefaMapper) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
    }

    /**
     * Save a tarefa.
     *
     * @param tarefaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TarefaDTO save(TarefaDTO tarefaDTO) {
        log.debug("Request to save Tarefa : {}", tarefaDTO);
        Tarefa tarefa = tarefaMapper.tarefaDTOToTarefa(tarefaDTO);
        tarefa = tarefaRepository.save(tarefa);
        TarefaDTO result = tarefaMapper.tarefaToTarefaDTO(tarefa);
        return result;
    }

    /**
     *  Get all the tarefas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TarefaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tarefas");
        Page<Tarefa> result = tarefaRepository.findAll(pageable);
        return result.map(tarefa -> tarefaMapper.tarefaToTarefaDTO(tarefa));
    }

    /**
     *  Get one tarefa by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TarefaDTO findOne(Long id) {
        log.debug("Request to get Tarefa : {}", id);
        Tarefa tarefa = tarefaRepository.findOne(id);
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);
        return tarefaDTO;
    }

    /**
     *  Delete the  tarefa by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tarefa : {}", id);
        tarefaRepository.delete(id);
    }
}
