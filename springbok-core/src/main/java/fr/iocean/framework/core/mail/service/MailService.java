package fr.iocean.framework.core.mail.service;

import com.google.common.base.Splitter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;

@Profile("mail")
@Slf4j
@Component
@Getter
@Setter
public class MailService {

    private static final String DEFAULT_TOS_SEPARATOR = ",";

    private static final String DEFAULT_ENCODING = "UTF-8";
    
    @Autowired
    private JavaMailSenderImpl sender;

    @Autowired
    private Configuration templateConf;
    
    private Map<String, Object> globalVars = new HashMap<>();

    private Locale locale;

    @Value("${spring.mail.enable}")
    boolean enable = true;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.replyTo}")
    private String replyTo;

    @Value("${spring.mail.fakeAddress:}")
    private String fakeAddress;

    @Value("${spring.mail.url.logo}")
    private String mailLogoURL = "";
    
    @PostConstruct
    public void loadMailLogo() {
        
        if (StringUtils.isEmpty(mailLogoURL)) {
            log.warn("Aucun chemin spécifié pour le logo du mail");
            return;
        }
        
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] img = new byte[0];
        try (InputStream stream = classLoader.getResourceAsStream(mailLogoURL)) {
            img = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            log.warn("Impossible de trouver le logo pour l'envoi de mail", e);
            return;
        }
        this.getGlobalVars().put("logoUrl", "data:image/jpg;base64," + DatatypeConverter.printBase64Binary(img));
    }
    
    public MimeMessageHelper newMessage() throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(sender.createMimeMessage(), true);
        setDefaultParameters(helper);
        
        return helper;
    }

    private void setDefaultParameters(MimeMessageHelper helper) {
        try {
            helper.setFrom(from);
            helper.setReplyTo(replyTo);
            locale = Locale.FRENCH;
        } catch (MessagingException e) {
            throw new IllegalStateException("Default address for 'from' or 'replyTo' has invalid format please check mail configuration file", e);
        }
    }

    public void send(MimeMessage message, MimeMessageHelper helper) {
        try {
            if (!StringUtils.isEmpty(fakeAddress)) {
                helper.setTo(fakeAddress);
            }

            if (isEnable() && getCountRecipient(message) > 0) {
                sender.send(message);
            }
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Integer getCountRecipient(MimeMessage message) {
        Integer count = 0;
        try {
            Address[] address = message.getAllRecipients();
            if (address != null) {
                count = address.length;
            }
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return count;
    }

    public void setTemplate(String templateName, Map<String, Object> vars, MimeMessageHelper helper) {
        try {
            Map<String, Object> tmplVars = new HashMap<>(globalVars);
            tmplVars.putAll(vars);
            Template template = templateConf.getTemplate(templateName, Locale.FRENCH, DEFAULT_ENCODING);
            String processedTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, tmplVars);
            helper.setText(processedTemplate, true);
        } catch (IOException e) {
            throw new IllegalArgumentException("Template " + templateName + " not found or unreadable", e);
        } catch (TemplateException e) {
            throw new IllegalArgumentException("Error while processing template " + templateName, e);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public boolean isValidateAddresses(MimeMessageHelper helper) {
        return helper.isValidateAddresses();
    }

    public void setTo(MimeMessageHelper helper, String to) {
        try {
            helper.setTo(to);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setTo(MimeMessageHelper helper, Iterable<String> tos) {
        try {
            for (String to : tos) {
                helper.addTo(to);
            }
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setFakeTo(MimeMessageHelper helper, Iterable<String> tos) {
        boolean first = true;
        try {
            for (String to : tos) {
                if (first) {
                    helper.setTo(to);
                    first = false;
                } else {
                    helper.addTo(to);
                }
            }
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setSubject(MimeMessageHelper helper, String subject) {
        try {
            helper.setSubject(subject);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void addFile(MimeMessageHelper helper, File file) {
        try {
            helper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            log.warn("unable to attach file to the Mime message", e);
        }
    }

    public void addFile(MimeMessageHelper helper, URI uri, String fileName) {
        try {
            helper.addAttachment(fileName, new ByteArrayResource(IOUtils.toByteArray(uri.toURL().openStream())));

        } catch (MessagingException e) {
            log.warn("unable to attach file to the Mime message", e);
        } catch (IOException e) {
            log.warn("unable to attach file to the Mime message", e);
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public void setHtml(MimeMessageHelper helper, String text) {
        try {
            helper.setText(text, true);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setTemplateConf(final Configuration templateConf) {
        this.templateConf = templateConf;
    }

    public void setSender(final JavaMailSenderImpl sender) {
        this.sender = sender;
    }

    public void setGlobalVars(final Map<String, Object> globalVars) {
        this.globalVars = globalVars;
    }

    public Iterable<String> getRecipientsFromString(String recipients) {
        return Splitter.on(DEFAULT_TOS_SEPARATOR)
                .trimResults()
                .omitEmptyStrings()
                .split(recipients);
    }

    public void setBcc(MimeMessageHelper helper, String[] bcc) {
        try {
            helper.setBcc(bcc);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
