package com.example.to_do.controllerFront;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.example.to_do.dtos.UsuarioDTO;
import com.example.to_do.dtos.UsuarioResponseDTO;
import com.example.to_do.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class ViewControllerUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "lo";
    }
     @PostMapping("/login")
    public String login(UsuarioDTO usuario, Model model, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/usuarios/login", usuario, String.class);
        String token = response.getBody();
        System.out.println("o tokem é -------------------------------------- "+token);

       
        request.getSession().setAttribute("token", token);

        return "redirect:/tarefas/pendentes";
    }

    @GetMapping("/cadastrar")
    public String mostrarFormulario(UsuarioDTO usuarioDTO) {
        return "cadastrar";
    }

   @PostMapping("/usuario/cadastrar")
    public String cadastrarUsuario(@Valid UsuarioDTO usuarioDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cadastrar";
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioDTO> request = new HttpEntity<>(usuarioDTO, headers);
        try {
            ResponseEntity<UsuarioResponseDTO> response = restTemplate.postForEntity("http://localhost:8080/api/usuarios/cadastrar", request, UsuarioResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "redirect:/login";
            } else {
                
                return "cadastrar";
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                model.addAttribute("error", "Já existe um usuário com este e-mail");
            } else {
                model.addAttribute("error", "Ocorreu um erro ao tentar registrar o usuário");
            }
            return "cadastrar";
        }
    }


}
