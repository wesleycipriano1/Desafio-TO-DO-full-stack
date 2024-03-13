package com.example.to_do.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@DeleteMapping("/excluir/{id}")
public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
    tarefaService.excluirTarefa(id);
    return ResponseEntity.noContent().build();
}
@PutMapping("/alterar/{id}")
public ResponseEntity<TarefaResponseDTO> alterarTarefa(@PathVariable Long id, @RequestBody TarefasDTO tarefaDTO) {
    TarefaResponseDTO tarefaResponseDTO = tarefaService.alterarTarefa(id, tarefaDTO);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaResponseDTO.getId()).toUri();
    return ResponseEntity.created(uri).body(tarefaResponseDTO);
}
@PutMapping("/concluir/{id}")
public ResponseEntity<TarefaResponseDTO> concluirTarefa(@PathVariable Long id) {
    TarefaResponseDTO tarefaResponseDTO = tarefaService.concluirTarefa(id);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tarefaResponseDTO.getId()).toUri();
    return ResponseEntity.created(uri).body(tarefaResponseDTO);
}
@GetMapping("/pendentes")
public ResponseEntity<List<TarefaResponseDTO>> listarTarefasPendentes(@RequestParam(required = false) String prioridade) {
    
        List<TarefaResponseDTO> tarefas = tarefaService.listarTarefasPendentes(prioridade);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(tarefas);
   
}







    
}
