package com.guilhermeneiva.demo.exception;

public class CampoNuloException extends RuntimeException {
    public CampoNuloException(String mensagem) {
        super(mensagem);
    }
}
