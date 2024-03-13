package com.example.to_do.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.apache.commons.lang.UnhandledException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.to_do.dtos.TarefaResponseDTO;
import com.example.to_do.dtos.TarefasDTO;
import com.example.to_do.entidades.Tarefas;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.repository.TarefaRepository;
import com.example.to_do.repository.UsuarioRepository;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;



    private UserDetails getAuthenticatedUser() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            } else {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
        }

        
    public TarefaResponseDTO criarTarefa(TarefasDTO tarefaDto) {
        Tarefas tarefa = new Tarefas();
        tarefa.setDescricao(tarefaDto.getDescricao());
        tarefa.setPrioridade(tarefaDto.getPrioridade()); 
        tarefa.setConcluida(tarefaDto.isConcluida());
    

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername(); 
        } else {
            email = principal.toString();
        }

        UserDetails userDetails = usuarioRepository.findByEmail(email);
        if (userDetails instanceof Usuario) {
            Usuario usuario = (Usuario) userDetails;
            tarefa.setUsuario(usuario);
        } else {
            throw new UsernameNotFoundException("Não foi possível encontrar o usuário com o e-mail: " + email);
        }

        tarefa = tarefaRepository.save(tarefa);

        TarefaResponseDTO tarefaResponseDTO = new TarefaResponseDTO();
        tarefaResponseDTO.setId(tarefa.getId());
        tarefaResponseDTO.setDescricao(tarefa.getDescricao());
        tarefaResponseDTO.setPrioridade(tarefa.getPrioridade());
        tarefaResponseDTO.setConcluido(tarefa.isConcluida());
    
        return tarefaResponseDTO;
    }


    public void excluirTarefa(Long id) {
        try {
            Tarefas tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Tarefa não encontrada com o id " + id));
    
            UserDetails userDetails = getAuthenticatedUser();
    
            if (!tarefa.getUsuario().getEmail().equals(userDetails.getUsername())) {
                throw new UnhandledException("Você não tem permissão para excluir esta tarefa.", null);
            }
    
            tarefaRepository.delete(tarefa);
        } catch (RoleNotFoundException e) {
            
            e.printStackTrace();
        }
    }
    
    
    public TarefaResponseDTO alterarTarefa(Long id, TarefasDTO tarefaDto) {
    Tarefas tarefa = tarefaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id " + id));

    UserDetails userDetails = getAuthenticatedUser();

    if (!tarefa.getUsuario().getEmail().equals(userDetails.getUsername())) {
        throw new UnhandledException("Você não tem permissão para alterar esta tarefa.", null);
    }

    tarefa.setDescricao(tarefaDto.getDescricao());
    tarefa.setPrioridade(tarefaDto.getPrioridade());

    tarefa = tarefaRepository.save(tarefa);

    TarefaResponseDTO tarefaResponseDTO = new TarefaResponseDTO();
    tarefaResponseDTO.setId(tarefa.getId());
    tarefaResponseDTO.setDescricao(tarefa.getDescricao());
    tarefaResponseDTO.setPrioridade(tarefa.getPrioridade());
    tarefaResponseDTO.setConcluido(tarefa.isConcluida());

    return tarefaResponseDTO;
}
public TarefaResponseDTO concluirTarefa(Long id) {
    Tarefas tarefa = tarefaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id " + id));

    UserDetails userDetails = getAuthenticatedUser();

    if (!tarefa.getUsuario().getEmail().equals(userDetails.getUsername())) {
        throw new UnhandledException("Você não tem permissão para concluir esta tarefa.", null);
    }

    tarefa.setConcluida(true);

    tarefa = tarefaRepository.save(tarefa);

    TarefaResponseDTO tarefaResponseDTO = new TarefaResponseDTO();
    tarefaResponseDTO.setId(tarefa.getId());
    tarefaResponseDTO.setDescricao(tarefa.getDescricao());
    tarefaResponseDTO.setPrioridade(tarefa.getPrioridade());
    tarefaResponseDTO.setConcluido(tarefa.isConcluida());

    return tarefaResponseDTO;
}
public List<TarefaResponseDTO> listarTarefasPendentes(String prioridade) {
    UserDetails userDetails = getAuthenticatedUser();
    
    Usuario usuario = usuarioRepository.findUsuarioByEmail(userDetails.getUsername());

    List<Tarefas> tarefas;
    if (prioridade != null) {
        tarefas = tarefaRepository.findByUsuarioAndConcluidaAndPrioridade(usuario, false, prioridade);
    } else {
        tarefas = tarefaRepository.findByUsuarioAndConcluida(usuario, false);
    }

    return tarefas.stream().map(tarefa -> {
        TarefaResponseDTO tarefaResponseDTO = new TarefaResponseDTO();
        tarefaResponseDTO.setId(tarefa.getId());
        tarefaResponseDTO.setDescricao(tarefa.getDescricao());
        tarefaResponseDTO.setPrioridade(tarefa.getPrioridade());
        tarefaResponseDTO.setConcluido(tarefa.isConcluida());
        return tarefaResponseDTO;
    }).collect(Collectors.toList());
}




    
}
