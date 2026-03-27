package com.guilhermeneiva.demo.infra;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class MensagemErro {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String mensagem;
    private String path;

    public MensagemErro(LocalDateTime timestamp, HttpStatus status, String mensagem, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.mensagem = mensagem;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}