package com.guilhermeneiva.demo.exception;

public class ServicoNaoEncontradoException extends RuntimeException{
    public ServicoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
