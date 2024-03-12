package com.example.to_do.dtos;

import com.example.to_do.entidades.Tarefas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class TarefasDTO {
    private Long id;
    private String descricao;
    private Tarefas.Prioridade prioridade;
    private boolean concluida;
    private Long usuarioId;

    
}
