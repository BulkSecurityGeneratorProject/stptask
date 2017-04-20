package com.stp.task.service.mapper;

import com.stp.task.domain.*;
import com.stp.task.service.dto.TarefaDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tarefa and its DTO TarefaDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TarefaMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    TarefaDTO tarefaToTarefaDTO(Tarefa tarefa);

    List<TarefaDTO> tarefasToTarefaDTOs(List<Tarefa> tarefas);

    @Mapping(source = "userId", target = "user")
    Tarefa tarefaDTOToTarefa(TarefaDTO tarefaDTO);

    List<Tarefa> tarefaDTOsToTarefas(List<TarefaDTO> tarefaDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Tarefa tarefaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);
        return tarefa;
    }
    

}
