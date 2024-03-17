package com.example.to_do.controllerFront;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.to_do.dtos.TarefaResponseDTO;
import com.example.to_do.dtos.TarefasDTO;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewControllerTarefa {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders obterCabecalhosAutorizacao(HttpSession session) {
        String token = (String) session.getAttribute("token");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }

    @GetMapping("/tarefas/pendentes")
    public String getTarefasPorPrioridade(@RequestParam(required = false) String prioridade, Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        HttpHeaders headers = obterCabecalhosAutorizacao(session);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "http://localhost:8080/api/tarefa/pendentes";
        if (prioridade != null && !prioridade.isEmpty() && !prioridade.equals("TODOS")) {
            url += "?prioridade=" + prioridade;
        }
        try {
            ResponseEntity<List<TarefaResponseDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<TarefaResponseDTO>>() {
                    });
            List<TarefaResponseDTO> tarefas = response.getBody();
            model.addAttribute("tarefas", tarefas);
            return "tarefasLista";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("error", "secessão expirada,faça login novamente ");
                return "redirect:/login";
            }
            throw e;
        }
    }

    @PostMapping("/concluir/{id}")
    public String concluirTarefa(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
            HttpSession session) {

        HttpHeaders headers = obterCabecalhosAutorizacao(session);
        String url = "http://localhost:8080/api/tarefa/concluir/" + id;

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            return "redirect:/tarefas/pendentes";
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("error", "secessão expirada,faça login novamente ");
                return "redirect:/login";

            } else {
                return "redirect:/erro";
            }

        }
    }

    @GetMapping("/cadastroTarefa")
    public String mostrarFormulario(Model model) {
        model.addAttribute("tarefa", new TarefasDTO());
        return "cadastroTarefa";
    }

    @PostMapping("/tarefa/cadastrar")
    public String cadastrarTarefa(@Valid TarefasDTO tarefaDTO, RedirectAttributes redirectAttributes,
            BindingResult result, Model model,
            HttpServletRequest request) {

        HttpHeaders headers = obterCabecalhosAutorizacao(request.getSession());
        headers.setContentType(MediaType.APPLICATION_JSON);

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
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                redirectAttributes.addFlashAttribute("error", "secessão expirada,faça login novamente ");
                return "redirect:/login";

            } else {

                redirectAttributes.addFlashAttribute("error", "Ocorreu um erro ao tentar registrar a tarefa");

                return "cadastroTarefa";
            }
        }

    }

}
