package com.example.to_do.config;

import java.net.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public FilterToken filter;


    //a parte mais complexa é sempre ter que liberar as rotas,fazendo o projeto junto os caminhos do css tem que ser liberado aqui
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET,"/cadastrar").permitAll()
            .requestMatchers("/lo").permitAll()
            .requestMatchers(HttpMethod.GET,"/login").permitAll()
            .requestMatchers(HttpMethod.POST,"/login").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/usuarios/login").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/usuarios/cadastrar").permitAll()
            .requestMatchers(HttpMethod.GET,"/home").permitAll()
            .requestMatchers(HttpMethod.GET,"/tarefas/pendentes").permitAll()
            .requestMatchers(HttpMethod.GET,"/cadastroTarefa").permitAll()
            .requestMatchers(HttpMethod.POST,"/tarefa/cadastrar").permitAll()
            .requestMatchers("/usuario/cadastrar").permitAll()
            .requestMatchers("/v3/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            
            .requestMatchers(HttpMethod.POST,"/concluir/{id}").permitAll() 
            .requestMatchers("/js/**", "/css/**", "/img/**").permitAll() 
          
            .anyRequest().authenticated().and().addFilterBefore(
                filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }

   

   
 

}
