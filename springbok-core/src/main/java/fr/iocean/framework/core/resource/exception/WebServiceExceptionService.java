package fr.iocean.framework.core.resource.exception;


import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class WebServiceExceptionService {

    public void throwWebServiceException(Object resource, String key, Object... arguments) {
        BindingResult bindingResult = new BeanPropertyBindingResult(resource, resource.getClass().getSimpleName());
        String[] codes = new String[]{key};
        bindingResult.addError(new ObjectError(resource.getClass().getSimpleName(), codes, arguments, null));
        throw new WebServiceException(bindingResult);
    }

}