package com.stp.task.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.stp.task.domain.enumeration.Status;

/**
 * A DTO for the Tarefa entity.
 */
public class TarefaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    @NotNull
    private ZonedDateTime dataCriacao;

    private String resposta;

    private Status status;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public ZonedDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TarefaDTO tarefaDTO = (TarefaDTO) o;

        if ( ! Objects.equals(id, tarefaDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TarefaDTO{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", dataCriacao='" + dataCriacao + "'" +
            ", resposta='" + resposta + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
