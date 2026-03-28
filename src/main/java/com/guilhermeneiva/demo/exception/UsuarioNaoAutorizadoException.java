package com.guilhermeneiva.demo.exception;

public class UsuarioNaoAutorizadoException extends RuntimeException{
    public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
