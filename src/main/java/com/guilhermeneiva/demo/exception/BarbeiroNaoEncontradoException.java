package com.guilhermeneiva.demo.exception;

public class BarbeiroNaoEncontradoException extends RuntimeException{
    public BarbeiroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
