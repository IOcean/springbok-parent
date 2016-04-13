package fr.iocean.framework.core.i18n;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepts all http requests and set locale to the RequestMessageSource request scope Spring component.
 * The chosen local is determined from the I18n-Language header argument.
 */
public class i18nInterceptor implements HandlerInterceptor {
    
    private static final String LOCALE_HEADER_PARAM_NAME = "I18n-Language";
    
    @Autowired
    RequestMessageSource requestMessageSource;
    
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (StringUtils.isNotBlank(httpServletRequest.getHeader(LOCALE_HEADER_PARAM_NAME))) {
            requestMessageSource.setLocale(LocaleUtils.toLocale(httpServletRequest.getHeader(LOCALE_HEADER_PARAM_NAME)));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
