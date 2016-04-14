package fr.iocean.framework.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
@AllArgsConstructor
public class RestErrors {

    private String message;

    private List<ObjectError> bindingResult;
}
