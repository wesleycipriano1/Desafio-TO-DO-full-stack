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
import com.example.to_do.excecao.DadosVaziosException;
import com.example.to_do.service.TarefaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/tarefa")
public class TarefaController {

    @Autowired
    public TarefaService tarefaService;

    @Operation(summary = "cadastra uma nova tarefa,token é necessario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "cadastro com sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado,token valido necessario"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(@RequestBody TarefasDTO tarefaDTO) {
        try {
            TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaDTO);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(tarefaResponseDTO.getId()).toUri();
            return ResponseEntity.created(uri).body(tarefaResponseDTO);
        } catch (DadosVaziosException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Exclui uma  tarefa,token é necessario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "exluido com sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado,token valido necessario"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        tarefaService.excluirTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "edita  uma  tarefa,token é necessario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "alterado com sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado,token valido necessario"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @PutMapping("/alterar/{id}")
    public ResponseEntity<TarefaResponseDTO> alterarTarefa(@PathVariable Long id, @RequestBody TarefasDTO tarefaDTO) {
        TarefaResponseDTO tarefaResponseDTO = tarefaService.alterarTarefa(id, tarefaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(tarefaResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(tarefaResponseDTO);
    }

    @Operation(summary = "marca uma tarefa como concluida ,token é necessario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "concuida com sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado,token valido necessario"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @PutMapping("/concluir/{id}")
    public ResponseEntity<TarefaResponseDTO> concluirTarefa(@PathVariable Long id) {
        TarefaResponseDTO tarefaResponseDTO = tarefaService.concluirTarefa(id);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(tarefaResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(tarefaResponseDTO);
    }

    @Operation(summary = "listas tarefas não concuidas ,token é necessario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado,token valido necessario"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @GetMapping("/pendentes")
    public ResponseEntity<List<TarefaResponseDTO>> listarTarefasPendentes(
            @RequestParam(required = false) String prioridade) {
        List<TarefaResponseDTO> tarefas = tarefaService.listarTarefasPendentes(prioridade);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(tarefas);

    }

}
