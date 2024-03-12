package com.example.to_do.dtos;

import com.example.to_do.entidades.Tarefas.Prioridade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarefaResponseDTO {

    private Long id;
    private Prioridade prioridade; 
    private String descricao;
    private boolean concluido;

    
}

