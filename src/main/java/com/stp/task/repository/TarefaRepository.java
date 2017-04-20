package com.stp.task.repository;

import com.stp.task.domain.Tarefa;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tarefa entity.
 */
@SuppressWarnings("unused")
public interface TarefaRepository extends JpaRepository<Tarefa,Long> {

    @Query("select tarefa from Tarefa tarefa where tarefa.user.login = ?#{principal.username}")
    List<Tarefa> findByUserIsCurrentUser();

}
