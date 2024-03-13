package com.example.to_do.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.to_do.entidades.Tarefas;
import com.example.to_do.entidades.Usuario;
public interface TarefaRepository extends JpaRepository<Tarefas, Long> {
    List<Tarefas> findByUsuarioAndConcluida(Usuario usuario, boolean concluida);
    List<Tarefas> findByUsuarioAndConcluidaAndPrioridade(Usuario usuario, boolean concluida, String prioridade);
    List<Tarefas> findByUsuario(Usuario usuario);
}
