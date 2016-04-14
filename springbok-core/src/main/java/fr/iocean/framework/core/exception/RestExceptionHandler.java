package fr.iocean.framework.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, RestExceptionService.createErrors(ex.getBindingResult()), headers, HttpStatus.PRECONDITION_FAILED, request);
    }
    
    @ExceptionHandler(value = RestException.class)
    public final ResponseEntity<Object> handleWebServiceException(RestException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, ex.getBody(), headers, HttpStatus.PRECONDITION_FAILED, request);
    }
    
}
