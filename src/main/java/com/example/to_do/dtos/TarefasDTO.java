package com.example.to_do.dtos;

import com.example.to_do.entidades.Tarefas;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class TarefasDTO {
    @Hidden
    private Long id;
    private String descricao;
    private Tarefas.Prioridade prioridade;
    private boolean concluida;
    @Hidden
    private Long usuarioId;

    
}
