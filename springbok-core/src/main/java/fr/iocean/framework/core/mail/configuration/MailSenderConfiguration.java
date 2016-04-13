package fr.iocean.framework.core.mail.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import javax.activation.MimeType;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.context.annotation.Profile;

@Profile("mail")
@Configuration
@ConditionalOnClass({MimeMessage.class, MimeType.class})
@ConditionalOnProperty(prefix = "spring.mail", value = "enable")
@ConditionalOnMissingBean(MailSender.class)
@EnableConfigurationProperties(MailProperties.class)
public class MailSenderConfiguration {

    @Autowired
    private MailProperties properties;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(this.properties.getHost());  
        
        if (this.properties.getPort() != null) {
            sender.setPort(this.properties.getPort());
        }
        
        if (!StringUtils.isEmpty(this.properties.getUsername())) {
            sender.setUsername(this.properties.getUsername());
        }
        
        if (!StringUtils.isEmpty(this.properties.getPassword())) {
            sender.setPassword(this.properties.getPassword());
        }
        
        sender.setDefaultEncoding(this.properties.getDefaultEncoding().name());
        
        if (!this.properties.getProperties().isEmpty()) {
            Properties properties = new Properties();
            properties.putAll(this.properties.getProperties());
            sender.setJavaMailProperties(properties);
        }
        
        return sender;
    }


}
