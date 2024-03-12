package com.example.to_do.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.to_do.dtos.TarefaResponseDTO;
import com.example.to_do.dtos.TarefasDTO;
import com.example.to_do.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @Autowired
    public TarefaService tarefaService;


    @PostMapping("/cadastrar")
public ResponseEntity<TarefaResponseDTO> cadastrarTarefa(@RequestBody TarefasDTO tarefaDTO) {
    TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaResponseDTO.getId()).toUri();
    return ResponseEntity.created(uri).body(tarefaResponseDTO);
}


    
}
