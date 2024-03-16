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
import com.example.to_do.excecao.DadosVaziosException;
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

    private Usuario getUsuarioFromEmail(String email) {
        UserDetails userDetails = usuarioRepository.findByEmail(email);
        if (userDetails instanceof Usuario) {
            return (Usuario) userDetails;
        } else {
            throw new UsernameNotFoundException("Não foi possível encontrar o usuário com o e-mail: " + email);
        }
    }

    private void verificarPermissao(Tarefas tarefa) {
        UserDetails userDetails = getAuthenticatedUser();
        if (!tarefa.getUsuario().getEmail().equals(userDetails.getUsername())) {
            throw new UnhandledException("Você não tem permissão para realizar esta ação.", null);
        }
    }

    public TarefaResponseDTO criarTarefa(TarefasDTO tarefaDto) {
        if (tarefaDto.getDescricao() == null || tarefaDto.getDescricao().isEmpty()) {
            throw new DadosVaziosException("A descrição não pode ser vazia.");
        }
        Tarefas tarefa = new Tarefas();
        tarefa.setDescricao(tarefaDto.getDescricao());
        tarefa.setPrioridade(tarefaDto.getPrioridade());
        tarefa.setConcluida(tarefaDto.isConcluida());
        String email = getAuthenticatedUser().getUsername();
        Usuario usuario = getUsuarioFromEmail(email);
        tarefa.setUsuario(usuario);
        tarefa = tarefaRepository.save(tarefa);
        return TarefaResponseDTO.from(tarefa);
    }

    public void excluirTarefa(Long id) {
        try {
            Tarefas tarefa = tarefaRepository.findById(id)
                    .orElseThrow(() -> new RoleNotFoundException("Tarefa não encontrada com o id " + id));

            verificarPermissao(tarefa);

            tarefaRepository.delete(tarefa);
        } catch (RoleNotFoundException e) {

            e.printStackTrace();
        }
    }

    public TarefaResponseDTO alterarTarefa(Long id, TarefasDTO tarefaDto) {
        Tarefas tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id " + id));

        verificarPermissao(tarefa);

        tarefa.setDescricao(tarefaDto.getDescricao());
        tarefa.setPrioridade(tarefaDto.getPrioridade());

        tarefa = tarefaRepository.save(tarefa);

        return TarefaResponseDTO.from(tarefa);
    }

    public TarefaResponseDTO concluirTarefa(Long id) {
        Tarefas tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id " + id));

        verificarPermissao(tarefa);

        tarefa.setConcluida(true);

        tarefa = tarefaRepository.save(tarefa);

        return TarefaResponseDTO.from(tarefa);
    }

    public List<TarefaResponseDTO> listarTarefasPendentes(String prioridadeStr) {
        UserDetails userDetails = getAuthenticatedUser();

        Usuario usuario = usuarioRepository.findUsuarioByEmail(userDetails.getUsername());

        List<Tarefas> tarefas;
        if (prioridadeStr != null) {
            Tarefas.Prioridade prioridade = Tarefas.Prioridade.valueOf(prioridadeStr.toUpperCase());
            tarefas = tarefaRepository.findByUsuarioAndConcluidaAndPrioridade(usuario, false, prioridade);
        } else {
            tarefas = tarefaRepository.findByUsuarioAndConcluida(usuario, false);
        }

        return tarefas.stream().map(tarefa -> {
            return TarefaResponseDTO.from(tarefa);
        }).collect(Collectors.toList());
    }

}
