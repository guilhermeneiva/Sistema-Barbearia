package com.guilhermeneiva.demo.infra;

import com.guilhermeneiva.demo.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    private ResponseEntity<MensagemErro> emailCadastrado(EmailJaCadastradoException emailJaCadastradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.CONFLICT, emailJaCadastradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagemErro);
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<MensagemErro> cpfJaCadastrado(CpfJaCadastradoException cpfJaCadastradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.CONFLICT, cpfJaCadastradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagemErro);
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> clienteNaoEncontrado(ClienteNaoEncontradoException clienteNaoEncontradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, clienteNaoEncontradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ErrorObject> erros = getErros(ex);
        ErrorResponse errorResponse = getErrorResponse(ex, HttpStatus.BAD_REQUEST, erros);
        return new ResponseEntity<>(errorResponse, status);

    }

    private ErrorResponse getErrorResponse(MethodArgumentNotValidException ex, HttpStatus status, List<ErrorObject> errors) {
        return new ErrorResponse("Requisição possui campos inválidos", status.value(),
                status.getReasonPhrase(), ex.getBindingResult().getObjectName(), errors);
    }

    private List<ErrorObject> getErros(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream().map(error -> new ErrorObject(error.getDefaultMessage(),
                error.getField(),
                error.getRejectedValue())).collect(Collectors.toList());
    }

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> servicoNaoEncontrado(ServicoNaoEncontradoException servicoNaoEncontradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, servicoNaoEncontradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }

    @ExceptionHandler(NomeJaCadastradoException.class)
    public ResponseEntity<MensagemErro> nomeJaCadastrado(NomeJaCadastradoException nomeJaCadastradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.CONFLICT, nomeJaCadastradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagemErro);
    }

    @ExceptionHandler(BarbeiroNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> barbeiroNaoEncontrado(BarbeiroNaoEncontradoException barbeiroNaoEncontradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, barbeiroNaoEncontradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }

    @ExceptionHandler(HorarioComConflitoException.class)
    public ResponseEntity<MensagemErro> horarioComConflito(HorarioComConflitoException horarioComConflitoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.CONFLICT, horarioComConflitoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagemErro);
    }

    @ExceptionHandler(AgendamentoNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> agendamentoNaoEncontrado(AgendamentoNaoEncontradoException agendamentoNaoEncontradoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, agendamentoNaoEncontradoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }

    @ExceptionHandler(DataDeFuncionamentoException.class)
    public ResponseEntity<MensagemErro> dataDeFuncionamento(DataDeFuncionamentoException dataDeFuncionamentoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST, dataDeFuncionamentoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }

    @ExceptionHandler(HorarioDeFuncionamentoException.class)
    public ResponseEntity<MensagemErro> horarioDeFuncionamento(HorarioDeFuncionamentoException horarioDeFuncionamentoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST, horarioDeFuncionamentoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }

    @ExceptionHandler(AgendamentoJaCanceladoException.class)
    public ResponseEntity<MensagemErro> agendamentoJaCancelado(AgendamentoJaCanceladoException agendamentoJaCanceladoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST, agendamentoJaCanceladoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }

    @ExceptionHandler(HorasParaAgendamentoException.class)
    public ResponseEntity<MensagemErro> horasParaAgendamento(HorasParaAgendamentoException horasParaAgendamentoException, HttpServletRequest httpServletRequest) {
        MensagemErro mensagemErro = new MensagemErro(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST, horasParaAgendamentoException.getMessage(), httpServletRequest.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }
}