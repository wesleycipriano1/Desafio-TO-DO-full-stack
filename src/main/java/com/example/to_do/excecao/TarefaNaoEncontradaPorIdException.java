package com.example.to_do.excecao;

public class TarefaNaoEncontradaPorIdException extends RuntimeException {
    public TarefaNaoEncontradaPorIdException(String message) {
        super(message);
    }
}
