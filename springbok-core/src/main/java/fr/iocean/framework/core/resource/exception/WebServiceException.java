package fr.iocean.framework.core.resource.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor
public class WebServiceException extends RuntimeException {
    BindingResult bindingResult;
}
