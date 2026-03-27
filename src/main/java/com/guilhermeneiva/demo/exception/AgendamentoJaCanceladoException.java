package com.guilhermeneiva.demo.exception;

public class AgendamentoJaCanceladoException extends RuntimeException{
    public AgendamentoJaCanceladoException(String mensagem) {
        super(mensagem);
    }
}
