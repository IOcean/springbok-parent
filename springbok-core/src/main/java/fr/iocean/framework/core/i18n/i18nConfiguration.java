package fr.iocean.framework.core.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

@Configuration
public class i18nConfiguration extends WebMvcConfigurerAdapter {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
    
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());

        return validator;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SmartLocaleResolver();
    }

    public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
            if (StringUtils.isNotBlank(acceptLanguage)) {
                return new Locale(acceptLanguage);
            } else {
                return Locale.FRENCH;
            }
        }

    }

    @Override
    public org.springframework.validation.Validator getValidator() {
        return validator();
    }
    
}
