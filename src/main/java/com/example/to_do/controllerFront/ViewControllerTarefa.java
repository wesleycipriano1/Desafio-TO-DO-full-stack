package com.example.to_do.controllerFront;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.to_do.dtos.TarefaResponseDTO;
import com.example.to_do.dtos.TarefasDTO;
import com.example.to_do.entidades.Tarefas;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewControllerTarefa {

    @GetMapping("/tarefas/pendentes")
    public String getTarefasPorPrioridade(@RequestParam(required = false) String prioridade, Model model,
            HttpSession session) {
        String token = (String) session.getAttribute("token");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "http://localhost:8080/api/tarefa/pendentes";
        if (prioridade != null && !prioridade.isEmpty() && !prioridade.equals("TODOS")) {
            url += "?prioridade=" + prioridade;
        }
        ResponseEntity<List<TarefaResponseDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<TarefaResponseDTO>>() {
                });
        List<TarefaResponseDTO> tarefas = response.getBody();
        model.addAttribute("tarefas", tarefas);
        return "tarefasLista";
    }

    @PostMapping("/concluir/{id}")
    public String concluirTarefa(@PathVariable("id") Long id, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();

        String token = (String) session.getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        String url = "http://localhost:8080/api/tarefa/concluir/" + id;

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            return "redirect:/tarefas/pendentes";
        } catch (HttpClientErrorException e) {

            return "redirect:/erro";
        }
    }

    @GetMapping("/cadastroTarefa")
    public String mostrarFormulario() {
        return "cadastroTarefa";
    }

    @PostMapping("/tarefa/cadastrar")
    public String cadastrarTarefa(@Valid TarefasDTO tarefaDTO, BindingResult result, Model model,
            HttpServletRequest request) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = (String) request.getSession().getAttribute("token");
        headers.set("Authorization", token);

        HttpEntity<TarefasDTO> entity = new HttpEntity<>(tarefaDTO, headers);
        try {
            ResponseEntity<TarefaResponseDTO> response = restTemplate
                    .postForEntity("http://localhost:8080/api/tarefa/cadastrar", entity, TarefaResponseDTO.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "redirect:/tarefas/pendentes";
            } else {
                return "cadastroTarefa";
            }
        } catch (HttpClientErrorException e) {

            model.addAttribute("error", "Ocorreu um erro ao tentar registrar a tarefa");

            return "cadastroTarefa";
        }
    }

}
