package fr.iocean.framework.core.resource.mock;

import fr.iocean.framework.core.exception.RestExceptionService;
import fr.iocean.framework.core.i18n.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
public class MessageController {
    
    @Autowired
    MessageService messageService;

    @Autowired
    private RestExceptionService restExceptionService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String getMsg(@RequestParam(value = "code",required = true) String code) {
        return messageService.getMessage(code);
    }

    @RequestMapping(value = "global", method = RequestMethod.GET)
    public void throwGlobalException(@RequestParam(value = "code",required = true) String code) {
        restExceptionService.throwRestException("invalid.code", code);
    }
    
}
