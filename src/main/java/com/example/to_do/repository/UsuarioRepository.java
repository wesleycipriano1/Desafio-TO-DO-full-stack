package com.example.to_do.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.to_do.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   UserDetails findByEmail(String email);

   Usuario findUsuarioByEmail(String email);

}
