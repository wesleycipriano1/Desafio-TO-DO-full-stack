package com.example.to_do.dtos;

import com.example.to_do.entidades.Tarefas;
import com.example.to_do.entidades.Tarefas.Prioridade;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarefaResponseDTO {

    private Long id;
    private String prioridade;
    private String descricao;
    private boolean concluido;

    @Hidden
    public static TarefaResponseDTO from(Tarefas tarefa) {
        TarefaResponseDTO tarefaResponseDTO = new TarefaResponseDTO();
        tarefaResponseDTO.setId(tarefa.getId());
        tarefaResponseDTO.setDescricao(tarefa.getDescricao());
        tarefaResponseDTO.setPrioridade(tarefa.getPrioridade().name());
        tarefaResponseDTO.setConcluido(tarefa.isConcluida());
        return tarefaResponseDTO;
    }

}
