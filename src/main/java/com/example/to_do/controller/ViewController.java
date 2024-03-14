package com.example.to_do.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.to_do.dtos.UsuarioDTO;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "Login";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "cadastrar";
    }
    @GetMapping("/tarefas/pendentes")
    public String listarTarefasPendentes() {
        return "tarefasLista";
    }
}

