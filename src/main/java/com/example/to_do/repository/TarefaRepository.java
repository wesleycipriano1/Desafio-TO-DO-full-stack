package com.example.to_do.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.to_do.entidades.Tarefas;

public interface TarefaRepository extends JpaRepository<Tarefas, Long> {
}
