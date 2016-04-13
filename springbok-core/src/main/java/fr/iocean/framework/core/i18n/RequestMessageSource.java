package fr.iocean.framework.core.i18n;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Locale;
import java.util.UUID;

/**
 * This class embed Spring MessageSource component. It is a scope request component. This means that
 * a new component is created for each request. A global application handler intercepts each request
 * and set the locale requested.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class RequestMessageSource {

    private static final Locale defaultLocale = Locale.FRENCH;
    
    private Locale locale;
    
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, Object[] args, String defaultMessage) {
        if (locale == null) {
            return messageSource.getMessage(code, args, defaultMessage, defaultLocale);
        }
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args) throws NoSuchMessageException {
        if (locale == null) {
            return messageSource.getMessage(code, args, defaultLocale);
        }
        return messageSource.getMessage(code, args, locale);
    }
    
}
