package fr.iocean.framework.core.resource.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = WebServiceException.class)
    public final ResponseEntity<Object> handleWebServiceException(WebServiceException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, ex.getBindingResult().getAllErrors(), headers, HttpStatus.PRECONDITION_FAILED, request);
    }
    
}
