package com.example.to_do.excecao;

public class UsuarioNaoAutenticadoException extends RuntimeException {
    public UsuarioNaoAutenticadoException(String message) {
        super(message);
    }

}
