package com.example.to_do.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.to_do.entidades.Tarefas;

public interface TarefaRepository extends JpaRepository<Tarefas, Long> {
    List<Tarefas> findByUsuarioAndConcluida(UserDetails usuario, boolean concluida);

    List<Tarefas> findByUsuarioAndConcluidaAndPrioridade(UserDetails usuario, boolean concluida, String prioridade);
}
