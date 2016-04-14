package fr.iocean.framework.core.exception;


import fr.iocean.framework.core.i18n.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class RestExceptionService {

    @Autowired
    MessageService messageService;
    
    public void throwRestException(String code) {
        throw new RestException(createErrors(messageService.getMessage(code, null)));
    }

    public void throwRestException(String code, Object... arguments) {
        throw new RestException(createErrors(messageService.getMessage(code, arguments)));
    }
    
    public static RestErrors createErrors(BindingResult bindingResult) {
        return new RestErrors(null, bindingResult.getAllErrors());
    }

    public static  RestErrors createErrors(String message) {
        return new RestErrors(message, null);
    }

}