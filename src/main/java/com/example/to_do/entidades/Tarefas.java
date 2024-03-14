package com.example.to_do.entidades;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefas implements Serializable{
    private static final long serialVersionUID = 1L;

    public enum Prioridade {
        ALTA,
        MEDIA,
        BAIXA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo descrição não pode ser vazio")
    private String descricao;

    @NotNull(message = "O campo prioridade não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private boolean concluida;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}

