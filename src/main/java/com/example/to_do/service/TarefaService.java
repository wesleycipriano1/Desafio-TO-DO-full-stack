package com.example.to_do.service;

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
    
}
